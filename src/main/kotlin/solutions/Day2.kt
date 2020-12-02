package solutions

import components.BasePuzzle

class Day2 : BasePuzzle("Day 2: Password Philosophy") {
    override fun solvePart1(input: String): String {
        return input.lineSequence()
                .filter { it.isNotEmpty() }
                .count { isValid(it) }
                .toString()
    }

    override fun solvePart2(input: String): String {
        return input.lineSequence()
                .filter { it.isNotEmpty() }
                .count { isValid2(it) }
                .toString()
    }

    private fun parseRow(row: String): Triple<IntRange, Char, String> {
        val (limits, letter, password) = row.split(' ')
        val (low, high) = limits.split('-').map { it.toInt() }
        val c = letter[0]
        return Triple(low..high, c, password)
    }

    private fun isValid(row: String): Boolean {
        val (limits, letter, password) = parseRow(row)
        return password.count { it == letter } in limits
    }

    private fun isValid2(row: String): Boolean {
        val (limits, letter, password) = parseRow(row)
        val i = limits.first-1
        val j = limits.last -1

        return (password[i] == letter).xor(password[j] == letter)
    }
}
