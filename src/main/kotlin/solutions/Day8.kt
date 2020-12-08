package solutions

import components.BasePuzzle
import kotlin.properties.Delegates

class Day8 : BasePuzzle("Day 8: Handheld Halting") {
    override fun solvePart1(input: String): String {
        val program = parseInput(input)

        val lastAcc = execute(program).first
        return lastAcc.toString()
    }

    override fun solvePart2(input: String): String {
        val program = parseInput(input)

        for ((i, opCode) in program.withIndex()) {
            val newOp = when (opCode.op) {
                Op.JMP -> Op.NOP
                Op.NOP -> Op.JMP
                Op.ACC -> continue
            }
            val newProgram = program.toMutableList()
            newProgram[i] = OpCode(newOp, opCode.arg)
            val (acc, term) = execute(newProgram)
            if (term) return acc.toString()
        }

        return "no solution"
    }

    private fun parseInput(input: String): List<OpCode> {
        return input.lineSequence()
            .filter { it.isNotBlank() }
            .map { parseOp(it) }
            .toList()
    }

    private fun parseOp(opCStr: String): OpCode {
        val (opS, arg) = opCStr.split(' ')

        val op = when (opS) {
            "acc" -> Op.ACC
            "jmp" -> Op.JMP
            else -> Op.NOP
        }

        return OpCode(op, arg.toInt())
    }

    private fun execute(program: List<OpCode>): Pair<Int, Boolean> {
        var acc = 0
        var pc = 0
        val seen = mutableSetOf<Int>()
        var term by Delegates.notNull<Boolean>()
        while (true) {
            if (pc == program.size) {
                term = true
                break
            }
            if (!seen.add(pc)) {
                term = false
                break
            }
            val opCode = program[pc]
            when (opCode.op) {
                Op.ACC -> {
                    acc += opCode.arg
                    pc += 1
                }
                Op.JMP -> pc += opCode.arg
                Op.NOP -> pc += 1
            }
        }
        return acc to term
    }

    data class OpCode(val op: Op, val arg: Int)

    enum class Op { ACC, JMP, NOP }
}