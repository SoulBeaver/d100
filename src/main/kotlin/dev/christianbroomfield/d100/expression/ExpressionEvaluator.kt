package dev.christianbroomfield.d100.expression

interface ExpressionEvaluator {
    fun evaluate(line: String): String
}