package com.christian.dnd.d100.parsers.block

import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.parsers.content.TableContentParser
import com.christian.dnd.d100.parsers.header.TableHeaderParser

private data class TableBlock(val table: Table, val linesRead: Int)

class StructuredTableBlockParser(
    simpleTableContentParser: TableContentParser,
    rangeTableContentParser: TableContentParser,
    tableHeaderParsers: List<TableHeaderParser>
) : TableBlockParser(simpleTableContentParser, rangeTableContentParser, tableHeaderParsers) {

    override fun parse(contents: List<String>, filename: String): List<Table> {
        val tables = mutableListOf<Table>()

        var i = 0
        while (i < contents.size) {
            val line = contents[i]

            if (isHeader(line)) {
                val tableBlock = parseTableBlock(line, contents.subList(i + 1, contents.size))

                tables.add(tableBlock.table)

                i += tableBlock.linesRead
            } else {
                i++
            }
        }

        return tables
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