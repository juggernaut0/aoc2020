package solutions

import components.BasePuzzle

class Day3 : BasePuzzle("Day 3: Toboggan Trajectory") {
    override fun solvePart1(input: String): String {
        val map = parseMap(input)

        return runSlope(map, 3, 1).toString()
    }

    override fun solvePart2(input: String): String {
        val map = parseMap(input)

        val a = runSlope(map, 1, 1).toLong()
        val b = runSlope(map, 3, 1).toLong()
        val c = runSlope(map, 5, 1).toLong()
        val d = runSlope(map, 7, 1).toLong()
        val e = runSlope(map, 1, 2).toLong()

        return (a*b*c*d*e).toString()
    }

    class SlopeMap(private val map: Map<Pair<Int, Int>, Char>, val width: Int, val height: Int) {
        operator fun get(x: Int, y: Int): Char {
            return map[x % width to y]!!
        }
    }

    private fun parseMap(input: String): SlopeMap {
        val map = mutableMapOf<Pair<Int, Int>, Char>()

        val width = input.lineSequence().first().length
        var height = 0

        for ((y, line) in input.lineSequence().filter { it.isNotBlank() }.withIndex()) {
            for ((x, c) in line.withIndex()) {
                map[x to y] = c
            }
            height++
        }

        return SlopeMap(map, width, height)
    }

    private fun runSlope(map: SlopeMap, right: Int, down: Int): Int {
        var trees = 0
        var x = 0
        for (y in (down until map.height).step(down)) {
            console.log(y)
            x += right
            if (map[x, y] == '#') trees++
        }

        return trees
    }
}
