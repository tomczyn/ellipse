package com.tomcz.ellipse.extensions

import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.NoAction
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.test.processorTest
import com.tomcz.ellipse.util.BaseCoroutineTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class ProcessorExtensions : BaseCoroutineTest() {

    @Test
    fun `test creating unit processor`() = runTest {
        val processor: Processor<Unit, Unit, Unit> = processor(
            initialState = Unit,
            prepare = { flowOf(NoAction()) },
            onEvent = {
                effects.send(Unit)
                flowOf(NoAction())
            }
        )

        // Test without thenStates
        processorTest(
            processor = { processor },
            given = {},
            whenEvents = listOf(Unit),
            thenEffects = { assertValues(Unit) }
        )

        // Test with thenStates
        processorTest(
            processor = { processor },
            given = {},
            whenEvents = listOf(Unit),
            thenStates = { assertValues(Unit, Unit) },
            thenEffects = { assertValues(Unit) }
        )
    }

    @Test
    fun `test creating nothing state, unit event effect processor`() = runTest {
        val processor: Processor<Unit, Nothing, Unit> = processor(
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
