package com.vincent.lottery539

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalysisTest {
    @Test fun countsAndSuggestsValidNumbers() {
        val draws = (1..20).map { Draw(it.toString(), "2026-01-$it", listOf(1, 2, 3, 4, 5)) }
        val result = analyze(draws)
        assertEquals(20, result.hot.first().second)
        assertEquals(5, result.suggested.size)
        assertTrue(result.suggested.all { it in 1..39 })
    }
}
