package dev.christianbroomfield.d100

import dev.christianbroomfield.d100.expression.ArithmeticExpressionEvaluator
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek

class ArithmeticExpressionEvaluatorSpec : Spek({
    val evaluator = ArithmeticExpressionEvaluator()

    group("various arithmetic expressions") {
        val testCases = mapOf(
            "1+1" to "2",
            "1 + 1" to "2",
            "1    + 1" to "2",
            "some stuff 1+1 more stuff" to "some stuff 2 more stuff",
            "[8 + 4] coins" to "[12] coins",
            "1231+ 42123" to "43354",

            "1 +* 1" to "1 +* 1",
            "" to "",
            "goblin1 + 2 more goblins attack the party" to "goblin1 + 2 more goblins attack the party"
        )

        testCases.forEach { expression, expected ->
            test("$expression should evaluate to $expected") {
                evaluator.evaluate(expression) shouldEqual expected
            }
        }
    }
})