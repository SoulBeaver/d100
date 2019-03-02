package dev.christianbroomfield.d100.parsers.header

import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek

class EndingTableHeaderParserSpec : Spek({
    val tableHeaderParser = EndingTableHeaderParser()

    group("an assortment of regex expressions to evaluate") {
        val testCases = mapOf(
            "d6" to true,
            "D20" to true,
            "some header d6" to true,
            "some header 2d6" to true,
            "some header (d6)" to true,
            "some header (2d6)" to true,
            "some header (2d6):" to true,
            "some header (2d6):     " to true,
            "some header - 2d6  " to true,

            "" to false,
            "some header" to false,
            "d20 header" to false,
            "2d6 header" to false,
            "(d6) header" to false,
            "(2d6) header" to false,
            "d20: header" to false,
            "D20: header" to false,
            "D%- header" to false,
            "D% - header" to false,
            "10d% header" to false,
            "In an ancient ruin (d6): 1. arena, 2. fortress, 3. mausoleum, 4. palace, 5. prison, 6. temple." to false,
            "some header (d6) with more stuff" to false
        )

        testCases.forEach { expression, expected ->
            test("Expression '$expression' should ${if (expected) "be parsed" else "not be parsed"}") {
                tableHeaderParser.isHeader(expression) shouldEqual expected
            }
        }
    }
})