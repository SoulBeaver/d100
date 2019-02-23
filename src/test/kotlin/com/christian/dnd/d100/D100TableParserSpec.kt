package com.christian.dnd.d100

import com.christian.dnd.d100.model.TableHeader
import com.christian.dnd.d100.parsers.block.FileIsTableBlockParser
import com.christian.dnd.d100.parsers.content.RangeTableContentParser
import com.christian.dnd.d100.parsers.content.SimpleTableContentParser
import com.christian.dnd.d100.parsers.block.StructuredTableBlockParser
import com.christian.dnd.d100.parsers.header.BeginningTableHeaderParser
import com.christian.dnd.d100.parsers.header.EndingTableHeaderParser
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import java.io.File
import kotlin.random.Random


class D100TableParserSpec: Spek({
    val diceExpressionEvaluator = DiceExpressionEvaluator(Random(0))
    val tableHeaderParsers = listOf(
        BeginningTableHeaderParser(),
        EndingTableHeaderParser()
    )

    val simpleTableContentParser = SimpleTableContentParser(diceExpressionEvaluator)
    val rangeTableContentParser = RangeTableContentParser(diceExpressionEvaluator)

    val structuredTableBlockParser = StructuredTableBlockParser(
        simpleTableContentParser,
        rangeTableContentParser,
        tableHeaderParsers
    )
    val fileIsTableBlockParser = FileIsTableBlockParser(
        simpleTableContentParser,
        rangeTableContentParser,
        tableHeaderParsers
    )

    val parser = D100TableParser(listOf(structuredTableBlockParser, fileIsTableBlockParser))

    group("parsing slime") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/slime").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            val slimeColorTable = tables[0]
            slimeColorTable.apply {
                header.validate("This slime’s colour:", 20, 1)

                results shouldContain "Is red. Its touch is burning hot."
                results shouldContain "Is grey. It attacks by exploding and then reforming itself 2 rounds later."
            }

            val slimeTextureTable = tables[1]
            slimeTextureTable.apply {
                header.validate("This slime’s texture:", 20, 1)

                results shouldContain "Is oily. It’s slippery and moves faster than other slimes."
                results shouldContain "Is ichorous. The slime regenerates damage over time."
            }

            val slimeUseTable = tables[4]
            slimeUseTable.apply {
                header.validate("Extracts from this slime can be used to:", 20, 1)

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
                header.validate("Roll --- Armor Type", 100, 1)

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
                header.validate("Location: The lair is located", 6, 1)

                results shouldContain "Beneath a mountain."
                results shouldContain "In a dense forest or jungle."
            }

            val hazardsTable = tables[8]
            hazardsTable.apply {
                header.validate("Hazard: If you survive the dragon and the", 6, 1)

                results shouldContain "Fiery, volcanic eruptions."
                results shouldContain "Poisonous mold spores."
            }

            val hoardTable = tables[9]
            hoardTable.apply {
                header.validate("Hoard: you might escape with a/an", 6, 1)

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
                header.validate("Town Square", 4, 1)

                results shouldContain "Open Market/Bazaar"
                results shouldContain "Bonfire"
            }

            val shopsTable = tables[3]
            shopsTable.apply {
                header.validate("Shops", 10, 2)

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
                header.validate("specialWeather", 85, 1)

                results shouldContain "Heat Metal. All metal within the vicinity of the storm is affected by the Heat Metal spell for 3 hours."
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
                header.validate("trinkets", 100, 1)

                results shouldContain "A well made hourglass with no sand inside."
                results shouldContain "A curious looking leaf that curls up when touched"
            }
        }
    }

    group("parsing pixies") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/pixies").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            val wingsTable = tables[0]
            wingsTable.apply {
                header.validate("This pixie has wings like", 6, 1)

                results shouldContain "a wren."
                results shouldContain "a moth."
            }

            val hairTable = tables[1]
            hairTable.apply {
                header.validate("This pixie has hair like", 6, 1)

                results shouldContain "a lamb."
                results shouldContain "a tarantula."
            }

            val aidTable = tables[5]
            aidTable.apply {
                header.validate("This pixie can call for aid on", 6, 1)

                results shouldContain "its superiors among the fey."
                results shouldContain "swarms of fellow pixies."
            }
        }
    }

    group("parsing ale") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/ale").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            val typeTable = tables[0]
            typeTable.apply {
                header.validate("The Drink is:", 20, 1)

                results shouldContain "Hoppy, pale ale."
                results shouldContain "A thick black liqueur brewed with herbs from the local area."
            }

            val tasteTable = tables[2]
            tasteTable.apply {
                header.validate("The drink tastes", 8, 1)

                results shouldContain "Bitter."
                results shouldContain "Minty."
            }

            val servedTable = tables[6]
            servedTable.apply {
                header.validate("The drink is served with", 6, 1)

                results shouldContain "Small bowls of nuts."
                results shouldContain "1 Copper Piece, in some strange local tradition."
            }
        }
    }
})

fun TableHeader.validate(description: String, dieSize: Int, rollsRequired: Int) {
    TableHeader(rollsRequired, dieSize, description) shouldEqual this
}