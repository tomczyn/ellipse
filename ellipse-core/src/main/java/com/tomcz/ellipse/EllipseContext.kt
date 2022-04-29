package com.tomcz.ellipse

import kotlinx.coroutines.flow.Flow

interface EllipseContext<ST : Any, EF : Any> {
    var state: ST
    fun setState(vararg partial: Partial<ST>)
    fun setState(vararg flow: Flow<Partial<ST>>)
    fun sendEffect(vararg effect: EF)
}
