package com.christian.dnd.d100.parsers.block

import com.christian.dnd.d100.Table
import com.christian.dnd.d100.parsers.content.TableContentParser

class StructuredTableBlockParser(
    simpleTableContentParser: TableContentParser,
    rangeTableContentParser: TableContentParser
): TableBlockParser(simpleTableContentParser, rangeTableContentParser) {

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

    private fun parseTableBlock(header: String, contents: List<String>) = contents.takeWhile { line -> !isHeader(line) }.let { tableContents ->
        Companion.TableBlock(
            parseTable(header, tableContents),
            tableContents.size
        )
    }
}