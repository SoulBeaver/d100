package com.christian.dnd.d100

import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import java.io.File


class D100TableParserSpec: Spek({
    group("parsing slime") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/slime").toURI())
        val tables = D100TableParser.parse(file)

        test("has the correct attributes set") {
            val firstTable = tables[0]

            firstTable.apply {
                descriptor shouldEqual "This slime’s colour:"
                dieSize shouldEqual 20
                rollsRequired shouldEqual 1

                results shouldContain "Is red. Its touch is burning hot."
                results shouldContain "Is grey. It attacks by exploding and then reforming itself 1d4 rounds later."
            }

            val secondTable = tables[1]

            secondTable.apply {
                descriptor shouldEqual "This slime’s texture:"
                dieSize shouldEqual 20
                rollsRequired shouldEqual 1

                results shouldContain "Is oily. It’s slippery and moves faster than other slimes."
                results shouldContain "Is ichorous. The slime regenerates damage over time."
            }

            val lastTable = tables[4]

            lastTable.apply {
                descriptor shouldEqual "Extracts from this slime can be used to:"
                dieSize shouldEqual 20
                rollsRequired shouldEqual 1

                results shouldContain "Disguise one’s scent."
                results shouldContain "Treat leather and textiles into superlative armours."
            }
        }
    }

    group("parsing armor") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/armor").toURI())
        val tables = D100TableParser.parse(file)

        test("has the correct attributes set") {
            val armorTable = tables[0]

            armorTable.apply {
                descriptor shouldEqual "Roll --- Armor Type"
                dieSize shouldEqual 100
                rollsRequired shouldEqual 1

                results shouldContain "Half plate"
                results shouldContain "Scale mail"
            }
        }
    }
})