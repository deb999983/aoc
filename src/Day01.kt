fun main() {
    val wordToNum = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9",
    )

    fun part1(input: List<String>): Int {
        return input.map { it.filter { it.isDigit() } }.map { (it.first().toString() + it.last().toString()).toInt() }.sum()
    }

    fun part2(input: List<String>): Int {
        val intArray = input.map {

            val wordNumToIndexes = listOf(
                wordToNum.keys.map { w -> it.indexOf(w) to wordToNum[w]!! }.filter { it.first != -1 }.minByOrNull { it.first }, // FirstWordIndex
                wordToNum.keys.map { w -> it.lastIndexOfAny(listOf(w)) to wordToNum[w]!! }.filter { it.first != -1 }.maxByOrNull { it.first }, // LastWordIndex
            ).filter { it != null }

            val digitNumToIndexes = listOf(
                it.indexOfFirst { it.isDigit() },
                it.indexOfLast { it.isDigit() },
            ).filter { it != -1 }.map { i -> i to it[i] }

            val indexes = (wordNumToIndexes + digitNumToIndexes).sortedBy { it!!.first }
            "${indexes.first()!!.second}${indexes.last()!!.second}"
        }.map { it.toInt() }
        println(intArray.mapIndexed { i, e -> "${i + 1}: ${input[i]} => $e" }.joinToString("\n"))
        return intArray.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 421)
    check(part2(testInput) == 317)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
