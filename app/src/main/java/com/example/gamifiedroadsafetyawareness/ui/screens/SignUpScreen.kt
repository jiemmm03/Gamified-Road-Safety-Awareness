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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Male
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.R
import com.example.gamifiedroadsafetyawareness.auth.AuthManager
import com.example.gamifiedroadsafetyawareness.auth.UserRole
import com.example.gamifiedroadsafetyawareness.ui.components.AppButton
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.components.AppTextField
import com.example.gamifiedroadsafetyawareness.ui.components.CelebrationBurst
import com.example.gamifiedroadsafetyawareness.ui.components.ScreenScaffold
import com.example.gamifiedroadsafetyawareness.ui.theme.AppTypeScale
import com.example.gamifiedroadsafetyawareness.ui.theme.BadgeGold
import com.example.gamifiedroadsafetyawareness.ui.theme.Dimens
import kotlinx.coroutines.launch

/** Bundle of already-localized validation messages, resolved once via stringResource at the
 * composable's top and threaded into these plain (non-composable) validator functions. */
private class SignUpValidationStrings(
    val fullNameRequired: String,
    val usernameRequired: String,
    val usernameMinLength: String,
    val usernameInvalidChars: String,
    val passwordRequired: String,
    val passwordMinLength: String,
    val passwordNeedsLetter: String,
    val passwordNeedsNumber: String,
    val confirmPasswordRequired: String,
    val passwordsDontMatch: String,
    val invalidPhMobile: String,
    val ageInvalid: String,
    val ageRange: String
)

private fun validateFullName(value: String, s: SignUpValidationStrings): String? =
    if (value.isBlank()) s.fullNameRequired else null

private fun validateUsername(value: String, s: SignUpValidationStrings): String? {
    val trimmed = value.trim()
    return when {
        trimmed.isBlank() -> s.usernameRequired
        trimmed.length < 3 -> s.usernameMinLength
        !trimmed.matches(Regex("^[a-zA-Z0-9_]+$")) -> s.usernameInvalidChars
        else -> null
    }
}

private fun validatePassword(value: String, s: SignUpValidationStrings): String? = when {
    value.isEmpty() -> s.passwordRequired
    value.length < AuthManager.MIN_PASSWORD_LENGTH -> s.passwordMinLength
    !value.any { it.isLetter() } -> s.passwordNeedsLetter
    !value.any { it.isDigit() } -> s.passwordNeedsNumber
    else -> null
}

private fun validateConfirmPassword(password: String, confirm: String, s: SignUpValidationStrings): String? = when {
    confirm.isEmpty() -> s.confirmPasswordRequired
    confirm != password -> s.passwordsDontMatch
    else -> null
}

private val phMobileRegex = Regex("^(\\+63|0)9\\d{9}$")
private fun validateContactNumber(value: String, s: SignUpValidationStrings): String? {
    if (value.isBlank()) return null
    val cleaned = value.replace(Regex("[\\s-]"), "")
    return if (!phMobileRegex.matches(cleaned)) s.invalidPhMobile else null
}

private const val MIN_AGE = 13
private const val MAX_AGE = 100
private fun validateAge(value: String, s: SignUpValidationStrings): String? {
    val age = value.trim().toIntOrNull()
    return when {
        value.isBlank() -> s.ageInvalid
        age == null -> s.ageInvalid
        age < MIN_AGE || age > MAX_AGE -> s.ageRange
        else -> null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpSuccess: (UserRole, String, Set<com.example.gamifiedroadsafetyawareness.auth.Permission>) -> Unit,
    onNavigateToLogin: () -> Unit,
    authManager: AuthManager,
    modifier: Modifier = Modifier
) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf<String?>(null) }
    var age by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }

    var fullNameError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var genderError by remember { mutableStateOf<String?>(null) }
    var ageError by remember { mutableStateOf<String?>(null) }
    var contactError by remember { mutableStateOf<String?>(null) }

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val fadeAnim = remember { Animatable(0f) }
    val shakeAnim = remember { Animatable(0f) }

    val validationStrings = SignUpValidationStrings(
        fullNameRequired = stringResource(R.string.signup_error_fullname_required),
        usernameRequired = stringResource(R.string.signup_error_username_required),
        usernameMinLength = stringResource(R.string.signup_error_username_min_length),
        usernameInvalidChars = stringResource(R.string.signup_error_username_invalid_chars),
        passwordRequired = stringResource(R.string.signup_error_password_required),
        passwordMinLength = stringResource(R.string.signup_error_password_min_length, AuthManager.MIN_PASSWORD_LENGTH),
        passwordNeedsLetter = stringResource(R.string.signup_error_password_needs_letter),
        passwordNeedsNumber = stringResource(R.string.signup_error_password_needs_number),
        confirmPasswordRequired = stringResource(R.string.signup_error_confirm_password_required),
        passwordsDontMatch = stringResource(R.string.signup_error_passwords_dont_match),
        invalidPhMobile = stringResource(R.string.signup_error_invalid_ph_mobile),
        ageInvalid = stringResource(R.string.signup_error_age_invalid),
        ageRange = stringResource(R.string.signup_error_age_range, MIN_AGE, MAX_AGE)
    )
    val errorGenderRequired = stringResource(R.string.signup_error_gender_required)
    val errorFixFields = stringResource(R.string.signup_error_fix_fields)
    val errorUsernameTakenGeneric = stringResource(R.string.signup_error_username_taken_generic)
    val errorRegistrationFailed = stringResource(R.string.signup_error_registration_failed)
    val usernameTakenMessage = stringResource(R.string.signup_error_username_taken, username.trim().lowercase())

    LaunchedEffect(Unit) {
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

    fun performSignUp() {
        showError = false
        showSuccess = false

        fullNameError = validateFullName(fullName, validationStrings)
        usernameError = validateUsername(username, validationStrings)
        passwordError = validatePassword(password, validationStrings)
        confirmPasswordError = validateConfirmPassword(password, confirmPassword, validationStrings)
        ageError = validateAge(age, validationStrings)
        contactError = validateContactNumber(contactNumber, validationStrings)
        genderError = if (gender == null) errorGenderRequired else null

        val hasFieldErrors = listOf(
            fullNameError, usernameError, passwordError,
            confirmPasswordError, ageError, contactError, genderError
        ).any { it != null }

        if (hasFieldErrors) {
            errorMessage = errorFixFields
            showError = true; triggerShake(); return
        }

        val trimmedUser = username.trim().lowercase()
        val existingAccounts = authManager.getAllAccounts()

        if (existingAccounts.any { it.username == trimmedUser }) {
            usernameError = usernameTakenMessage
            errorMessage = errorUsernameTakenGeneric
            showError = true; triggerShake(); return
        }

        val loginResult = authManager.registerAndLogin(
            username = trimmedUser,
            password = password,
            role = UserRole.USER,
            displayName = fullName.trim(),
            gender = gender ?: "",
            age = age.trim().toIntOrNull(),
            contactNumber = contactNumber.trim()
        )

        when (loginResult) {
            is com.example.gamifiedroadsafetyawareness.auth.LoginResult.Success -> {
                onSignUpSuccess(loginResult.role, loginResult.displayName, loginResult.permissions)
            }
            else -> {
                errorMessage = errorRegistrationFailed
                showError = true; triggerShake()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Dagami Municipal Hall Official Background Image
        Image(
            painter = painterResource(id = R.drawable.background_dagami),
            contentDescription = "Municipality of Dagami Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Semi-transparent tactical gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF060B14).copy(alpha = 0.78f),
                            Color(0xFF002266).copy(alpha = 0.65f),
                            Color(0xFF060B14).copy(alpha = 0.88f)
                        )
                    )
                )
        )

        ScreenScaffold(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            scrollable = true,
            backgroundColor = Color.Transparent
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Municipality of Dagami, Leyte Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(10.dp))

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
                    text = stringResource(R.string.signup_title),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.signup_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(fadeAnim.value)
                .graphicsLayer { translationX = shakeAnim.value },
            borderColor = BadgeGold.copy(alpha = 0.35f),
            elevation = 6
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NEW DRIVER REGISTRATION",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Create an account to start earning road safety badges & XP",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                AnimatedVisibility(visible = showSuccess) {
                    Box {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                                .border(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "✅", fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = stringResource(R.string.signup_account_created),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                Text(
                                    text = stringResource(R.string.signup_can_sign_in),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                AppButton(
                                    text = stringResource(R.string.signup_go_to_login),
                                    onClick = { onSignUpSuccess(UserRole.USER, fullName.trim(), emptySet()) },
                                    containerColor = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                        CelebrationBurst(trigger = showSuccess, modifier = Modifier.matchParentSize())
                    }
                }

                AnimatedVisibility(visible = showError) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .border(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                if (!showSuccess) {
                    AppTextField(
                        value = fullName,
                        onValueChange = { fullName = it; fullNameError = validateFullName(it, validationStrings) },
                        label = stringResource(R.string.signup_full_name),
                        leadingIcon = Icons.Rounded.Badge,
                        isError = fullNameError != null,
                        supportingText = fullNameError
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    AppTextField(
                        value = username,
                        onValueChange = { username = it; usernameError = validateUsername(it, validationStrings) },
                        label = stringResource(R.string.common_username),
                        leadingIcon = Icons.Rounded.AlternateEmail,
                        isError = usernameError != null,
                        supportingText = usernameError
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    AppTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = validatePassword(it, validationStrings)
                            if (confirmPassword.isNotEmpty()) {
                                confirmPasswordError = validateConfirmPassword(it, confirmPassword, validationStrings)
                            }
                        },
                        label = stringResource(R.string.common_password),
                        leadingIcon = Icons.Rounded.Key,
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    contentDescription = stringResource(R.string.common_toggle_password_visibility),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = passwordError != null,
                        supportingText = passwordError
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    AppTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            confirmPasswordError = validateConfirmPassword(password, it, validationStrings)
                        },
                        label = stringResource(R.string.signup_confirm_password),
                        leadingIcon = Icons.Rounded.VerifiedUser,
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    contentDescription = stringResource(R.string.common_toggle_password_visibility),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = confirmPasswordError != null,
                        supportingText = confirmPasswordError
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(R.string.signup_password_hint, AuthManager.MIN_PASSWORD_LENGTH),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth().padding(start = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.signup_gender),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = if (genderError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectableGroup(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GenderOption(
                            label = stringResource(R.string.signup_gender_male),
                            icon = Icons.Rounded.Male,
                            selected = gender == "MALE",
                            onClick = { gender = "MALE"; genderError = null },
                            modifier = Modifier.weight(1f)
                        )
                        GenderOption(
                            label = stringResource(R.string.signup_gender_female),
                            icon = Icons.Rounded.Female,
                            selected = gender == "FEMALE",
                            onClick = { gender = "FEMALE"; genderError = null },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (genderError != null) {
                        Text(
                            text = genderError!!,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    AppTextField(
                        value = age,
                        onValueChange = { input ->
                            if (input.length <= 3 && input.all { it.isDigit() }) {
                                age = input
                                ageError = validateAge(input, validationStrings)
                            }
                        },
                        label = stringResource(R.string.signup_age),
                        placeholder = stringResource(R.string.signup_age_placeholder),
                        leadingIcon = Icons.Rounded.CalendarMonth,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = ageError != null,
                        supportingText = ageError
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    AppTextField(
                        value = contactNumber,
                        onValueChange = { contactNumber = it; contactError = validateContactNumber(it, validationStrings) },
                        label = stringResource(R.string.signup_contact_number),
                        leadingIcon = Icons.Rounded.Smartphone,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = contactError != null,
                        supportingText = contactError
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    AppButton(
                        text = stringResource(R.string.login_create_account),
                        onClick = { performSignUp() }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.signup_already_have_account),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.login_log_in),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { onNavigateToLogin() }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
}

@Composable
private fun GenderOption(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    val bgColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    val contentColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(Dimens.cornerRadiusMedium))
            .background(bgColor)
            .border(if (selected) 2.dp else 1.dp, borderColor, RoundedCornerShape(Dimens.cornerRadiusMedium))
            .selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(contentColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Icon(
            imageVector = if (selected) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
            contentDescription = if (selected) stringResource(R.string.signup_gender_selected) else null,
            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(15.dp)
        )
    }
}
