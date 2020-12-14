package solutions

import components.BasePuzzle
import kotlin.math.pow

class Day14 : BasePuzzle("Day 14: Docking Data") {
    override fun solvePart1(input: String): String {
        val instrs = parseInput(input)

        val mem = Memory(List(36) { null }, mutableMapOf())
        for (instr in instrs) {
            instr.apply(mem)
        }
        return mem.mem.values.sum().toString()
    }

    override fun solvePart2(input: String): String {
        val instrs = parseInput(input)

        val mem = Memory(List(36) { null }, mutableMapOf())
        for (instr in instrs) {
            instr.apply2(mem)
        }
        println(mem.mem)
        return mem.mem.values.sum().toString()
    }

    private val maskRe = Regex("mask = ([01X]{36})")
    private val setRe = Regex("mem\\[(\\d+)] = (\\d+)")

    private fun parseInput(input: String): List<Instruction> {
        return input.lineSequence()
            .filter { it.isNotBlank() }
            .map { line ->
                val maskMatch = maskRe.matchEntire(line)
                val setMatch = setRe.matchEntire(line)
                when {
                    maskMatch != null -> {
                        val mask = maskMatch.groupValues[1].map {
                            when (it) {
                                '0' -> false
                                '1' -> true
                                'X' -> null
                                else -> throw IllegalStateException(it.toString())
                            }
                        }.reversed()
                        Instruction.Mask(mask)
                    }
                    setMatch != null -> {
                        val addr = setMatch.groupValues[1].toLong()
                        val value = setMatch.groupValues[2].toLong()
                        Instruction.Set(addr, value)
                    }
                    else -> {
                        throw IllegalStateException("no match")
                    }
                }
            }
            .toList()
    }



    sealed class Instruction {
        class Mask(private val mask: List<Boolean?>) : Instruction() {
            override fun apply(memory: Memory) {
                memory.mask = mask
            }

            override fun apply2(memory: Memory) {
                memory.mask = mask
            }
        }
        class Set(private val addr: Value, private val value: Value) : Instruction() {
            override fun apply(memory: Memory) {
                memory[addr] = value
            }

            override fun apply2(memory: Memory) {
                memory.setQuantum(addr, value)
            }
        }

        abstract fun apply(memory: Memory)
        abstract fun apply2(memory: Memory)
    }
}

private typealias Value = Long
data class Memory(var mask: List<Boolean?>, val mem: MutableMap<Long, Long>) {
    operator fun set(addr: Value, value: Value) {
        mem[addr] = transformValue(value)
    }

    fun setQuantum(addr: Value, value: Value) {
        for (a in transformAddr(addr)) {
            mem[a] = value
        }
    }

    private fun transformValue(value: Value): Value {
        var res = value
        for (i in 0 until 36) {
            val m = mask[i]
            if (m != null) {
                val v = 2.0.pow(i).toLong()
                res = if (m) {
                    res or v
                } else {
                    res and (v.inv())
                }
            }
        }
        return res
    }

    private fun transformAddr(addr: Value): Sequence<Value> {
        return sequence {
            val size = 2.0.pow(mask.count { it == null }).toLong()
            for (i in 0 until size) {
                val resAddr = addr.toBits().toMutableList()
                val qbits = i.toBits()
                var q = 0
                for (j in 0 until 36) {
                    val m = mask[j]
                    if (m == null) {
                        resAddr[j] = qbits[q]
                        q++
                    } else if (m) {
                        resAddr[j] = true
                    }
                }
                yield(resAddr.toValue())
            }
        }
    }

    private fun Value.toBits(): List<Boolean> {
        return List(36) { i -> this and 2.0.pow(i).toLong() > 0 }
    }

    private fun List<Boolean>.toValue(): Value {
        return foldIndexed(0L) { i, v, b -> if (b) v + 2.0.pow(i).toLong() else v }
    }
}
