package solutions

import components.BasePuzzle
import kotlin.math.abs

class Day12 : BasePuzzle("Day 12: Rain Risk") {
    override fun solvePart1(input: String): String {
        val instrs = parseInput(input)

        var state = State(0, 0, Direction.E)
        for (instr in instrs) {
            state = instr.apply(state)
        }
        return (abs(state.x) + abs(state.y)).toString()
    }

    override fun solvePart2(input: String): String {
        val instrs = parseInput(input)

        var state = State(0, 0, Direction.E, wx = 10, wy = 1)
        for (instr in instrs) {
            state = instr.apply2(state)
            console.log(state)
        }
        return (abs(state.x) + abs(state.y)).toString()
    }

    private fun parseInput(input: String): List<Instruction> {
        return input
            .lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val amt = it.substring(1).toInt()
                when (it[0]) {
                    'N' -> Instruction.North(amt)
                    'E' -> Instruction.East(amt)
                    'S' -> Instruction.South(amt)
                    'W' -> Instruction.West(amt)
                    'L' -> Instruction.Left(amt)
                    'R' -> Instruction.Right(amt)
                    'F' -> Instruction.Forward(amt)
                    else -> throw IllegalStateException()
                }
            }
            .toList()
    }

    sealed class Instruction {
        class North(val amt: Int): Instruction() {
            override fun apply(state: State): State {
                return state.copy(y = state.y + amt)
            }

            override fun apply2(state: State): State {
                return state.copy(wy = state.wy + amt)
            }
        }
        class East(val amt: Int): Instruction() {
            override fun apply(state: State): State {
                return state.copy(x = state.x + amt)
            }

            override fun apply2(state: State): State {
                return state.copy(wx = state.wx + amt)
            }
        }
        class South(val amt: Int): Instruction() {
            override fun apply(state: State): State {
                return state.copy(y = state.y - amt)
            }

            override fun apply2(state: State): State {
                return state.copy(wy = state.wy - amt)
            }
        }
        class West(val amt: Int): Instruction() {
            override fun apply(state: State): State {
                return state.copy(x = state.x - amt)
            }

            override fun apply2(state: State): State {
                return state.copy(wx = state.wx - amt)
            }
        }
        class Left(val amt: Int): Instruction() {
            override fun apply(state: State): State {
                val newDir = (0 until (360 - amt)/90).fold(state.dir) { d, _ -> d.next() }
                return state.copy(dir = newDir)
            }

            override fun apply2(state: State): State {
                val nwx = state.wx * cos(amt) - state.wy * sin(amt)
                val nwy = state.wx * sin(amt) + state.wy * cos(amt)
                return state.copy(wx = nwx, wy = nwy)
            }
        }
        class Right(val amt: Int): Instruction() {
            override fun apply(state: State): State {
                val newDir = (0 until amt/90).fold(state.dir) { d, _ -> d.next() }
                return state.copy(dir = newDir)
            }

            override fun apply2(state: State): State {
                val amt = 360 - amt
                val nwx = state.wx * cos(amt) - state.wy * sin(amt)
                val nwy = state.wx * sin(amt) + state.wy * cos(amt)
                return state.copy(wx = nwx, wy = nwy)
            }
        }
        class Forward(val amt: Int): Instruction() {
            override fun apply(state: State): State {
                return when (state.dir) {
                    Direction.N -> North(amt).apply(state)
                    Direction.E -> East(amt).apply(state)
                    Direction.S -> South(amt).apply(state)
                    Direction.W -> West(amt).apply(state)
                }
            }

            override fun apply2(state: State): State {
                val dx = state.wx * amt
                val dy = state.wy * amt
                return state.copy(x = state.x + dx, y = state.y + dy)
            }
        }

        abstract fun apply(state: State): State
        abstract fun apply2(state: State): State
    }
    enum class Direction {
        N, E, S, W;

        fun next(): Direction {
            return when (this) {
                N -> E
                E -> S
                S -> W
                W -> N
            }
        }
    }

    data class State(val x: Int, val y: Int, val dir: Direction, val wx: Int = 0, val wy: Int = 0)
}

fun sin(deg: Int): Int {
    return when (deg) {
        0, 180, 360 -> 0
        90 -> 1
        270 -> -1
        else -> throw IllegalArgumentException(deg.toString())
    }
}

fun cos(deg: Int): Int {
    return when (deg) {
        0, 360 -> 1
        90, 270 -> 0
        180 -> -1
        else -> throw IllegalArgumentException(deg.toString())
    }
}