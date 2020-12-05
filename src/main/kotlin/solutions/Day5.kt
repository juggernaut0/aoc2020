package solutions

import components.BasePuzzle

class Day5 : BasePuzzle("Day 5: Binary Boarding") {
    override fun solvePart1(input: String): String {
        return input.lineSequence()
            .filter { it.isNotBlank() }
            .maxOf { rowId(it) }
            .toString()
    }

    override fun solvePart2(input: String): String {
        val seats = input.lineSequence()
            .filter { it.isNotBlank() }
            .mapTo(mutableSetOf()) { rowId(it) }

        val min = seats.minOrNull()!!

        return seats.find { it != min && it-1 !in seats }?.let { it-1 }.toString()
    }

    private val translation = mapOf(
        'F' to '0',
        'B' to '1',
        'L' to '0',
        'R' to '1',
    )

    private fun rowId(input: String): Int {
        return input.mapNotNull { translation[it] }.toCharArray().concatToString().toInt(2)
    }
}
