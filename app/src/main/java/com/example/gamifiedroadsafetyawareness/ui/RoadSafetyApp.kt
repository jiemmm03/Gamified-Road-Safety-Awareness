package com.example.gamifiedroadsafetyawareness.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.ManageAccounts
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.CloudSync
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import com.example.gamifiedroadsafetyawareness.ui.components.AppNavigationDrawer
import com.example.gamifiedroadsafetyawareness.ui.components.AppTopBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.audit.ActionType
import com.example.gamifiedroadsafetyawareness.audit.AuditManager
import com.example.gamifiedroadsafetyawareness.audit.Module
import com.example.gamifiedroadsafetyawareness.audit.RiskLevel
import com.example.gamifiedroadsafetyawareness.auth.AuthManager
import com.example.gamifiedroadsafetyawareness.auth.Permission
import com.example.gamifiedroadsafetyawareness.auth.RolePermissions
import com.example.gamifiedroadsafetyawareness.auth.UserRole
import com.example.gamifiedroadsafetyawareness.model.DecisionOption
import com.example.gamifiedroadsafetyawareness.model.LearningModule
import com.example.gamifiedroadsafetyawareness.model.MockData
import com.example.gamifiedroadsafetyawareness.model.UserAccount
import com.example.gamifiedroadsafetyawareness.model.XpManager
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import com.example.gamifiedroadsafetyawareness.ui.screens.AccessDeniedScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AdminAnswerReviewScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AdminDashboardScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AuditDashboardScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AnalyticsScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AssessmentScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.DashboardScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.FeedbackScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.LoginScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.MyProgressScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.ProfileScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.QuizScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.SignUpScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.SimulationScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.XpHistoryScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AdminXpManagementScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AdminModuleManagementScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AdminReportsScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AccountSecurityScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AdminUserManagementScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AiTutorScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.ModuleReviewScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.LearningHistoryScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.ModuleSummaryScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AdminModuleAnalyticsScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AdminQuizAttemptsScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.AdminManagementScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.SettingsScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.CloudMonitoringScreen
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptEntity
import com.example.gamifiedroadsafetyawareness.model.LanguageManager
import androidx.compose.material.icons.rounded.RateReview
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Settings
import com.example.gamifiedroadsafetyawareness.ui.theme.NavyPrimary
import com.example.gamifiedroadsafetyawareness.ui.theme.PoliceBlue
import com.example.gamifiedroadsafetyawareness.ui.theme.BadgeGold
import com.example.gamifiedroadsafetyawareness.ui.theme.PureWhite

sealed class Screen(
    val route: String,
    val title: String,
    val getIcon: @Composable () -> ImageVector,
    val requiredPermission: Permission
) {
    object Login : Screen("login", "Login", { Icons.Rounded.Person }, Permission.VIEW_DASHBOARD)
    object SignUp : Screen("signup", "Sign Up", { Icons.Rounded.Person }, Permission.VIEW_DASHBOARD)
    object Dashboard : Screen("dashboard", "Home", { Icons.Rounded.Home }, Permission.VIEW_DASHBOARD)
    object Simulation : Screen("simulation", "Simulation", { Icons.Rounded.Psychology }, Permission.LAUNCH_SIMULATION)
    object Feedback : Screen("feedback", "Feedback", { Icons.Rounded.Psychology }, Permission.LAUNCH_SIMULATION)
    object Assessment : Screen("assessment", "Learn", { Icons.AutoMirrored.Rounded.MenuBook }, Permission.VIEW_ASSESSMENT)
    object Gamification : Screen("gamification", "Progress", { Icons.AutoMirrored.Rounded.TrendingUp }, Permission.VIEW_GAMIFICATION)
    object Analytics : Screen("analytics", "AI Coach", { Icons.Rounded.Psychology }, Permission.VIEW_ANALYTICS)
    object AiTutor : Screen("ai_tutor", "RoadSafe AI", { Icons.Rounded.AutoAwesome }, Permission.VIEW_ANALYTICS)
    object Profile : Screen("profile", "Profile", { Icons.Rounded.Person }, Permission.VIEW_PROFILE)
    object AccountSecurity : Screen("account_security", "Account & Security", { Icons.Rounded.Security }, Permission.VIEW_PROFILE)
    object Settings : Screen("settings", "Settings", { Icons.Rounded.Settings }, Permission.VIEW_PROFILE)
    object AdminDashboard : Screen("admin", "Dashboard", { Icons.Rounded.Dashboard }, Permission.VIEW_ADMIN_PANEL)
    object AdminManagement : Screen("admin_management", "Management", { Icons.Rounded.ManageAccounts }, Permission.VIEW_ADMIN_PANEL)
    object AuditDashboard : Screen("audit_dashboard", "Audit Trail", { Icons.Rounded.AdminPanelSettings }, Permission.VIEW_AUDIT_LOGS)
    object QuizTaking : Screen("quiz_taking", "Quiz", { Icons.Rounded.Quiz }, Permission.VIEW_ASSESSMENT)
    object XpHistory : Screen("xp_history", "XP History", { Icons.Rounded.History }, Permission.VIEW_GAMIFICATION)
    object ModuleReview : Screen("module_review", "Review Answers", { Icons.Rounded.RateReview }, Permission.VIEW_GAMIFICATION)
    object LearningHistory : Screen("learning_history", "Learning History", { Icons.Rounded.History }, Permission.VIEW_GAMIFICATION)
    object ModuleSummary : Screen("module_summary", "Module Summary", { Icons.Rounded.Quiz }, Permission.VIEW_GAMIFICATION)
    object AdminXpManagement : Screen("admin_xp", "Manage Gamification", { Icons.Rounded.EmojiEvents }, Permission.VIEW_ADMIN_PANEL)
    object AdminUserManagement : Screen("admin_users", "Users", { Icons.Rounded.ManageAccounts }, Permission.MANAGE_USERS)
    object AdminModuleManagement : Screen("admin_modules", "Modules", { Icons.AutoMirrored.Rounded.MenuBook }, Permission.MANAGE_CONTENT)
    object AdminReports : Screen("admin_reports", "Reports & Analytics", { Icons.Rounded.Analytics }, Permission.VIEW_SYSTEM_OVERVIEW)
    object AdminModuleAnalytics : Screen("admin_module_analytics", "Module Analytics", { Icons.Rounded.Analytics }, Permission.VIEW_SYSTEM_OVERVIEW)
    object AdminQuizAttempts : Screen("admin_quiz_attempts", "Quiz Attempts", { Icons.Rounded.Quiz }, Permission.VIEW_SYSTEM_OVERVIEW)
    object AdminAnswerReview : Screen("admin_answer_review", "Answer Review", { Icons.Rounded.RateReview }, Permission.VIEW_SYSTEM_OVERVIEW)
    object CloudMonitoring : Screen("cloud_monitoring", "Cloud Monitor", { Icons.Rounded.CloudSync }, Permission.VIEW_SYSTEM_OVERVIEW)
}

@Composable
fun RoadSafetyApp() {
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }
    val auditManager = remember { AuditManager(context) }
    val xpManager = remember { XpManager(context) }
    val languageManager = remember { LanguageManager(context) }
    var languageCode by remember { mutableStateOf(languageManager.getLanguage()) }

    // Real back-navigation history.
    val screenStack = remember { mutableStateListOf<Screen>(Screen.Login) }
    val currentScreen: Screen = screenStack.last()
    var isCheckingSession by remember { mutableStateOf(true) }
    // Bumped on every login/logout so a new session never resumes a previous session's
    // retained screen state (see SaveableStateProvider key below).
    var sessionId by remember { mutableIntStateOf(0) }

    var lastSubmittedOption by remember { mutableStateOf<DecisionOption?>(null) }
    var loggedInDisplayName by remember { mutableStateOf("") }
    var loggedInUsername by remember { mutableStateOf("") }
    var currentUserRole by remember { mutableStateOf(UserRole.USER) }
    var currentUserPermissions by remember { mutableStateOf<Set<Permission>>(emptySet()) }
    var activeQuizModuleId by remember { mutableStateOf("") }
    var activeAttemptId by remember { mutableStateOf(0L) }
    var activeAnalyticsQuizId by remember { mutableStateOf("") }
    var xpManagementPreselectUsername by remember { mutableStateOf<String?>(null) }
    var historyFilterAccount by remember { mutableStateOf<UserAccount?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun homeScreen(): Screen = when (currentUserRole) {
        UserRole.ADMIN -> Screen.AdminDashboard
        UserRole.USER -> Screen.Dashboard
    }

    /** Clears history and starts fresh at [screen] — for terminal transitions (login, logout,
     *  sign-up success) where "go back into what came before" isn't meaningful. */
    fun resetStackTo(screen: Screen) {
        screenStack.clear()
        screenStack.add(screen)
    }

    fun performLogout() {
        authManager.logout()
        currentUserRole = UserRole.USER
        currentUserPermissions = emptySet()
        loggedInDisplayName = ""
        loggedInUsername = ""
        sessionId++
        resetStackTo(Screen.Login)
    }

    LaunchedEffect(Unit) {
        val saved = authManager.getSavedSession()
        if (saved is com.example.gamifiedroadsafetyawareness.auth.LoginResult.Success) {
            currentUserRole = saved.role
            loggedInDisplayName = saved.displayName
            currentUserPermissions = saved.permissions
            loggedInUsername = authManager.getLoggedInUsername() ?: ""
            sessionId++
            val startScreen = when (saved.role) {
                UserRole.ADMIN -> Screen.AdminDashboard
                UserRole.USER -> Screen.Dashboard
            }
            screenStack.clear()
            screenStack.add(startScreen)
        } else {
            screenStack.clear()
            screenStack.add(Screen.Login)
        }
        isCheckingSession = false
    }

    val userProgress by remember(loggedInUsername) {
        if (loggedInUsername.isNotBlank()) xpManager.observeProgress(loggedInUsername) else flowOf(null)
    }.collectAsState(initial = null)
    var lastSimXpEarned by remember { mutableIntStateOf(0) }
    var lastSimLeveledUp by remember { mutableStateOf(false) }
    var lastSimNewLevel by remember { mutableIntStateOf(1) }

    fun logUserAction(
        actionType: ActionType,
        module: Module,
        description: String,
        riskLevel: RiskLevel = RiskLevel.LOW
    ) {
        auditManager.logAction(
            userId = loggedInUsername,
            fullName = loggedInDisplayName,
            username = loggedInUsername,
            role = currentUserRole.name,
            actionType = actionType,
            module = module,
            description = description,
            riskLevel = riskLevel
        )
    }

    fun navigateTo(screen: Screen) {
        if (screen == Screen.Login || screen == Screen.SignUp) {
            if (screenStack.lastOrNull() != screen) screenStack.add(screen)
            return
        }

        if (currentUserPermissions.contains(screen.requiredPermission)) {
            if (screenStack.lastOrNull() != screen) screenStack.add(screen)
        } else {
            Toast.makeText(
                context,
                "ACCESS DENIED: ${screen.requiredPermission.name.replace("_", " ")}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /** Bottom-nav tab taps reset history to a fresh root instead of pushing — otherwise hopping
     *  between tabs would grow the stack forever and back-mashing would walk through tab-switch
     *  history instead of behaving like a normal tab bar. */
    fun navigateToTab(screen: Screen) {
        if (screenStack.size == 1 && screenStack.last() == screen) return
        resetStackTo(screen)
    }

    fun goBack() {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.lastIndex)
            // Defense in depth: skip past any stale entry that's no longer permitted
            // (e.g. permissions changed mid-session) rather than exposing it.
            while (
                screenStack.size > 1 &&
                screenStack.last() != Screen.Login &&
                screenStack.last() != Screen.SignUp &&
                !currentUserPermissions.contains(screenStack.last().requiredPermission)
            ) {
                screenStack.removeAt(screenStack.lastIndex)
            }
        } else if (screenStack.last() != Screen.Login && screenStack.last() != homeScreen()) {
            screenStack[0] = homeScreen()
        }
        // Already at the true root (Login, or Home with nothing behind it) — nothing to do;
        // the system's native back behavior (exit/minimize) applies instead.
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val canGoBack = screenStack.size > 1 ||
        (screenStack.last() != Screen.Login && screenStack.last() != homeScreen())

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch { drawerState.close() }
    }

    BackHandler(enabled = !drawerState.isOpen && canGoBack) {
        goBack()
    }

    val userPrimaryScreens = listOf(
        Screen.Dashboard,
        Screen.Assessment,
        Screen.Gamification,
        Screen.Profile
    )

    val adminPrimaryScreens = listOf(
        Screen.AdminDashboard,
        Screen.AdminXpManagement,
        Screen.AdminReports,
        Screen.AuditDashboard,
        Screen.AdminManagement,
        Screen.Profile
    )

    val primaryScreens = when (currentUserRole) {
        UserRole.USER -> userPrimaryScreens
        UserRole.ADMIN -> adminPrimaryScreens
    }

    val showTopBar = currentScreen != Screen.Login
            && currentScreen != Screen.SignUp
            && primaryScreens.contains(currentScreen)

    if (isCheckingSession) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = BadgeGold)
        }
        return
    }

    when (currentScreen) {
        Screen.Login -> {
            if (authManager.hasActiveSession() && loggedInUsername.isNotBlank()) {
                resetStackTo(homeScreen())
                return
            }
            LoginScreen(
                onLoginSuccess = { role, displayName, permissions ->
                    currentUserRole = role
                    loggedInDisplayName = displayName
                    currentUserPermissions = permissions
                    loggedInUsername = authManager.getLoggedInUsername() ?: ""
                    sessionId++
                    resetStackTo(homeScreen())
                },
                onNavigateToSignUp = { navigateTo(Screen.SignUp) },
                authManager = authManager
            )
            return
        }
        Screen.SignUp -> {
            SignUpScreen(
                onSignUpSuccess = { role, displayName, permissions ->
                    currentUserRole = role
                    loggedInDisplayName = displayName
                    currentUserPermissions = permissions
                    loggedInUsername = authManager.getLoggedInUsername() ?: ""
                    sessionId++
                    resetStackTo(homeScreen())
                },
                onNavigateToLogin = { resetStackTo(Screen.Login) },
                authManager = authManager
            )
            return
        }
        else -> { /* Post-auth — handled below with scaffold */ }
    }

    if (!RolePermissions.hasPermission(currentUserRole, currentScreen.requiredPermission)) {
        AccessDeniedScreen(
            userRole = currentUserRole,
            requiredPermission = currentScreen.requiredPermission,
            onNavigateBack = { resetStackTo(homeScreen()) }
        )
        return
    }

    ProvideLocalizedContext(languageCode = languageCode) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentScreen != Screen.Login && currentScreen != Screen.SignUp,
        drawerContent = {
            AppNavigationDrawer(
                currentScreen = currentScreen,
                currentUserRole = currentUserRole,
                displayName = loggedInDisplayName,
                username = loggedInUsername,
                userProgress = userProgress,
                onNavigateTo = { screen ->
                    coroutineScope.launch {
                        drawerState.close()
                        if (primaryScreens.contains(screen)) {
                            navigateToTab(screen)
                        } else {
                            navigateTo(screen)
                        }
                    }
                },
                onLogout = {
                    coroutineScope.launch {
                        drawerState.close()
                        performLogout()
                    }
                },
                onClose = {
                    coroutineScope.launch { drawerState.close() }
                }
            )
        }
    ) {
    Scaffold(
        topBar = {
            if (showTopBar) {
                AppTopBar(
                    title = if (currentScreen == Screen.Dashboard) "RoadSafe AI"
                           else if (currentScreen == Screen.AdminDashboard) "Dashboard"
                           else currentScreen.title,
                    subtitle = when (currentScreen) {
                        Screen.Dashboard -> "Safety Command & Training"
                        Screen.AdminDashboard -> "System Overview & Status"
                        Screen.Assessment -> "Rules & Certification"
                        Screen.Gamification -> "Officer Progress & Ranks"
                        Screen.AdminManagement -> "Management Hub"
                        Screen.AdminReports -> "System Statistics & Trends"
                        Screen.AdminXpManagement -> "XP, Levels, Streaks & Rewards"
                        Screen.AuditDashboard -> "Security & Activity Records"
                        Screen.AdminUserManagement -> "User Accounts & Roles"
                        Screen.AdminModuleManagement -> "Training Content & Quizzes"
                        Screen.AdminAnswerReview -> "Submitted Quiz Attempts"
                        else -> null
                    },
                    onOpenDrawer = {
                        coroutineScope.launch { drawerState.open() }
                    },
                    canGoBack = false,
                    onBackClick = null,
                    userProgress = userProgress,
                    onNavigateToAiTutor = if (currentUserPermissions.contains(Permission.VIEW_ANALYTICS)) {
                        { navigateTo(Screen.AiTutor) }
                    } else null
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Keeps each screen's entire composition (scroll position, form inputs, filters,
            // in-progress quiz state, ...) alive-but-deactivated when navigated away from, and
            // restores it exactly as left when navigated back to — the same mechanism Jetpack
            // Navigation Compose uses internally. Keyed by session so a new login never resumes
            // a previous session's retained state.
            val stateHolder = rememberSaveableStateHolder()
            stateHolder.SaveableStateProvider(key = "$sessionId:${currentScreen.route}") {
                when (currentScreen) {
                Screen.Dashboard -> {
                    DashboardScreen(
                        userName = loggedInDisplayName,
                        onLaunchSimulation = { navigateTo(Screen.Simulation) },
                        onNavigateToGamification = { navigateTo(Screen.Gamification) },
                        onNavigateToAnalytics = { navigateTo(Screen.Analytics) },
                        modifier = Modifier.padding(innerPadding),
                        userProgress = userProgress,
                        hoursUntilStreakExpires = xpManager.hoursUntilStreakExpires()
                    )
                }
                Screen.Simulation -> {
                    SimulationScreen(
                        onSubmitDecision = { option ->
                            lastSubmittedOption = option
                            coroutineScope.launch {
                                val xpEarned: Int
                                if (option.isCorrect) {
                                    val result = xpManager.awardSimulationDecision(
                                        username = loggedInUsername,
                                        tier = option.tier,
                                        aiRecommendationFollowed = option.aiRecommended,
                                        scenarioTitle = MockData.activeScenario.title
                                    )
                                    xpEarned = result.totalAwarded
                                    lastSimLeveledUp = result.leveledUp
                                    lastSimNewLevel = result.newLevel
                                } else {
                                    xpEarned = 0
                                    lastSimLeveledUp = false
                                }
                                lastSimXpEarned = xpEarned
                                if (xpEarned > 0) {
                                    Toast.makeText(context, "+$xpEarned XP earned!", Toast.LENGTH_SHORT).show()
                                }
                                logUserAction(
                                    actionType = ActionType.SIMULATION_DECISION,
                                    module = Module.CONTENT_DATA,
                                    description = "Completed simulation '${MockData.activeScenario.title}' choosing '${option.label}' (${if (option.isCorrect) "Correct" else "Incorrect"}) — earned $xpEarned XP",
                                    riskLevel = if (option.isCorrect) RiskLevel.LOW else RiskLevel.MEDIUM
                                )
                                navigateTo(Screen.Feedback)
                            }
                        },
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.Feedback -> {
                    FeedbackScreen(
                        selectedOption = lastSubmittedOption,
                        xpEarned = lastSimXpEarned,
                        leveledUp = lastSimLeveledUp,
                        newLevel = lastSimNewLevel,
                        onNextScenario = {
                            Toast.makeText(context, "Loading next scenario...", Toast.LENGTH_SHORT).show()
                            navigateTo(Screen.Simulation)
                        },
                        onRetry = { navigateTo(Screen.Simulation) },
                        onReturnHome = { resetStackTo(homeScreen()) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.Assessment -> {
                    AssessmentScreen(
                        username = loggedInUsername,
                        xpManager = xpManager,
                        onLaunchSimulation = { navigateTo(Screen.Simulation) },
                        onStartQuiz = { moduleId ->
                            activeQuizModuleId = moduleId
                            navigateTo(Screen.QuizTaking)
                        },
                        onModuleComplete = { module ->
                            coroutineScope.launch {
                                val result = xpManager.awardModuleCompletion(
                                    username = loggedInUsername,
                                    moduleId = module.id,
                                    moduleType = module.moduleType,
                                    moduleTitle = module.title
                                )
                                if (result == null) {
                                    Toast.makeText(context, "Module already completed.", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "+${result.totalAwarded} XP earned!", Toast.LENGTH_SHORT).show()
                                    logUserAction(
                                        actionType = ActionType.QUIZ_COMPLETED,
                                        module = Module.CONTENT_DATA,
                                        description = "Completed ${module.moduleType.label.lowercase()} '${module.title}' — earned ${result.totalAwarded} XP"
                                    )
                                }
                            }
                        },
                        completedModuleIds = userProgress?.completedModuleIds
                            ?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet(),
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.QuizTaking -> {
                    QuizScreen(
                        quizId = activeQuizModuleId,
                        username = loggedInUsername,
                        xpManager = xpManager,
                        onNavigateBack = { goBack() },
                        onQuizFinished = { score, total, result ->
                            logUserAction(
                                actionType = ActionType.QUIZ_COMPLETED,
                                module = Module.CONTENT_DATA,
                                description = "Completed quiz '$activeQuizModuleId' scoring $score/$total — earned ${result.totalAwarded} XP"
                            )
                        },
                        onReviewAnswers = { attemptId ->
                            activeAttemptId = attemptId
                            navigateTo(Screen.ModuleReview)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.ModuleReview -> {
                    ModuleReviewScreen(
                        attemptId = activeAttemptId,
                        xpManager = xpManager,
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.LearningHistory -> {
                    LearningHistoryScreen(
                        username = loggedInUsername,
                        xpManager = xpManager,
                        onOpenAttempt = { attemptId ->
                            activeAttemptId = attemptId
                            navigateTo(Screen.ModuleSummary)
                        },
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.ModuleSummary -> {
                    var reopenedAttempt by remember(activeAttemptId) { mutableStateOf<QuizAttemptEntity?>(null) }
                    var reopenedHighest by remember(activeAttemptId) { mutableStateOf(0) }
                    LaunchedEffect(activeAttemptId) {
                        val loaded = xpManager.getAttempt(activeAttemptId)
                        reopenedAttempt = loaded
                        if (loaded != null) {
                            reopenedHighest = xpManager.getHighestScorePercent(loggedInUsername, loaded.quizId)
                        }
                    }
                    val loadedAttempt = reopenedAttempt
                    if (loadedAttempt == null) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(innerPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        ModuleSummaryScreen(
                            attempt = loadedAttempt,
                            awardResult = null,
                            highestScorePercentEver = reopenedHighest,
                            onReviewAnswers = { navigateTo(Screen.ModuleReview) },
                            onViewLearningHistory = { navigateTo(Screen.LearningHistory) },
                            onDone = { goBack() },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
                Screen.Gamification -> {
                    MyProgressScreen(
                        username = loggedInUsername,
                        xpManager = xpManager,
                        onNavigateToXpHistory = { navigateTo(Screen.XpHistory) },
                        onNavigateToLearningHistory = { navigateTo(Screen.LearningHistory) },
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.XpHistory -> {
                    XpHistoryScreen(
                        username = loggedInUsername,
                        xpManager = xpManager,
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.Analytics -> {
                    AnalyticsScreen(
                        onBackClick = { goBack() },
                        onNavigateToAiTutor = { navigateTo(Screen.AiTutor) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.AiTutor -> {
                    AiTutorScreen(
                        username = loggedInUsername,
                        xpManager = xpManager,
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.Profile -> {
                    val accountCreatedAt = remember(loggedInUsername) {
                        authManager.getAllAccounts().find { it.username == loggedInUsername }?.createdAt
                            ?: System.currentTimeMillis()
                    }
                    ProfileScreen(
                        displayName = loggedInDisplayName,
                        username = loggedInUsername,
                        role = currentUserRole,
                        memberSince = accountCreatedAt,
                        authManager = authManager,
                        onDisplayNameChanged = { loggedInDisplayName = it },
                        onNavigateToAccountSecurity = { navigateTo(Screen.AccountSecurity) },
                        onNavigateToSettings = { navigateTo(Screen.Settings) },
                        onLogout = { performLogout() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.Settings -> {
                    SettingsScreen(
                        currentLanguage = languageCode,
                        onLanguageChange = { code ->
                            languageManager.setLanguage(code)
                            languageCode = code
                        },
                        onNavigateToAccountSecurity = { navigateTo(Screen.AccountSecurity) },
                        onLogout = { performLogout() },
                        onDeleteAccount = {
                            authManager.deleteAccount(loggedInUsername)
                            performLogout()
                        },
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.AccountSecurity -> {
                    AccountSecurityScreen(
                        currentDisplayName = loggedInDisplayName,
                        authManager = authManager,
                        username = loggedInUsername,
                        onLogout = { performLogout() },
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.AdminDashboard -> {
                    AdminDashboardScreen(
                        adminName = loggedInDisplayName,
                        authManager = authManager,
                        xpManager = xpManager,
                        auditManager = auditManager,
                        onLogout = { performLogout() },
                        onNavigateToAudit = {
                            historyFilterAccount = null
                            navigateTo(Screen.AuditDashboard)
                        },
                        onManageXp = {
                            xpManagementPreselectUsername = null
                            navigateTo(Screen.AdminXpManagement)
                        },
                        onViewReports = {
                            navigateTo(Screen.AdminReports)
                        },
                        onNavigateToProfile = {
                            navigateTo(Screen.Profile)
                        },
                        onNavigateToCloudMonitor = {
                            navigateTo(Screen.CloudMonitoring)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.AdminManagement -> {
                    AdminManagementScreen(
                        onManageUsers = {
                            navigateTo(Screen.AdminUserManagement)
                        },
                        onManageModules = {
                            navigateTo(Screen.AdminModuleManagement)
                        },
                        onReviewAnswers = {
                            navigateTo(Screen.AdminAnswerReview)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.AdminAnswerReview -> {
                    AdminAnswerReviewScreen(
                        authManager = authManager,
                        xpManager = xpManager,
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.AdminXpManagement -> {
                    AdminXpManagementScreen(
                        adminUsername = loggedInUsername,
                        xpManager = xpManager,
                        authManager = authManager,
                        preselectedUsername = xpManagementPreselectUsername,
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.AdminUserManagement -> {
                    AdminUserManagementScreen(
                        authManager = authManager,
                        xpManager = xpManager,
                        currentAdminUsername = loggedInUsername,
                        onManageXp = { username ->
                            xpManagementPreselectUsername = username
                            navigateTo(Screen.AdminXpManagement)
                        },
                        onViewFullHistory = { account ->
                            historyFilterAccount = account
                            navigateTo(Screen.AuditDashboard)
                        },
                        onBackClick = { goBack() },
                        canExport = currentUserPermissions.contains(Permission.EXPORT_DATA),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.AdminModuleManagement -> {
                    AdminModuleManagementScreen(
                        currentAdminUsername = loggedInUsername,
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.AdminReports -> {
                    AdminReportsScreen(
                        authManager = authManager,
                        xpManager = xpManager,
                        onOpenModuleAnalytics = { quizId ->
                            activeAnalyticsQuizId = quizId
                            navigateTo(Screen.AdminModuleAnalytics)
                        },
                        onOpenQuizAttempts = { navigateTo(Screen.AdminQuizAttempts) },
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.AdminModuleAnalytics -> {
                    AdminModuleAnalyticsScreen(
                        quizId = activeAnalyticsQuizId,
                        xpManager = xpManager,
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.AdminQuizAttempts -> {
                    AdminQuizAttemptsScreen(
                        xpManager = xpManager,
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.AuditDashboard -> {
                    AuditDashboardScreen(
                        onBackClick = {
                            historyFilterAccount = null
                            goBack()
                        },
                        filterUsername = historyFilterAccount?.username,
                        filterDisplayName = historyFilterAccount?.displayName,
                        canExport = currentUserPermissions.contains(Permission.EXPORT_DATA),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.CloudMonitoring -> {
                    CloudMonitoringScreen(
                        onBackClick = { goBack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                else -> { /* Handled pre-auth */ }
                }
            }
        }
    }
    }
    }
}
