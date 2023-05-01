package com.tomczyn.ellipse.common

import com.tomczyn.ellipse.EffectsCollector
import kotlinx.coroutines.flow.StateFlow

class EllipseContext<ST : Any, EF : Any>(
    val state: StateFlow<ST>,
    val effects: EffectsCollector<EF>
)
