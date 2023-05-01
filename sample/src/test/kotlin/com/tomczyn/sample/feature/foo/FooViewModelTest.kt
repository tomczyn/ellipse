package com.tomczyn.sample.feature.foo

import androidx.lifecycle.viewModelScope
import com.tomczyn.ellipse.test.processorTest
import com.tomczyn.sample.feature.common.BaseCoroutineTest
import com.tomczyn.sample.feature.common.TestDispatcherProvider
import com.tomczyn.sample.feature.foo.state.FooEffect
import com.tomczyn.sample.feature.foo.state.FooEvent
import com.tomczyn.sample.feature.foo.state.FooState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.advanceTimeBy
import org.junit.jupiter.api.Test

@FlowPreview
@ExperimentalCoroutinesApi
internal class FooViewModelTest : BaseCoroutineTest() {

    private val dispatchers = TestDispatcherProvider(dispatcher)

    private val viewModel: FooViewModel by lazy { FooViewModel(dispatchers) }

    /**
     * Testing a more complex scenario with intermediate changes to StateFlow value.
     * Intermediate changes meaning multiple changes to the value in-between suspends.
     */
    @Test
    fun `test prepare`() = processorTest(
        processor = { viewModel.processor },
        thenStates = {
            assertValues(
                FooState(0),
                FooState(1),
                FooState(0),
                FooState(1),
                FooState(2),
                FooState(1),
            )
        },
        thenEffects = {
            assertValues(
                FooEffect.BarEffect,
                FooEffect.BarEffect,
                FooEffect.BarEffect,
            )
        },
    )

    /**
     * Adding one Increase action at the end of state list
     */
    @Test
    fun `test first button click`() = processorTest(
        processor = { viewModel.processor },
        whenEvent = FooEvent.FirstButtonClick,
        thenStates = {
            assertValues(
                FooState(0),
                FooState(1),
                FooState(0),
                FooState(1),
                FooState(2),
                FooState(1),
                FooState(2),
            )
        },
        thenEffects = { assertSize(3) },
    )

    /**
     * No differences with prepare
     */
    @Test
    fun `test second button click`() = processorTest(
        processor = { viewModel.processor },
        whenEvent = FooEvent.SecondButtonClick,
        thenStates = {
            assertSize(6)
            assertLast(FooState(1))
        },
        thenEffects = {
            assertSize(3)
        },
    )

    /**
     * No changes to state, but with additional effect
     */
    @Test
    fun `test third button click`() = processorTest(
        processor = { viewModel.processor },
        whenEvent = FooEvent.ThirdButtonClick,
        thenStates = {
            assertSize(6)
            assertLast(FooState(1))
        },
        thenEffects = { assertSize(4) },
    )

    /**
     * Example testing strategy for infinite stream
     */
    @Test
    fun `test fourth button click`() = processorTest(
        processor = { viewModel.processor },
        whenEvent = FooEvent.FourthButtonClick,
        // Can't use advanceUntilIdle() because of infinite flow,
        // we must stop the time progression at fixed point in time (e.g. 100 * 51 ms)
        afterPrepare = { advanceTimeBy(100 * 51) },
        afterEvents = { advanceTimeBy(100 * 51) },
        thenStates = {
            assertValues(
                FooState(0),
                FooState(1),
                FooState(0),
                FooState(1),
                FooState(2),
                FooState(1),
                FooState(0)
            )
        },
        thenEffects = { assertSize(3) },
        // Manually cancel infinite flow
        cleanup = { viewModel.viewModelScope.cancel() }
    )
}
