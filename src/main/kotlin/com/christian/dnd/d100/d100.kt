package com.christian.dnd.d100

import com.christian.dnd.d100.parsers.block.FileIsTableBlockParser
import com.christian.dnd.d100.parsers.content.RangeTableContentParser
import com.christian.dnd.d100.parsers.content.SimpleTableContentParser
import com.christian.dnd.d100.parsers.block.StructuredTableBlockParser
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import java.io.File

class D100ArgParser(parser: ArgParser) {
    val silent by parser.flagging(
        "-s", "--silent",
        help = "Don't display descriptors, only the results."
    )

    val rolls by parser.storing(
        "-r", "--rolls",
        help = "How many results to roll.") { toInt() }.default(1)

    val file by parser.positional(
        "FILE",
        help = "d100 file to roll from."
    ) { File(this) }
}

fun main(args: Array<String>)  = mainBody {
    val commandLineParser = ArgParser(args).parseInto(::D100ArgParser)

    val d100File = commandLineParser.file
    val runSilently = commandLineParser.silent

    val simpleTableContentParser = SimpleTableContentParser()
    val rangeTableContentParser = RangeTableContentParser()

    val tables = D100TableParser(
        StructuredTableBlockParser(
            simpleTableContentParser,
            rangeTableContentParser
        ),
        FileIsTableBlockParser(simpleTableContentParser, rangeTableContentParser)
    ).parse(d100File)

    val rollMethod = {
        if (!runSilently) {
            RollMaster().rollWithDescriptor(tables)
        } else {
            RollMaster().rollWithoutDescriptor(tables)
        }
    }

    (0 until commandLineParser.rolls).forEach {
        if (commandLineParser.rolls > 1) {
            println("Roll $it:")
        }

        rollMethod().forEach { println(it) }
    }
}