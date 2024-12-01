import kotlin.math.absoluteValue

fun main() {
    val input = """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """
    check(locationsDistances(input) == 11)
    check(locationsSimilarities(input) == 31)
}

fun locationsSimilarities(input: String): Int {
    val lists = parseLocationsString(input)

    val secondListPresences = lists.second
        .groupingBy { it }
        .eachCount()

    return lists.first.sumOf {
        val multiplier = secondListPresences[it] ?: 0
        it * multiplier
    }
}

fun locationsDistances(input: String): Int = calculateListDistances(parseLocationsString(input))

private fun parseLocationsString(input: String): Pair<MutableList<Int>, MutableList<Int>> {
    return input
        .trimIndent()
        .lines()
        .map {
            val pair = it.split("   ")
            Pair(pair[0].toInt(), pair[1].toInt())
        }
        .fold(
            Pair(mutableListOf(), mutableListOf())
        ) { acc, newPair ->
            acc.first.add(newPair.first)
            acc.second.add(newPair.second)
            acc
        }
}

private fun calculateListDistances(lists: Pair<List<Int>, List<Int>>): Int {
    return lists.first.sorted()
        .zip(lists.second.sorted())
        .map { it.first - it.second }
        .sumOf { it.absoluteValue }
}
