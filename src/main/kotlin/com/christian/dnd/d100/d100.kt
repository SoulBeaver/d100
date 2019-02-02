package com.christian.dnd.d100

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import java.io.File
import java.util.regex.Pattern

/*
 * Usage: d100 <table> <flags>
 * Flags:
 *  - silent (-s)   No descriptor, only the result.
 *  - repeat (-3)   Roll the d100 file multiple times*
 *  - ignore (-i)   Ignore constraint for full tables and reroll empty results*
 *
 *  * Not MVP
 */

/*
 * Tables
 * - List of Table
 */



/*
 * Table
 * - Descriptor
 * - Die size
 * - List of Results
 * - Rolls required
 *
 * Requirement: table after parsing has die size options to choose from.
 */

/*
 * Descriptor: (Die Size) (Description)
 */

/*
 * d10 Title. This is a...
    Potion
    Elixir
    Draught
    Vial
    Philter
    Tonic
    Brew
    Ichor
    Juice
    Concoction
 *
 * d100 potion
 *
 * This is a Potion.
 */

/*
 * d10 Title. This is a...
   1 Potion
   2 Elixir
   3 Draught
   4 Vial
   5 Philter
   6 Tonic
   7 Brew
   8 Ichor
   9 Juice
   10 Concoction
 *
 * d10 Strength. The potions strength is...
    Regular with a slight side effect.
    Regular with no side effect.
    Regular with a strong side effect.
    Minor with a strong side effect.
    Minor with a slight side effect.
    Major with a strong side effect.
    Major with a small side effect.
    A poison. Almost no positive affect all side effect.
    Temporary but strong and wears off quickly.
    Seemingly permanent.
 *
 *  d100 potion
 *
 * This is a Potion. The potion's strength is Major with a small side effect.
 */

/*
d100 	Magic Item
01–50 	Potion of healing
51–60 	Spell scroll (cantrip)
61–70 	Potion of climbing
71–90 	Spell scroll (1st level)
91–94 	Spell scroll (2nd level)
95–98 	Potion of greater healing
99 	Bag of holding
00 	Driftglobe

 * d100 magicitem -s
 *
 * Potion of healing
 */

/*
2d10 | Shops
General Store
Alchemist/Herbalist/Healer
Blacksmith (Armor, Weapons, Tools)
Carpenter (Boats, Buildings, Wagons)
Clothing (Common, Fine)
Enchanter/Hex Den
Glassblower
Leatherworks (Armor, Saddlery)
Stables
Exotic Goods (Carpets & Cloth, Jewelry, Perfumes, Curio)
 */

class D100ArgParser(parser: ArgParser) {
    val silent by parser.flagging(
        "-s", "--silent",
        help = "Don't display descriptors, only the results."
    )

    val file by parser.positional(
        "FILE",
        help = "d100 file to roll from."
    ) { File(this) }
}

fun main(args: Array<String>) {
    val commandLineParser = ArgParser(args).parseInto(::D100ArgParser)

    val d100File = commandLineParser.file
    val runSilently = commandLineParser.silent

    val tables = D100TableParser.parse(d100File)

    RollMaster().roll(tables).forEach { println(it) }
}