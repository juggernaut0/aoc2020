package solutions

import components.BasePuzzle

class Day4 : BasePuzzle("Day 4: Passport Processing") {
    override fun solvePart1(input: String): String {
        val pps = parseInput(input)

        return pps.count { it.isValid1() }.toString()
    }

    override fun solvePart2(input: String): String {
        val pps = parseInput(input)

        return pps.count { it.isValid2() }.toString()
    }

    data class Passport(val store: Map<String, String>) {
        private fun byrValid(): Boolean {
            return store["byr"]?.toIntOrNull()?.takeIf { it in 1920..2002 } != null
        }

        private fun iyrValid(): Boolean {
            return store["iyr"]?.toIntOrNull()?.takeIf { it in 2010..2020 } != null
        }

        private fun eyrValid(): Boolean {
            return store["eyr"]?.toIntOrNull()?.takeIf { it in 2020..2030 } != null
        }

        private fun hgtValid(): Boolean {
            return store["hgt"]?.takeIf {
                val qt = it.substring(0, it.length - 2).toIntOrNull() ?: return false
                val unit = it.substring(it.length - 2)

                when (unit) {
                    "in" -> qt in 59..76
                    "cm" -> qt in 150..193
                    else -> false
                }
            } != null
        }

        private fun hclValid(): Boolean {
            return store["hcl"]?.takeIf { it.matches("^#[0-9a-f]{6}$") } != null
        }

        private fun eclValid(): Boolean {
            return store["ecl"] in setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
        }

        private fun pidValid(): Boolean {
            return store["pid"]?.takeIf { it.matches("^\\d{9}$") } != null
        }

        fun isValid1(): Boolean {
            return "byr" in store &&
                    "iyr" in store &&
                    "eyr" in store &&
                    "hgt" in store &&
                    "hcl" in store &&
                    "ecl" in store &&
                    "pid" in store
        }

        fun isValid2(): Boolean {
            return byrValid() &&
                    iyrValid() &&
                    eyrValid() &&
                    hgtValid() &&
                    hclValid() &&
                    eclValid() &&
                    pidValid()
        }
    }

    private fun parseInput(input: String): List<Passport> {
        val res = mutableListOf<Passport>()
        var current = mutableMapOf<String, String>()
        for (line in input.lineSequence()) {
            if (line.isBlank()) {
                res.add(Passport(current))
                current = mutableMapOf()
            } else {
                line.split(' ').forEach {
                    val (k, v) = it.split(':')
                    current[k] = v
                }
            }
        }
        if (current.isNotEmpty()) res.add(Passport(current))
        return res
    }
}
