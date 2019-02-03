package com.christian.dnd.d100

import com.christian.dnd.d100.parsers.block.FileIsTableBlockParser
import com.christian.dnd.d100.parsers.content.RangeTableContentParser
import com.christian.dnd.d100.parsers.content.SimpleTableContentParser
import com.christian.dnd.d100.parsers.block.StructuredTableBlockParser
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import java.io.File


class D100TableParserSpec: Spek({
    val simpleTableContentParser = SimpleTableContentParser()
    val rangeTableContentParser = RangeTableContentParser()

    val structuredTableBlockParser = StructuredTableBlockParser(
        simpleTableContentParser,
        rangeTableContentParser
    )
    val fileIsTableBlockParser =
        FileIsTableBlockParser(simpleTableContentParser, rangeTableContentParser)

    val parser = D100TableParser(structuredTableBlockParser, fileIsTableBlockParser)

    group("parsing slime") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/slime").toURI())
        val tables = parser.parse(file)

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
        val tables = parser.parse(file)

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

    group("parsing dragonLair") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/dragonLair").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            val lairLocationTable = tables[0]

            lairLocationTable.apply {
                descriptor shouldEqual "Location: The lair is located"
                dieSize shouldEqual 6
                rollsRequired shouldEqual 1

                results shouldContain "Beneath a mountain."
                results shouldContain "In a dense forest or jungle."
            }

            val hazardsTable = tables[8]

            hazardsTable.apply {
                descriptor shouldEqual "Hazard: If you survive the dragon and the"
                dieSize shouldEqual 6
                rollsRequired shouldEqual 1

                results shouldContain "Fiery, volcanic eruptions."
                results shouldContain "Poisonous mold spores."
            }

            val hoardTable = tables[9]

            hoardTable.apply {
                descriptor shouldEqual "Hoard: you might escape with a/an"
                dieSize shouldEqual 6
                rollsRequired shouldEqual 1

                results shouldContain "Sack full of gems."
                results shouldContain "Legendary sword or hammer."
            }
        }
    }

    group("parsing town") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/town").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            val townSquareTable = tables[0]

            townSquareTable.apply {
                descriptor shouldEqual "Town Square"
                dieSize shouldEqual 4
                rollsRequired shouldEqual 1

                results shouldContain "Open Market/Bazaar"
                results shouldContain "Bonfire"
            }

            val shopsTable = tables[3]

            shopsTable.apply {
                descriptor shouldEqual "Shops"
                dieSize shouldEqual 10
                rollsRequired shouldEqual 2

                results shouldContain "Blacksmith (Armor, Weapons, Tools)"
                results shouldContain "Leatherworks (Armor, Saddlery)"
            }
        }
    }

    group("parsing specialWeather") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/specialWeather").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            val weatherTable = tables[0]

            weatherTable.apply {
                descriptor shouldEqual "specialWeather"
                dieSize shouldEqual 85
                rollsRequired shouldEqual 1

                results shouldContain "Heat Metal. All metal within the vicinity of the storm is affected by the Heat Metal spell for 1d4 hours."
                results shouldContain "Plant Growth. Plants in the storm's area grow to gargantuan size for one year. A creature moving through an effected area must spend 4 feet of movement for every 1 foot it wishes to move."
            }
        }
    }

    group("parsing trinkets") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/trinkets").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            val trinketsTable = tables[0]

            trinketsTable.apply {
                descriptor shouldEqual "trinkets"
                dieSize shouldEqual 100
                rollsRequired shouldEqual 1

                results shouldContain "A well made hourglass with no sand inside."
                results shouldContain "A curious looking leaf that curls up when touched"
            }
        }
    }
})