package solutions

import components.BasePuzzle

class Day17 : BasePuzzle("17") {
    override fun solvePart1(input: String): String {
        val field = parseInput(input)

        val after = (1..6).fold(field) { f, _ -> f.step1() }

        return after.store.values.count { it }.toString()
    }

    override fun solvePart2(input: String): String {
        val field = parseInput(input)

        val after = (1..6).fold(field) { f, _ -> f.step2() }

        return after.store.values.count { it }.toString()
    }

    private fun parseInput(input: String): Field {
        val map = mutableMapOf<Coords, Boolean>()
        for ((y, line) in input.lineSequence().withIndex()) {
            for ((x, c) in line.withIndex()) {
                map[Coords(x, y, 0, 0)] = c == '#'
            }
        }
        return Field(map)
    }

    private data class Coords(val x: Int, val y: Int, val z: Int, val w: Int) {
        fun surrounding3(): Sequence<Coords> {
            return sequence {
                val self = this@Coords
                for (x in -1..1) {
                    for (y in -1..1) {
                        for (z in -1..1) {
                            if (x != 0 || y != 0 || z != 0) {
                                yield(Coords(self.x + x, self.y + y, self.z + z, self.w))
                            }
                        }
                    }
                }
            }
        }

        fun surrounding4(): Sequence<Coords> {
            return sequence {
                val self = this@Coords
                for (x in -1..1) {
                    for (y in -1..1) {
                        for (z in -1..1) {
                            for (w in -1..1) {
                                if (x != 0 || y != 0 || z != 0 || w != 0) {
                                    yield(Coords(self.x + x, self.y + y, self.z + z, self.w + w))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private data class Field(val store: Map<Coords, Boolean>) {
        operator fun get(coords: Coords): Boolean {
            return store[coords] ?: false
        }

        fun step1(): Field {
            if (store.isEmpty()) return this
            val (minx, maxx) = store.keys.minMaxOf { it.x }!!
            val (miny, maxy) = store.keys.minMaxOf { it.y }!!
            val (minz, maxz) = store.keys.minMaxOf { it.z }!!

            val newMap = mutableMapOf<Coords, Boolean>()
            for (x in minx-1..maxx+1) {
                for (y in miny-1..maxy+1) {
                    for (z in minz-1..maxz+1) {
                        val curr = Coords(x, y, z, 0)
                        val currState = get(curr)
                        val surrounding = curr.surrounding3().count { get(it) }
                        if (currState && surrounding in 2..3) {
                            newMap[curr] = true
                        } else if (!currState && surrounding == 3) {
                            newMap[curr] = true
                        }
                    }
                }
            }
            return Field(newMap)
        }

        fun step2(): Field {
            if (store.isEmpty()) return this
            val (minx, maxx) = store.keys.minMaxOf { it.x }!!
            val (miny, maxy) = store.keys.minMaxOf { it.y }!!
            val (minz, maxz) = store.keys.minMaxOf { it.z }!!
            val (minw, maxw) = store.keys.minMaxOf { it.z }!!

            val newMap = mutableMapOf<Coords, Boolean>()
            for (x in minx-1..maxx+1) {
                for (y in miny-1..maxy+1) {
                    for (z in minz-1..maxz+1) {
                        for (w in minw-1..maxw+1) {
                            val curr = Coords(x, y, z, w)
                            val currState = get(curr)
                            val surrounding = curr.surrounding4().count { get(it) }
                            if (currState && surrounding in 2..3) {
                                newMap[curr] = true
                            } else if (!currState && surrounding == 3) {
                                newMap[curr] = true
                            }
                        }
                    }
                }
            }
            return Field(newMap)
        }
    }


}

inline fun <T> Iterable<T>.minMaxOf(selector: (T) -> Int): Pair<Int, Int>? {
    val iter = iterator()
    if (!iter.hasNext()) return null
    var min = Int.MAX_VALUE
    var max = Int.MIN_VALUE
    for (t in iter) {
        val k = selector(t)
        min = kotlin.math.min(min, k)
        max = kotlin.math.max(max, k)
    }
    return min to max
}
