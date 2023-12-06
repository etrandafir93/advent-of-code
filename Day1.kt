data class FoundDigit(val index: Int, val digit: Int) {}

val tokenToDigits = mapOf(
    "one" to 1,
    "1" to 1,
    "two" to 2,
    "2" to 2,
    "three" to 3,
    "3" to 3,
    "four" to 4,
    "4" to 4,
    "five" to 5,
    "5" to 5,
    "six" to 6,
    "6" to 6,
    "seven" to 7,
    "7" to 7,
    "eight" to 8,
    "8" to 8,
    "nine" to 9,
    "9" to 9
)

fun main() {
    assertEquals(allIndexesOf("11_1", "1"), listOf(0, 1, 3))
    assertEquals(allIndexesOf("aaa3456a", "a"), listOf(0, 1, 2, 7))
    assertEquals(allIndexesOf("abcd", "z"), listOf<Int>())

    assertEquals(extractDigits("11_1"), listOf(1, 1, 1))
    assertEquals(extractDigits("aaa3456a"), listOf(3, 4, 5, 6))
    assertEquals(extractDigits("onetwo3"), listOf(1, 2, 3))
    assertEquals(extractDigits("eightwo3"), listOf(8, 2, 3))

    assertEquals(concatExtremityDigits(listOf(8, 2, 3)), 83)
    assertEquals(concatExtremityDigits(listOf(8)), 88)
    assertEquals(concatExtremityDigits(listOf()), null)

    assertEquals(
        totalCalibrationValue( """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
        """), expecetd = 142
    )

    assertEquals(
        totalCalibrationValue( """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
	      """), expecetd = 281
    )
}

fun totalCalibrationValue(multiLineString: String): Int =
    multiLineString
        .split("\n")
        .map { line -> extractDigits(line) }
        .map { digits -> concatExtremityDigits(digits) ?: 0 }
        .sum()

fun concatExtremityDigits(values: List<Int>): Int? {
    if (values.isEmpty()) return null
    return values.first() * 10 + values.last()
}

fun extractDigits(target: String): List<Int> {
    return tokenToDigits
        .flatMap { (token, digit) -> allIndexesOf(target, token).map { FoundDigit(it, digit) } }
        .sortedBy { it.index }
        .map { it.digit }
}

fun allIndexesOf(target: String, token: String): List<Int> {
    var result: List<Int> = ArrayList()
    var index = target.indexOf(token)

    while (index != -1) {
        result += index
        index = target.indexOf(token, index + 1)
    }
    return result
}

var testCount = 0
fun assertEquals(value: Any?, expecetd: Any?) {
    testCount++
    if (value == expecetd) {
        println("#$testCount ✅ [passed]: $value == $expecetd")
    } else {
        println("#$testCount ❌ [failed]: $value != $expecetd")
    }
}
