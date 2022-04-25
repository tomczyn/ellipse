package com.tomcz.ellipse.extensions

import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.test.processorTest
import com.tomcz.ellipse.util.BaseCoroutineTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class ProcessorExtensionsTest : BaseCoroutineTest() {

    @Test
    fun `test creating unit processor`() = processorTest(
        processor = {
            val processor: Processor<Unit, Unit, Unit> = processor(
                initialState = Unit,
                prepare = { emptyFlow() },
                onEvent = { emptyFlow() }
            )
            processor
        },
        given = {},
        whenEvents = listOf(Unit),
        thenEffects = { assertValues(Unit) }
    )

    @Test
    fun `test creating nothing state, unit event effect processor`() =
        runTest(UnconfinedTestDispatcher()) {
            val processor: Processor<Unit, Unit, Unit> = processor(
                prepare = {},
                onEvent = { effects.send(Unit) }
            )

            processorTest(
                processor = { processor },
                given = {},
                whenEvents = listOf(Unit),
                thenEffects = { assertValues(Unit) }
            )
        }
}
