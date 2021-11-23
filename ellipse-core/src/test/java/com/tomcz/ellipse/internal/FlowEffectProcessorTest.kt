package com.tomcz.ellipse.internal

import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.util.BaseCoroutineTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal typealias CounterEffectProcessor = Processor<CounterEvent, Nothing, CounterEffect>

@ExperimentalCoroutinesApi
internal class FlowEffectProcessorTest : BaseCoroutineTest() {

    @Test
    fun `test effect`() = runBlockingTest {
        val processorScope = CoroutineScope(coroutineContext + Job())
        val processor: CounterEffectProcessor =
            processorScope.processor { sendEffect(CounterEffect) }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()
        processorScope.cancel()
    }

    @Test
    fun `test resubscribing effects`() = runBlockingTest {
        val processorScope = CoroutineScope(coroutineContext + Job())
        val processor: CounterEffectProcessor =
            processorScope.processor { sendEffect(CounterEffect) }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { processor.effect.collect { emptyEffects.add(it) } }
        assertEquals(emptyList<CounterEffect>(), emptyEffects)
        emptyEffectJob.cancelAndJoin()
        processorScope.cancel()
    }

    @Test
    fun `test having multiple subscribers`() = runBlockingTest {
        val processorScope = CoroutineScope(coroutineContext + Job())
        val processor: CounterEffectProcessor =
            processorScope.processor { sendEffect(CounterEffect) }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        val effect2Job = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        assertEquals(listOf(CounterEffect, CounterEffect), effects)
        effectJob.cancelAndJoin()
        effect2Job.cancelAndJoin()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { processor.effect.collect { emptyEffects.add(it) } }
        assertEquals(emptyList<CounterEffect>(), emptyEffects)
        emptyEffectJob.cancelAndJoin()
        processorScope.cancel()
    }

    @Test
    fun `test caching effects when there are no subscribers`() = runBlockingTest {
        val processorScope = CoroutineScope(coroutineContext + Job())
        val processor: CounterEffectProcessor = processorScope.processor {
            sendEffect(CounterEffect)
        }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()

        processor.sendEvent(CounterEvent)
        processor.sendEvent(CounterEvent)

        // Test resubscribing
        val cachedEffects = mutableListOf<CounterEffect>()
        val cachedEffectsJob = launch { processor.effect.collect { cachedEffects.add(it) } }
        assertEquals(listOf(CounterEffect, CounterEffect), cachedEffects)
        cachedEffectsJob.cancelAndJoin()
        processorScope.cancel()
    }
}
