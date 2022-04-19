package com.tomcz.ellipse.test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

interface TestResult<T : Any> {

    val values: List<T>

    fun assertValues(vararg listToAssert: T) {
        assertEquals(listToAssert.toList(), values)
    }

    fun assertSize(size: Int) {
        assertEquals(size, values.size)
    }

    fun assertAt(index: Int, value: T) {
        assertEquals(value, values[index])
    }

    fun assertFirst(value: T) {
        assertEquals(value, values.first())
    }

    fun assertLast(value: T) {
        assertEquals(value, values.last())
    }

    fun assertEmpty() {
        assertTrue(values.isEmpty())
    }
}

internal class TestResultImpl<T : Any>(override val values: List<T>) : TestResult<T>
