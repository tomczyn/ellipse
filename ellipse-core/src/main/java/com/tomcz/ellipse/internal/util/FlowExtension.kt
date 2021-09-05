package com.tomcz.ellipse.internal.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flattenMerge

internal fun <EV : Any> List<Flow<EV>>.mergeEvents(): Flow<EV> = when {
    isEmpty() -> emptyFlow()
    else -> asFlow().flattenMerge(size)
}
