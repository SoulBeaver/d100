package dev.christianbroomfield.d100

import dev.christianbroomfield.d100.cleaner.TableCleaner
import dev.christianbroomfield.d100.expression.ArithmeticExpressionEvaluator
import dev.christianbroomfield.d100.expression.DiceExpressionEvaluator
import dev.christianbroomfield.d100.model.TableHeader
import dev.christianbroomfield.d100.parsers.block.FileIsTableBlockParser
import dev.christianbroomfield.d100.parsers.block.MixedTableBlockParser
import dev.christianbroomfield.d100.parsers.block.StructuredTableBlockParser
import dev.christianbroomfield.d100.parsers.block.WhiteSpaceDelimitedTableBlockParser
import dev.christianbroomfield.d100.parsers.block.WideTableBlockParser
import dev.christianbroomfield.d100.parsers.content.RangeTableContentParser
import dev.christianbroomfield.d100.parsers.content.SimpleTableContentParser
import dev.christianbroomfield.d100.parsers.header.BeginningTableHeaderParser
import dev.christianbroomfield.d100.parsers.header.EndingTableHeaderParser
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotContain
import org.spekframework.spek2.Spek
import java.io.File
import kotlin.random.Random

class D100TableParserSpec : Spek({
    val tableCleaner = TableCleaner()

    val expressionEvaluationPipeline = listOf(
        DiceExpressionEvaluator(Random(0)),
        ArithmeticExpressionEvaluator()
    )

    val tableHeaderParsers = listOf(
        BeginningTableHeaderParser(),
        EndingTableHeaderParser()
    )

    val simpleTableContentParser = SimpleTableContentParser()
    val rangeTableContentParser = RangeTableContentParser()

    val wideTableBlockParser = WideTableBlockParser()
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

    val whiteSpaceDelimitedTableBlockParser = WhiteSpaceDelimitedTableBlockParser()

    val parser = dev.christianbroomfield.d100.D100TableParser(
        listOf(
            wideTableBlockParser,
            MixedTableBlockParser(
                structuredTableBlockParser,
                whiteSpaceDelimitedTableBlockParser
            ),
            fileIsTableBlockParser
        ),
        expressionEvaluationPipeline,
        tableCleaner
    )

    group("parsing slime") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/slime").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            val slimeColorTable = tables[0]
            slimeColorTable.apply {
                header.validate("This slime’s colour", 20, 1)

                results shouldContain "Is red. Its touch is burning hot."
                results shouldContain "Is grey. It attacks by exploding and then reforming itself 2 rounds later."
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

                results shouldContain "Illusory Horrors. Visions of pure terror reside within this storm. Any creature with an intelligence of 2 or greater must make a DC 14 Wisdom saving throw or become Stunned by the atrocious images within. Creatures that become stunned in this manner may repeat their saving throw every minute thereafter. If a creature fails 3 saving throws in a row, their worst fear becomes real before them."
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
                results shouldContain "A wooden hand with the ring finger missing"
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
            val demandTable = tables[1]
            demandTable.apply {
                header.validate("They also demand", 10, 1)

                results shouldContain "that we give them half of all the food we grow"
                results shouldContain "free food and drink at the tavern every time they come through town"
                results shouldNotContain "Worse, they routinely torment us by..."
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
                results shouldContainAll listOf(
                    "Beef jerky",
                    "Deer jerky",
                    "Dried pork rinds",
                    "Smoked sausage",
                    "Spicy sausage",
                    "Sweet sausage"
                )
            }

            val breadTable = tables[1]
            breadTable.apply {
                header.validate("Bread is", 12, 1)
                results shouldContainAll listOf(
                    "Beer bread",
                    "Biscuits",
                    "Brown bread",
                    "Pumpernickel",
                    "Rye bread",
                    "Sourdough bread"
                )
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
                results shouldContainAll listOf(
                    "Beets",
                    "Black beans",
                    "Mushrooms",
                    "Lentils",
                    "White beans",
                    "Turnips"
                )
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
                results shouldContainAll listOf(
                    "Beef jerky",
                    "Deer jerky",
                    "Dried pork rinds",
                    "Smoked sausage",
                    "Spicy sausage",
                    "Sweet sausage"
                )
            }

            val breadTable = tables[1]
            breadTable.apply {
                header.validate("Bread is", 12, 1)
                results shouldContainAll listOf(
                    "Beer bread",
                    "Biscuits",
                    "Brown bread",
                    "Pumpernickel",
                    "Rye bread",
                    "Sourdough bread"
                )
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
                results shouldContainAll listOf(
                    "Beets",
                    "Black beans",
                    "Mushrooms",
                    "Lentils",
                    "White beans",
                    "Turnips"
                )
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

    group("parsing spire") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/spire").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 6

            val houseTable = tables[0]
            houseTable.apply {
                header.validate("WHICH HOUSE IS THIS DROW NOBLE CLAIMING TO COME FROM?", 10, 1)
                results shouldContainAll listOf(
                    "Destera, the Weavers: deposed rulers of Spire, all faded grandeur",
                    "Yssen, the Unquiet Blades: glory-hound warriors",
                    "Quinn, the Noble and Most High; nouveau riche, not bound by blood but sworn in",
                    "Roll again, re-rolling further results of 10 – they have been disowned by this house"
                )
            }
        }
    }

    group("parsing exoticJungle") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/exoticJungle").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 9

            val raceTable = tables[0]
            raceTable.apply {
                header.validate("Dominant race", 20, 1)
                results shouldContainAll listOf(
                    "Aett-raths (Goblins)",
                    "Nold-raths (Giants)",
                    "Isstatsessei (lizardfolk)",
                    "Chinasa (therianthropes)"
                )
            }

            val funFactTable = tables[5]
            funFactTable.apply {
                header.validate("Fun fact", 8, 1)
                results shouldContainAll listOf(
                    "Men",
                    "Women",
                    "Children",
                    "The Elderly"
                )
            }

            val knownToTable = tables[6]
            knownToTable.apply {
                header.validate("Are known to", 20, 1)
                results shouldContainAll listOf(
                    "Perform religious rituals",
                    "Provide for the chief’s harem",
                    "Heal",
                    "Be sacrificed to the gods"
                )
            }
        }
    }

    group("parsing exoticJungle-mixing") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/exoticJungle-mixing").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 9

            val raceTable = tables[0]
            raceTable.apply {
                header.validate("Dominant race", 20, 1)
                results shouldContainAll listOf(
                    "Aett-raths (Goblins)",
                    "Nold-raths (Giants)",
                    "Isstatsessei (lizardfolk)",
                    "Chinasa (therianthropes)"
                )
            }

            val knownToTable = tables[3]
            knownToTable.apply {
                header.validate("And", 20, 1)
                results shouldContainAll listOf(
                    "Decorations of humanoid bones",
                    "A spider totem",
                    "Every house is decorated with multi-coloured feathers",
                    "A moat with a giant man-eating catfish"
                )
            }

            val funFactTable = tables[5]
            funFactTable.apply {
                header.validate("Fun fact", 8, 1)
                results shouldContainAll listOf(
                    "Men",
                    "Women",
                    "Children",
                    "The Elderly"
                )
            }
        }
    }

    group("parsing exoticJungle-mixing2") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/exoticJungle-mixing2").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 9

            val raceTable = tables[0]
            raceTable.apply {
                header.validate("Dominant race", 20, 1)
                results shouldContainAll listOf(
                    "Aett-raths (Goblins)",
                    "Nold-raths (Giants)",
                    "Isstatsessei (lizardfolk)",
                    "Chinasa (therianthropes)"
                )
            }

            val knownToTable = tables[8]
            knownToTable.apply {
                header.validate("Local transport", 20, 1)
                results shouldContainAll listOf(
                    "Giant crocodile",
                    "Pterodactyl",
                    "Giant spider"
                )
            }
        }
    }

    group("parsing zeke") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/zeke").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 1

            val locationTable = tables[0]
            locationTable.apply {
                header.validate("LOCATION", 100, 1)
                results shouldContainAll listOf(
                    "Asleep in the hut",
                    "Eating smoked meat",
                    "Crying",
                    "Chopping down a tree—will return in 4x10 minutes with firewood"
                )

                results.count { it == "Asleep in the hut" } shouldEqual 25
                results.count { it == "Crying" } shouldEqual 1
                results.count { it == "Eating smoked meat" } shouldEqual 2
            }
        }
    }

    group("parsing unthing") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/unthing").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 1

            val unthingTable = tables[0]
            unthingTable.apply {
                header.validate("UNTHING PROPERTIES", 10, 1)
                results shouldContainAll listOf(
                    "Two Hit Dice",
                    "Two Hit Dice, spit stomach acid 10’ for 3 damage",
                    "Two Hit Dice, paralyzing touch, always on fire",
                    "Four Hit Dice, steals away a level (down to halfway up the last level) and a die of hit points at a touch, adding the hit points to its own."
                )

                results.count { it == "Two Hit Dice, paralyzing touch" } shouldEqual 2
            }
        }
    }

    group("parsing changeling") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/changeling").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 12

            val reasonTable = tables[0]
            reasonTable.apply {
                header.validate("Reason", 20, 1)
                results shouldContainAll listOf(
                    "A faerie lord or lady desired an heir and sent forth faeries to steal away the infant.",
                    "Faeries simply stole the child on a whim."
                )
            }

            val parentsTable = tables[1]
            parentsTable.apply {
                header.validate("Parental Status", 3, 2)
                results shouldContainAll listOf(
                    "Both parents are dead.",
                    "One parent is dead, even chance for mother or father.",
                    "Both parents are alive.",
                    "You do not know the fate of your foster parents"
                )

                results.count { it == "Both parents are alive." } shouldEqual 2
            }

            val familyWealthTable = tables[4]
            familyWealthTable.apply {
                header.validate("Family Wealth", 6, 3)
                results shouldContainAll listOf(
                    "Destitute. Subtract 6 from your Childhood roll.",
                    "Poor. Subtract 3 from your Childhood roll.",
                    "Wealthy. Add 3 to your Childhood roll."
                )
            }

            val genderTable = tables[9]
            genderTable.apply {
                header.validate("Gender", 6, 1)
                results shouldContainAll listOf(
                    "Female",
                    "Male"
                )
            }
        }
    }

    group("parsing witchFood") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/witchFood").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 7

            val cookedTable = tables[0]
            cookedTable.apply {
                header.validate("Tonight, in a witch's hovel, we dine on", 12, 1)
                results shouldContainAll listOf(
                    "Stewed.",
                    "Battered."
                )
            }

            val foodTable = tables[1]
            foodTable.apply {
                header.validate("", 20, 1)
                results shouldContainAll listOf(
                    "Songbird.",
                    "Snake.",
                    "Dog.",
                    "Dwarf."
                )
            }

            val withTable = tables[2]
            withTable.apply {
                header.validate("with", 20, 1)
                results shouldContainAll listOf(
                    "Dumplings.",
                    "Red cabbage.",
                    "String beans."
                )
            }

            val andTable = tables[3]
            andTable.apply {
                header.validate("and", 20, 1)
                results shouldContainAll listOf(
                    "Apples.",
                    "Carrots.",
                    "Garlic."
                )
            }

            val sideTable = tables[4]
            sideTable.apply {
                header.validate("On the side, there are some", 20, 1)
                results shouldContainAll listOf(
                    "Rum cakes.",
                    "Maggots.",
                    "Mysterious sausages."
                )
            }
        }
    }

    group("parsing exoticFood") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/exoticFood").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 16

            val sizeTable = tables[0]
            sizeTable.apply {
                header.validate("Size", 6, 1)
                results shouldContainAll listOf(
                    "Tiny – this fruit could be easily flicked away",
                    "Enormous – this fruit is man-sized or larger"
                )
            }

            val seedsTable = tables[12]
            seedsTable.apply {
                header.validate("Seeds", 8, 1)
                results shouldContainAll listOf(
                    "One big core",
                    "On the stem",
                    "None"
                )
            }

            val seedColorTable = tables[13]
            seedColorTable.apply {
                header.validate("Seed Colour", 8, 1)

                results.count { it == "Different shade of Skin Colour." } shouldEqual 4
            }

            val specialDetailTable = tables[15]
            specialDetailTable.apply {
                header.validate("Special Detail", 60, 1)
                results shouldContainAll listOf(
                    "The skin pattern is fluorescent",
                    "It is extremely rare and prohibitively expensive"
                )
            }
        }
    }

    group("parsing strangePlaces") {
        val file = File(D100TableParserSpec::class.java.getResource("/tables/strangePlaces").toURI())
        val tables = parser.parse(file)

        test("has the correct attributes set") {
            tables.size shouldEqual 8

            val sourceTable = tables[0]
            sourceTable.apply {
                header.validate("SOURCE", 10, 1)
                results shouldContainAll listOf(
                    "Rumors speak of a",
                    "Passers-by talk about the disappearance of someone. They mention a"
                )
            }

            val descriptor1Table = tables[1]
            descriptor1Table.apply {
                header.validate("DESCRIPTOR 1", 20, 1)
                results shouldContainAll listOf(
                    "haunted",
                    "cursed",
                    "dark"
                )
            }

            val placeTable = tables[2]
            placeTable.apply {
                header.validate("PLACE", 100, 1)
                results shouldContainAll listOf(
                    "house",
                    "graveyard",
                    "lair"
                )
            }

            val actionTable = tables[7]
            actionTable.apply {
                header.validate("ACTION", 20, 1)
                results shouldContainAll listOf(
                    "starts crying when asked about the place.",
                    "calls the guard, when asked about the place.",
                    "says it’s already too late."
                )
            }
        }
    }
})

fun TableHeader.validate(description: String, dieSize: Int, rollsRequired: Int) {
    TableHeader(rollsRequired, dieSize, description) shouldEqual this
}