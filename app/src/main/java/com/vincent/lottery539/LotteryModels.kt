package com.vincent.lottery539

data class Draw(
    val period: String,
    val date: String,
    val numbers: List<Int>
)

data class Analysis(
    val hot: List<Pair<Int, Int>>,
    val cold: List<Pair<Int, Int>>,
    val suggested: List<Int>
)

fun analyze(draws: List<Draw>): Analysis {
    val counts = (1..39).associateWith { 0 }.toMutableMap()
    draws.take(20).flatMap { it.numbers }.forEach { n -> counts[n] = counts.getValue(n) + 1 }
    val hot = counts.entries.sortedWith(compareByDescending<Map.Entry<Int, Int>> { it.value }.thenBy { it.key })
        .take(8).map { it.key to it.value }
    val cold = counts.entries.sortedWith(compareBy<Map.Entry<Int, Int>> { it.value }.thenBy { it.key })
        .take(8).map { it.key to it.value }
    val candidates = (hot.take(5).map { it.first } + cold.take(4).map { it.first }).distinct()
    val seed = draws.firstOrNull()?.period?.hashCode()?.toLong() ?: System.currentTimeMillis()
    val suggested = candidates.shuffled(kotlin.random.Random(seed)).take(5).sorted()
    return Analysis(hot, cold, suggested)
}
