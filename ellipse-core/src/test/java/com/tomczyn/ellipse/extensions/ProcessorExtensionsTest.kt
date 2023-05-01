package com.tomczyn.ellipse.extensions

import com.tomczyn.ellipse.Processor
import com.tomczyn.ellipse.common.processor
import com.tomczyn.ellipse.test.processorTest
import com.tomczyn.ellipse.util.BaseCoroutineTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class ProcessorExtensionsTest : BaseCoroutineTest() {

    private val scope: CoroutineScope = MainScope()

    private val unitProcessor: Processor<Unit, Unit, Unit> = scope.processor(
        prepare = {},
        onEvent = { effects.send(Unit) }
    )

    private val standardProcessor: Processor<Unit, String, Unit> = scope.processor(
        initialState = "",
        prepare = { emptyFlow() },
        onEvent = { effects.send(Unit); emptyFlow() }
    )

    @Test
    fun `test creating unit processor`() = processorTest(
        processor = { unitProcessor },
        whenEvent = Unit,
        thenStates = { assertValues(Unit) },
        thenEffects = { assertValues(Unit) }
    )

    @Test
    fun `test creating unit processor with separate runTest`() = runTest {
        processorTest(
            processor = { unitProcessor },
            whenEvent = Unit,
            thenStates = { assertValues(Unit) },
            thenEffects = { assertValues(Unit) }
        )
    }

    @Test
    fun `test creating standard processor`() = processorTest(
        processor = { standardProcessor },
        whenEvent = Unit,
        thenStates = { assertValues("") },
        thenEffects = { assertValues(Unit) }
    )

    @Test
    fun `test creating standard processor with separate runTest`() = runTest {
        processorTest(
            processor = { standardProcessor },
            whenEvent = Unit,
            thenStates = { assertValues("") },
            thenEffects = { assertValues(Unit) }
        )
    }
}
