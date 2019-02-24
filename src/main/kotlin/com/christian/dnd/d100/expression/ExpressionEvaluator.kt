package com.christian.dnd.d100.expression

interface ExpressionEvaluator {
    fun evaluate(line: String): String
}