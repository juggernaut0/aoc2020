package solutions

import components.BasePuzzle

class Day6 : BasePuzzle("Day 6: Custom Customs") {
    override fun solvePart1(input: String): String {
        val groups = parseInput(input)

        return groups.sumBy { group ->
            group.reduce { a, b -> a.union(b) }.size
        }.toString()
    }

    override fun solvePart2(input: String): String {
        val groups = parseInput(input)

        return groups.sumBy { group ->
            group.reduce { a, b -> a.intersect(b) }.size
        }.toString()
    }

    private fun parseInput(input: String): List<List<Set<Char>>> {
        val res = mutableListOf<List<Set<Char>>>()

        var current = mutableListOf<Set<Char>>()
        for (line in input.lineSequence()) {
            if (line.isBlank()) {
                res.add(current)
                current = mutableListOf()
            } else {
                current.add(line.toSet())
            }
        }

        if (current.isNotEmpty()) {
            res.add(current)
        }

        return res
    }
}
