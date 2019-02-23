package com.christian.dnd.d100.parsers.block

import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.parsers.content.TableContentParser
import com.christian.dnd.d100.parsers.header.TableHeaderParser

/**
 * Assumes that the file has a structure wherein one or more tables will have a descriptor and a list of die roll results.
 */
class StructuredTableBlockParser(
    simpleTableContentParser: TableContentParser,
    rangeTableContentParser: TableContentParser,
    tableHeaderParsers: List<TableHeaderParser>
) : TableBlockParser(simpleTableContentParser, rangeTableContentParser, tableHeaderParsers) {

    override fun parse(contents: List<String>, filename: String): List<Table> {
        return parseRecursively(contents, filename)
    }

    private tailrec fun parseRecursively(contents: List<String>, filename: String, tablesAcc: List<Table> = emptyList()): List<Table> {
        // Workaround for compiler bug that crashes if you replace the if expression with a ?.let {}
        val header = contents.firstOrNull { line -> isHeader(line) }
        return if (header != null) {
            val tableBlock = parseTableBlock(header, contents.subList(contents.indexOf(header) + 1, contents.size))

            parseRecursively(contents.subList(tableBlock.linesRead + 1, contents.size), filename, tablesAcc + tableBlock.table)
        } else {
            tablesAcc
        }
    }

    override fun canParse(contents: List<String>) = contents.any { line -> isHeader(line) }

    private fun parseTableBlock(header: String, contents: List<String>) =
        contents.takeWhile { line -> !isHeader(line) }
            .let { tableContents ->
                TableBlock(
                    parseTable(header, tableContents),
                    tableContents.size
                )
            }
}

private data class TableBlock(val table: Table, val linesRead: Int)