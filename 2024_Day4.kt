import java.nio.file.Files
import java.nio.file.Path


fun countOccurrencesOfXmas(inputStr: String): Int {
    val text = parseText(inputStr)
    return text.entries
        .filter { it.value == 'X' }
        .flatMap { surroundingFourLetterWords(it.key, text) }
        .count { it == "XMAS" }
}

private fun parseText(input: String) = input.lines()
    .flatMapIndexed { row, line ->
        line.toCharArray()
            .mapIndexed { col, char -> Pair(Coords(row, col), char) }
    }
    .fold(HashMap<Coords, Char>()) { result, node ->
        result[node.first] = node.second
        result
    }


fun surroundingFourLetterWords(start: Coords, text: HashMap<Coords, Char>): List<String> {
    return listOf(
        fourLetterWord(text, start, Direction.N),
        fourLetterWord(text, start, Direction.S),
        fourLetterWord(text, start, Direction.E),
        fourLetterWord(text, start, Direction.W),
        fourLetterWord(text, start, Direction.NE),
        fourLetterWord(text, start, Direction.NW),
        fourLetterWord(text, start, Direction.SE),
        fourLetterWord(text, start, Direction.SW),
    )
}

private fun fourLetterWord(text: HashMap<Coords, Char>, start: Coords, direction: Direction): String {
    return listOf(
        start,
        start.go(direction),
        start.go(direction, 2),
        start.go(direction, 3)
    )
        .map { text[it]?.run { toString() } ?: "_" }
        .reduce { word, char -> word + char }
}

enum class Direction {
    N, E, S, W, NE, NW, SE, SW
}

data class Coords(val row: Int, val col: Int) {
    fun go(direction: Direction, times: Int = 1): Coords {
        val newRow = row + when (direction) {
            Direction.S, Direction.SE, Direction.SW -> times
            Direction.N, Direction.NE, Direction.NW -> -1 * times
            else -> 0
        }
        val newCol = col + when (direction) {
            Direction.E, Direction.NE, Direction.SE -> times
            Direction.W, Direction.NW, Direction.SW -> -1 * times
            else -> 0
        }
        return Coords(newRow, newCol)
    }
}

// test
fun main() {
    val inputStr = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """
    check(countOccurrencesOfXmas(inputStr) == 18)

    val str = Files.readString(Path.of("src/day4.txt"))
    println(countOccurrencesOfXmas(str))
}
