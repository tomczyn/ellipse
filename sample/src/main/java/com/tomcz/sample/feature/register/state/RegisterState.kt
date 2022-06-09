package com.tomcz.sample.feature.register.state

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val blind: String? = null
)
