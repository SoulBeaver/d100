package dev.christianbroomfield.d100.expression

/**
 * Evaluates simple mathematical expressions inside table results.
 */
class ArithmeticExpressionEvaluator : ExpressionEvaluator {
    // Examples: 1+1, 10 +2, 3-5, 1*1
    private val arithmeticExpression = """\b(\d+)\s*([+])\s*(\d+)\b""".toRegex(RegexOption.IGNORE_CASE)

    private val operation = mapOf<String, (Int, Int) -> Int>(
        "+" to { a, b -> a + b }
    )

    /**
     * Evaluates the result if it has any mathematical expressions.
     *
     * @param line the line to be evaluated
     * @return a copy of the line in which all relevant expressions have been evaluated
     */
    override fun evaluate(line: String): String {
        return arithmeticExpression.findAll(line)
            .fold(line) { replacedLineAcc, match ->
                val expressionToReplace = match.value

                val leftValue = match.groupValues[1].toInt()
                val operand = match.groupValues[2]
                val rightValue = match.groupValues[3].toInt()

                val result = operation[operand]!!(leftValue, rightValue)

                replacedLineAcc.replace(
                    expressionToReplace,
                    result.toString()
                )
            }
    }
}