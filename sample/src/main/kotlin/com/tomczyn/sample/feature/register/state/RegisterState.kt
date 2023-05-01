package com.tomczyn.sample.feature.register.state

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
)
