package com.tomcz.sample.login.state


sealed interface LoginEffect {
    object GoToHome : LoginEffect
    object ShowError : LoginEffect
}
