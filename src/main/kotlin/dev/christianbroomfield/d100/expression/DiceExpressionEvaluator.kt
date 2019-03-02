package dev.christianbroomfield.d100.expression

import kotlin.random.Random

class DiceExpressionEvaluator(private val random: Random = Random.Default) : ExpressionEvaluator {
    // Match any expression of the form (X)dY or the special case d%
    // Examples: d20, D20, 1d20, d%, 1000d1000
    private val diceExpression = """(\d*)d(\d+|%)""".toRegex(RegexOption.IGNORE_CASE)

    override fun evaluate(line: String): String {
        return diceExpression.findAll(line)
            .fold(line) { replacedLineAcc, match ->
                val expressionToReplace = match.value

                val rollsRequired = if (match.groupValues[1].isNotBlank()) match.groupValues[1].toInt() else 1
                val dieSize = if (match.groupValues[2] == "%") 100 else match.groupValues[2].toInt()

                val dieResult = when {
                    dieSize <= 0 -> 0
                    else -> (0 until rollsRequired).sumBy { random.nextInt(1, dieSize + 1) }
                }

                replacedLineAcc.replaceFirst(expressionToReplace, dieResult.toString())
            }
    }
}