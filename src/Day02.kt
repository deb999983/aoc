data class Game(val index: Int, val sets: List<Map<String, Int>>) {
    fun isValid(): Boolean {
        return sets.map { set ->
            set.getOrDefault("red", 0) <= 12 && set.getOrDefault("green", 0) <= 13 && set.getOrDefault("blue", 0) <= 14
        }.reduce { acc, b -> acc && b }
    }
    companion object {
        fun getGameListFromInput(input: List<String>): List<Game> {
            return input.mapIndexed { index, line -> Game(index = index, sets = readLine(line)) }
        }
        private fun readLine(line: String): List<Map<String, Int>> {
            val lineComponents = line.split(":")[1].trim().split(';').map { set ->
                set.split(",").map { colors ->
                    colors.trim().split(" ")
                }.map {
                    it[1] to it[0].toInt()
                }.toMap()
            }
            return lineComponents
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return Game.getGameListFromInput(input).filter { it.isValid() }.map { it.index + 1 }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
//    check(part2(testInput) == 317)

    val input = readInput("Day02")
    part1(input).println()
//    part2(input).println()
}
