package solutions

import components.BasePuzzle

class Day16 : BasePuzzle("Day 16: Ticket Translation") {
    override fun solvePart1(input: String): String {
        val tickets = parseInput(input)

        return tickets.nearbyTickets
            .flatMap { ticket ->
                ticket.fields.filter { v -> tickets.rules.none { v in it } }
            }
            .sum()
            .toString()
    }

    override fun solvePart2(input: String): String {
        val tickets = parseInput(input)

        val possibleRules = List(tickets.yourTicket.fields.size) { tickets.rules.toMutableSet() }

        val valids = tickets.nearbyTickets.filter { it.isValid(tickets.rules) }

        for (ticket in valids) {
            for ((i, vrules) in ticket.fields.zip(possibleRules).withIndex()) {
                val (v, rules) = vrules
                rules.retainAll { rule -> v in rule }
                if (rules.isEmpty()) throw IllegalStateException("field $i has no valid rules")
            }
        }

        val singleRules = MutableList<Rule?>(possibleRules.size) { null }
        while (possibleRules.any { it.size == 1 }) {
            val singles = possibleRules.withIndex().filter { (_, rules) -> rules.size == 1 }
            for ((i, rules) in singles) {
                val rule = rules.single()
                singleRules[i] = rule
                possibleRules.forEach { it.remove(rule) }
            }
        }

        val ruleOrder = singleRules.mapIndexed { i, it -> it ?: throw IllegalStateException("Rule not found for field $i") }

        var res = 1L
        for ((v, rule) in tickets.yourTicket.fields.zip(ruleOrder)) {
            if (rule.name.startsWith("departure")) {
                res *= v
            }
        }
        return res.toString()
    }

    private val ruleRe = Regex("([a-z ]+): (\\d+)-(\\d+) or (\\d+)-(\\d+)")

    private fun parseInput(input: String): Input {
        var mode = 0
        val rules = mutableListOf<Rule>()
        lateinit var yourTicket: Ticket
        val nearbyTickets = mutableListOf<Ticket>()
        for (line in input.lineSequence().filter { it.isNotBlank() }) {
            when {
                line == "your ticket:" -> {
                    mode = 1
                }
                line == "nearby tickets:" -> {
                    mode = 2
                }
                mode == 0 -> {
                    val groups = ruleRe.matchEntire(line)!!.groupValues
                    val rule = Rule(groups[1], groups[2].toInt()..groups[3].toInt(), groups[4].toInt()..groups[5].toInt())
                    rules.add(rule)
                }
                mode == 1 -> {
                    yourTicket = parseTicket(line)
                }
                mode == 2 -> {
                    nearbyTickets.add(parseTicket(line))
                }
            }
        }
        return Input(rules, yourTicket, nearbyTickets)
    }

    private fun parseTicket(line: String): Ticket {
        return Ticket(line.split(',').map { it.toInt() })
    }

    private data class Input(val rules: List<Rule>, val yourTicket: Ticket, val nearbyTickets: List<Ticket>)
    private data class Rule(val name: String, val r1: IntRange, val r2: IntRange) {
        operator fun contains(v: Int): Boolean {
            return v in r1 || v in r2
        }
    }
    private data class Ticket(val fields: List<Int>) {
        fun isValid(rules: List<Rule>): Boolean {
            return fields.all { v -> rules.any { v in it } }
        }
    }
}