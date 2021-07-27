package com.tomcz.sample.login

import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.tomcz.mvi.common.onCreated
import com.tomcz.sample.R
import com.tomcz.sample.login.state.LoginEvent
import com.tomcz.sample.login.state.LoginState
import com.tomcz.sample.util.textChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private val email: EditText by lazy { findViewById(R.id.email) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        onCreated(
            processor = viewModel::processor,
            intents = ::viewEvents,
            onState = ::render
        )
    }

    private fun render(state: LoginState) {
        email.setText(state.email)
    }

    private fun viewEvents(): List<Flow<LoginEvent>> = listOf(
        email.textChanged().map { LoginEvent.EmailChanged(it) }
    )
}