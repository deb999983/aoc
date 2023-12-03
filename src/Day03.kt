import java.math.BigInteger


data class EngineSchema(val data: List<List<Char>>, val W: Int, val H: Int) {
	val parts: List<Part> = data.flatMapIndexed{ i, row -> partsFromRow(i, row) }
	val gears: List<Gear> = gearsFromPart(parts)
	private fun getPartNeighbours(start: Pair<Int, Int>, end: Pair<Int, Int>): List<Pair<Pair<Int, Int>, Char>> {
		val neighboursIndices = listOf(
			Pair(start.first, start.second - 1),
			Pair(start.first - 1, start.second - 1)
		) + (start.second .. end.second).toList().map { Pair(start.first - 1, it) } + listOf(
			Pair(end.first - 1, end.second + 1),
			Pair(end.first, end.second + 1),
			Pair(end.first + 1, end.second + 1),
		) + (start.second .. end.second).toList().map { Pair(start.first + 1, it) } + listOf( Pair(start.first + 1, start.second - 1) )

		return neighboursIndices.filter { (it.first in 0..<H) && (it.second in 0 ..<W) }.map { Pair(it.first, it.second) to data[it.first][it.second] }
	}

	private fun partsFromRow(rowIndex: Int, row: List<Char>): List<Part> {
		val parts = mutableListOf<Part>()
		var currentPart = ""
		for (i in row.indices) {
			if (row[i].isDigit()) {
				currentPart += row[i]
			} else {
				if (currentPart.isEmpty()) continue
				val (start, end) = listOf(Pair(rowIndex, i - currentPart.length), Pair(rowIndex, i - 1))
				parts.add(Part(start = start, num = currentPart.toInt(), end = end, neighbours = getPartNeighbours(start, end)))
				currentPart = ""
			}
		}

		if (currentPart.toIntOrNull() != null) {
			val (start, end) = listOf(Pair(rowIndex, row.size - currentPart.length), Pair(rowIndex, row.size - 1))
			parts.add(Part(start = start, num = currentPart.toInt(), end = end, neighbours = getPartNeighbours(start, end)))
		}

		return parts
	}

	private fun gearsFromPart(parts: List<Part>): List<Gear> {
		val gMap = mutableMapOf<Pair<Int, Int>, MutableList<Part>>()
		for (part in parts) {
			part.neighbours.filter { it.second == '*' }.forEach {
				val p = gMap.getOrDefault(it.first, mutableListOf())
				p.add(part)
				gMap[it.first] = p

			}
		}
		return gMap.entries.map { Gear(it.key, it.value) }
	}

	companion object {
		fun fromInput(input: List<String>): EngineSchema {
			val data = input.map { it.toList() }
			return EngineSchema(data = data, W=data[0].size, H=data.size)
		}
	}
}

data class Part(val start: Pair<Int, Int>, val end: Pair<Int, Int>, val num: Int, val neighbours: List<Pair<Pair<Int, Int>, Char>>) {
	fun isValid(): Boolean {
		return neighbours.filter { it.second != '.' && !it.second.isDigit() }.isNotEmpty()
	}
}

data class Gear(val position: Pair<Int, Int>, val parts: List<Part>) {
	fun isValid(): Boolean {
		return parts.size == 2
	}

	fun ratio(): Int {
		return if (isValid()) parts.map { it.num }.reduce { acc, p -> acc * p } else 0
	}
}

fun main() {
	fun part1(input: List<String>): Int {
		return EngineSchema.fromInput(input).parts.filter { it.isValid() }.sumOf { it.num }
	}

	fun part2(input: List<String>): Int {
		return EngineSchema.fromInput(input).gears.filter { it.isValid() }.sumOf { it.ratio() }
	}

	val testInput = readInput("Day03_test")
	check(part1(testInput) == 4361)
	check(part2(testInput) == 467835)

	val input = readInput("Day03")
	part1(input).println()
	part2(input).println()
}