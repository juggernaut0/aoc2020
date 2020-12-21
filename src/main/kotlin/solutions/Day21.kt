package solutions

import components.BasePuzzle

class Day21 : BasePuzzle("Day 21: Allergen Assessment") {
    override fun solvePart1(input: String): String {
        val foods = parseInput(input)

        val allMap = solve(foods)

        return foods.sumBy { it.ingredients.count { i -> i !in allMap.values } }.toString()
    }

    override fun solvePart2(input: String): String {
        val foods = parseInput(input)

        val allMap = solve(foods)

        return allMap.entries.sortedBy { it.key }.joinToString(",") { it.value }
    }

    private fun solve(foods: List<Food>): Map<String, String> {
        val allergens = foods.flatMapTo(mutableSetOf()) { it.allergens }
        // allergens to what ingredient they are in
        val allMap: MutableMap<String, String> = mutableMapOf()
        val toDetermine = allergens.toMutableSet()

        // ingredients that possibly contain allergen
        fun collapse(allergen: String): Set<String> {
            return foods.filter { allergen in it.allergens }
                .map { it.ingredients.filterNotTo(mutableSetOf()) { i -> i in allMap.values } as Set<String> }
                .reduce { f1, f2 -> f1.intersect(f2) }
                .toSet()
        }

        while (toDetermine.isNotEmpty()) {
            var toRemove: String? = null
            for (a in toDetermine) {
                if (a in allMap) {
                    toRemove = a
                    break
                }
                val ming = collapse(a).singleOrNull()
                if (ming != null) {
                    allMap[a] = ming
                    toRemove = a
                    break
                }
            }
            if (toRemove != null) {
                toDetermine.remove(toRemove)
            } else {
                error("Could not reduce")
            }
        }

        return allMap
    }

    private val regex = Regex("(.+) \\(contains (.+)\\)")

    private fun parseInput(input: String): List<Food> {
        val res = mutableListOf<Food>()
        for (line in input.lineSequence().filter { it.isNotBlank() }) {
            val match = regex.matchEntire(line) ?: error("Unknown line: $line")
            val (ings, alls) = match.destructured
            res.add(Food(ings.split(' ').toSet(), alls.split(", ").toSet()))
        }
        return res
    }

    class Food(val ingredients: Set<String>, val allergens: Set<String>)
}