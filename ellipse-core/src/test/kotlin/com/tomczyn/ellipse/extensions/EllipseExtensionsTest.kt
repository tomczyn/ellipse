package com.tomczyn.ellipse.extensions

import com.tomczyn.ellipse.Ellipse
import com.tomczyn.ellipse.common.ellipse
import com.tomczyn.ellipse.test.ellipseTest
import com.tomczyn.ellipse.util.BaseCoroutineTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class EllipseExtensionsTest : BaseCoroutineTest() {

    private val scope: CoroutineScope = MainScope()

    private val unitEllipse: Ellipse<Unit, Unit, Unit> = scope.ellipse(
        prepare = {},
        onEvent = { effects.send(Unit) }
    )

    private val standardEllipse: Ellipse<Unit, String, Unit> = scope.ellipse(
        initialState = "",
        prepare = { emptyFlow() },
        onEvent = { effects.send(Unit); emptyFlow() }
    )

    @Test
    fun `test creating unit ellipse`() = ellipseTest(
        ellipse = { unitEllipse },
        whenEvent = Unit,
        thenStates = { assertValues(Unit) },
        thenEffects = { assertValues(Unit) }
    )

    @Test
    fun `test creating unit ellipse with separate runTest`() = runTest {
        ellipseTest(
            ellipse = { unitEllipse },
            whenEvent = Unit,
            thenStates = { assertValues(Unit) },
            thenEffects = { assertValues(Unit) }
        )
    }

    @Test
    fun `test creating standard ellipse`() = ellipseTest(
        ellipse = { standardEllipse },
        whenEvent = Unit,
        thenStates = { assertValues("") },
        thenEffects = { assertValues(Unit) }
    )

    @Test
    fun `test creating standard ellipse with separate runTest`() = runTest {
        ellipseTest(
            ellipse = { standardEllipse },
            whenEvent = Unit,
            thenStates = { assertValues("") },
            thenEffects = { assertValues(Unit) }
        )
    }
}
