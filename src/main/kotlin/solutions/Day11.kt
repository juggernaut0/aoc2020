package solutions

import components.BasePuzzle
import components.Grid
import kotlinx.browser.window
import kui.MarkupBuilder
import kui.Props
import kui.classes

class Day11 : BasePuzzle("Day 11: Seating System") {
    override fun solvePart1(input: String): String {
        map = parseInput(input)
        stepFn = SeatMap::step
        return ""
    }

    override fun solvePart2(input: String): String {
        map = parseInput(input)
        stepFn = SeatMap::step2
        return ""
    }

    private fun runToCompletion() {
        fun callback() {
            val newMap = map.stepFn()
            if (map != newMap) {
                map = newMap
                render()
                window.requestAnimationFrame { callback() }
            }
        }

        callback()
    }

    private lateinit var map: SeatMap
    private var stepFn: SeatMap.() -> SeatMap = SeatMap::step

    override fun MarkupBuilder.visualize() {
        if (::map.isInitialized) {
            div(classes(Grid.col3)) { +map.map.values.count { it == Tile.OCCUPIED }.toString() }
            button(Props(classes = listOf(Grid.col3), click = { map = map.stepFn(); render() })) { +"Step" }
            button(Props(classes = listOf(Grid.col3), click = { runToCompletion() })) { +"Run" }
            pre(classes(Grid.col12)) {
                +map.toString()
            }
        }
    }

    private fun parseInput(input: String): SeatMap {
        val map = mutableMapOf<Pair<Int, Int>, Tile>()
        for ((y, line) in input.lineSequence().withIndex()) {
            for ((x, c) in line.withIndex()) {
                val tile = when (c) {
                    '.' -> Tile.FLOOR
                    'L' -> Tile.EMPTY
                    else -> Tile.WALL
                }
                map[x to y] = tile
            }
        }
        return SeatMap(map)
    }

    data class SeatMap(val map: Map<Pair<Int, Int>, Tile>) {
        operator fun get(xy: Pair<Int, Int>): Tile {
            return map.getOrElse(xy) { Tile.WALL }
        }

        operator fun get(x: Int, y: Int): Tile {
            return get(x to y)
        }

        fun step(): SeatMap {
            val newMap = mutableMapOf<Pair<Int, Int>, Tile>()
            for ((pos, tile) in map) {
                if (tile == Tile.FLOOR || tile == Tile.WALL) {
                    newMap[pos] = tile
                    continue
                }
                val (x, y) = pos
                var count = 0
                if (get(x - 1, y - 1) == Tile.OCCUPIED) count += 1
                if (get(x - 1, y) == Tile.OCCUPIED) count += 1
                if (get(x - 1, y + 1) == Tile.OCCUPIED) count += 1
                if (get(x, y - 1) == Tile.OCCUPIED) count += 1
                if (get(x, y + 1) == Tile.OCCUPIED) count += 1
                if (get(x + 1, y - 1) == Tile.OCCUPIED) count += 1
                if (get(x + 1, y) == Tile.OCCUPIED) count += 1
                if (get(x + 1, y + 1) == Tile.OCCUPIED) count += 1
                val newTile =
                    if (tile == Tile.EMPTY && count == 0) Tile.OCCUPIED
                    else if (tile == Tile.OCCUPIED && count >= 4) Tile.EMPTY
                    else tile
                newMap[pos] = newTile
            }
            return SeatMap(newMap)
        }

        fun step2(): SeatMap {
            val newMap = mutableMapOf<Pair<Int, Int>, Tile>()
            for ((pos, tile) in map) {
                if (tile == Tile.FLOOR || tile == Tile.WALL) {
                    newMap[pos] = tile
                    continue
                }
                val (x, y) = pos
                var count = 0

                fun scan(dx: Int, dy: Int): Boolean {
                    var x = x + dx
                    var y = y + dy
                    while (true) {
                        when (get(x, y)) {
                            Tile.OCCUPIED -> return true
                            Tile.EMPTY -> return false
                            Tile.WALL -> return false
                            else -> {
                                x += dx
                                y += dy
                            }
                        }
                    }
                }

                if (scan(-1, -1)) count += 1
                if (scan(-1,  0)) count += 1
                if (scan(-1, +1)) count += 1
                if (scan( 0, -1)) count += 1
                if (scan( 0, +1)) count += 1
                if (scan(+1, -1)) count += 1
                if (scan(+1,  0)) count += 1
                if (scan(+1, +1)) count += 1
                val newTile =
                    if (tile == Tile.EMPTY && count == 0) Tile.OCCUPIED
                    else if (tile == Tile.OCCUPIED && count >= 5) Tile.EMPTY
                    else tile
                newMap[pos] = newTile
            }
            return SeatMap(newMap)
        }

        override fun toString(): String {
            val maxy = map.keys.maxOf { it.second }
            val maxx = map.keys.maxOf { it.first }
            return buildString {
                for (y in 0..maxy) {
                    for (x in 0..maxx) {
                        append(when (get(x, y)) {
                            Tile.FLOOR -> '.'
                            Tile.EMPTY -> 'L'
                            Tile.OCCUPIED -> '#'
                            Tile.WALL -> 'X'
                        })
                    }
                    append('\n')
                }
            }
        }
    }

    enum class Tile {
        FLOOR,
        EMPTY,
        OCCUPIED,
        WALL,
    }
}
