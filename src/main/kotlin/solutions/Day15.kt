package solutions

import components.BasePuzzle

class Day15 : BasePuzzle("Day 15: Rambunctious Recitation") {
    override fun solvePart1(input: String): String {
        return run(parseInput(input), 2020).toString()
    }

    override fun solvePart2(input: String): String {
        return run(parseInput(input), 30000000).toString()
    }

    private fun run(seed: List<Int>, n: Int): Int {
        // n to turn last spoken
        val spoken: MutableMap<Int, Int> = seed.withIndex().associateTo(mutableMapOf()) { (i, n) -> n to i }

        var last = seed.last()
        for (i in seed.lastIndex until n-1) {
            val old = last
            val new = spoken[last]?.let { i - it } ?: 0
            spoken[old] = i
            last = new
        }
        return last
    }

    private fun parseInput(input: String): List<Int> {
        return input.split(',').map { it.toInt() }
    }
}