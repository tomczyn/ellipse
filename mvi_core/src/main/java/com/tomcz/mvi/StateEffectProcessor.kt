package com.tomcz.mvi

import kotlinx.coroutines.flow.Flow

interface StateEffectProcessor<in EV : Any, out ST : Any, out EF : Any> : StateProcessor<EV, ST> {
    val effect: Flow<EF>
}
