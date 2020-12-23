package solutions

import components.BasePuzzle

class Day23 : BasePuzzle("Day 23: Crab Cups") {
    override fun solvePart1(input: String): String {
        val game = parseInput(input)

        for (i in 1..100) {
            console.log(game.toString())
            game.move()
        }
        console.log(game.toString())

        return game.iter(1).drop(1).joinToString(separator = "")
    }

    override fun solvePart2(input: String): String {
        val game = parseInput(input, 10..1000000)

        for (i in 1..10000000) {
            if (i % 10000 == 0) {
                console.log(i)
            }
            game.move()
        }

        val iter = game.iter(1).drop(1).iterator()
        val a = iter.next().toLong()
        val b = iter.next()
        return (a * b).toString()
    }

    private fun parseInput(input: String, extra: Iterable<Int> = emptyList()): CupGame {
        return CupGame(input.lines().first().toCharArray().mapTo(mutableListOf()) { it.toString().toInt() }.also { it.addAll(extra) })
    }

    private class CupGame(cups: List<Int>) {
        val succ = cups.zipWithNext().toMap().toMutableMap().also { it[cups.last()] = cups.first() }
        init { println(succ) }
        val size = cups.size
        private var current = cups.first()

        fun modSize(n: Int) = (n + size) % size

        fun iter(start: Int = current): Sequence<Int> {
            return sequence {
                var curr = start
                var go = true
                while (curr != start || go) {
                    go = false
                    yield(curr)
                    curr = succ[curr]!!
                }
            }
        }

        fun move() {
            val (currCup, pickup) = first4()
            var destCup = modSize(currCup - 2) + 1
            while (destCup in pickup) {
                destCup = modSize(destCup - 2) + 1
            }
            val oldDSucc = succ[destCup]!!
            val (i1, _, i3) = pickup
            succ[current] = succ[i3]!!
            succ[destCup] = i1
            succ[i3] = oldDSucc
            current = succ[current]!!
        }

        private fun first4(): Pair<Int, List<Int>> {
            val i0 = current
            val i1 = succ[i0]!!
            val i2 = succ[i1]!!
            val i3 = succ[i2]!!
            return i0 to listOf(i1, i2, i3)
        }

        override fun toString(): String {
            return iter()
                .joinToString(separator = " ")
        }
    }
}