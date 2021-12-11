package com.tomcz.ellipse.common

import com.tomcz.ellipse.EffectsCollector
import kotlinx.coroutines.flow.StateFlow

class EllipseContext<ST : Any, EF : Any>(
    val state: StateFlow<ST>,
    val effects: EffectsCollector<EF>
)
