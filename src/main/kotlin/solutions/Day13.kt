package solutions

import components.BasePuzzle

class Day13 : BasePuzzle("Day 13: Shuttle Search") {
    override fun solvePart1(input: String): String {
        val (time, buses) = parseInput(input)

        val (busId, nextDepart) = buses.filterNotNull()
            .associateWith { busId ->
                val last = time / busId
                busId * (last + 1)
            }
            .minByOrNull { it.value }!!

        return ((nextDepart - time) * busId).toString()
    }

    override fun solvePart2(input: String): String {
        return parseInput(input)
            .second
            .mapIndexedNotNull { index, busId -> busId?.let { it.toLong() to index.toLong() } }
            .fold(1L to 0L) { (p1, o1), (p2, o2) ->
                var offset = o1
                while((offset + o2) % p2 != 0L) {
                    offset += p1
                }
                val period = p1*p2
                console.log("reduce: ($p1, $o1) ($p2, $o2) -> $period, $offset")
                period to offset
            }
            .second
            .toString()
    }

    private fun parseInput(input: String): Pair<Int, List<Int?>> {
        val lines = input.lines()
        val time = lines[0].toInt()
        val buses = lines[1].split(',').map { it.toIntOrNull() }
        return time to buses
    }
}
