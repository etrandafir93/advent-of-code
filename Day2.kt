data class Round(val red: Int, val green: Int, val blue: Int)

data class Game(val id: Int, val rounds: List<Round>) {
    fun possibleFor(predicate: (Round) -> Boolean) :Boolean {
        return rounds.all(predicate)
    }
    fun power() : Int {
        return maxnReds() * maxGreens() * maxBlues()
    }
    private fun maxnReds() : Int {
        return rounds.maxOfOrNull { it.red } ?: 0
    }
    private fun maxGreens() : Int {
        return rounds.maxOfOrNull { it.green } ?: 0
    }
    private fun maxBlues() : Int {
        return rounds.maxOfOrNull { it.blue } ?: 0
    }
}

fun main() {
    val input = """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent()

    assertEquals(
        actual = sumIds(input) { it.red <= 12 && it.green <= 13 && it.blue <= 14 },
        expected = 8,
        testName = "part 1"
    )

    assertEquals(
        actual = sumPower(input),
        expected = 2286,
        testName = "part 2"
    )
}


fun sumIds(multiLineString : String, predicate: (Round) -> Boolean) : Int {
    return multiLineString.split("\n")
        .map { parseGame(it) }
        .filter { game -> game.possibleFor(predicate) }
        .sumOf { game -> game.id }
}

fun sumPower(multiLineString : String) : Int {
    return multiLineString.split("\n")
        .map { parseGame(it) }
        .sumOf { game -> game.power() }
}

fun parseGame(str: String): Game {
    val idAndGame = str.split(":")
    val id = idAndGame[0].split("Game ")[1].toInt();
    val rounds = idAndGame[1]
        .split(";")
        .map { parseRound(it) }
    return Game(id, rounds)
}

fun parseRound(string: String): Round {
    return string.split(Regex(","))
        .asSequence()
        .map { it.trim() }
        .fold(Round(0, 0, 0)) { acc, str ->
            val (count, color) = str.split(Regex(" "))
            when (color) {
                "blue" -> acc.copy(blue = acc.blue + count.toInt())
                "red" -> acc.copy(red = acc.red + count.toInt())
                "green" -> acc.copy(green = acc.green + count.toInt())
                else -> acc
            }
        }
}



var testCount = 0
fun assertEquals(actual: Any?, expected: Any?, testName: String = "#${testCount + 1}") {
    if (actual == expected) {
        println("$testName ✅ [passed]: $actual == $expected")
    } else {
        println("$testName ❌ [failed]: $actual != $expected")
    }
    testCount++
}
