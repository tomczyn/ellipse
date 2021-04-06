package com.tomcz.sample.register

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tomcz.sample.R
import com.tomcz.sample.register.state.RegisterEvent
import com.tomcz.sample.ui.DoublePadding
import com.tomcz.sample.ui.MainAppTheme

class RegisterActivity : ComponentActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    RegisterScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(viewModel: RegisterViewModel) {
    val processor = viewModel.processor
    val state by viewModel.processor.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val passwordFocus = FocusRequester()
    val repeatPasswordFocus = FocusRequester()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(DoublePadding)
    ) {

        val (column, fab) = createRefs()

        Column(
            modifier = Modifier
                .constrainAs(column) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            RegisterTitle()

            Spacer(modifier = Modifier.height(DoublePadding))

            TextField(
                value = state.email,
                colors = textFieldColors(backgroundColor = Color.Transparent),
                onValueChange = { processor.process(RegisterEvent.EmailChanged(it)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
                singleLine = true,
                label = { Text(text = "Email") })

            Spacer(modifier = Modifier.height(DoublePadding))

            TextField(
                value = state.password,
                modifier = Modifier.focusRequester(passwordFocus),
                colors = textFieldColors(backgroundColor = Color.Transparent),
                onValueChange = { processor.process(RegisterEvent.PasswordChanged(it)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { repeatPasswordFocus.requestFocus() }),
                visualTransformation = PasswordVisualTransformation(),
                label = { Text(text = "Password") })

            Spacer(modifier = Modifier.height(DoublePadding))

            TextField(
                value = state.repeatPassword,
                modifier = Modifier.focusRequester(repeatPasswordFocus),
                colors = textFieldColors(backgroundColor = Color.Transparent),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                onValueChange = { processor.process(RegisterEvent.RepeatPasswordChanged(it)) },
                label = { Text(text = "Repeat password") })
        }

        FloatingActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .size(64.dp)
                .constrainAs(fab) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
            content = {
                Surface(modifier = Modifier.padding(6.dp), color = Color.Transparent) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_right),
                        contentDescription = "Register"
                    )
                }
            }
        )
    }
}

@Composable
private fun RegisterTitle() {
    Text(
        text = "Create\nAccount",
        style = MaterialTheme.typography.h4,
    )
}
