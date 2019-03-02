package dev.christianbroomfield.d100.expression

/**
 * Evaluates lines based on its contents.
 * - {@see ArithmeticExpressionEvaluator}
 * - {@see DiceExpressionEvaluator}
 */
interface ExpressionEvaluator {
    /**
     * @param line the line to be evaluated
     * @return the evaluated line in which all relevant expressions have been replaced
     */
    fun evaluate(line: String): String
}