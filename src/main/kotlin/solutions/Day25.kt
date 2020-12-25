package solutions

import components.BasePuzzle
import kotlin.properties.Delegates

class Day25 : BasePuzzle("Day 25: Combo Breaker") {
    override fun solvePart1(input: String): String {
        val (pk1, pk2) = parseInput(input)

        var ls1: Int by Delegates.notNull()
        for ((i, k) in genKeys(7).withIndex()) {
            if (k == pk1) {
                ls1 = i
                break
            }
        }

        return genKeys(pk2).drop(ls1).first().toString()
    }

    override fun solvePart2(input: String): String {
        return "Merry Christmas!"
    }

    private fun parseInput(input: String): Pair<Int, Int> {
        val lines = input.lines()
        return lines[0].toInt() to lines[1].toInt()
    }

    // spits out keys of incrementing loop sizes
    private fun genKeys(subject: Int): Sequence<Int> {
        return sequence {
            var value = 1L
            yield(value.toInt())
            while (true) {
                value *= subject
                value %= 20201227
                yield(value.toInt())
            }
        }
    }
}