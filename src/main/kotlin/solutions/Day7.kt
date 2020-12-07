package solutions

import components.BasePuzzle

class Day7 : BasePuzzle("Day 7: Handy Haversacks") {
    override fun solvePart1(input: String): String {
        val rules = parseInput(input)

        val map = mutableMapOf<String, MutableList<String>>() // x to bag colors that directly contain x
        for (rule in rules) {
            for (name in rule.contents) {
                map.getOrPut(name.first) { mutableListOf() }.add(rule.name)
            }
        }

        val res = mutableSetOf<String>()
        val stack = mutableListOf("shiny gold")

        while (stack.isNotEmpty()) {
            val bag = stack.removeAt(stack.lastIndex)
            val canContain = map[bag].orEmpty()
            for (c in canContain) {
                if (res.add(c)) {
                    stack.add(c)
                }
            }
        }

        return res.size.toString()
    }

    override fun solvePart2(input: String): String {
        val rules = parseInput(input).associateBy { it.name }

        val memo = mutableMapOf<String, Int>() // bag to total number of bags inside
        fun countRec(bag: String): Int {
            return memo.getOrPut(bag) {
                rules[bag]!!.contents.sumBy { (innerBag, count) -> count * (1 + countRec(innerBag)) }
            }
        }

        return countRec("shiny gold").toString()
    }

    private fun parseInput(input: String): List<Rule> {
        return input.lineSequence().filter { it.isNotBlank() }.map { parseRule(it) }.toList()
    }

    private fun parseRule(line: String): Rule {
        val (nameRaw, contentsRaw) = line.split(" contain ")
        val name = Regex("([a-z ]+) bags?").matchEntire(nameRaw.trim())!!.groupValues[1]
        val contents = if (contentsRaw == "no other bags.") {
            emptyList()
        } else {
            contentsRaw.split(",").map { parseBag(it.trim()) }
        }
        return Rule(name, contents)
    }

    private fun parseBag(raw: String): Pair<String, Int> {
        val match = Regex("(\\d+) ([a-z ]+) bags?\\.?").matchEntire(raw)!!.groupValues
        return match[2] to match[1].toInt()
    }

    data class Rule(val name: String, val contents: List<Pair<String, Int>>)
}