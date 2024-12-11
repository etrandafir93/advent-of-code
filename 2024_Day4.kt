import java.nio.file.Files
import java.nio.file.Path

// lvl1
fun countOccurrencesOfXmas(inputStr: String): Int {
    val text = parseText(inputStr)
    return text.entries
        .filter { it.value == 'X' }
        .flatMap { surroundingFourLetterWords(it.key, text) }
        .count { word -> word == "XMAS" }
}

// lvl2
fun countDiagonalOccurrencesOfMas(inputStr: String): Int {
    val text = parseText(inputStr)
    return text.entries
        .filter { it.value == 'A' }
        .map { wordsDiagonally(it.key, text) }
        .count { diagonals ->
            diagonals.all { word -> word == "MAS" || word == "MAS".reversed() }
        }
}

// lvl2 impl
fun wordsDiagonally(middle: Coords, text: HashMap<Coords, Char>): List<String> {
    val swneDiag = listOf(
        middle.go(Direction.SW),
        middle,
        middle.go(Direction.NE)
    )
        .map { text[it]?.run { toString() } ?: "_" }
        .reduce { word, char -> word + char }

    val nwseDiag = listOf(
        middle.go(Direction.NW),
        middle,
        middle.go(Direction.SE)
    )
        .map { text[it]?.run { toString() } ?: "_" }
        .reduce { word, char -> word + char }

    return listOf(swneDiag, nwseDiag)
}

// lvl1 impl
private fun surroundingFourLetterWords(start: Coords, text: HashMap<Coords, Char>): List<String> {
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

//common
private fun parseText(input: String) = input.lines()
    .flatMapIndexed { row, line ->
        line.toCharArray()
            .mapIndexed { col, char -> Pair(Coords(row, col), char) }
    }
    .fold(HashMap<Coords, Char>()) { result, node ->
        result[node.first] = node.second
        result
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
    val lvl1test = """
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
    check(countOccurrencesOfXmas(lvl1test) == 18)

    val lvl2test = """
        .M.S......
        ..A..MSMS.
        .M.S.MAA..
        ..A.ASMSM.
        .M.S.M....
        ..........
        S.S.S.S.S.
        .A.A.A.A..
        M.M.M.M.M.
        ..........
    """
    check(countDiagonalOccurrencesOfMas(lvl2test) == 9)

    val inputStr = Files.readString(Path.of("src/day4.txt"))
    println(countOccurrencesOfXmas(inputStr)) // 2718
    println(countDiagonalOccurrencesOfMas(inputStr)) // 2046
}
