package com.tomcz.ellipse

import kotlinx.coroutines.CoroutineScope

interface EllipseContext<ST : Any, EF : Any> : CoroutineScope {
    var state: ST
    fun sendEffect(vararg effect: EF)
}
