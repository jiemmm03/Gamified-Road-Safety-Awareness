package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.R
import com.example.gamifiedroadsafetyawareness.auth.AuthManager
import com.example.gamifiedroadsafetyawareness.auth.LoginResult
import com.example.gamifiedroadsafetyawareness.auth.UserRole
import com.example.gamifiedroadsafetyawareness.ui.components.AppButton
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.components.AppTextField
import com.example.gamifiedroadsafetyawareness.ui.components.ScreenScaffold
import com.example.gamifiedroadsafetyawareness.ui.theme.AppTypeScale
import com.example.gamifiedroadsafetyawareness.ui.theme.BadgeGold
import com.example.gamifiedroadsafetyawareness.ui.theme.Dimens
import com.example.gamifiedroadsafetyawareness.ui.theme.NavyPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (UserRole, String, Set<com.example.gamifiedroadsafetyawareness.auth.Permission>) -> Unit,
    onNavigateToSignUp: () -> Unit,
    authManager: AuthManager,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(authManager.isRememberMe()) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val fadeAnim = remember { Animatable(0f) }
    val shakeAnim = remember { Animatable(0f) }

    val errorEmptyFields = stringResource(R.string.login_error_empty_fields)
    val errorInvalidCredentials = stringResource(R.string.login_error_invalid_credentials)
    val errorAccountDeactivated = stringResource(R.string.login_error_account_deactivated)

    LaunchedEffect(Unit) {
        val session = authManager.getSavedSession()
        if (session is LoginResult.Success) {
            val username = authManager.getLoggedInUsername() ?: ""
            com.example.gamifiedroadsafetyawareness.firebase.FirebaseSyncManager.getInstance().recordUserLogin(
                username = username,
                displayName = session.displayName,
                role = session.role.name,
                isSuccess = true
            )
            onLoginSuccess(session.role, session.displayName, session.permissions)
            return@LaunchedEffect
        }
        fadeAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    fun triggerShake() {
        scope.launch {
            shakeAnim.animateTo(15f, tween(50))
            shakeAnim.animateTo(-15f, tween(50))
            shakeAnim.animateTo(10f, tween(50))
            shakeAnim.animateTo(-10f, tween(50))
            shakeAnim.animateTo(5f, tween(50))
            shakeAnim.animateTo(0f, tween(50))
        }
    }

    fun performLogin() {
        if (username.isBlank() || password.isBlank()) {
            errorMessage = errorEmptyFields
            showError = true
            triggerShake()
            return
        }

        val result = authManager.login(username, password)
        when (result) {
            is LoginResult.Success -> {
                authManager.setRememberMe(rememberMe)
                onLoginSuccess(result.role, result.displayName, result.permissions)
            }
            is LoginResult.InvalidCredentials -> {
                errorMessage = errorInvalidCredentials
                showError = true
                triggerShake()
            }
            is LoginResult.AccountDeactivated -> {
                errorMessage = errorAccountDeactivated
                showError = true
                triggerShake()
            }
        }
    }

    ScreenScaffold(
        modifier = modifier.imePadding(),
        scrollable = true,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Dimens.spacingExtraLarge, horizontal = Dimens.spacingMedium),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 440.dp) // Limits width on tablets/desktops
                    .alpha(fadeAnim.value)
                    .graphicsLayer { translationX = shakeAnim.value },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🇵🇭 Official Philippine Police & Government Traffic Enforcement Header
                Box(contentAlignment = Alignment.BottomEnd) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Official Municipality & Police Law Enforcement Seal",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .shadow(
                                elevation = 10.dp,
                                shape = CircleShape,
                                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            )
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(2.dp, BadgeGold, CircleShape)
                    )
                    // Security Shield Badge
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(NavyPrimary)
                            .border(1.dp, BadgeGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Shield,
                            contentDescription = null,
                            tint = BadgeGold,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.spacingMedium))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "REPUBLIC OF THE PHILIPPINES",
                        style = AppTypeScale.eyebrowLabel.copy(
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = BadgeGold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Road Safety Command",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Traffic Rule Education & Driver Enforcement System",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.spacingLarge))

                // Authentication Card with Tactical Gold Bevel Border
                AppCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = BadgeGold.copy(alpha = 0.35f),
                    elevation = 6
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.spacingLarge),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "SECURE ACCESS PORTAL",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = Dimens.spacingSmall)
                        )
                        Text(
                            text = "Sign in to access official road safety modules & command logs",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = Dimens.spacingMedium)
                        )

                        // Error Message Container (Fixed height to prevent layout shift)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .padding(bottom = Dimens.spacingSmall),
                            contentAlignment = Alignment.Center
                        ) {
                            if (showError) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(Dimens.cornerRadiusSmall))
                                        .background(MaterialTheme.colorScheme.errorContainer)
                                        .border(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f), RoundedCornerShape(Dimens.cornerRadiusSmall))
                                        .padding(Dimens.spacingSmall)
                                ) {
                                    Text(
                                        text = errorMessage,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.labelMedium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        AppTextField(
                            value = username,
                            onValueChange = {
                                username = it
                                showError = false
                            },
                            label = stringResource(R.string.common_username),
                            leadingIcon = Icons.Rounded.Person,
                            isError = showError
                        )

                        Spacer(modifier = Modifier.height(Dimens.spacingMedium))

                        AppTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                showError = false
                            },
                            label = stringResource(R.string.common_password),
                            leadingIcon = Icons.Rounded.Lock,
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                        contentDescription = stringResource(R.string.common_toggle_password_visibility)
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            isError = showError
                        )

                        Spacer(modifier = Modifier.height(Dimens.spacingMedium))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Text(
                                text = stringResource(R.string.login_remember_me),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(Dimens.spacingLarge))

                        AppButton(
                            text = stringResource(R.string.login_log_in),
                            onClick = { performLogin() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dimens.buttonHeight)
                        )

                        Spacer(modifier = Modifier.height(Dimens.spacingLarge))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.login_no_account),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = stringResource(R.string.login_create_account),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onNavigateToSignUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}
