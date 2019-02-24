package com.christian.dnd.d100

import com.christian.dnd.d100.cleaner.TableCleaner
import com.christian.dnd.d100.model.TableHeader
import com.christian.dnd.d100.parsers.block.FileIsTableBlockParser
import com.christian.dnd.d100.parsers.content.RangeTableContentParser
import com.christian.dnd.d100.parsers.content.SimpleTableContentParser
import com.christian.dnd.d100.parsers.block.MultiTableBlockParser
import com.christian.dnd.d100.parsers.block.WideTableBlockParser
import com.christian.dnd.d100.parsers.header.BeginningTableHeaderParser
import com.christian.dnd.d100.parsers.header.EndingTableHeaderParser
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotContain
import org.spekframework.spek2.Spek
import java.io.File


class D100TableParserSpec: Spek({
    val diceExpressionEvaluator = mockk<DiceExpressionEvaluator>()
    // Return the original string argument without evaluating any dice expressions.
    every {
        diceExpressionEvaluator.evaluate(any())
    } answers { root -> root.invocation.args[0].toString() }

    val tableCleaner = TableCleaner()

    val tableHeaderParsers = listOf(
        BeginningTableHeaderParser(),
        EndingTableHeaderParser()
    )

    val simpleTableContentParser = SimpleTableContentParser()
    val rangeTableContentParser = RangeTableContentParser()

    val wideTableBlockParser = WideTableBlockParser()
    val structuredTableBlockParser = MultiTableBlockParser(
        simpleTableContentParser,
        rangeTableContentParser,
        tableHeaderParsers
    )
    val fileIsTableBlockParser = FileIsTableBlockParser(
        simpleTableContentParser,
        rangeTableContentParser,
        tableHeaderParsers
    )

    val parser = D100TableParser(
        listOf(
            wideTableBlockParser,
            structuredTableBlockParser,
            fileIsTableBlockParser
        ),
        diceExpressionEvaluator,
        tableCleaner)

    group("parsing slime") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/slime").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            val slimeColorTable = tables[0]
            slimeColorTable.apply {
                header.validate("This slime’s colour", 20, 1)

                results shouldContain "Is red. Its touch is burning hot."
                results shouldContain "Is grey. It attacks by exploding and then reforming itself 1d4 rounds later."
            }

            val slimeTextureTable = tables[1]
            slimeTextureTable.apply {
                header.validate("This slime’s texture", 20, 1)

                results shouldContain "Is oily. It’s slippery and moves faster than other slimes."
                results shouldContain "Is ichorous. The slime regenerates damage over time."
            }

            val slimeUseTable = tables[4]
            slimeUseTable.apply {
                header.validate("Extracts from this slime can be used to", 20, 1)

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
                header.validate("The Drink is", 20, 1)

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

    group("parsing raiders") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/raiders").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            val extortTable = tables[0]
            extortTable.apply {
                header.validate("The raiders are extorting our village for", 4, 1)

                results shouldContain "[2d10] CP per week from each family"
                results shouldContain "[1d6] SP per month from each family"
                results shouldNotContain "They also demand..."
            }

            val tormentTable = tables[2]
            tormentTable.apply {
                header.validate("Worse, they routinely torment us by", 20, 1)

                results shouldContain "making us burn our clothing and shave our heads"
                results shouldContain "making us watch as they destroy our market stands"
                results shouldNotContain "If we disobey or resist, they have threatened to..."
            }

            val elseTable = tables[4]
            elseTable.apply {
                header.validate("What Else?", 20, 1)

                results shouldContain "We've had enough, and are preparing an ambush."
                results shouldContain "Just when we thought things couldn't get worse, one of our daughters was found murdered and brutalized. Enough is enough!"
            }
        }
    }

    group("parsing trailRations") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/trailRations").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 6

            val meatTable = tables[0]
            meatTable.apply {
                header.validate("Meat is", 12, 1)
                results shouldContainAll listOf("Beef jerky", "Deer jerky", "Dried pork rinds", "Smoked sausage", "Spicy sausage", "Sweet sausage")
            }

            val breadTable = tables[1]
            breadTable.apply {
                header.validate("Bread is", 12, 1)
                results shouldContainAll listOf("Beer bread", "Biscuits", "Brown bread", "Pumpernickel", "Rye bread", "Sourdough bread")
            }

            val vegetableTable = tables[2]
            vegetableTable.apply {
                header.validate("Pickled Vegetable is", 12, 1)
                results shouldContainAll listOf("Asparagus", "Beets", "Cauliflower", "Green beans", "Red cabbage")
            }

            val nutsTable = tables[3]
            nutsTable.apply {
                header.validate("Nuts / Seeds is", 12, 1)
                results shouldContainAll listOf("Cashews", "Pistachios", "Sunflower seeds", "Walnuts")
            }

            val fruitTable = tables[4]
            fruitTable.apply {
                header.validate("Dried Fruit is", 12, 1)
                results shouldContainAll listOf("Apples", "Apricots", "Mangoes", "Raisins", "Sun-dried tomatoes")
            }

            val boilTable = tables[5]
            boilTable.apply {
                header.validate("Boil and Serve is", 12, 1)
                results shouldContainAll listOf("Beets", "Black beans", "Mushrooms", "Lentils", "White beans", "Turnips")
            }
        }
    }

    group("parsing trailRations without dieSize") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/trailRations-reduced").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 6

            val meatTable = tables[0]
            meatTable.apply {
                header.validate("Meat is", 12, 1)
                results shouldContainAll listOf("Beef jerky", "Deer jerky", "Dried pork rinds", "Smoked sausage", "Spicy sausage", "Sweet sausage")
            }

            val breadTable = tables[1]
            breadTable.apply {
                header.validate("Bread is", 12, 1)
                results shouldContainAll listOf("Beer bread", "Biscuits", "Brown bread", "Pumpernickel", "Rye bread", "Sourdough bread")
            }

            val vegetableTable = tables[2]
            vegetableTable.apply {
                header.validate("Pickled Vegetable is", 12, 1)
                results shouldContainAll listOf("Asparagus", "Beets", "Cauliflower", "Green beans", "Red cabbage")
            }

            val nutsTable = tables[3]
            nutsTable.apply {
                header.validate("Nuts / Seeds is", 12, 1)
                results shouldContainAll listOf("Cashews", "Pistachios", "Sunflower seeds", "Walnuts")
            }

            val fruitTable = tables[4]
            fruitTable.apply {
                header.validate("Dried Fruit is", 12, 1)
                results shouldContainAll listOf("Apples", "Apricots", "Mangoes", "Raisins", "Sun-dried tomatoes")
            }

            val boilTable = tables[5]
            boilTable.apply {
                header.validate("Boil and Serve is", 12, 1)
                results shouldContainAll listOf("Beets", "Black beans", "Mushrooms", "Lentils", "White beans", "Turnips")
            }
        }
    }

    group("parsing gladiator") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/gladiator").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 5

            val armorTable = tables[0]
            armorTable.apply {
                header.validate("Armor is", 6, 1)
                results shouldContainAll listOf("None", "Bronze helm", "Chainmail")
            }

            val weaponryTable = tables[1]
            weaponryTable.apply {
                header.validate("Weaponry is", 6, 1)
                results shouldContainAll listOf("Two shortswords", "Spear and shield", "Scimitar and whip")
            }

            val featureTable = tables[3]
            featureTable.apply {
                header.validate("Identifying feature is", 6, 1)
                results shouldContainAll listOf("Maritime tattoo", "Scars on face", "Long hair")
            }

            val tragedyTable = tables[4]
            tragedyTable.apply {
                header.validate("Personal tragedy is", 6, 1)
                results shouldContainAll listOf("Doomed love affair", "Death of a child", "Prisoner of war")
            }
        }
    }

    group("parsing alchemy") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/alchemy").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 7

            val ingredientTable = tables[0]
            ingredientTable.apply {
                header.validate("Ingredient is", 6, 1)
                results shouldContainAll listOf("Exotic oil", "Rare herb", "Heavy metal")
            }

            val formTable = tables[2]
            formTable.apply {
                header.validate("Usable form is", 6, 1)
                results shouldContainAll listOf("Paste", "Powder", "Oil")
            }

            val activationTable = tables[4]
            activationTable.apply {
                header.validate("Activation is", 6, 1)
                results shouldContainAll listOf("Burn", "Shake", "Splash")
            }

            val expirationTable = tables[6]
            expirationTable.apply {
                header.validate("Expiration is", 6, 1)
                results shouldContainAll listOf("1 min", "1 hour", "1 week")
            }
        }
    }
})

fun TableHeader.validate(description: String, dieSize: Int, rollsRequired: Int) {
    TableHeader(rollsRequired, dieSize, description) shouldEqual this
}