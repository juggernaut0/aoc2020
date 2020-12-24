package solutions

import components.BasePuzzle

class Day24 : BasePuzzle("Day 24: Lobby Layout") {
    override fun solvePart1(input: String): String {
        val tiles = parseInput(input)
        val map = HexGrid()
        for (tile in tiles) {
            map.flip(tile)
        }
        return map.flipped().toString()
    }

    override fun solvePart2(input: String): String {
        val tiles = parseInput(input)
        val map = HexGrid()
        for (tile in tiles) {
            map.flip(tile)
        }
        for (i in 1..100) {
            map.step()
        }
        return map.flipped().toString()
    }

    private fun parseInput(input: String): List<List<Direction>> {
        return input.lineSequence().filter { it.isNotBlank() }.map { line ->
            val chars = line.iterator()
            val res = mutableListOf<Direction>()
            while (chars.hasNext()) {
                val dir = when (chars.nextChar()) {
                    'e' -> Direction.E
                    'w' -> Direction.W
                    'n' -> when (chars.nextChar()) {
                        'e' -> Direction.NE
                        'w' -> Direction.NW
                        else -> error(line)
                    }
                    's' -> when (chars.nextChar()) {
                        'e' -> Direction.SE
                        'w' -> Direction.SW
                        else -> error(line)
                    }
                    else -> error(line)
                }
                res.add(dir)
            }
            res
        }.toList()
    }

    private class HexGrid {
        private var store: MutableMap<Pair<Int, Int>, Boolean> = mutableMapOf()

        /*
           +y
            \
        -x --\-- +x
              \
              -y
         */

        private fun get(x: Int, y: Int): Boolean  = store[x to y] ?: false


        fun flip(dirs: List<Direction>) {
            var x = 0
            var y = 0
            for (dir in dirs) {
                when (dir) {
                    Direction.E -> x += 1
                    Direction.SE -> y -= 1
                    Direction.SW ->  {
                        x -= 1
                        y -= 1
                    }
                    Direction.W -> x -= 1
                    Direction.NW -> y += 1
                    Direction.NE -> {
                        x += 1
                        y += 1
                    }
                }
            }
            val p = x to y
            store[p] = !(store[p] ?: false)
        }

        fun step() {
            val newMap = mutableMapOf<Pair<Int, Int>, Boolean>()

            val (minX, maxX) = store.keys.minMaxOf { it.first }!!
            val (minY, maxY) = store.keys.minMaxOf { it.second }!!

            for (x in minX-1..maxX+1) {
                for (y in minY-1..maxY+1) {
                    val numAdj = adj(x, y).count { store[it] ?: false }
                    val black = get(x, y)
                    if (black && (numAdj == 0 || numAdj > 2)) {
                        // nothing
                    } else if (!black && numAdj == 2) {
                        newMap[x to y] = true
                    } else {
                        newMap[x to y] = black
                    }
                }
            }

            store = newMap
        }

        private fun adj(x: Int, y: Int): List<Pair<Int, Int>> {
            return listOf(
                x + 1 to y,
                x to y - 1,
                x - 1 to y - 1,
                x - 1 to y,
                x to y + 1,
                x + 1 to y + 1,
            )
        }

        fun flipped(): Int = store.values.count { it }
    }

    private enum class Direction {
        E, SE, SW, W, NW, NE
    }
}