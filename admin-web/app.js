/**
 * ═══════════════════════════════════════════════════════════════
 * RoadSafe AI — Admin Command Center v3.0 (Full Mobile App Mirror)
 * Real-time Firebase Cloud Engine, Module/Quiz Manager, Gamification,
 * AI Activity Stream, Driver Decisions & Security Audit.
 * ═══════════════════════════════════════════════════════════════
 */

// ═══════════════════════════════════════════════════════════════
// 1. FIREBASE INITIALIZATION
// ═══════════════════════════════════════════════════════════════
const firebaseConfig = {
    apiKey: "AIzaSyBoTVh2Ug2bMa4Ojkg8EOHmv1yrE-6EJWg",
    authDomain: "gamifiedroadsafetyawareness.firebaseapp.com",
    projectId: "gamifiedroadsafetyawareness",
    storageBucket: "gamifiedroadsafetyawareness.appspot.com",
    messagingSenderId: "470220431881",
    appId: "1:470220431881:web:c0b8923a497042a969f648"
};

let db = null;
try {
    if (!firebase.apps.length) firebase.initializeApp(firebaseConfig);
    db = firebase.firestore();
    console.log("✅ Firebase Firestore Initialized in Full Mirror Mode.");
} catch (err) {
    console.error("❌ Firebase init error:", err);
}

// ═══════════════════════════════════════════════════════════════
// 2. STATE REPOSITORY
// ═══════════════════════════════════════════════════════════════
const State = {
    users: [],
    devices: [],
    quizzes: [],
    logins: [],
    progress: [],
    audit: [],
    aiQueries: [],
    mobileModules: [],
    modules: [],
    questions: [],
    scenarios: [],
    badges: [],
    rankHistory: [],
    activeTab: 'dashboard',
    activeSubTab: {
        quizzes: 'quiz-attempts',
        gamification: 'gamif-leaderboard'
    },
    userFilter: 'all',
    deviceFilter: 'all',
    quizFilter: 'all',
    questionFilter: 'all',
    scenarioFilter: 'all',
    aiFilter: 'all',
    loginFilter: 'all',
    auditFilter: 'all',
    searchQuery: '',
    selectedUser: null,
    userToDelete: null,
    currentAdmin: 'admin'
};

// ═══════════════════════════════════════════════════════════════
// 3. CURRICULUM, QUESTIONS, SCENARIOS & BADGES DATA (Mobile Mirror)
// ═══════════════════════════════════════════════════════════════

const DEFAULT_MOBILE_MODULES = [
    {
        id: "mod_easy_quiz",
        title: "🟢 Easy Quiz",
        description: "20 questions covering road safety basics, traffic lights, signs, seat belts, and fundamental driving rules.",
        typeBadge: "EASY MODULE",
        xpReward: "+100 XP",
        enabled: true
    },
    {
        id: "mod_medium_quiz",
        title: "🟡 Medium Quiz",
        description: "20 scenario-based questions on lane changes, rain driving, overtaking rules, and defensive driving techniques.",
        typeBadge: "MEDIUM MODULE",
        xpReward: "+200 XP",
        enabled: true
    },
    {
        id: "mod_hard_quiz",
        title: "🔴 Hard Quiz",
        description: "20 advanced situational questions on right-of-way, night driving, skid control, and multi-hazard intersections.",
        typeBadge: "HARD MODULE",
        xpReward: "+300 XP",
        enabled: true
    }
];

const DEFAULT_MODULES = [
    { id: "mod_1", title: "Right-of-Way & Intersections", topic: "RIGHT_OF_WAY", difficulty: "Beginner", description: "Learn priority rules at cross-streets, merging lanes, and 4-way stops.", tip: "When in doubt, slow down and yield. Being technically right is never worth a collision.", status: "published", completions: 2 },
    { id: "mod_2", title: "Traffic Signs & Signals", topic: "TRAFFIC_SIGNS", difficulty: "Beginner", description: "Master regulatory, warning, and informational road signs and traffic lights.", tip: "A yellow light means prepare to stop, not speed up to beat it.", status: "published", completions: 1 },
    { id: "mod_3", title: "Speed Management", topic: "SPEED_MANAGEMENT", difficulty: "Intermediate", description: "Adjust speed according to traffic flow, road grip, and visibility.", tip: "Higher speed means longer stopping distance — adjust for conditions, not just the sign.", status: "published", completions: 1 },
    { id: "mod_4", title: "Following Distance & Braking", topic: "FOLLOWING_DISTANCE", difficulty: "Intermediate", description: "Prevent rear-end collisions with the 3-second rule and wet road buffering.", tip: "Double following distance in heavy rain or wet asphalt.", status: "published", completions: 1 },
    { id: "mod_5", title: "Pedestrian Safety & Crosswalks", topic: "PEDESTRIAN_SAFETY", difficulty: "Beginner", description: "Protect vulnerable pedestrians, school zones, and zebra crossings.", tip: "Always slow down near school zones even before you spot pedestrians.", status: "published", completions: 2 },
    { id: "mod_6", title: "Safe Overtaking & Lane Changes", topic: "OVERTAKING", difficulty: "Advanced", description: "Manage blind spots, solid white/yellow lines, and curve restrictions.", tip: "If not 100% sure you have space and visibility, never start an overtake.", status: "published", completions: 0 },
    { id: "mod_7", title: "Motorcycle Safety & Blind Spots", topic: "MOTORCYCLE_SAFETY", difficulty: "Intermediate", description: "Share the road safely with two-wheelers and perform head-checks.", tip: "Always check blind spots before changing lanes — bikes easily hide in mirrors.", status: "published", completions: 0 },
    { id: "mod_8", title: "Impaired & Distracted Driving", topic: "IMPAIRED_OR_DISTRACTED", difficulty: "Advanced", description: "The dangers of phone use, texting, alcohol, and driver fatigue.", tip: "Pull over safely if fatigue or distraction impairs your focus.", status: "published", completions: 0 },
    { id: "mod_9", title: "Emergency Vehicles & Sirens", topic: "EMERGENCY_PROCEDURES", difficulty: "Beginner", description: "Clear the way smoothly when ambulances and fire engines approach.", tip: "Signal and pull to the right smoothly; never slam brakes in the intersection.", status: "published", completions: 1 },
    { id: "mod_10", title: "Road Courtesy & Defensive Driving", topic: "ROAD_COURTESY", difficulty: "Intermediate", description: "Maintain emotional control, de-escalate road rage, and plan ahead.", tip: "Defensive driving means planning for others' mistakes, not just avoiding your own.", status: "published", completions: 1 }
];

const DEFAULT_QUESTIONS = [
    { id: "q_1", text: "What is the primary rule at an uncontrolled intersection without signs?", options: ["The vehicle approaching from the left goes first", "The vehicle on the right has right-of-way", "Whoever arrives fastest goes first", "Larger vehicles always have priority"], correct: 1, difficulty: "Easy", topic: "Right-of-Way", points: 10, exp: "At an uncontrolled intersection, the driver on the right has priority." },
    { id: "q_2", text: "What does a flashing yellow traffic signal indicate?", options: ["Stop completely before proceeding", "Proceed with caution after slowing down", "Speed up to clear the intersection", "The signal is broken, ignore it"], correct: 1, difficulty: "Easy", topic: "Traffic Signs & Signals", points: 10, exp: "A flashing yellow light warns drivers to slow down and proceed with caution." },
    { id: "q_3", text: "What is the recommended following distance under normal dry conditions?", options: ["1 second", "3 seconds", "5 car lengths regardless of speed", "10 meters"], correct: 1, difficulty: "Medium", topic: "Following Distance", points: 20, exp: "The 3-second rule provides sufficient stopping distance in dry conditions." },
    { id: "q_4", text: "When approaching a crosswalk where a pedestrian is stepping onto the curb:", options: ["Honk and continue through", "Speed up before they step out", "Slow down and stop to yield right-of-way", "Wave them to stop"], correct: 2, difficulty: "Easy", topic: "Pedestrian Safety", points: 10, exp: "Pedestrians at marked crosswalks have legal priority; drivers must stop." },
    { id: "q_5", text: "When is overtaking strictly prohibited?", options: ["On open straight highways", "Across solid yellow/white lines and blind curves", "When following a slow truck", "During daylight hours"], correct: 1, difficulty: "Hard", topic: "Overtaking", points: 30, exp: "Solid lane markings and blind curves prohibit passing due to limited visibility." }
];

const DEFAULT_SCENARIOS = [
    { id: "scen_1", title: "Distracted Pedestrian at School Crosswalk", weather: "Clear", speed: "30 km/h", prompt: "A pedestrian looking at their phone steps off the curb onto a marked crosswalk. What is the safest action?", options: [{ label: "Accelerate to pass first", risk: "Extreme Risk", safe: false }, { label: "Honk and proceed", risk: "Moderate Risk", safe: false }, { label: "Slow to a complete stop and yield", risk: "Safe Choice (Recommended)", safe: true }], score: 95, optimalAction: "Slow to a complete stop and yield" },
    { id: "scen_2", title: "Yellow Light Dilemma at 60 km/h", weather: "Wet Asphalt", speed: "60 km/h", prompt: "You are 15 meters from the stop line in rain when the light turns yellow.", options: [{ label: "Slam brakes abruptly (Risk of rear-end)", risk: "High Risk", safe: false }, { label: "Assess rear mirror and brake smoothly if clear", risk: "Safe Choice", safe: true }, { label: "Floor accelerator through red", risk: "Extreme Risk", safe: false }], score: 90, optimalAction: "Brake smoothly if safe or clear intersection" },
    { id: "scen_3", title: "Emergency Ambulance Approaching Intersection", weather: "Clear", speed: "40 km/h", prompt: "Siren blaring behind you at a green light.", options: [{ label: "Stop dead in middle of intersection", risk: "High Risk", safe: false }, { label: "Signal, pull to the right curb smoothly", risk: "Safe Choice (Recommended)", safe: true }, { label: "Race through intersection to stay ahead", risk: "Extreme Risk", safe: false }], score: 100, optimalAction: "Signal and pull right smoothly" }
];

const DEFAULT_BADGES = [
    { id: "first_step", title: "First Step", icon: "🏅", description: "Complete your first learning module", bonusXp: 50, req: "1 Module Completed" },
    { id: "hot_streak", title: "Hot Streak", icon: "🔥", description: "Achieve 10 consecutive correct quiz answers", bonusXp: 100, req: "10 Answer Streak" },
    { id: "knowledge_seeker", title: "Knowledge Seeker", icon: "📚", description: "Complete 5 learning modules", bonusXp: 200, req: "5 Modules Completed" },
    { id: "perfect_driver", title: "Perfect Driver", icon: "🎯", description: "Achieve a perfect 100% quiz score", bonusXp: 150, req: "100% Quiz Score" },
    { id: "night_hawk", title: "Night Hawk", icon: "🌙", description: "Complete evening safety assessments", bonusXp: 100, req: "Night Assessment" },
    { id: "safety_sentinel", title: "Safety Sentinel", icon: "🛡️", description: "Master all driver decision scenarios", bonusXp: 300, req: "All Scenarios Cleared" }
];

// Initialize default data arrays
State.mobileModules = [...DEFAULT_MOBILE_MODULES];
State.modules = [...DEFAULT_MODULES];
State.questions = [...DEFAULT_QUESTIONS];
State.scenarios = [...DEFAULT_SCENARIOS];
State.badges = [...DEFAULT_BADGES];

// ═══════════════════════════════════════════════════════════════
// 4. DOM REFERENCES
// ═══════════════════════════════════════════════════════════════
const $ = id => document.getElementById(id);

const DOM = {
    // Top Bar & Navigation
    pageTitle: $('page-title'),
    pageSubtitle: $('page-subtitle'),
    refreshBtn: $('refresh-btn'),
    menuToggle: $('menu-toggle'),
    sidebar: $('sidebar'),
    connectionStatus: $('connection-status'),
    navItems: document.querySelectorAll('.nav-item'),
    tabContents: document.querySelectorAll('.tab-content'),

    // Dashboard Metrics
    metricOnline: $('metric-online'),
    metricDevices: $('metric-total-devices'),
    metricQuizzes: $('metric-total-quizzes'),
    metricTotalXp: $('metric-total-xp'),

    // Sidebar Badges
    badgeUsers: $('badge-users'),
    badgeModules: $('badge-modules'),
    badgeQuizzes: $('badge-quizzes'),
    badgeScenarios: $('badge-scenarios'),
    badgeAi: $('badge-ai'),
    badgeDevices: $('badge-devices'),
    badgeLogins: $('badge-logins'),
    badgeAudit: $('badge-audit'),

    // Data Containers
    activityFeed: $('activity-feed'),
    usersList: $('users-list'),
    modulesList: $('modules-list'),
    quizzesList: $('quizzes-list'),
    questionsList: $('questions-list'),
    scenariosList: $('scenarios-list'),
    progressList: $('progress-list'),
    badgesCatalogList: $('badges-catalog-list'),
    rankHistoryList: $('rank-history-list'),
    aiActivityList: $('ai-activity-list'),
    devicesList: $('devices-list'),
    loginsList: $('logins-list'),
    auditList: $('audit-list'),
    analyticsSummaryTable: $('analytics-summary-table'),

    // Modals
    profileModalOverlay: $('profile-modal-overlay'),
    moduleModalOverlay: $('module-modal-overlay'),
    questionModalOverlay: $('question-modal-overlay'),
    deleteModalOverlay: $('delete-modal-overlay'),
    installModalOverlay: $('install-modal-overlay')
};

// ═══════════════════════════════════════════════════════════════
// 5. FIRESTORE REAL-TIME SYNCHRONIZATION
// ═══════════════════════════════════════════════════════════════

function startListeners() {
    if (!db) {
        setOfflineMode();
        return;
    }

    // 1. Users Stream
    db.collection('users').onSnapshot(snap => {
        State.users = [];
        snap.forEach(doc => {
            const data = doc.data();
            data.id = doc.id;
            State.users.push(data);
        });
        updateMetrics();
        renderUsersList();
        renderDevicesList();
        updateLeaderboardAndRanks();
        console.log(`👤 Users synced: ${State.users.length}`);
    }, err => console.warn('Users listener:', err));

    // 2. User Progress Stream
    db.collection('user_progress').onSnapshot(snap => {
        State.progress = [];
        snap.forEach(doc => {
            const data = doc.data();
            data.userId = doc.id;
            State.progress.push(data);
        });
        updateMetrics();
        updateLeaderboardAndRanks();
        renderProgressList();
    }, err => console.warn('Progress listener:', err));

    // 3. Quiz Attempts Stream
    db.collection('quiz_attempts').orderBy('timestamp', 'desc').limit(100).onSnapshot(snap => {
        State.quizzes = [];
        snap.forEach(doc => {
            const data = doc.data();
            data.id = doc.id;
            State.quizzes.push(data);
        });
        updateMetrics();
        renderQuizzesList();
        renderActivityFeed();
        updateQuizChart();
    }, err => console.warn('Quizzes listener:', err));

    // 4. Logins / Activity Stream
    db.collection('user_logins').orderBy('timestamp', 'desc').limit(100).onSnapshot(snap => {
        State.logins = [];
        snap.forEach(doc => {
            const data = doc.data();
            data.id = doc.id;
            State.logins.push(data);
        });
        renderLoginsList();
        renderActivityFeed();
    }, err => console.warn('Logins listener:', err));

    // 5. Security Audit Stream
    db.collection('audit_logs').orderBy('timestamp', 'desc').limit(100).onSnapshot(snap => {
        State.audit = [];
        snap.forEach(doc => {
            const data = doc.data();
            data.id = doc.id;
            State.audit.push(data);
        });
        renderAuditList();
    }, err => console.warn('Audit listener:', err));

    // 6. AI Interactions Stream (if present)
    db.collection('ai_interactions').orderBy('timestamp', 'desc').limit(100).onSnapshot(snap => {
        State.aiQueries = [];
        snap.forEach(doc => {
            const data = doc.data();
            data.id = doc.id;
            State.aiQueries.push(data);
        });
        renderAiActivityList();
    }, () => {
        // Fallback demo AI interactions if collection is fresh
        if (State.aiQueries.length === 0) {
            State.aiQueries = [
                { id: "ai_1", userId: "camancho", prompt: "Why is overtaking on a solid line prohibited?", response: "A solid line signifies restricted visibility or hazard ahead. Overtaking risks head-on collision with oncoming vehicles.", topic: "Overtaking", timestamp: new Date(Date.now() - 3600000) },
                { id: "ai_2", userId: "user", prompt: "What should I do if an ambulance is behind me at a red light?", response: "Check for safe cross-traffic, move forward slightly and to the right if safe, allowing the emergency vehicle to pass without entering danger.", topic: "Emergency Procedures", timestamp: new Date(Date.now() - 7200000) }
            ];
            renderAiActivityList();
        }
    });

    // 7. System Module Settings Stream
    db.collection('system_settings').doc('modules').onSnapshot(doc => {
        if (doc.exists) {
            const data = doc.data();
            State.mobileModules.forEach(m => {
                if (data[m.id] !== undefined) {
                    m.enabled = data[m.id];
                }
            });
            renderMobileModulesList();
        }
    }, err => console.warn('Module settings listener:', err));

    setOnlineStatus(true);
}

function setOnlineStatus(online) {
    if (DOM.connectionStatus) {
        DOM.connectionStatus.innerHTML = `
            <span class="status-dot ${online ? 'pulse' : ''}" style="background:${online ? 'var(--emerald-green)' : 'var(--traffic-red)'}"></span>
            <span class="status-text font-caption">${online ? 'Connected to Firestore' : 'Local Offline Mode'}</span>
        `;
    }
}

function setOfflineMode() {
    setOnlineStatus(false);
    renderModulesList();
    renderQuestionsList();
    renderScenariosList();
    renderBadgesCatalogList();
}

// ═══════════════════════════════════════════════════════════════
// 6. METRICS & COUNTERS
// ═══════════════════════════════════════════════════════════════

function updateMetrics() {
    const fiveMinsAgo = Date.now() - 5 * 60 * 1000;
    const onlineCount = State.users.filter(u => {
        if (u.isOnline === true) return true;
        if (u.lastActive) {
            const t = u.lastActive.toMillis ? u.lastActive.toMillis() : new Date(u.lastActive).getTime();
            return t > fiveMinsAgo;
        }
        return false;
    }).length;

    let totalXp = 0;
    State.progress.forEach(p => totalXp += (p.totalXp || p.xp || 0));

    if (DOM.metricOnline) DOM.metricOnline.textContent = onlineCount;
    if (DOM.metricDevices) DOM.metricDevices.textContent = State.users.length;
    if (DOM.metricQuizzes) DOM.metricQuizzes.textContent = State.quizzes.length;
    if (DOM.metricTotalXp) DOM.metricTotalXp.textContent = totalXp.toLocaleString();

    // Update Sidebar Badges
    if (DOM.badgeUsers) DOM.badgeUsers.textContent = State.users.length;
    if (DOM.badgeQuizzes) DOM.badgeQuizzes.textContent = State.quizzes.length;
    if (DOM.badgeDevices) DOM.badgeDevices.textContent = State.users.filter(u => u.deviceModel).length || State.users.length;
    if (DOM.badgeLogins) DOM.badgeLogins.textContent = State.logins.length;
    if (DOM.badgeAudit) DOM.badgeAudit.textContent = State.audit.length;
    if (DOM.badgeAi) DOM.badgeAi.textContent = State.aiQueries.length;
}

// ═══════════════════════════════════════════════════════════════
// 7. TAB NAVIGATION & SUBTABS
// ═══════════════════════════════════════════════════════════════

const TAB_TITLES = {
    'dashboard': { title: 'Dashboard', subtitle: 'Real-Time System Overview' },
    'users': { title: 'User Management', subtitle: 'Driver Directory & Account Control' },
    'modules': { title: 'Road Safety Modules', subtitle: 'Educational Curriculum & Topic Manager' },
    'quizzes': { title: 'Quiz Bank & Submissions', subtitle: 'Questions Repository & Real-Time Exam Results' },
    'scenarios': { title: 'Driver Decisions', subtitle: 'Simulation Scenarios & Risk Assessment' },
    'gamification': { title: 'Gamification & Ranks', subtitle: 'Cadet Leaderboard, Badges & Rank Movements' },
    'ai-activity': { title: 'AI Road Tutor', subtitle: 'Cadet Queries & Educational Assistant Analytics' },
    'devices': { title: 'Device Fleet', subtitle: 'Connected Mobile Telemetry & Hardware' },
    'logins': { title: 'Activity History', subtitle: 'Global Authentication & System Event Stream' },
    'analytics': { title: 'Analytics & Reports', subtitle: 'System Performance & Educational Insights' },
    'audit': { title: 'Security Audit Logs', subtitle: 'Privileged Administrative Ledger' },
    'settings': { title: 'System Settings', subtitle: 'Configuration, Thresholds & Role Controls' }
};

DOM.navItems.forEach(item => {
    item.addEventListener('click', () => {
        const tab = item.dataset.tab;
        switchTab(tab);
    });
});

function switchTab(tab) {
    State.activeTab = tab;
    DOM.navItems.forEach(n => n.classList.toggle('active', n.dataset.tab === tab));
    DOM.tabContents.forEach(s => s.classList.toggle('active', s.id === `tab-${tab}`));

    if (TAB_TITLES[tab]) {
        DOM.pageTitle.textContent = TAB_TITLES[tab].title;
        DOM.pageSubtitle.textContent = TAB_TITLES[tab].subtitle;
    }

    // Lazy renders
    if (tab === 'modules') renderModulesList();
    if (tab === 'quizzes') renderQuizzesList();
    if (tab === 'scenarios') renderScenariosList();
    if (tab === 'gamification') { renderProgressList(); renderBadgesCatalogList(); renderRankHistoryList(); }
    if (tab === 'ai-activity') renderAiActivityList();
    if (tab === 'analytics') renderAnalyticsView();
}

// Subtab Switchers
document.querySelectorAll('.sub-tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        const target = btn.dataset.subtab;
        const parent = btn.closest('.tab-content');
        parent.querySelectorAll('.sub-tab-btn').forEach(b => b.classList.toggle('active', b === btn));
        parent.querySelectorAll('.sub-tab-content').forEach(c => c.classList.toggle('active', c.id === `sub-tab-${target}`));
    });
});

// ═══════════════════════════════════════════════════════════════
// 8. USER MANAGEMENT & PROFILE INSPECTOR
// ═══════════════════════════════════════════════════════════════

function renderUsersList() {
    if (!DOM.usersList) return;
    let list = [...State.users];

    // Search filter
    const searchInput = $('search-users');
    const q = (searchInput ? searchInput.value : State.searchQuery || '').toLowerCase().trim();
    if (q) {
        list = list.filter(u =>
            (u.name || '').toLowerCase().includes(q) ||
            (u.username || '').toLowerCase().includes(q) ||
            (u.email || '').toLowerCase().includes(q) ||
            (u.contact || '').toLowerCase().includes(q) ||
            (u.deviceModel || '').toLowerCase().includes(q) ||
            (u.role || '').toLowerCase().includes(q) ||
            (u.gender || '').toLowerCase().includes(q)
        );
    }

    // Filter chips
    if (State.userFilter === 'online') list = list.filter(u => u.isOnline);
    else if (State.userFilter === 'offline') list = list.filter(u => !u.isOnline);
    else if (State.userFilter === 'admin') list = list.filter(u => (u.role || '').toLowerCase() === 'admin');
    else if (State.userFilter === 'cadet') list = list.filter(u => (u.role || '').toLowerCase() !== 'admin');
    else if (State.userFilter === 'male') list = list.filter(u => (u.gender || '').toLowerCase() === 'male');
    else if (State.userFilter === 'female') list = list.filter(u => (u.gender || '').toLowerCase() === 'female');

    if (list.length === 0) {
        DOM.usersList.innerHTML = `
            <div class="empty-state">
                <span class="material-icons-round">person_search</span>
                <h3 class="font-h3">No Users Found</h3>
                <p class="font-body">No registered users match your selected filter (${State.userFilter}).</p>
            </div>
        `;
        return;
    }

    DOM.usersList.innerHTML = list.map(u => {
        const isOnline = !!u.isOnline;
        const role = (u.role || 'Cadet').toUpperCase();
        const progress = State.progress.find(p => p.userId === u.username || p.userId === u.id) || {};
        const xp = progress.totalXp || progress.xp || u.xp || 0;
        const level = progress.currentLevel || progress.level || u.level || 1;

        return `
            <div class="data-row">
                <div class="data-avatar user-avatar">
                    <span class="material-icons-round">${role === 'ADMIN' ? 'shield' : 'person'}</span>
                </div>
                <div class="data-main-info">
                    <div class="data-title font-body">${escapeHtml(u.name || u.username || 'Unnamed User')}</div>
                    <div class="data-subtitle font-body-sm">
                        <span>@${escapeHtml(u.username || u.id)}</span>
                        ${u.email ? `<span>· ${escapeHtml(u.email)}</span>` : ''}
                        <span>· Lv.${level} (${xp} XP)</span>
                    </div>
                </div>
                <div class="data-meta-cell">
                    <span class="role-tag ${role === 'ADMIN' ? 'admin' : 'cadet'} font-badge">${role}</span>
                    <span class="status-badge ${isOnline ? 'online' : 'offline'} font-badge">
                        <span class="badge-dot"></span>${isOnline ? 'Online' : 'Offline'}
                    </span>
                </div>
                <div class="data-actions">
                    <button class="btn btn-secondary font-button" onclick="viewUserProfile('${u.id || u.username}')" title="Inspect Complete Profile">
                        <span class="material-icons-round">visibility</span>
                        <span>Profile</span>
                    </button>
                    ${role !== 'ADMIN' ? `
                    <button class="btn btn-danger font-button" onclick="openDeleteModal('${u.id || u.username}')" title="Delete User">
                        <span class="material-icons-round">delete_forever</span>
                    </button>
                    ` : ''}
                </div>
            </div>
        `;
    }).join('');
}

// Multi-Tab Profile Inspector Modal
window.viewUserProfile = function(userId) {
    const user = State.users.find(u => u.id === userId || u.username === userId);
    if (!user) return;
    State.selectedUser = user;

    $('modal-profile-name').textContent = user.name || user.username;
    $('modal-profile-handle').textContent = `@${user.username || user.id} · ${user.email || 'No email'}`;

    renderProfileTab('p-overview');
    DOM.profileModalOverlay.classList.add('visible');
};

function renderProfileTab(tab) {
    const user = State.selectedUser;
    if (!user) return;

    const progress = State.progress.find(p => p.userId === user.username || p.userId === user.id) || {};
    const userQuizzes = State.quizzes.filter(q => q.userId === user.username || q.userId === user.id);
    const userLogins = State.logins.filter(l => l.userId === user.username || l.userId === user.id);

    const body = $('modal-profile-body');
    if (tab === 'p-overview') {
        body.innerHTML = `
            <div class="modal-metrics">
                <div class="modal-metric">
                    <div class="modal-metric-value font-statistic" style="color:var(--badge-gold);">${progress.currentLevel || 1}</div>
                    <div class="modal-metric-label font-caption">Current Level</div>
                </div>
                <div class="modal-metric">
                    <div class="modal-metric-value font-statistic" style="color:var(--emerald-green);">${progress.totalXp || 0}</div>
                    <div class="modal-metric-label font-caption">Total XP</div>
                </div>
                <div class="modal-metric">
                    <div class="modal-metric-value font-statistic" style="color:var(--info-blue);">${userQuizzes.length}</div>
                    <div class="modal-metric-label font-caption">Quizzes Taken</div>
                </div>
                <div class="modal-metric">
                    <div class="modal-metric-value font-statistic" style="color:#F59E0B;">${progress.currentStreak || 0}d</div>
                    <div class="modal-metric-label font-caption">Active Streak</div>
                </div>
            </div>
            <div class="modal-section-title font-label">Account Details</div>
            <div class="modal-detail-row"><span class="modal-detail-label font-caption">Gender:</span><span class="modal-detail-value font-body-sm">${user.gender || 'Not specified'}</span></div>
            <div class="modal-detail-row"><span class="modal-detail-label font-caption">Hardware Device:</span><span class="modal-detail-value font-body-sm">${user.deviceModel || 'Mobile Device'}</span></div>
            <div class="modal-detail-row"><span class="modal-detail-label font-caption">Account Role:</span><span class="role-tag ${user.role === 'admin' ? 'admin' : 'cadet'} font-badge">${(user.role || 'Cadet').toUpperCase()}</span></div>
        `;
    } else if (tab === 'p-learning') {
        const completed = (progress.completedModules || 'Traffic Signs, Right-of-Way').split(',').map(s => s.trim()).filter(Boolean);
        body.innerHTML = `
            <div class="modal-section-title font-label">Completed Curriculum Modules (${completed.length}/10)</div>
            ${completed.map(m => `
                <div class="modal-detail-row">
                    <span class="font-body-sm">📘 ${escapeHtml(m)}</span>
                    <span class="status-badge online font-badge">Completed</span>
                </div>
            `).join('') || '<p class="font-body-sm" style="color:var(--text-muted);">No modules completed yet.</p>'}
        `;
    } else if (tab === 'p-quizzes') {
        body.innerHTML = `
            <div class="modal-section-title font-label">Recent Quiz Assessments</div>
            ${userQuizzes.map(q => `
                <div class="modal-detail-row">
                    <div>
                        <div class="font-body-sm font-weight-semibold">${escapeHtml(q.topic || 'Road Safety Quiz')}</div>
                        <span class="font-caption">${q.score}/${q.totalQuestions || 5} (${q.percentage || Math.round(q.score/5*100)}%)</span>
                    </div>
                    <span class="status-badge ${q.passed ? 'online' : 'offline'} font-badge">${q.passed ? 'PASSED' : 'FAILED'}</span>
                </div>
            `).join('') || '<p class="font-body-sm" style="color:var(--text-muted);">No quiz attempts recorded yet.</p>'}
        `;
    } else if (tab === 'p-gamif') {
        const unlockedBadges = DEFAULT_BADGES.slice(0, 2);
        body.innerHTML = `
            <div class="modal-section-title font-label">Unlocked Badges & Honors</div>
            <div style="display:grid;grid-template-columns:1fr 1fr;gap:10px;">
                ${unlockedBadges.map(b => `
                    <div style="background:var(--navy-surface);padding:10px;border-radius:6px;display:flex;align-items:center;gap:8px;">
                        <span style="font-size:24px;">${b.icon}</span>
                        <div>
                            <div class="font-body-sm font-weight-semibold">${b.title}</div>
                            <span class="font-caption">+${b.bonusXp} XP</span>
                        </div>
                    </div>
                `).join('')}
            </div>
        `;
    } else if (tab === 'p-activity') {
        body.innerHTML = `
            <div class="modal-section-title font-label">Recent Session Activity</div>
            ${userLogins.map(l => `
                <div class="modal-detail-row">
                    <span class="font-body-sm">🔑 ${escapeHtml(l.action || 'Login')} from ${escapeHtml(l.deviceModel || 'Mobile')}</span>
                    <span class="font-caption">${formatRelativeTime(l.timestamp)}</span>
                </div>
            `).join('') || '<p class="font-body-sm" style="color:var(--text-muted);">No recent session records.</p>'}
        `;
    }
}

document.querySelectorAll('.profile-tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        document.querySelectorAll('.profile-tab-btn').forEach(b => b.classList.toggle('active', b === btn));
        renderProfileTab(btn.dataset.ptab);
    });
});

$('profile-modal-close').addEventListener('click', () => DOM.profileModalOverlay.classList.remove('visible'));
$('btn-close-profile-modal').addEventListener('click', () => DOM.profileModalOverlay.classList.remove('visible'));
$('btn-profile-delete-user').addEventListener('click', () => {
    DOM.profileModalOverlay.classList.remove('visible');
    if (State.selectedUser) openDeleteModal(State.selectedUser.id || State.selectedUser.username);
});

// ═══════════════════════════════════════════════════════════════
// 9. MODULE MANAGEMENT (Mobile Controls & Curriculum)
// ═══════════════════════════════════════════════════════════════

function renderMobileModulesList() {
    const container = $('mobile-modules-list');
    if (!container) return;
    container.innerHTML = State.mobileModules.map(m => `
        <div class="mobile-module-card ${m.enabled ? '' : 'disabled'}" id="card-${m.id}">
            <div class="mobile-module-info">
                <div class="mobile-module-title font-h3">
                    ${escapeHtml(m.title)}
                </div>
                <div class="mobile-module-desc font-body">
                    ${escapeHtml(m.description)}
                </div>
                <div class="mobile-module-badges">
                    <span class="tag-badge blue font-badge">${escapeHtml(m.typeBadge)}</span>
                    <span class="tag-badge gold font-badge">${escapeHtml(m.xpReward)}</span>
                </div>
            </div>
            <label class="switch" title="Toggle module availability to mobile users">
                <input type="checkbox" ${m.enabled ? 'checked' : ''} onchange="toggleMobileModule('${m.id}', this.checked)">
                <span class="slider"></span>
            </label>
        </div>
    `).join('');
}

window.toggleMobileModule = function(id, enabled) {
    const mod = State.mobileModules.find(m => m.id === id);
    if (!mod) return;
    mod.enabled = enabled;
    const card = document.getElementById(`card-${id}`);
    if (card) card.classList.toggle('disabled', !enabled);

    // Save module availability state to Firestore
    if (db) {
        db.collection('system_settings').doc('modules').set({
            [id]: enabled,
            updatedAt: firebase.firestore.FieldValue.serverTimestamp()
        }, { merge: true }).catch(err => console.warn('Module settings update:', err));

        // Emit security audit log to Firestore
        db.collection('audit_logs').add({
            action: 'RECORD_EDITED',
            adminId: State.currentAdmin,
            targetUser: 'System Curriculum',
            description: `${enabled ? 'Enabled' : 'Disabled'} module: ${mod.title}`,
            riskLevel: 'MEDIUM',
            timestamp: firebase.firestore.FieldValue.serverTimestamp()
        }).catch(err => console.warn('Audit log error:', err));
    }

    showToast(`${mod.title} is now ${enabled ? 'ENABLED' : 'DISABLED'} for learners.`, enabled ? 'success' : 'info');
};

function renderModulesList() {
    renderMobileModulesList();
    if (!DOM.modulesList) return;
    DOM.modulesList.innerHTML = State.modules.map((m, idx) => `
        <div class="module-card">
            <div>
                <div class="module-header">
                    <span class="font-caption" style="color:var(--badge-gold);">MODULE ${idx + 1}</span>
                    <span class="status-badge ${m.status === 'published' ? 'online' : 'offline'} font-badge">
                        ${m.status === 'published' ? 'PUBLISHED' : 'DRAFT'}
                    </span>
                </div>
                <h3 class="module-title">${escapeHtml(m.title)}</h3>
                <p class="module-desc">${escapeHtml(m.description)}</p>
                <div class="module-tip-box">
                    <strong>💡 Safety Tip:</strong> ${escapeHtml(m.tip)}
                </div>
            </div>
            <div class="module-footer">
                <span class="font-caption" style="color:var(--text-secondary);">👥 ${m.completions || 0} Cadets Completed</span>
                <div style="display:flex;gap:6px;">
                    <button class="btn btn-secondary font-button" onclick="toggleModulePublish('${m.id}')" title="Publish/Unpublish">
                        <span class="material-icons-round">${m.status === 'published' ? 'visibility_off' : 'visibility'}</span>
                    </button>
                    <button class="btn btn-secondary font-button" onclick="editModule('${m.id}')" title="Edit Module">
                        <span class="material-icons-round">edit</span>
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

window.openModuleEditor = function() {
    $('module-modal-title').textContent = 'Create Road Safety Module';
    $('module-input-title').value = '';
    $('module-input-desc').value = '';
    $('module-input-tip').value = '';
    DOM.moduleModalOverlay.classList.add('visible');
};

window.editModule = function(id) {
    const mod = State.modules.find(m => m.id === id);
    if (!mod) return;
    $('module-modal-title').textContent = 'Edit Road Safety Module';
    $('module-input-title').value = mod.title;
    $('module-input-desc').value = mod.description;
    $('module-input-tip').value = mod.tip;
    DOM.moduleModalOverlay.classList.add('visible');
};

window.toggleModulePublish = function(id) {
    const mod = State.modules.find(m => m.id === id);
    if (!mod) return;
    mod.status = mod.status === 'published' ? 'draft' : 'published';
    renderModulesList();
    showToast(`Module "${mod.title}" set to ${mod.status.toUpperCase()}.`, 'success');
};

$('module-modal-close').addEventListener('click', () => DOM.moduleModalOverlay.classList.remove('visible'));
$('btn-cancel-module').addEventListener('click', () => DOM.moduleModalOverlay.classList.remove('visible'));
$('btn-save-module').addEventListener('click', () => {
    const title = $('module-input-title').value.trim();
    const desc = $('module-input-desc').value.trim();
    const tip = $('module-input-tip').value.trim();
    if (!title) { showToast('Please enter a module title.', 'error'); return; }

    State.modules.push({
        id: `mod_${Date.now()}`,
        title,
        description: desc,
        tip,
        topic: $('module-input-topic').value,
        difficulty: $('module-input-difficulty').value,
        status: 'published',
        completions: 0
    });
    DOM.moduleModalOverlay.classList.remove('visible');
    renderModulesList();
    showToast('Module created and published successfully.', 'success');
});

// ═══════════════════════════════════════════════════════════════
// 10. QUIZ MANAGEMENT & QUESTION BANK
// ═══════════════════════════════════════════════════════════════

function renderQuizzesList() {
    if (!DOM.quizzesList) return;
    let list = [...State.quizzes];

    const searchInput = $('search-quizzes');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(quiz =>
            (quiz.topic || '').toLowerCase().includes(q) ||
            (quiz.userId || '').toLowerCase().includes(q) ||
            (quiz.username || '').toLowerCase().includes(q)
        );
    }

    if (State.quizFilter === 'passed') list = list.filter(q => q.passed);
    else if (State.quizFilter === 'failed') list = list.filter(q => !q.passed);

    if (list.length === 0) {
        DOM.quizzesList.innerHTML = `
            <div class="empty-state">
                <span class="material-icons-round">rate_review</span>
                <h3 class="font-h3">No Quiz Submissions Found</h3>
                <p class="font-body">No quiz attempts match your criteria (${State.quizFilter}).</p>
            </div>
        `;
        return;
    }

    DOM.quizzesList.innerHTML = list.map(q => `
        <div class="data-row">
            <div class="data-avatar" style="background:${q.passed ? 'var(--emerald-green-dim)' : 'var(--traffic-red-dim)'};">
                <span class="material-icons-round" style="color:${q.passed ? 'var(--emerald-green)' : 'var(--traffic-red)'};">
                    ${q.passed ? 'check_circle' : 'cancel'}
                </span>
            </div>
            <div class="data-main-info">
                <div class="data-title font-body">${escapeHtml(q.topic || 'Traffic Rules Quiz')}</div>
                <div class="data-subtitle font-body-sm">
                    <span>Cadet: @${escapeHtml(q.userId || q.username || 'cadet')}</span>
                    <span>· Score: ${q.score}/${q.totalQuestions || 5} (${q.percentage || Math.round(q.score/5*100)}%)</span>
                </div>
            </div>
            <div class="data-meta-cell">
                <span class="status-badge ${q.passed ? 'online' : 'offline'} font-badge">
                    ${q.passed ? 'PASSED' : 'FAILED'}
                </span>
                <span class="font-caption">${formatRelativeTime(q.timestamp)}</span>
            </div>
        </div>
    `).join('');
}

function renderQuestionsList() {
    if (!DOM.questionsList) return;
    let list = [...State.questions];

    const searchInput = $('search-questions');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(question =>
            (question.text || '').toLowerCase().includes(q) ||
            (question.exp || '').toLowerCase().includes(q) ||
            (question.difficulty || '').toLowerCase().includes(q) ||
            (question.options || []).some(opt => (opt || '').toLowerCase().includes(q))
        );
    }

    if (State.questionFilter && State.questionFilter !== 'all') {
        list = list.filter(question => (question.difficulty || '').toLowerCase() === State.questionFilter.toLowerCase());
    }

    if (list.length === 0) {
        DOM.questionsList.innerHTML = `
            <div class="empty-state">
                <span class="material-icons-round">quiz</span>
                <h3 class="font-h3">No Questions Found</h3>
                <p class="font-body">No questions match your search or difficulty filter.</p>
            </div>
        `;
        return;
    }

    DOM.questionsList.innerHTML = list.map((q, idx) => `
        <div class="question-card">
            <div class="question-header">
                <div class="question-text"><strong>Q${idx + 1}:</strong> ${escapeHtml(q.text)}</div>
                <span class="role-tag cadet font-badge">${q.difficulty} (${q.points} XP)</span>
            </div>
            <div class="question-options-grid">
                ${q.options.map((opt, oIdx) => `
                    <div class="question-opt ${oIdx === q.correct ? 'correct' : ''}">
                        <strong>${String.fromCharCode(65 + oIdx)}:</strong> ${escapeHtml(opt)}
                        ${oIdx === q.correct ? ' ✓ (Correct)' : ''}
                    </div>
                `).join('')}
            </div>
            <div class="question-explanation">
                <strong>Explanation:</strong> ${escapeHtml(q.exp)}
            </div>
        </div>
    `).join('');
}

window.openQuestionEditor = function() {
    DOM.questionModalOverlay.classList.add('visible');
};

$('question-modal-close').addEventListener('click', () => DOM.questionModalOverlay.classList.remove('visible'));
$('btn-cancel-question').addEventListener('click', () => DOM.questionModalOverlay.classList.remove('visible'));
$('btn-save-question').addEventListener('click', () => {
    const text = $('q-input-text').value.trim();
    const a = $('q-input-opt-a').value.trim();
    const b = $('q-input-opt-b').value.trim();
    const c = $('q-input-opt-c').value.trim();
    const d = $('q-input-opt-d').value.trim();
    const exp = $('q-input-exp').value.trim();
    if (!text || !a || !b) { showToast('Please fill in question and options.', 'error'); return; }

    State.questions.push({
        id: `q_${Date.now()}`,
        text,
        options: [a, b, c || "None", d || "None"],
        correct: parseInt($('q-input-correct').value),
        difficulty: $('q-input-diff').value,
        points: $('q-input-diff').value === 'Hard' ? 30 : 20,
        exp: exp || "Standard traffic safety rule."
    });
    DOM.questionModalOverlay.classList.remove('visible');
    renderQuestionsList();
    showToast('New question added to Question Bank.', 'success');
});

// ═══════════════════════════════════════════════════════════════
// 11. DRIVER DECISION SCENARIOS & SIMULATION
// ═══════════════════════════════════════════════════════════════

function renderScenariosList() {
    if (!DOM.scenariosList) return;
    let list = [...State.scenarios];

    const searchInput = $('search-scenarios');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(s =>
            (s.title || '').toLowerCase().includes(q) ||
            (s.prompt || '').toLowerCase().includes(q) ||
            (s.weather || '').toLowerCase().includes(q) ||
            (s.optimalAction || '').toLowerCase().includes(q)
        );
    }

    if (State.scenarioFilter && State.scenarioFilter !== 'all') {
        const sf = State.scenarioFilter.toLowerCase();
        list = list.filter(s =>
            (s.title || '').toLowerCase().includes(sf) ||
            (s.prompt || '').toLowerCase().includes(sf) ||
            (s.weather || '').toLowerCase().includes(sf)
        );
    }

    if (list.length === 0) {
        DOM.scenariosList.innerHTML = `
            <div class="empty-state">
                <span class="material-icons-round">alt_route</span>
                <h3 class="font-h3">No Scenarios Found</h3>
                <p class="font-body">No driving scenarios match your filter (${State.scenarioFilter}).</p>
            </div>
        `;
        return;
    }

    DOM.scenariosList.innerHTML = list.map((s, idx) => `
        <div class="scenario-card">
            <div class="scenario-header">
                <h3 class="font-h3"><span class="material-icons-round" style="color:var(--badge-gold);">alt_route</span> Scenario ${idx + 1}: ${escapeHtml(s.title)}</h3>
                <span class="role-tag cadet font-badge">Speed: ${s.speed} · ${s.weather}</span>
            </div>
            <p class="scenario-prompt">${escapeHtml(s.prompt)}</p>
            <div class="decision-options-list">
                ${s.options.map((opt, oIdx) => `
                    <div class="decision-option-item ${opt.safe ? 'safe' : ''}">
                        <div>
                            <strong>Option ${String.fromCharCode(65 + oIdx)}:</strong> ${escapeHtml(opt.label)}
                        </div>
                        <span class="status-badge ${opt.safe ? 'online' : 'offline'} font-badge">${opt.risk}</span>
                    </div>
                `).join('')}
            </div>
            <div style="margin-top:14px;display:flex;justify-content:space-between;align-items:center;">
                <span class="font-caption" style="color:var(--emerald-green);">🎯 Optimal Decision: ${escapeHtml(s.optimalAction)}</span>
                <span class="font-caption">Safety Score: <strong>${s.score}%</strong></span>
            </div>
        </div>
    `).join('');
}

// ═══════════════════════════════════════════════════════════════
// 12. GAMIFICATION, LEADERBOARD, BADGES & RANK HISTORY
// ═══════════════════════════════════════════════════════════════

function updateLeaderboardAndRanks() {
    renderProgressList();
    renderBadgesCatalogList();
    renderRankHistoryList();
}

function renderProgressList() {
    if (!DOM.progressList) return;
    const leaderboard = State.users.map(u => {
        const p = State.progress.find(pr => pr.userId === u.username || pr.userId === u.id) || {};
        return {
            user: u.username || u.name || 'cadet',
            name: u.name || u.username,
            xp: p.totalXp || p.xp || u.xp || 0,
            level: p.currentLevel || p.level || u.level || 1,
            streak: p.currentStreak || 0
        };
    }).sort((a, b) => b.xp - a.xp);

    if (leaderboard.length === 0) {
        DOM.progressList.innerHTML = `<div class="empty-state"><p class="font-body">No cadet standings recorded yet.</p></div>`;
        return;
    }

    DOM.progressList.innerHTML = leaderboard.map((l, idx) => `
        <div class="data-row">
            <div class="data-avatar" style="background:${idx === 0 ? 'rgba(212,168,67,0.2)' : 'var(--navy-surface)'};color:${idx === 0 ? 'var(--badge-gold)' : 'var(--text-primary)'};font-weight:bold;">
                #${idx + 1}
            </div>
            <div class="data-main-info">
                <div class="data-title font-body">${escapeHtml(l.name)}</div>
                <div class="data-subtitle font-body-sm">
                    <span>@${escapeHtml(l.user)}</span>
                    <span>· 🔥 ${l.streak} day streak</span>
                </div>
            </div>
            <div class="data-meta-cell">
                <span class="font-statistic" style="font-size:20px;color:var(--emerald-green);">${l.xp.toLocaleString()} XP</span>
                <span class="role-tag cadet font-badge">LEVEL ${l.level}</span>
            </div>
        </div>
    `).join('');
}

function renderBadgesCatalogList() {
    if (!DOM.badgesCatalogList) return;
    DOM.badgesCatalogList.innerHTML = State.badges.map(b => `
        <div class="badge-item-card">
            <div class="badge-icon-lg">${b.icon}</div>
            <div class="badge-info-wrap">
                <h4 class="font-h3" style="font-size:16px;">${escapeHtml(b.title)}</h4>
                <p class="font-body-sm" style="color:var(--text-secondary);margin:4px 0;">${escapeHtml(b.description)}</p>
                <div style="display:flex;justify-content:space-between;align-items:center;margin-top:6px;">
                    <span class="font-caption" style="color:var(--badge-gold);">+${b.bonusXp} Bonus XP</span>
                    <span class="font-caption" style="color:var(--text-muted);">${escapeHtml(b.req)}</span>
                </div>
            </div>
        </div>
    `).join('');
}

function renderRankHistoryList() {
    if (!DOM.rankHistoryList) return;
    const history = [
        { user: "camancho", oldRank: 3, newRank: 1, delta: "+2 Positions", reason: "Scored 100% on Traffic Rules Exam", time: "2 hours ago" },
        { user: "user", oldRank: 2, newRank: 2, delta: "Maintained", reason: "Completed Right-of-Way Module", time: "5 hours ago" }
    ];

    DOM.rankHistoryList.innerHTML = history.map(h => `
        <div class="rank-history-card">
            <div>
                <div class="font-body font-weight-semibold">@${escapeHtml(h.user)}: #${h.oldRank} → #${h.newRank}</div>
                <span class="font-caption">${escapeHtml(h.reason)} · ${h.time}</span>
            </div>
            <span class="rank-delta-pill rank-delta-up">${h.delta}</span>
        </div>
    `).join('');
}

// ═══════════════════════════════════════════════════════════════
// 13. AI ROAD TUTOR ACTIVITY STREAM
// ═══════════════════════════════════════════════════════════════

function renderAiActivityList() {
    if (!DOM.aiActivityList) return;
    let list = [...State.aiQueries];

    const searchInput = $('search-ai');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(a =>
            (a.userId || '').toLowerCase().includes(q) ||
            (a.topic || '').toLowerCase().includes(q) ||
            (a.prompt || a.question || '').toLowerCase().includes(q) ||
            (a.response || a.answer || '').toLowerCase().includes(q)
        );
    }

    if (State.aiFilter && State.aiFilter !== 'all') {
        const af = State.aiFilter.toLowerCase();
        list = list.filter(a =>
            (a.topic || '').toLowerCase().includes(af) ||
            (a.prompt || a.question || '').toLowerCase().includes(af)
        );
    }

    if (list.length === 0) {
        DOM.aiActivityList.innerHTML = `
            <div class="empty-state">
                <span class="material-icons-round">smart_toy</span>
                <h3 class="font-h3">No AI Interactions Found</h3>
                <p class="font-body">No AI road safety queries match your search or filter.</p>
            </div>
        `;
        return;
    }

    DOM.aiActivityList.innerHTML = list.map(q => `
        <div class="ai-query-card">
            <div class="ai-query-header">
                <span class="font-body-sm font-weight-semibold">👤 Cadet @${escapeHtml(q.userId || 'user')} asked:</span>
                <span class="role-tag cadet font-badge">${escapeHtml(q.topic || 'General Safety')}</span>
            </div>
            <div class="ai-prompt-box">
                "${escapeHtml(q.prompt || q.question || '')}"
            </div>
            <div class="ai-response-box">
                <strong>🤖 AI Road Tutor Response:</strong> ${escapeHtml(q.response || q.answer || 'Provided rule explanation.')}
            </div>
            <div style="text-align:right;margin-top:8px;">
                <span class="font-caption">${formatRelativeTime(q.timestamp)}</span>
            </div>
        </div>
    `).join('');
}

// ═══════════════════════════════════════════════════════════════
// 14. DEVICES, LOGINS & AUDIT TRAILS
// ═══════════════════════════════════════════════════════════════

function renderDevicesList() {
    if (!DOM.devicesList) return;
    let list = [...State.users];

    const searchInput = $('search-devices');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(u =>
            (u.deviceModel || '').toLowerCase().includes(q) ||
            (u.name || '').toLowerCase().includes(q) ||
            (u.username || '').toLowerCase().includes(q)
        );
    }

    if (State.deviceFilter === 'online') list = list.filter(u => u.isOnline);
    else if (State.deviceFilter === 'offline') list = list.filter(u => !u.isOnline);

    if (list.length === 0) {
        DOM.devicesList.innerHTML = `<div class="empty-state"><p class="font-body">No device fleet records found matching filter (${State.deviceFilter}).</p></div>`;
        return;
    }

    DOM.devicesList.innerHTML = list.map(u => `
        <div class="data-row">
            <div class="data-avatar"><span class="material-icons-round">phone_android</span></div>
            <div class="data-main-info">
                <div class="data-title font-body">${escapeHtml(u.deviceModel || 'Android Mobile Device')}</div>
                <div class="data-subtitle font-body-sm">
                    <span>Assigned Cadet: @${escapeHtml(u.username || u.id)} (${escapeHtml(u.name || 'User')})</span>
                    <span>· OS: Android</span>
                </div>
            </div>
            <div class="data-meta-cell">
                <span class="status-badge ${u.isOnline ? 'online' : 'offline'} font-badge">
                    ${u.isOnline ? 'ONLINE' : 'OFFLINE'}
                </span>
            </div>
        </div>
    `).join('');
}

function renderLoginsList() {
    if (!DOM.loginsList) return;
    let list = [...State.logins];

    const searchInput = $('search-logins');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(l =>
            (l.userId || l.username || '').toLowerCase().includes(q) ||
            (l.action || '').toLowerCase().includes(q) ||
            (l.deviceModel || '').toLowerCase().includes(q) ||
            (l.ip || '').toLowerCase().includes(q)
        );
    }

    if (State.loginFilter === 'login') list = list.filter(l => (l.action || '').toLowerCase().includes('login'));
    else if (State.loginFilter === 'logout') list = list.filter(l => (l.action || '').toLowerCase().includes('logout'));
    else if (State.loginFilter === 'failed') list = list.filter(l => (l.action || '').toLowerCase().includes('fail'));

    if (list.length === 0) {
        DOM.loginsList.innerHTML = `<div class="empty-state"><p class="font-body">No activity stream logs found.</p></div>`;
        return;
    }

    DOM.loginsList.innerHTML = list.map(l => `
        <div class="data-row">
            <div class="data-avatar"><span class="material-icons-round">login</span></div>
            <div class="data-main-info">
                <div class="data-title font-body">@${escapeHtml(l.userId || l.username || 'user')} — ${escapeHtml((l.action || 'AUTH').toUpperCase())}</div>
                <div class="data-subtitle font-body-sm">
                    <span>${escapeHtml(l.deviceModel || 'Mobile')}</span>
                    ${l.ip ? `<span>· IP: ${escapeHtml(l.ip)}</span>` : ''}
                </div>
            </div>
            <div class="data-meta-cell">
                <span class="font-caption">${formatRelativeTime(l.timestamp)}</span>
            </div>
        </div>
    `).join('');
}

function renderAuditList() {
    if (!DOM.auditList) return;
    let list = [...State.audit];

    const searchInput = $('search-audit');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(a =>
            (a.action || '').toLowerCase().includes(q) ||
            (a.adminId || '').toLowerCase().includes(q) ||
            (a.targetUser || '').toLowerCase().includes(q) ||
            (a.details || '').toLowerCase().includes(q)
        );
    }

    if (State.auditFilter === 'high') list = list.filter(a => a.riskLevel === 'HIGH');
    else if (State.auditFilter === 'medium') list = list.filter(a => a.riskLevel === 'MEDIUM');
    else if (State.auditFilter === 'low') list = list.filter(a => a.riskLevel === 'LOW');

    if (list.length === 0) {
        DOM.auditList.innerHTML = `<div class="empty-state"><p class="font-body">No security audit records found.</p></div>`;
        return;
    }

    DOM.auditList.innerHTML = list.map(a => `
        <div class="data-row">
            <div class="data-avatar"><span class="material-icons-round">security</span></div>
            <div class="data-main-info">
                <div class="data-title font-body">${escapeHtml(a.action || 'SECURITY_EVENT')}</div>
                <div class="data-subtitle font-body-sm">
                    <span>Admin: @${escapeHtml(a.adminId || 'admin')}</span>
                    <span>· Target: ${escapeHtml(a.targetUser || 'System')}</span>
                </div>
            </div>
            <div class="data-meta-cell">
                <span class="role-tag ${a.riskLevel === 'HIGH' ? 'admin' : 'cadet'} font-badge">${a.riskLevel || 'LOW'} RISK</span>
                <span class="font-caption">${formatRelativeTime(a.timestamp)}</span>
            </div>
        </div>
    `).join('');
}

function renderActivityFeed() {
    if (!DOM.activityFeed) return;
    const combined = [
        ...State.quizzes.map(q => ({ title: `@${q.userId || 'user'} completed ${q.topic || 'Quiz'} (${q.score}/5)`, time: q.timestamp, icon: 'quiz' })),
        ...State.logins.map(l => ({ title: `@${l.userId || 'user'} — ${l.action || 'Session'}`, time: l.timestamp, icon: 'login' }))
    ].sort((a, b) => (b.time ? (b.time.toMillis ? b.time.toMillis() : new Date(b.time).getTime()) : 0) - (a.time ? (a.time.toMillis ? a.time.toMillis() : new Date(a.time).getTime()) : 0)).slice(0, 10);

    if (combined.length === 0) {
        DOM.activityFeed.innerHTML = `<div class="empty-state mini"><p class="font-body">Waiting for real-time events…</p></div>`;
        return;
    }

    DOM.activityFeed.innerHTML = combined.map(c => `
        <div class="data-row" style="padding:10px 14px;">
            <span class="material-icons-round" style="color:var(--badge-gold);font-size:20px;">${c.icon}</span>
            <div class="data-main-info">
                <div class="data-title font-body-sm">${escapeHtml(c.title)}</div>
            </div>
            <span class="font-caption">${formatRelativeTime(c.time)}</span>
        </div>
    `).join('');
}

// ═══════════════════════════════════════════════════════════════
// 15. ANALYTICS CHARTS & SYSTEM SUMMARY
// ═══════════════════════════════════════════════════════════════

let quizChartInstance = null;
let levelDistChartInstance = null;
let topicMasteryChartInstance = null;

function updateQuizChart() {
    const canvas = $('quizChart');
    if (!canvas) return;
    const passed = State.quizzes.filter(q => q.passed).length;
    const failed = State.quizzes.filter(q => !q.passed).length;

    if (quizChartInstance) quizChartInstance.destroy();
    quizChartInstance = new Chart(canvas, {
        type: 'bar',
        data: {
            labels: ['Passed (≥70%)', 'Failed (<70%)'],
            datasets: [{
                label: 'Quiz Attempts',
                data: [passed, failed],
                backgroundColor: ['#10B981', '#EF4444'],
                borderRadius: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: {
                y: { beginAtZero: true, grid: { color: 'rgba(255,255,255,0.05)' } },
                x: { grid: { display: false } }
            }
        }
    });
}

function renderAnalyticsView() {
    updateLevelDistChart();
    updateTopicMasteryChart();
    renderSummaryTable();
}

function updateLevelDistChart() {
    const canvas = $('levelDistChart');
    if (!canvas) return;
    if (levelDistChartInstance) levelDistChartInstance.destroy();
    levelDistChartInstance = new Chart(canvas, {
        type: 'doughnut',
        data: {
            labels: ['Level 1 (Novice)', 'Level 2 (Cadet)', 'Level 3 (Scholar)', 'Level 4+ (Master)'],
            datasets: [{
                data: [3, 1, 0, 0],
                backgroundColor: ['#3B82F6', '#10B981', '#F59E0B', '#8B5CF6']
            }]
        },
        options: { responsive: true, maintainAspectRatio: false }
    });
}

function updateTopicMasteryChart() {
    const canvas = $('topicMasteryChart');
    if (!canvas) return;
    if (topicMasteryChartInstance) topicMasteryChartInstance.destroy();
    topicMasteryChartInstance = new Chart(canvas, {
        type: 'radar',
        data: {
            labels: ['Right-of-Way', 'Traffic Signs', 'Speed Mgmt', 'Pedestrian Safety', 'Overtaking'],
            datasets: [{
                label: 'Cadet Mastery %',
                data: [85, 90, 75, 95, 60],
                backgroundColor: 'rgba(212, 168, 67, 0.2)',
                borderColor: '#D4A843'
            }]
        },
        options: { responsive: true, maintainAspectRatio: false }
    });
}

function renderSummaryTable() {
    if (!DOM.analyticsSummaryTable) return;
    DOM.analyticsSummaryTable.innerHTML = `
        <div style="display:grid;grid-template-columns:repeat(3, 1fr);gap:16px;">
            <div class="modal-detail-row" style="flex-direction:column;align-items:flex-start;">
                <span class="font-caption">Total Registered Cadets</span>
                <span class="font-statistic" style="font-size:24px;color:var(--text-primary);">${State.users.length}</span>
            </div>
            <div class="modal-detail-row" style="flex-direction:column;align-items:flex-start;">
                <span class="font-caption">Average Quiz Pass Rate</span>
                <span class="font-statistic" style="font-size:24px;color:var(--emerald-green);">
                    ${State.quizzes.length ? Math.round(State.quizzes.filter(q => q.passed).length / State.quizzes.length * 100) : 100}%
                </span>
            </div>
            <div class="modal-detail-row" style="flex-direction:column;align-items:flex-start;">
                <span class="font-caption">Active Safety Curriculum</span>
                <span class="font-statistic" style="font-size:24px;color:var(--info-blue);">${State.modules.length} Modules</span>
            </div>
        </div>
    `;
}

window.exportSystemReport = function() {
    const csvContent = "data:text/csv;charset=utf-8," +
        "Category,Metric,Value\n" +
        `Users,Total Cadets,${State.users.length}\n` +
        `Quizzes,Attempts,${State.quizzes.length}\n` +
        `Modules,Active Modules,${State.modules.length}\n` +
        `Security,Audit Logs Recorded,${State.audit.length}\n`;
    const encodedUri = encodeURI(csvContent);
    const link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", `RoadSafeAI_Report_${Date.now()}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    showToast('Executive summary report exported.', 'success');
};

// ═══════════════════════════════════════════════════════════════
// 16. DELETE USER WORKFLOW & MULTI-COLLECTION CLEANUP
// ═══════════════════════════════════════════════════════════════

window.openDeleteModal = function(userId) {
    const user = State.users.find(u => u.id === userId || u.username === userId);
    if (!user) return;
    State.userToDelete = user;

    const isSelf = (user.username || user.id).toLowerCase() === State.currentAdmin.toLowerCase();
    $('self-delete-warning').style.display = isSelf ? 'flex' : 'none';
    $('btn-confirm-delete').disabled = isSelf;

    $('delete-user-preview').innerHTML = `
        <div class="preview-row"><span class="preview-label font-caption">Name:</span><span class="preview-value font-body-sm">${escapeHtml(user.name || user.username)}</span></div>
        <div class="preview-row"><span class="preview-label font-caption">Username:</span><span class="preview-value font-body-sm">@${escapeHtml(user.username || user.id)}</span></div>
        <div class="preview-row"><span class="preview-label font-caption">Role:</span><span class="preview-value font-body-sm">${(user.role || 'Cadet').toUpperCase()}</span></div>
    `;

    DOM.deleteModalOverlay.classList.add('visible');
};

$('delete-modal-close').addEventListener('click', () => DOM.deleteModalOverlay.classList.remove('visible'));
$('btn-cancel-delete').addEventListener('click', () => DOM.deleteModalOverlay.classList.remove('visible'));

$('btn-confirm-delete').addEventListener('click', async () => {
    const user = State.userToDelete;
    if (!user || !db) {
        DOM.deleteModalOverlay.classList.remove('visible');
        return;
    }

    $('delete-loading-indicator').style.display = 'flex';
    $('btn-confirm-delete').disabled = true;

    try {
        const uId = user.id || user.username;
        const uName = user.username || user.id;

        // Atomic multi-collection cleanup
        const batch = db.batch();
        batch.delete(db.collection('users').doc(uId));
        batch.delete(db.collection('user_progress').doc(uName));

        // Emit security audit log
        const auditRef = db.collection('audit_logs').doc();
        batch.set(auditRef, {
            action: 'DELETE_USER',
            adminId: State.currentAdmin,
            targetUser: uName,
            riskLevel: 'HIGH',
            timestamp: firebase.firestore.FieldValue.serverTimestamp()
        });

        await batch.commit();

        DOM.deleteModalOverlay.classList.remove('visible');
        showToast(`User @${uName} and associated records permanently deleted.`, 'success');
    } catch (err) {
        console.error("Delete failed:", err);
        showToast('Error deleting user: ' + err.message, 'error');
    } finally {
        $('delete-loading-indicator').style.display = 'none';
        $('btn-confirm-delete').disabled = false;
    }
});

// ═══════════════════════════════════════════════════════════════
// 17. SYSTEM SETTINGS & APP INSTALL MODAL
// ═══════════════════════════════════════════════════════════════

window.saveSettings = function() {
    showToast('System configuration saved successfully.', 'success');
};

window.showAppInstallModal = function() {
    if (DOM.installModalOverlay) DOM.installModalOverlay.classList.add('visible');
};

const installModalClose = $('install-modal-close');
if (installModalClose) installModalClose.addEventListener('click', () => DOM.installModalOverlay.classList.remove('visible'));

const btnCloseInstallModal = $('btn-close-install-modal');
if (btnCloseInstallModal) btnCloseInstallModal.addEventListener('click', () => DOM.installModalOverlay.classList.remove('visible'));

// Dynamic QR Code generation - auto-detect local & public download page URLs
const qrUrlInput = $('install-qr-url-input');
const qrImg = $('install-qr-image');
const btnCopyQrUrl = $('btn-copy-qr-url');
const btnDownloadQrImg = $('btn-download-qr-img');
const btnOpenDownloadPage = $('btn-open-download-page');
const btnQrModePublic = $('btn-qr-mode-public');
const btnQrModeLocal = $('btn-qr-mode-local');
const qrDescText = $('qr-desc-text');

const PUBLIC_PAGE_URL = 'https://jiemmm03.github.io/Gamified-Road-Safety-Awareness/';
const localDownloadPageUrl = window.location.origin + window.location.pathname.replace(/\/[^\/]*$/, '') + '/download.html';

let currentQrUrl = PUBLIC_PAGE_URL;

function updateQrCode(url) {
    if (!qrImg) return;
    const targetUrl = url && url.trim() ? url.trim() : PUBLIC_PAGE_URL;
    currentQrUrl = targetUrl;
    qrImg.src = `https://api.qrserver.com/v1/create-qr-code/?size=220x220&data=${encodeURIComponent(targetUrl)}`;
    if (qrUrlInput) qrUrlInput.value = targetUrl;
    if (btnOpenDownloadPage) btnOpenDownloadPage.href = targetUrl;
}

// Default to Public GitHub URL for reliable mobile scanning anywhere
updateQrCode(PUBLIC_PAGE_URL);

if (btnQrModePublic) {
    btnQrModePublic.addEventListener('click', () => {
        btnQrModePublic.style.background = 'var(--emerald-green)';
        btnQrModePublic.style.color = '#fff';
        btnQrModePublic.style.border = 'none';
        if (btnQrModeLocal) {
            btnQrModeLocal.style.background = 'var(--navy-card)';
            btnQrModeLocal.style.color = 'var(--text-secondary)';
            btnQrModeLocal.style.border = '1px solid var(--navy-card-border)';
        }
        if (qrDescText) qrDescText.textContent = 'Point your Android camera at this code to open the public download page on any phone worldwide.';
        updateQrCode(PUBLIC_PAGE_URL);
    });
}

if (btnQrModeLocal) {
    btnQrModeLocal.addEventListener('click', () => {
        btnQrModeLocal.style.background = 'var(--electric-blue)';
        btnQrModeLocal.style.color = '#fff';
        btnQrModeLocal.style.border = 'none';
        if (btnQrModePublic) {
            btnQrModePublic.style.background = 'var(--navy-card)';
            btnQrModePublic.style.color = 'var(--text-secondary)';
            btnQrModePublic.style.border = '1px solid var(--navy-card-border)';
        }
        if (qrDescText) qrDescText.textContent = 'Point your Android camera at this code to download directly over local Wi-Fi (phone must be on same network).';
        updateQrCode(localDownloadPageUrl);
    });
}

if (btnCopyQrUrl) {
    btnCopyQrUrl.addEventListener('click', () => {
        const url = qrUrlInput ? qrUrlInput.value.trim() : downloadPageUrl;
        if (url) {
            navigator.clipboard.writeText(url).then(() => {
                showToast('Download page link copied to clipboard!', 'success');
            }).catch(() => {
                // Fallback for non-HTTPS contexts (local network)
                const tempInput = document.createElement('input');
                tempInput.value = url;
                document.body.appendChild(tempInput);
                tempInput.select();
                document.execCommand('copy');
                document.body.removeChild(tempInput);
                showToast('Download page link copied to clipboard!', 'success');
            });
        }
    });
}

if (btnDownloadQrImg && qrImg) {
    btnDownloadQrImg.addEventListener('click', () => {
        const link = document.createElement('a');
        link.href = qrImg.src;
        link.download = 'RoadSafeAI-Install-QRCode.png';
        link.target = '_blank';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        showToast('Downloading QR Code image...', 'info');
    });
}

// ═══════════════════════════════════════════════════════════════
// 18. UTILITIES & TOASTS
// ═══════════════════════════════════════════════════════════════

function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function formatRelativeTime(ts) {
    if (!ts) return 'just now';
    let date = ts.toDate ? ts.toDate() : new Date(ts);
    const diff = Math.floor((Date.now() - date.getTime()) / 1000);
    if (diff < 60) return `${diff}s ago`;
    if (diff < 3600) return `${Math.floor(diff / 60)}m ago`;
    if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`;
    return `${Math.floor(diff / 86400)}d ago`;
}

function showToast(msg, type = 'info', duration = 3500) {
    const container = $('toast-container');
    if (!container) return;
    const toast = document.createElement('div');
    toast.className = `toast toast-${type} font-body-sm`;
    toast.innerHTML = `
        <span class="material-icons-round" style="font-size:20px;">
            ${type === 'success' ? 'check_circle' : type === 'error' ? 'error' : 'info'}
        </span>
        <span>${escapeHtml(msg)}</span>
    `;
    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 300);
    }, duration);
}

// ═══════════════════════════════════════════════════════════════
// FILTER CHIPS & SEARCH EVENT HANDLERS
// ═══════════════════════════════════════════════════════════════

// Universal Filter Chips Handler across all tabs
document.querySelectorAll('.filter-chips').forEach(container => {
    container.addEventListener('click', e => {
        const chip = e.target.closest('.chip');
        if (!chip) return;

        // Update active class within this chip group
        container.querySelectorAll('.chip').forEach(c => c.classList.remove('active'));
        chip.classList.add('active');

        const filterVal = chip.dataset.filter || 'all';
        const tabContent = chip.closest('.tab-content, .sub-tab-content');
        if (!tabContent) return;

        const tabId = tabContent.id;
        if (tabId === 'tab-users') {
            State.userFilter = filterVal;
            renderUsersList();
        } else if (tabId === 'sub-tab-quiz-attempts') {
            State.quizFilter = filterVal;
            renderQuizzesList();
        } else if (tabId === 'sub-tab-quiz-bank') {
            State.questionFilter = filterVal;
            renderQuestionsList();
        } else if (tabId === 'tab-scenarios') {
            State.scenarioFilter = filterVal;
            renderScenariosList();
        } else if (tabId === 'tab-ai-activity') {
            State.aiFilter = filterVal;
            renderAiActivityList();
        } else if (tabId === 'tab-devices') {
            State.deviceFilter = filterVal;
            renderDevicesList();
        } else if (tabId === 'tab-logins') {
            State.loginFilter = filterVal;
            renderLoginsList();
        } else if (tabId === 'tab-audit') {
            State.auditFilter = filterVal;
            renderAuditList();
        }
    });
});

// Tab-Specific Live Search Inputs
document.querySelectorAll('input[id^="search-"]').forEach(input => {
    input.addEventListener('input', e => {
        const id = input.id;
        State.searchQuery = e.target.value;
        if (id === 'search-users') renderUsersList();
        else if (id === 'search-quizzes') renderQuizzesList();
        else if (id === 'search-questions') renderQuestionsList();
        else if (id === 'search-scenarios') renderScenariosList();
        else if (id === 'search-progress') renderProgressList();
        else if (id === 'search-ai') renderAiActivityList();
        else if (id === 'search-devices') renderDevicesList();
        else if (id === 'search-logins') renderLoginsList();
        else if (id === 'search-audit') renderAuditList();
        else renderUsersList();
    });
});

// Refresh button
DOM.refreshBtn.addEventListener('click', () => {
    DOM.refreshBtn.classList.add('spinning');
    setTimeout(() => DOM.refreshBtn.classList.remove('spinning'), 600);
    updateMetrics();
    renderUsersList();
    renderModulesList();
    renderQuizzesList();
    renderQuestionsList();
    renderScenariosList();
    renderProgressList();
    renderAiActivityList();
    renderDevicesList();
    renderLoginsList();
    renderAuditList();
    showToast('Real-time data refreshed.', 'info', 2000);
});

// Mobile menu toggle
DOM.menuToggle.addEventListener('click', () => {
    DOM.sidebar.classList.toggle('open');
});

// ═══════════════════════════════════════════════════════════════
// 19. BOOTSTRAP INITIALIZATION & SPLASH SCREEN
// ═══════════════════════════════════════════════════════════════

function dismissSplashScreen() {
    const splash = $('admin-splash-screen');
    const statusText = $('splash-status-text');
    if (!splash) return;

    if (statusText) statusText.textContent = 'Command Center Ready';

    setTimeout(() => {
        splash.classList.add('fade-out');
        setTimeout(() => {
            splash.style.display = 'none';
        }, 500);
    }, 450);
}

document.addEventListener('DOMContentLoaded', () => {
    console.log('🛡️ RoadSafe AI — Complete Mirror Platform v3.0');
    startListeners();
    renderModulesList();
    renderQuestionsList();
    renderScenariosList();
    renderBadgesCatalogList();
    
    // Smooth splash screen reveal
    setTimeout(dismissSplashScreen, 600);
});
