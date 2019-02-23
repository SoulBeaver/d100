package com.christian.dnd.d100

import kotlin.random.Random

class DiceExpressionEvaluator(private val random: Random = Random.Default) {
    // Match any expression of the form (X)dY or the special case d%
    // Examples: d20, D20, 1d20, d%, 1000d1000
    private val diceExpressionRegex = """(\d*)d(\d+|%)""".toRegex(RegexOption.IGNORE_CASE)

    fun evaluate(line: String): String {
        return diceExpressionRegex.findAll(line)
            .fold(line) { replacedLineAcc: String, match ->
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