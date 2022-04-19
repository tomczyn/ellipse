package com.tomcz.sample.feature.foo

import com.tomcz.sample.common.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class Bar(
    dispatchers: DispatcherProvider
) {

    private val scope: CoroutineScope = CoroutineScope(dispatchers.io + SupervisorJob())
    private val flow: MutableSharedFlow<Boolean> = MutableSharedFlow()
    private var counter: Int = 0

    fun infiniteStream(): Flow<Boolean> {
        scope.launch {
            while (isActive) {
                delay(10)
                if (counter > 50) counter = 0 else counter++
                flow.emit(counter == 50)
            }
        }
        return flow
    }

    fun close() = scope.cancel()
}
