fun main() {
    val inputStr = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
        """

    check(countValidReports(inputStr) == 4)
}

fun countValidReports(inputStr: String) = parseInputReports(inputStr)
    .count { isValidReport(it) }

fun parseInputReports(inputStr: String) = inputStr
    .trimIndent()
    .lines()
    .map { line -> line.split(" ").map { it.toInt() } }

fun isValidReport(report: List<Int>, canRecover: Boolean = true): Boolean {
    val trendAsc = report[0] < report[1]

    for (index in 1..<report.size) {
        val prev = report[index - 1]
        val curr = report[index]

        val diff = curr - prev
        val invalid = when {
            trendAsc -> diff !in 1..3
            else -> diff !in -3..-1
        }

        if (invalid) {
            return when {
                canRecover -> testAlternativeReports(report)
                else -> false
            }
        }
    }
    return true
}

private fun testAlternativeReports(report: List<Int>): Boolean {
    return report.indices
        .map { index ->
            report.toMutableList().also { it.removeAt(index) }
        }
        .any { alternative ->
            isValidReport(alternative, false)
        }
}
