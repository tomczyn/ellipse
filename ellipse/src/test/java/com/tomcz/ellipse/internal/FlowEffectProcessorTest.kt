package com.tomcz.ellipse.internal

import com.tomcz.ellipse.EffectProcessor
import com.tomcz.ellipse.common.effectProcessor
import com.tomcz.ellipse.util.BaseCoroutineTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class FlowEffectProcessorTest : BaseCoroutineTest() {

    object CounterEvent
    object CounterEffect

    @Test
    fun `test effect`() = runBlockingTest {
        val processor: EffectProcessor<CounterEvent, CounterEffect> =
            effectProcessor { effects, _ -> effects.send(CounterEffect) }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        Assertions.assertEquals(listOf(CounterEffect), effects)
        effectJob.cancel()
    }

    @Test
    fun `test resubscribing effects`() = runBlockingTest {
        val processor: EffectProcessor<CounterEvent, CounterEffect> =
            effectProcessor() { effects, _ -> effects.send(CounterEffect) }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        Assertions.assertEquals(listOf(CounterEffect), effects)
        effectJob.cancel()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { processor.effect.collect { emptyEffects.add(it) } }
        Assertions.assertEquals(emptyList<CounterEffect>(), emptyEffects)
        emptyEffectJob.cancel()
    }

    @Test
    fun `test having multiple subscribers`() = runBlockingTest {
        val processor: EffectProcessor<CounterEvent, CounterEffect> =
            effectProcessor() { effects, _ -> effects.send(CounterEffect) }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { processor.effect.collect { effects.add(it) } }
        val effect2Job = launch { processor.effect.collect { effects.add(it) } }
        processor.sendEvent(CounterEvent)
        Assertions.assertEquals(listOf(CounterEffect, CounterEffect), effects)
        effectJob.cancel()
        effect2Job.cancel()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { processor.effect.collect { emptyEffects.add(it) } }
        Assertions.assertEquals(emptyList<CounterEffect>(), emptyEffects)
        emptyEffectJob.cancel()
    }
}