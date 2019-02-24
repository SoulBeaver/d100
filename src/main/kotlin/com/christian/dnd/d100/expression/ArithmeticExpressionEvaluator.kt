package com.christian.dnd.d100.expression

class ArithmeticExpressionEvaluator: ExpressionEvaluator {
    // Examples: 1+1, 10 +2, 3-5, 1*1
    private val arithmeticExpression = """\b(\d+)\s*([+])\s*(\d+)\b""".toRegex(RegexOption.IGNORE_CASE)

    private val operation = mapOf<String, (Int, Int) -> Int>(
        "+" to { a, b -> a + b }
    )

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
                    result.toString())
            }
    }
}