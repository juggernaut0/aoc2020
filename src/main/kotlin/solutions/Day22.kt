package solutions

import components.BasePuzzle

class Day22 : BasePuzzle("Day 22: Crab Combat") {
    override fun solvePart1(input: String): String {
        val game = parseInput(input)

        val winner = game.play()

        return winner.zip(winner.size downTo 1).sumBy { (a, b) -> a * b }.toString()
    }

    override fun solvePart2(input: String): String {
        val game = parseInput(input).toRecursive()

        val winner = game.play()
        val deck = if (winner == 1) game.p1 else game.p2

        return deck.zip(deck.size downTo 1).sumBy { (a, b) -> a * b }.toString()
    }

    private fun parseInput(input: String): Game {
        var mode = true
        val p1 = mutableListOf<Int>()
        val p2 = mutableListOf<Int>()
        for (line in input.lineSequence()) {
            if (line.startsWith("Player")) continue
            if (line.isBlank()) {
                mode = false
                continue
            }
            if (mode) {
                p1.add(line.toInt())
            } else {
                p2.add(line.toInt())
            }
        }
        return Game(p1, p2)
    }

    private class Game(val p1: MutableList<Int>, val p2: MutableList<Int>) {
        fun toRecursive() = RecursiveGame(p1, p2)

        fun play(): List<Int> {
            var winner: List<Int>? = null
            while (winner == null) {
                turn()
                winner = winner()
            }
            return winner
        }

        fun winner(): List<Int>? {
            return when {
                p1.isEmpty() -> p2
                p2.isEmpty() -> p1
                else -> null
            }
        }

        fun turn() {
            val c1 = p1.removeAt(0)
            val c2 = p2.removeAt(0)
            if (c1 > c2) {
                p1.add(c1)
                p1.add(c2)
            } else {
                p2.add(c2)
                p2.add(c1)
            }
        }
    }

    private class RecursiveGame(val p1: MutableList<Int>, val p2: MutableList<Int>) {
        val history = mutableSetOf<Pair<List<Int>, List<Int>>>()

        fun play(): Int {
            var winner: Int? = null
            while (winner == null) {
                turn()
                winner = winner()
            }
            return winner
        }

        fun winner(): Int? {
            return when {
                history.contains(p1 to p2) -> 1
                p1.isEmpty() -> 2
                p2.isEmpty() -> 1
                else -> null
            }
        }

        fun turn() {
            history.add(p1.toList() to p2.toList())
            val c1 = p1.removeAt(0)
            val c2 = p2.removeAt(0)
            if (c1 <= p1.size && c2 <= p2.size) {
                val winner = RecursiveGame(p1.subList(0, c1).toMutableList(), p2.subList(0, c2).toMutableList()).play()
                if (winner == 1) {
                    p1.add(c1)
                    p1.add(c2)
                } else {
                    p2.add(c2)
                    p2.add(c1)
                }
            } else if (c1 > c2) {
                p1.add(c1)
                p1.add(c2)
            } else {
                p2.add(c2)
                p2.add(c1)
            }
        }
    }
}