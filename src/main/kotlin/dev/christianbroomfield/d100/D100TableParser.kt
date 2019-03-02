package dev.christianbroomfield.d100

import dev.christianbroomfield.d100.cleaner.TableCleaner
import dev.christianbroomfield.d100.expression.ArithmeticExpressionEvaluator
import dev.christianbroomfield.d100.expression.DiceExpressionEvaluator
import dev.christianbroomfield.d100.expression.ExpressionEvaluator
import dev.christianbroomfield.d100.model.Table
import dev.christianbroomfield.d100.parsers.block.FileIsTableBlockParser
import dev.christianbroomfield.d100.parsers.block.MixedTableBlockParser
import dev.christianbroomfield.d100.parsers.block.StructuredTableBlockParser
import dev.christianbroomfield.d100.parsers.block.TableBlockParser
import dev.christianbroomfield.d100.parsers.block.WhiteSpaceDelimitedTableBlockParser
import dev.christianbroomfield.d100.parsers.block.WideTableBlockParser
import dev.christianbroomfield.d100.parsers.content.RangeTableContentParser
import dev.christianbroomfield.d100.parsers.content.SimpleTableContentParser
import dev.christianbroomfield.d100.parsers.header.BeginningTableHeaderParser
import dev.christianbroomfield.d100.parsers.header.EndingTableHeaderParser
import java.io.File

/**
 * Parses a file into one or more tables, if applicable. Use the default() method to create the standard
 * D100TableParser or create your own by modifying the parsers, evaluators and cleaners that will process a file.
 */
class D100TableParser(
    private val tableBlockParsers: List<TableBlockParser>,
    private val expressionEvaluatorPipeline: List<ExpressionEvaluator>,
    private val tableCleaner: TableCleaner
) {
    /**
     * Parses a list of tables from a file if possible.
     *
     * @param d100Table the file to be parsed
     * @return a list of PreppedTables that can be used for rolling results
     */
    fun parse(d100Table: File): List<Table.PreppedTable> {
        if (!d100Table.exists() || !d100Table.isFile) {
            return emptyList()
        }

        val filename = d100Table.nameWithoutExtension
        val contents = d100Table.readLines()
            .asSequence()
            .map { line -> line.replace("\u200B", "") }
            .toList()

        if (contents.isEmpty()) {
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

    companion object {
        /**
         * Create a default D100TableParser capable of parsing a variety of tables including table grids.
         */
        fun default(): D100TableParser {
            val expressionEvaluationPipeline = listOf(
                DiceExpressionEvaluator(),
                ArithmeticExpressionEvaluator()
            )

            val tableCleaner = TableCleaner()
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

            return D100TableParser(
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
        }
    }
}
