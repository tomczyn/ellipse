package com.tomczyn.sample.feature.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.tomczyn.ellipse.common.collectAsState
import com.tomczyn.ellipse.common.onProcessor
import com.tomczyn.sample.R
import com.tomczyn.sample.feature.register.state.RegisterEffect
import com.tomczyn.sample.feature.register.state.RegisterEvent
import com.tomczyn.sample.ui.BasePadding
import com.tomczyn.sample.ui.BezierBackground
import com.tomczyn.sample.ui.DarkGray
import com.tomczyn.sample.ui.MainAppTheme
import com.tomczyn.sample.ui.QuadruplePadding
import com.tomczyn.sample.ui.SampleTypography
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@FlowPreview
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onProcessor(
            lifecycleState = Lifecycle.State.RESUMED,
            ellipse = viewModel::processor,
            onEffect = ::trigger
        )
        return ComposeView(requireContext()).apply {
            setContent {
                MainAppTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        BezierBackground()
                        RegisterScreen()
                    }
                }
            }
        }
    }

    private fun trigger(effect: RegisterEffect) {
        when (effect) {
            RegisterEffect.GoToLogin ->
                findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                )
            RegisterEffect.GoToHome -> TODO()
        }
    }
}

@Composable
fun RegisterScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(QuadruplePadding),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(QuadruplePadding))
        RegisterTitle()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(QuadruplePadding),
        verticalArrangement = Arrangement.Bottom
    ) {
        val focusManager = LocalFocusManager.current
        val passwordFocus = FocusRequester()
        val repeatPasswordFocus = FocusRequester()

        Spacer(modifier = Modifier.height(BasePadding * 6))
        EmailField(
            keyboardActions = KeyboardActions { passwordFocus.requestFocus() }
        )
        Spacer(modifier = Modifier.height(BasePadding))
        PasswordField(
            modifier = Modifier.focusRequester(passwordFocus),
            keyboardActions = KeyboardActions { repeatPasswordFocus.requestFocus() }
        )
        Spacer(modifier = Modifier.height(BasePadding))
        RepeatPasswordField(
            modifier = Modifier.focusRequester(repeatPasswordFocus),
            keyboardActions = KeyboardActions { focusManager.clearFocus() }
        )
        Spacer(modifier = Modifier.height(QuadruplePadding))
        ProceedButton()
        GoToLoginButton()
    }
}

@Composable
private fun GoToLoginButton() {
    val processor = viewModel<RegisterViewModel>().processor
    TextButton(onClick = { processor.sendEvent(RegisterEvent.GoToLogin) }) {
        Text(
            text = "Already have an account?",
            fontStyle = SampleTypography.body2.fontStyle,
            color = Color.Gray
        )
    }
}

@Composable
private fun RegisterTitle() {
    Text(
        text = "Create\nAccount",
        style = MaterialTheme.typography.h4,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colors.background
    )
}

@Composable
private fun EmailField(
    keyboardActions: KeyboardActions,
) {
    val processor = viewModel<RegisterViewModel>().processor
    val email by processor.collectAsState { it.email }
    TextField(
        value = email,
        modifier = Modifier.fillMaxWidth(),
        colors = textFieldColors(backgroundColor = Color.Transparent),
        onValueChange = { processor.sendEvent(RegisterEvent.EmailChanged(it)) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = keyboardActions,
        singleLine = true,
        label = { Text(text = "Email") })
}

@Composable
private fun PasswordField(
    modifier: Modifier,
    keyboardActions: KeyboardActions
) {
    val processor = viewModel<RegisterViewModel>().processor
    val password by processor.collectAsState { it.password }
    TextField(
        value = password,
        modifier = modifier.fillMaxWidth(),
        colors = textFieldColors(backgroundColor = Color.Transparent),
        onValueChange = { processor.sendEvent(RegisterEvent.PasswordChanged(it)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = keyboardActions,
        visualTransformation = PasswordVisualTransformation(),
        label = { Text(text = "Password") })
}

@Composable
private fun RepeatPasswordField(
    modifier: Modifier,
    keyboardActions: KeyboardActions
) {
    val processor = viewModel<RegisterViewModel>().processor
    val repeatPassword by processor.collectAsState { it.repeatPassword }
    TextField(
        value = repeatPassword,
        modifier = modifier.fillMaxWidth(),
        colors = textFieldColors(backgroundColor = Color.Transparent),
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = keyboardActions,
        onValueChange = { processor.sendEvent(RegisterEvent.RepeatPasswordChanged(it)) },
        label = { Text(text = "Repeat password") },
    )
}

@Composable
private fun ProceedButton() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val processor = viewModel<RegisterViewModel>().processor
        FloatingActionButton(
            onClick = {
                processor.sendEvent(RegisterEvent.RegisterClicked)
            },
            backgroundColor = DarkGray,
            modifier = Modifier.size(64.dp),
            content = {
                Surface(modifier = Modifier.padding(20.dp), color = Color.Transparent) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_right),
                        tint = Color.White,
                        contentDescription = "Register"
                    )
                }
            }
        )
    }
}
