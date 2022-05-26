package com.tomcz.ellipse.internal

import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.util.BaseCoroutineTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal typealias CounterEffectProcessor = Processor<CounterEvent, Unit, CounterEffect>

@ExperimentalCoroutinesApi
internal class FlowEffectProcessorTest : BaseCoroutineTest() {

    @Test
    fun `test effect`() = runTest {
        val processorScope = TestScope(testScheduler)
        val processor: CounterEffectProcessor =
            processorScope.processor { effects.send(CounterEffect.Effect1) }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent.Event1)
        runCurrent()
        assertEquals(listOf(CounterEffect.Effect1), effects)
        effectJob.cancelAndJoin()
        processorScope.cancel()
    }

    @Test
    fun `test resubscribing effects`() = runTest {
        val processorScope = TestScope(testScheduler)
        val processor: CounterEffectProcessor =
            processorScope.processor { effects.send(CounterEffect.Effect1) }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent.Event1)
        runCurrent()
        assertEquals(listOf(CounterEffect.Effect1), effects)
        effectJob.cancelAndJoin()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { processor.effect.collect { emptyEffects.add(it) } }
        assertEquals(emptyList<CounterEffect>(), emptyEffects)
        emptyEffectJob.cancelAndJoin()
        processorScope.cancel()
    }

    @Test
    fun `test having multiple subscribers`() = runTest {
        val processorScope = TestScope(testScheduler)
        val processor: CounterEffectProcessor =
            processorScope.processor { effects.send(CounterEffect.Effect1) }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        val effect2Job = launch { processor.effect.collect { effects.add(it) } }
        runCurrent()
        processor.sendEvent(CounterEvent.Event1)
        runCurrent()
        assertEquals(listOf(CounterEffect.Effect1, CounterEffect.Effect1), effects)
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
    fun `test caching effects when there are no subscribers`() = runTest {
        val processorScope = TestScope(testScheduler)
        val processor: CounterEffectProcessor = processorScope.processor {
            effects.send(CounterEffect.Effect1)
        }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent.Event1)
        runCurrent()
        assertEquals(listOf(CounterEffect.Effect1), effects)
        effectJob.cancelAndJoin()

        processor.sendEvent(CounterEvent.Event1)
        processor.sendEvent(CounterEvent.Event1)

        // Test resubscribing
        val cachedEffects = mutableListOf<CounterEffect>()
        val cachedEffectsJob = launch { processor.effect.collect { cachedEffects.add(it) } }
        runCurrent()
        assertEquals(listOf(CounterEffect.Effect1, CounterEffect.Effect1), cachedEffects)
        cachedEffectsJob.cancelAndJoin()
        processorScope.cancel()
    }
}
