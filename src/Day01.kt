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
    val numToWord = mapOf(
        "1" to "one",
        "2" to "two",
        "3" to "three",
        "4" to "four",
        "5" to "five",
        "6" to "six",
        "7" to "seven",
        "8" to "eight",
        "9" to "nine",
    )

    fun part1(input: List<String>): Int {
        return input.map { it.filter { it.isDigit() } }.map { (it.first().toString() + it.last().toString()).toInt() }.sum()
    }

    fun part2(input: List<String>): Int {
        val intArray = input.map {
            val wordNumFirst = wordToNum.keys.map { w -> it.indexOf(w) to wordToNum[w]!! }.filter { it.first != -1 }.sortedBy { it.first }.firstOrNull()
            val wordNumLast = wordNumFirst?.let { wf ->
                wordToNum.keys.map { w ->
                    val offset = wf.first + numToWord[wf.second]!!.length
                    val li = it.substring(offset).indexOf(w)
                    if(li != -1) (li + offset) to wordToNum[w] else li to wordToNum[w]
                }.filter {
                    it.first != -1
                }.sortedBy { it.first }.lastOrNull()
            }

            val indexMap = (
                listOf(wordNumFirst, wordNumLast).filterNotNull() +
                    listOf(it.indexOfFirst { it.isDigit() }, it.indexOfLast { it.isDigit() }).filter { it != -1 }.map { i -> i to it[i].toString() }
                ).toMap()

            val indexes = indexMap.keys.sorted()
            indexMap[indexes.first()]!! + indexMap[indexes.last()]!!
        }.map { it.toInt() }
        println(intArray.mapIndexed { i, e -> "${i + 1}: ${input[i]} => $e" }.joinToString("\n"))
        return intArray.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 421)
    check(part2(testInput) == 342)

    val input = readInput("Day01")
//    part1(input).println()
//    part2(input).println()
}

/*

two1nine
eightwothree
abcone2threexyz
xtwone3four
nineightseven2
zoneight234
7pqrstsixteen
 */