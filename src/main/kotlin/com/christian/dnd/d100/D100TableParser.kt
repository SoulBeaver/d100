package com.christian.dnd.d100

import com.christian.dnd.d100.cleaner.TableCleaner
import com.christian.dnd.d100.expression.ExpressionEvaluator
import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.parsers.block.TableBlockParser
import java.io.File

class D100TableParser(
    private val tableBlockParsers: List<TableBlockParser>,
    private val expressionEvaluatorPipeline: List<ExpressionEvaluator>,
    private val tableCleaner: TableCleaner
) {

    fun parse(d100Table: File): List<Table.PreppedTable> {
        if (!d100Table.exists() || !d100Table.isFile) {
            System.out.println("The file ${d100Table.name} either does not exist or is not a file.")
            return emptyList()
        }

        val filename = d100Table.nameWithoutExtension
        val contents = d100Table.readLines()
            .asSequence()
            .map { line -> line.replace("\u200B", "") }
            .toList()

        if (contents.isEmpty()) {
            System.out.println("The file ${d100Table.name} is empty.")
            return emptyList()
        }

        return tableBlockParsers
            .first { parser -> parser.canParse(contents) }
            .parse(contents, filename)
            .asSequence()
            .map { dirtyTable -> tableCleaner.clean(dirtyTable) }
            .map { cleanedTable ->
                val evaluatedTableResults = cleanedTable.results.map {
                    expressionEvaluatorPipeline.fold(it) { evaluatedExpressionAcc, evaluator ->
                        evaluator.evaluate(evaluatedExpressionAcc)
                    }
                }
                Table.PreppedTable(cleanedTable.header, evaluatedTableResults, cleanedTable.rollBehavior)
            }
            .toList()
    }
}
