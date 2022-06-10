package com.tomcz.ellipse

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass

@Stable
interface Processor<in EV : Any, out ST : Any, EF : Any> {
    val state: StateFlow<ST>
    val effect: Flow<EF>
    fun <T : EF> effect(filterClass: KClass<T>): Flow<T>
    fun sendEvent(vararg event: EV)
}
