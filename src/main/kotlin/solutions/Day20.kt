package solutions

import components.BasePuzzle
import components.Grid
import kui.MarkupBuilder
import kui.classes

class Day20 : BasePuzzle("Day 20: Jurassic Jigsaw") {
    private var tiles: Map<Point, Tile>? = null

    override fun solvePart1(input: String): String {
        val tiles = solvePuzzle(input)
        this.tiles = tiles

        val (minx, maxx) = tiles.keys.minMaxOf { it.x }!!
        val (miny, maxy) = tiles.keys.minMaxOf { it.y }!!
        val a = tiles[Point(minx, miny)]!!.id.toLong()
        val b = tiles[Point(minx, maxy)]!!.id
        val c = tiles[Point(maxx, miny)]!!.id
        val d = tiles[Point(maxx, maxy)]!!.id

        return (a * b * c * d).toString()
    }

    override fun MarkupBuilder.visualize() {
        val state = tiles
        if (state != null) {
            pre(classes(Grid.col12)) {
                +buildString {
                    val (minx, maxx) = state.keys.minMaxOf { it.x }!!
                    val (miny, maxy) = state.keys.minMaxOf { it.y }!!
                    appendLine(state[Point(minx, miny)])
                    appendLine(state[Point(minx, maxy)])
                    appendLine(state[Point(maxx, miny)])
                    appendLine(state[Point(maxx, maxy)])
                }
            }
        }
    }

    override fun solvePart2(input: String): String {
        val tiles = solvePuzzle(input)
        val image = Image(tiles)

        val totalHash = run {
            var count = 0
            for (y in 0 until image.height) {
                for (x in 0 until image.width) {
                    if (image[x, y] == '#') {
                        count++
                    }
                }
            }
            count
        }

        val monsterPixels = """
            |                  # 
            |#    ##    ##    ###
            | #  #  #  #  #  #   
        """.trimMargin()
        val seaMonster = Tile(0, monsterPixels.lines(), 20, 3)

        for (smt in seaMonster.orientations()) {
            val monsters = image.findMatches(smt)
            if (monsters.isEmpty()) continue

            return (totalHash - (monsters.size * 15)).toString()
        }
        return "no solution"
    }

    private fun parseInput(input: String): List<Tile> {
        val lines = input.lineSequence().iterator()
        val tiles = mutableListOf<Tile>()
        while (lines.hasNext()) {
            val header = lines.next()
            val id = Regex("Tile (\\d+):").matchEntire(header)!!.groupValues[1].toInt()
            val contents = mutableListOf<String>()
            while (lines.hasNext()) {
                val line = lines.next()
                if (line.isBlank()) break
                contents.add(line)
            }
            tiles.add(Tile(id, contents, 10, 10))
        }
        return tiles
    }

    private fun solvePuzzle(input: String): Map<Point, Tile> {
        val tiles = parseInput(input).associateByTo(mutableMapOf()) { it.id }

        val first = tiles.values.random()
        println("first is ${first.id}")
        first.orientations().forEach { println(it) }
        tiles.remove(first.id)
        // location to tile
        val placed = mutableMapOf(Point(0, 0) to first)
        // edge to location edge is pointing at
        val openEdges = Direction.values().mapTo(mutableListOf()) { first.edge(it) to Point(0, 0).towards(it) }

        while (tiles.isNotEmpty()) {
            if (openEdges.isEmpty()) throw IllegalStateException("Ran out of edges")
            val (edge, p) = openEdges.removeAt(0)
            val tile = tiles.values.asSequence().flatMap { it.orientations() }.find { tile ->
                val otherEdge = tile.edge(edge.direction.opposite())
                otherEdge.matchesReversed(edge)
            }
            if (tile != null) {
                tiles.remove(tile.id)
                placed[p] = tile
                openEdges.removeAll { (_, p) -> p in placed }
                Direction.values().asSequence()
                    .map { tile.edge(it) to p.towards(it) }
                    .filterNot { (_, np) -> np in placed }
                    .forEach { openEdges.add(it) }
            }
        }

        return placed
    }

    private class Tile(val id: Int, private val pixels: List<String>, val width: Int, val height: Int) {
        operator fun get(x: Int, y: Int): Char {
            return pixels[y][x]
        }

        fun flipY(): Tile = Tile(id, pixels.reversed(), width, height)
        fun flipX(): Tile = Tile(id, pixels.map { it.reversed() }, width, height)

        // rotates 90 clockwise
        fun rotate(): Tile {
            val newPixels = mutableListOf<String>()
            for (x in 0 until width) {
                newPixels.add((height-1 downTo 0).map { y -> get(x, y) }.toCharArray().concatToString())
            }
            return Tile(id, newPixels, height, width)
        }

        fun rotations(): Sequence<Tile> {
            return sequence {
                yield(this@Tile)
                val r1 = rotate().also { yield(it) }
                val r2 = r1.rotate().also { yield(it) }
                yield(r2.rotate())
            }
        }

        fun orientations(): Sequence<Tile> {
            // TODO this duplicates a lot of orientations
            return rotations().flatMap { tile ->
                sequence {
                    yield(tile)
                    yield(tile.flipX())
                    yield(tile.flipY())
                }
            }
        }

        fun edge(dir: Direction): Edge {
            val pixels = when (dir) {
                Direction.NORTH -> pixels[0].toCharArray()
                Direction.EAST -> (0 until height).map { y -> get(width-1, y) }.toCharArray()
                Direction.SOUTH -> pixels[height-1].toCharArray().reversedArray()
                Direction.WEST -> (height-1 downTo 0).map { y -> get(0, y) }.toCharArray()
            }
            return Edge(pixels, dir)
        }

        fun stripBorder(): Tile {
            val newPixels = pixels.subList(1, height-1).map { it.substring(1, width-1) }
            return Tile(id, newPixels, width-2, height-2)
        }

        override fun toString(): String {
            return buildString {
                appendLine("Tile $id")
                for (line in pixels) {
                    appendLine(line)
                }
            }
        }
    }

    private class Edge(private val chars: CharArray, val direction: Direction) {
        fun matchesReversed(other: Edge): Boolean {
            return chars.contentEquals(other.chars.reversedArray())
        }

        override fun toString(): String {
            return "'${chars.concatToString()}' facing $direction"
        }
    }

    private enum class Direction {
        NORTH, EAST, SOUTH, WEST;

        fun opposite(): Direction {
            return when (this) {
                NORTH -> SOUTH
                EAST -> WEST
                SOUTH -> NORTH
                WEST -> EAST
            }
        }
    }
    private data class Point(val x: Int, val y: Int) {
        fun towards(direction: Direction): Point {
            return when (direction) {
                Direction.NORTH -> Point(x, y - 1)
                Direction.EAST -> Point(x + 1, y)
                Direction.SOUTH -> Point(x, y + 1)
                Direction.WEST -> Point(x - 1, y)
            }
        }
    }

    private class Image(tiles: Map<Point, Tile>) {
        private val tiles: Map<Point, Tile>
        val width: Int
        val height: Int

        init {
            require(tiles.isNotEmpty())
            val (minx, maxx) = tiles.keys.minMaxOf { it.x }!!
            val (miny, maxy) = tiles.keys.minMaxOf { it.y }!!
            this.tiles = tiles.mapKeys { Point(it.key.x - minx, it.key.y - miny) }.mapValues { it.value.stripBorder() }
            width = (maxx - minx + 1) * 8
            height = (maxy - miny + 1) * 8
        }

        operator fun get(x: Int, y: Int): Char {
            return tiles[Point(x / 8, y / 8)]!![x % 8, y % 8]
        }

        fun getOrNull(x: Int, y: Int): Char? {
            return runCatching { get(x, y) }.getOrNull()
        }

        fun findMatches(tile: Tile): List<Point> {
            val res = mutableListOf<Point>()
            for (y in 0..height-tile.height) {
                for (x in 0..width-tile.width) {
                    if (matches(tile, x, y)) res.add(Point(x, y))
                }
            }
            return res
        }

        private fun matches(tile: Tile, ix: Int, iy: Int): Boolean {
            for (y in 0 until tile.height) {
                for (x in 0 until tile.width) {
                    val tc = tile[x, y]
                    if (tc != ' ' && tc != getOrNull(ix + x, iy + y)) return false
                }
            }
            return true
        }
    }
}