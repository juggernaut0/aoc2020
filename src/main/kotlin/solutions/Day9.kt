package solutions

import components.BasePuzzle

class Day9 : BasePuzzle("Day 9: Encoding Error") {
    override fun solvePart1(input: String): String {
        val nums = input.lineSequence()
            .filter { it.isNotBlank() }
            .map { it.toLong() }
            .toList()

        return findInvalid(nums).toString()
    }

    override fun solvePart2(input: String): String {
        val nums = input.lineSequence()
            .filter { it.isNotBlank() }
            .map { it.toLong() }
            .toList()

        val target = findInvalid(nums)
        val cont = findContiguous(nums, target)

        return (cont.minOrNull()!! + cont.maxOrNull()!!).toString()
    }

    private fun isValid(prev25: List<Long>, e: Long): Boolean {
        console.log(e, prev25.toTypedArray())
        return prev25.flatMap {x -> prev25.map { x to it } }.any { (a, b) ->
            a != b && a + b == e
        }
    }

    private fun findInvalid(nums: List<Long>): Long {
        for (i in 25 until nums.size) {
            if (!isValid(nums.subList(i-25, i), nums[i])) return nums[i]
        }
        return -1
    }

    private fun findContiguous(nums: List<Long>, target: Long): List<Long> {
        for (i in 0 until nums.size) {
            for (j in i+2 until nums.size) {
                val sub = nums.subList(i, j)
                val sum = sub.sum()
                if (sum == target) return sub
                if (sum > target) break
            }
        }
        throw IllegalStateException("no solution")
    }
}