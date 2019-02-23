package com.christian.dnd.d100

import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.model.TableHeader
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainAll
import org.spekframework.spek2.Spek
import kotlin.random.Random

class RollMasterSpec : Spek({
    group("rolling a single table with a single entry") {
        val tables = listOf(
            Table(
                TableHeader(1, 1, "This slime's colour:"),
                listOf("red")
            )
        )

        test("has descriptor and result") {
            RollMaster().rollWithDescriptor(tables) shouldContain "This slime's colour: red"
        }
    }

    group("rolling a single table with multiple entries") {
        val tables = listOf(
            Table(
                TableHeader(1, 20, "This slime's colour:"),
                listOf(
                    "red",
                    "green",
                    "blue",
                    "teal",
                    "cyan",
                    "purple",
                    "yellow",
                    "brown",
                    "black",
                    "white",
                    "pink",
                    "orange",
                    "lime",
                    "gray",
                    "eggshell",
                    "bronze",
                    "copper",
                    "sherwood green",
                    "oxblood",
                    "fritzrot"
                )
            )
        )

        test("picks a result determined by fair random die roll") {
            val rollMaster = RollMaster(object : Random() {
                override fun nextBits(bitCount: Int): Int = 0

                override fun nextInt(until: Int): Int {
                    return 4
                }
            })

            rollMaster.rollWithDescriptor(tables) shouldContain "This slime's colour: cyan"
        }
    }

    group("rolling a single table with multiple entries multiple times") {
        val tables = listOf(
            Table(
                TableHeader(5, 20, "This slime's colour:"),
                listOf(
                    "red",
                    "green",
                    "blue",
                    "teal",
                    "cyan",
                    "purple",
                    "yellow",
                    "brown",
                    "black",
                    "white",
                    "pink",
                    "orange",
                    "lime",
                    "gray",
                    "eggshell",
                    "bronze",
                    "copper",
                    "sherwood green",
                    "oxblood",
                    "fritzrot"
                )
            )
        )

        test("rolls the same table multiple times") {
            val rollMaster = RollMaster(Random(0))

            rollMaster.rollWithDescriptor(tables) shouldContainAll listOf(
                "This slime's colour: eggshell",
                "This slime's colour: black",
                "This slime's colour: sherwood green",
                "This slime's colour: brown",
                "This slime's colour: blue"
            )
        }
    }

    group("rolling multiple tables of varying sizes") {
        val tables = listOf(
            Table(
                TableHeader(1, 20, "This slime's colour:"),
                listOf(
                    "red",
                    "green",
                    "blue",
                    "teal",
                    "cyan",
                    "purple",
                    "yellow",
                    "brown",
                    "black",
                    "white",
                    "pink",
                    "orange",
                    "lime",
                    "gray",
                    "eggshell",
                    "bronze",
                    "copper",
                    "sherwood green",
                    "oxblood",
                    "fritzrot"
                )
            ),
            Table(
                TableHeader(1, 4, "This slime's texture:"),
                listOf("smooth", "marbled", "cracked", "viscous")
            ),
            Table(
                TableHeader(1, 6, "This slime's odor:"),
                listOf("musky", "sweet", "like strawberries", "oily", "sulphuric", "corrosive")
            )
        )

        test("rolls each table once") {
            val rollMaster = RollMaster(Random(0))

            rollMaster.rollWithDescriptor(tables) shouldContainAll listOf(
                "This slime's colour: eggshell",
                "This slime's texture: marbled",
                "This slime's odor: oily"
            )
        }
    }

    group("rolling multiple tables of varying sizes a variable number of times") {
        val tables = listOf(
            Table(
                TableHeader(1, 20, "This slime's colour:"),
                listOf(
                    "red",
                    "green",
                    "blue",
                    "teal",
                    "cyan",
                    "purple",
                    "yellow",
                    "brown",
                    "black",
                    "white",
                    "pink",
                    "orange",
                    "lime",
                    "gray",
                    "eggshell",
                    "bronze",
                    "copper",
                    "sherwood green",
                    "oxblood",
                    "fritzrot"
                )
            ),
            Table(
                TableHeader(3, 4, "This slime's texture:"),
                listOf("smooth", "marbled", "cracked", "viscous")
            ),
            Table(
                TableHeader(2, 6, "This slime's odor:"),
                listOf("musky", "sweet", "like strawberries", "oily", "sulphuric", "corrosive")
            )
        )

        test("rolls each table once") {
            val rollMaster = RollMaster(Random(0))

            rollMaster.rollWithDescriptor(tables) shouldContainAll listOf(
                "This slime's colour: eggshell",
                "This slime's texture: marbled",
                "This slime's texture: viscous",
                "This slime's texture: cracked",
                "This slime's odor: musky",
                "This slime's odor: corrosive"
            )
        }
    }

    group("rolling a table with quick'n'dirty results silently") {
        val tables = listOf(
            Table(
                TableHeader(1, 4, "Armor"),
                listOf("Breastplate", "Chain mail", "Chain shirt", "Half plate")
            )
        )

        test("returns the result without a descriptor") {
            val rollMaster = RollMaster(object : Random() {
                override fun nextBits(bitCount: Int): Int = 0

                override fun nextInt(until: Int): Int {
                    return 0
                }
            })

            rollMaster.rollWithoutDescriptor(tables) shouldContain "Breastplate"
        }
    }
})