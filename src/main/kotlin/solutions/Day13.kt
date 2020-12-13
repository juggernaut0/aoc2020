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
        val (_, buses) = parseInput(input)

        val z = buses.mapIndexedNotNull { index, busId -> busId?.let { it to index } }

        val (a, _) = z.first()
        val (start, _) = z.asSequence().drop(1).map { (b, i) ->
            var n: Long = 0
            while(true) {
                if ((n*a + i) % b == 0L) {
                    break
                }
                n += 1
            }
            val start = n*a
            val period = a.toLong()*b
            console.log(start.toString(), period.toString())
            start to period
        }.reduce { (start1, period1), (start2, period2) ->
            var n = 0
            while (true) {
                if ((start1 + n*period1) % period2 == start2) break
                n += 1
            }
            val start = start1 + n*period1
            val period = (period1 * period2)/a
            console.log("reduce: ", n, start.toString(), period.toString())
            start to period
        }

        return start.toString()
    }

    private fun parseInput(input: String): Pair<Int, List<Int?>> {
        val lines = input.lines()
        val time = lines[0].toInt()
        val buses = lines[1].split(',').map { it.toIntOrNull() }
        return time to buses
    }
}
