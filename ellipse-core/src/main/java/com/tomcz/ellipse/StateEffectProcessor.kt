package com.tomcz.ellipse

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow

@Stable
interface StateEffectProcessor<in EV : Any, out ST : Any, out EF : Any> : StateProcessor<EV, ST> {
    val effect: Flow<EF>
}
