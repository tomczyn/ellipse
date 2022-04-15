package com.tomcz.sample.feature.foo

import com.tomcz.ellipse.test.processorTest
import com.tomcz.sample.feature.foo.state.FooEffect
import com.tomcz.sample.feature.foo.state.FooState
import com.tomcz.sample.feature.register.util.BaseCoroutineTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class FooViewModelTest : BaseCoroutineTest() {

    private val viewModel: FooViewModel by lazy { FooViewModel() }

    /**
     * Testing a more complex scenario with intermediate changes to StateFlow value.
     */
    @Test
    fun `test prepare`() = processorTest(
        processor = { viewModel.processor },
        thenStates = {
            assertValues(
                FooState(0),
                FooState(1),
                FooState(2),
                FooState(3),
                FooState(4),
                FooState(5),
            )
        },
        thenEffects = {
            assertValues(
                FooEffect.BarEffect,
                FooEffect.BarEffect,
                FooEffect.BarEffect,
            )
        }
    )
}