package com.christian.dnd.d100

import com.christian.dnd.d100.cleaner.TableCleaner
import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.parsers.block.FileIsTableBlockParser
import com.christian.dnd.d100.parsers.block.MultiTableBlockParser
import com.christian.dnd.d100.parsers.block.WideTableBlockParser
import com.christian.dnd.d100.parsers.content.RangeTableContentParser
import com.christian.dnd.d100.parsers.content.SimpleTableContentParser
import com.christian.dnd.d100.parsers.header.BeginningTableHeaderParser
import com.christian.dnd.d100.parsers.header.EndingTableHeaderParser
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
        help = "How many results to roll."
    ) { toInt() }.default(1)

    val file by parser.positional(
        "FILE",
        help = "d100 file to roll from."
    ) { File(this) }
}

fun main(args: Array<String>) = mainBody {
    val commandLineParser = ArgParser(args).parseInto(::D100ArgParser)
    val d100File = commandLineParser.file
    val runSilently = commandLineParser.silent

    val diceExpressionEvaluator = DiceExpressionEvaluator()
    val tableCleaner = TableCleaner()
    val tableHeaderParsers = listOf(
        BeginningTableHeaderParser(),
        EndingTableHeaderParser()
    )

    val simpleTableContentParser = SimpleTableContentParser()
    val rangeTableContentParser = RangeTableContentParser()

    val wideTableBlockParser = WideTableBlockParser()
    val multiTableBlockParser = MultiTableBlockParser(
        simpleTableContentParser,
        rangeTableContentParser,
        tableHeaderParsers
    )
    val fileIsTableBlockParser = FileIsTableBlockParser(
        simpleTableContentParser,
        rangeTableContentParser,
        tableHeaderParsers
    )

    val tables = D100TableParser(
        listOf(
            wideTableBlockParser,
            multiTableBlockParser,
            fileIsTableBlockParser
        ),
        diceExpressionEvaluator,
        tableCleaner
    ).parse(d100File)

    roll(runSilently, commandLineParser.rolls, tables)
}

private fun roll(runSilently: Boolean, rolls: Int, tables: List<Table.PreppedTable>) {
    val rollMethod = {
        if (!runSilently) {
            RollMaster().rollWithDescriptor(tables)
        } else {
            RollMaster().rollWithoutDescriptor(tables)
        }
    }

    (0 until rolls).forEach { roll ->
        if (rolls > 1) {
            println("Roll $roll:")
        }

        rollMethod().forEach { println(it) }
    }
}