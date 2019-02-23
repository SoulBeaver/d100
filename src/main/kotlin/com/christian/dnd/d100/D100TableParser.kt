package com.christian.dnd.d100

import com.christian.dnd.d100.cleaner.TableCleaner
import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.parsers.block.TableBlockParser
import java.io.File

class D100TableParser(
    private val tableBlockParsers: List<TableBlockParser>,
    private val diceExpressionEvaluator: DiceExpressionEvaluator,
    private val tableCleaner: TableCleaner) {

    fun parse(d100Table: File): List<Table.PreppedTable> {
        val filename = d100Table.nameWithoutExtension
        val contents = d100Table.readLines()
            .asSequence()
            .map { line -> line.replace("\u200B", "") }
            .filter(String::isNotBlank)
            .toList()

        return tableBlockParsers
            .first { parser -> parser.canParse(contents) }
            .parse(contents, filename)
            .asSequence()
            .map { dirtyTable -> tableCleaner.clean(dirtyTable) }
            .map { cleanedTable ->
                val evaluatedTableResults = cleanedTable.results.map { diceExpressionEvaluator.evaluate(it) }
                Table.PreppedTable(cleanedTable.header, evaluatedTableResults, cleanedTable.rollBehavior)
            }
            .toList()
    }
}
