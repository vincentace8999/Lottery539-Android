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
        assertEquals(5, result.tailSuggested.size)
        assertTrue(result.tailSuggested.none { it in result.previousNumbers })
        assertTrue(result.tailSuggested.all { it % 10 in setOf(1, 2, 3, 4, 5) })
    }

    @Test fun tailSuggestionStillContainsFiveNumbersWhenTailsRepeat() {
        val draws = listOf(Draw("202600001", "2026-01-01", listOf(1, 2, 11, 21, 31)))
        val result = analyze(draws)
        assertEquals(5, result.tailSuggested.size)
        assertEquals(5, result.tailSuggested.distinct().size)
        assertTrue(result.tailSuggested.all { it % 10 in setOf(1, 2) })
    }
}
