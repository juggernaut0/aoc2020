package solutions

import components.BasePuzzle

class Day19 : BasePuzzle("Day 19: Monster Messages") {
    override fun solvePart1(input: String): String {
        val (rules, strings) = parseInput(input)

        for ((k, v) in rules) {
            println("$k: $v")
        }

        val rule0 = rules[0]!!
        return strings.count { s -> rule0.validate(StrIter(s), rules).any { it.isEof() } }.toString()
    }

    override fun solvePart2(input: String): String {
        val parsed = parseInput(input)
        val rules = parsed.first.toMutableMap()
        val strings = parsed.second

        rules[8] = AltRule(CatRule(listOf(42, 8)), CatRule(listOf(42)))
        rules[11] = AltRule(CatRule(listOf(42, 11, 31)), CatRule(listOf(42, 31)))

        val rule0 = rules[0]!!
        return strings.count { s -> rule0.validate(StrIter(s), rules).any { it.isEof() } }.toString()
    }

    private fun parseInput(input: String): Pair<Map<Int, Rule>, List<String>> {
        val rules = mutableMapOf<Int, Rule>()
        val strings = mutableListOf<String>()
        var readingRules = true
        for (line in input.lineSequence()) {
            when {
                line.isBlank() -> {
                    readingRules = false
                }
                readingRules -> {
                    val termMatch = Regex("(\\d+): \"(.)\"").matchEntire(line)
                    val catMatch = Regex("(\\d+):((?: \\d+)+)").matchEntire(line)
                    val altMatch = Regex("(\\d+):((?: \\d+)+) \\|((?: \\d+)+)").matchEntire(line)
                    when {
                        termMatch != null -> {
                            val (num, cs) = termMatch.destructured
                            rules[num.toInt()] = TerminalRule(cs[0])
                        }
                        catMatch != null -> {
                            val (num, parts) = catMatch.destructured
                            rules[num.toInt()] = CatRule(parts.trim().split(' ').map { it.toInt() })
                        }
                        altMatch != null -> {
                            val (num, a, b) = altMatch.destructured
                            val ai = a.trim().split(' ').map { it.toInt() }
                            val bi = b.trim().split(' ').map { it.toInt() }
                            rules[num.toInt()] = AltRule(CatRule(ai), CatRule(bi))
                        }
                        else -> error("Unknown rule: '$line'")
                    }
                }
                else -> {
                    strings.add(line)
                }
            }
        }
        return rules to strings
    }

    private interface Rule {
        fun validate(s: StrIter, rules: Map<Int, Rule>): Sequence<StrIter>
    }
    private class TerminalRule(val c: Char) : Rule {
        override fun validate(s: StrIter, rules: Map<Int, Rule>): Sequence<StrIter> {
            return if(s.peek() == c) sequenceOf(s.advance()) else emptySequence()
        }

        override fun toString(): String {
            return "\"$c\""
        }
    }
    private class CatRule(val parts: List<Int>) : Rule {
        override fun validate(s: StrIter, rules: Map<Int, Rule>): Sequence<StrIter> {
            var its = sequenceOf(s)
            for (part in parts) {
                its = its.flatMap { rules[part]!!.validate(it, rules) }
            }
            return its
        }

        override fun toString(): String {
            return parts.joinToString(separator = " ")
        }
    }
    private class AltRule(val first: CatRule, val second: CatRule) : Rule {
        override fun validate(s: StrIter, rules: Map<Int, Rule>): Sequence<StrIter> {
            return sequence {
                yieldAll(first.validate(s, rules))
                yieldAll(second.validate(s, rules))
            }
        }

        override fun toString(): String {
            return "$first | $second"
        }
    }

    private class StrIter(private val str: String, private val i: Int = 0) {
        fun peek(): Char? {
            return str.getOrNull(i)
        }

        fun advance(): StrIter {
            return StrIter(str, i+1)
        }

        fun isEof(): Boolean {
            return i == str.length
        }
    }
}