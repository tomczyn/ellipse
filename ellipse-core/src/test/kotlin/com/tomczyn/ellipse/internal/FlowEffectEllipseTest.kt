package com.tomczyn.ellipse.internal

import com.tomczyn.ellipse.Ellipse
import com.tomczyn.ellipse.common.ellipse
import com.tomczyn.ellipse.util.BaseCoroutineTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal typealias CounterEffectEllipse = Ellipse<CounterEvent, Unit, CounterEffect>

@ExperimentalCoroutinesApi
internal class FlowEffectEllipseTest : BaseCoroutineTest() {

    @Test
    fun `test effect`() = runTest {
        val ellipseScope = TestScope(testScheduler)
        val ellipse: CounterEffectEllipse =
            ellipseScope.ellipse { effects.send(CounterEffect) }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { ellipse.effect.collect { effects.add(it) } }
        ellipse.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()
        ellipseScope.cancel()
    }

    @Test
    fun `test resubscribing effects`() = runTest {
        val ellipseScope = TestScope(testScheduler)
        val ellipse: CounterEffectEllipse =
            ellipseScope.ellipse { effects.send(CounterEffect) }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { ellipse.effect.collect { effects.add(it) } }
        ellipse.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { ellipse.effect.collect { emptyEffects.add(it) } }
        assertEquals(emptyList<CounterEffect>(), emptyEffects)
        emptyEffectJob.cancelAndJoin()
        ellipseScope.cancel()
    }

    @Test
    fun `test having multiple subscribers`() = runTest {
        val ellipseScope = TestScope(testScheduler)
        val ellipse: CounterEffectEllipse =
            ellipseScope.ellipse { effects.send(CounterEffect) }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { ellipse.effect.collect { effects.add(it) } }
        val effect2Job = launch { ellipse.effect.collect { effects.add(it) } }
        ellipse.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(listOf(CounterEffect, CounterEffect), effects)
        effectJob.cancelAndJoin()
        effect2Job.cancelAndJoin()

        // Test resubscribing
        val emptyEffects = mutableListOf<CounterEffect>()
        val emptyEffectJob = launch { ellipse.effect.collect { emptyEffects.add(it) } }
        assertEquals(emptyList<CounterEffect>(), emptyEffects)
        emptyEffectJob.cancelAndJoin()
        ellipseScope.cancel()
    }

    @Test
    fun `test caching effects when there are no subscribers`() = runTest {
        val ellipseScope = TestScope(testScheduler)
        val ellipse: CounterEffectEllipse = ellipseScope.ellipse {
            effects.send(CounterEffect)
        }
        val effects = mutableListOf<CounterEffect>()
        val effectJob = launch { ellipse.effect.collect { effects.add(it) } }
        ellipse.sendEvent(CounterEvent)
        runCurrent()
        assertEquals(listOf(CounterEffect), effects)
        effectJob.cancelAndJoin()

        ellipse.sendEvent(CounterEvent)
        ellipse.sendEvent(CounterEvent)

        // Test resubscribing
        val cachedEffects = mutableListOf<CounterEffect>()
        val cachedEffectsJob = launch { ellipse.effect.collect { cachedEffects.add(it) } }
        runCurrent()
        assertEquals(listOf(CounterEffect, CounterEffect), cachedEffects)
        cachedEffectsJob.cancelAndJoin()
        ellipseScope.cancel()
    }
}
