import kotlin.math.pow

data class Card(val id: Int, val winningList: List<Int>, val ownList: List<Int>) {
	fun matchCount(): Int {
		val wMap = winningList.map { it to true }.toMap()
		return ownList.filter { (wMap.get(it) != null)  }.count()
	}

	fun worth(): Int {
		return 2.0.pow((matchCount() - 1).toDouble()).toInt()
	}

	companion object {
		var cards: List<Card> = mutableListOf()
		fun cardsFromInput(input: List<String>): List<Card> {
			cards = input.mapIndexed { i, s ->
				val (_, numbers) = s.split(":")
				val (winningList, ownList) = numbers.split("|").map {
					it.trim().split(" ").filter { it != "" }.map { it.trim().toInt() }
				}
				Card(i + 1, winningList, ownList)
			}
			return cards
		}

		fun getCardById(id: Int): Card {
			return cards.find { it.id == id }!!
		}
		fun getCopies(id: Int): List<Card> {
			val matchingCount = getCardById(id).matchCount()
			if (matchingCount == 0) { return listOf() }

			val maxId = minOf(id + matchingCount, cards.size)
			return (id + 1 .. maxId).toList().map { getCardById(it) }
		}

		fun processCards(cards: List<Card>): Int {
			if (cards.isEmpty()) {
				return 0
			}

			var c = cards.size
			cards.forEach {
				val copies = getCopies(it.id)
				c += processCards(copies)
			}
			return c
		}
	}
}

fun main() {
	fun part1(input: List<String>): Int {
		return Card.cardsFromInput(input).map { it.worth() }.sum()
	}

	fun part2(input: List<String>): Int {
		val cards = Card.cardsFromInput(input)
		return Card.processCards(cards)
	}

	val testInput = readInput("Day04_test")
	check(part1(testInput) == 13)
	check(part2(testInput) == 30)

	val input = readInput("Day04")
	part1(input).println()
	part2(input).println()
}