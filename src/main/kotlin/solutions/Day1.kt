package solutions

import components.BasePuzzle
import kui.MarkupBuilder

class Day1 : BasePuzzle("Day 1: Report Repair") {
    private var nums = emptyList<Int>()

    override fun solvePart1(input: String): String {
        val nums = parseInput(input)

        this.nums = emptyList()

        return nums
            .asSequence()
            .flatMap { a -> nums.map { b -> a to b } }
            .find { (a, b) -> a + b == 2020 }
            ?.also { this.nums = it.toList() }
            ?.let { (a, b) -> a * b }
            ?.toString()
            ?: "no solution"
    }

    override fun solvePart2(input: String): String {
        val nums = parseInput(input)

        this.nums = emptyList()

        val table = mutableMapOf<Int, Pair<Int, Int>>()

        nums
            .asSequence()
            .flatMap { a -> nums.map { b -> a to b } }
            .filter { (a, b) -> a + b <= 2020 }
            .forEach { p -> table[2020 - p.first - p.second] = p }

        return nums
            .asSequence()
            .mapNotNull { c -> table[c]?.let { (a, b) -> Triple(a, b, c) } }
            .firstOrNull()
            ?.also { this.nums = it.toList() }
            ?.let { (a, b, c) -> a * b * c }
            ?.toString()
            ?: "no solution"
    }

    override fun MarkupBuilder.visualize() {
        if (nums.isNotEmpty()) {
            p { +"The entries are: $nums" }
        }
    }

    private fun parseInput(input: String): List<Int> {
        return input.lineSequence()
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
                .toList()
    }
}