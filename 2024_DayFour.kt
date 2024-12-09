fun evaluateCmd(input: String): Int {
    return slitIntoBlocksOfInstructions(input)
        .filter { it.startsWith("do()") }
        .sumOf { executeMultiplications(it) }
}

private fun slitIntoBlocksOfInstructions(input: String): List<String> {
    val regex = """(?=do\(\))|(?=don't\(\))""".toRegex()
    return "do()__${input}__don't()".split(regex)
}

private fun executeMultiplications(input: String): Int {
    val regex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
    return regex.findAll(input).map {
        val first = it.groupValues[1].toInt()
        val second = it.groupValues[2].toInt()
        first * second
    }.sum()
}


fun main() {
    check(executeMultiplications("asdasd_mul(22,333)_asdasd") == 22 * 333)

    val in1 = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
    check(executeMultiplications(in1) == (2 * 4) + (5 * 5) + (11 * 8) + (8 * 5))
    check(evaluateCmd(in1) == (2 * 4) + (5 * 5) + (11 * 8) + (8 * 5))

    val in2 = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"
    check(evaluateCmd(in2) == (2 * 4) + (8 * 5))
}
