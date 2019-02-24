package com.christian.dnd.d100

import com.christian.dnd.d100.expression.DiceExpressionEvaluator
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import kotlin.random.Random

class DiceExpressionEvaluatorSpec: Spek({
    group("different dice expressions") {
        val testCases = listOf(
            TestCase("d0", "0", Random.Default),
            TestCase("0d0", "0", Random.Default),
            TestCase("d1", "1", Random.Default),
            TestCase("d20", "10", expectedRandomResult(10)),
            TestCase("1d20", "10", expectedRandomResult(10)),
            TestCase("2d4", "4", expectedRandomResult(2)),
            TestCase("4d6", "16", expectedRandomResult(4)),
            TestCase("1000d1000", "1000000", expectedRandomResult(1000)),
            TestCase("d%", "100", expectedRandomResult(100)),
            TestCase("3d%", "99", expectedRandomResult(33))
        )

        testCases.forEach { (expression, expected, random) ->
            test ("$expression should evaluate to $expected") {
                val actual = DiceExpressionEvaluator(random).evaluate(expression)
                actual shouldEqual expected
            }
        }
    }

    group("exhaustive dice expression tests") {
        val evaluator = DiceExpressionEvaluator()

        val testCases = mapOf(
            "1d1" to 1..1,
            "d20" to 1..20,
            "2d4" to 2..8,
            "6d6" to 6..36,
            "10d%" to 10..1000
        )

        testCases.forEach { (expression, expectedRange) ->
            test("$expression is always in range $expectedRange") {
                (1..1000).forEach { _ ->
                    expectedRange shouldContain evaluator.evaluate(expression).toInt()
                }
            }
        }
    }
})

private data class TestCase(val expression: String, val expected: String, val random: Random)

private fun expectedRandomResult(result: Int) = object: Random() {
    override fun nextBits(bitCount: Int) = 0
    override fun nextInt(from: Int, until: Int) = result
}