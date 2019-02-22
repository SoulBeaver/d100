package com.christian.dnd.d100.parsers.header

import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek

class BeginningTableHeaderParserSpec: Spek({
    val tableHeaderParser = BeginningTableHeaderParser()

    group("an assortment of regex expressions to evaluate") {
        val testCases = mapOf(
            "d6" to true,
            "D20" to true,
            "d20 header" to true,
            "2d6 header" to true,
            "(d6) header" to true,
            "(2d6) header" to true,
            "d20: header" to true,
            "D20: header" to true,
            "D%- header" to true,
            "D% - header" to true,
            "10d% header" to true,

            "" to false,
            "some header" to false,
            "some header d6" to false,
            "some header (d6)" to false
        )

        testCases.forEach { expression, expected ->
            test("Expression '$expression' should ${if (expected) "be parsed" else "not be parsed"}") {
                tableHeaderParser.isHeader(expression) shouldEqual expected
            }
        }
    }

    group("cleaning regex descriptors") {
        val testCases = mapOf(
            "(d6) header" to "header",
            "(2d6) header" to "header",
            "d20: header" to "header",
            "2D20: header" to "header",
            "2D%- header" to "header",
            "2D% - header" to "header",
            "  10d% header  " to "header")

        testCases.forEach { expression, expectedDescriptor ->
            test("the expression '$expression' should have the descriptor '$expectedDescriptor'") {
                tableHeaderParser.parse(expression).descriptor shouldEqual expectedDescriptor
            }
        }
    }
})