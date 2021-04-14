package com.tomcz.sample.register

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tomcz.mvi.compose.collectAsState
import com.tomcz.sample.R
import com.tomcz.sample.register.state.RegisterEvent
import com.tomcz.sample.ui.*

class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    RegisterBackground()
                    RegisterScreen()
                }
            }
        }
    }
}

@Composable
fun RegisterScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(QuadruplePadding)
    ) {
        val focusManager = LocalFocusManager.current
        val passwordFocus = FocusRequester()
        val repeatPasswordFocus = FocusRequester()

        RegisterTitle()
        Spacer(modifier = Modifier.height(DoublePadding))
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
        Spacer(modifier = Modifier.height(BasePadding * 6))
        ProceedButton()
    }
}

@Composable
private fun RegisterTitle() {
    Text(
        text = "Create\nAccount",
        style = MaterialTheme.typography.h4,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun EmailField(
    keyboardActions: KeyboardActions,
) {
    val processor = viewModel<RegisterViewModel>().processor
    val email by processor.collectAsState("") { it.email }
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
    val password by processor.collectAsState("") { it.password }
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
    val repeatPassword by processor.collectAsState("") { it.repeatPassword }
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
        FloatingActionButton(
            onClick = { /*TODO*/ },
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

@Composable
private fun RegisterBackground() {
    DrawOrangeBlob()
}

@Composable
private fun DrawOrangeBlob() {
    Canvas(modifier = Modifier.fillMaxSize()) {

        val path = Path()

        val startX = 0f
        val startY = size.height * .17f
        val end1X = size.width * .4f
        val end1Y = size.height * .06f
        val end2X = size.width * .75f
        val end2Y = 0f

        val anc0X = size.width * .15f
        val anc0Y = size.height * .18f
        val anc1X = size.width * .1f
        val anc1Y = size.height * .04f

        val anc3X = size.width * 0.6f
        val anc3Y = 0f
        val anc2X = size.width * .6f
        val anc2Y = size.height * .07f
        path.moveTo(startX, startY)
        path.cubicTo(anc0X, anc0Y, anc1X, anc1Y, end1X, end1Y)
        path.cubicTo(anc2X, anc2Y, anc3X, anc3Y, end2X, end2Y)
        path.lineTo(0f, 0f)
        path.close()
        drawPath(
            path = path,
            color = Orange,
            style = Fill
        )
    }
}
