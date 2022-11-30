package com.tomcz.ellipse.common

fun <ST : Any, EF : Any> EllipseContext<ST, EF>.sendEffect(vararg effect: EF) {
    effects.send(*effect)
}
