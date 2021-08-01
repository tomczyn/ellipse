package com.tomcz.sample.login

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tomcz.mvi.common.clicks
import com.tomcz.mvi.common.onCreated
import com.tomcz.sample.databinding.ActivityLoginBinding
import com.tomcz.sample.login.state.LoginEffect
import com.tomcz.sample.login.state.LoginEvent
import com.tomcz.sample.login.state.LoginState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onCreated(
            processor = viewModel::processor,
            viewEvents = ::viewEvents,
            onState = ::render,
            onEffect = ::trigger
        )
    }

    private fun render(state: LoginState) = with(state) {
        binding.progress.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun viewEvents(): List<Flow<LoginEvent>> = listOf(
        binding.login.clicks().map {
            LoginEvent.LoginClick(
                binding.email.text.toString(),
                binding.pass.text.toString()
            )
        }
    )

    private fun trigger(effect: LoginEffect): Unit = when (effect) {
        LoginEffect.GoToHome -> openHome()
        LoginEffect.ShowError -> showErrorToast()
    }

    private fun openHome() { /* TODO */ }
    private fun showErrorToast() { /* TODO */ }
}
