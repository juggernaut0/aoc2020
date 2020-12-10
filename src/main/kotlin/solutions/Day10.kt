package solutions

import components.BasePuzzle

class Day10 : BasePuzzle("Day 10: Adapter Array") {
    override fun solvePart1(input: String): String {
        val adaps = parseInput(input)

        var ones = 0
        var threes = 0

        for ((a, b) in adaps.zipWithNext()) {
            when(b - a) {
                1 -> ones++
                3 -> threes++
            }
        }

        return (ones*threes).toString()
    }

    override fun solvePart2(input: String): String {
        val adaps = parseInput(input)
        val diffs = adaps.zipWithNext { a, b -> b - a }
        console.log(diffs.toTypedArray())
        var res: Long = 1
        var run = 0
        val muls = arrayOf(1, 1, 2, 4, 7, 13, 23)
        for (d in diffs) {
            if (d == 1) {
                run++
            } else {
                res *= muls[run]
                run = 0
            }
        }
        return res.toString()
    }

    private fun parseInput(input: String): List<Int> {
        val res = input.lineSequence().filter { it.isNotBlank() }.map { it.toInt() }.toMutableList()

        res.add(0)
        res.add(res.maxOrNull()!! + 3)
        res.sort()

        return res
    }
}

/*
7) 0 1 2 3 4 5 6 7 10 -> 1 + 6 + 15 +
6) 0 1 2 3 4 5 6 9    -> 1 + 5 + 10 + 6 + 1 + 0 = 23
5) 0 1 2 3 4 5 8      -> 1 + 4 +  6 + 2 + 0 = 13
4) 0 1 2 3 4 7        -> 1 + 3 +  3 + 0 = 7
3) 0 1 2 3 6          -> 1 + 2 +  1 = 4
2) 0 1 2 5            -> 1 + 1 =  2
1) 0 1 4              -> 1 = 1
0) 0 3                -> 1 = 1
*/
