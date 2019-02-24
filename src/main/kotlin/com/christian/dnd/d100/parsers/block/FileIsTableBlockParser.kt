package com.christian.dnd.d100.parsers.block

import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.model.TableHeader
import com.christian.dnd.d100.model.TableResults
import com.christian.dnd.d100.parsers.content.TableContentParser
import com.christian.dnd.d100.parsers.header.TableHeaderParser

/**
 * The FileIsTableBlockParser considers the given file and its entire contents to be one single table.
 */
class FileIsTableBlockParser(
    private val simpleTableContentParser: TableContentParser,
    private val rangeTableContentParser: TableContentParser,
    private val tableHeaderParsers: List<TableHeaderParser>
) : TableBlockParser {

    override fun parse(contents: List<String>, filename: String): List<Table.DirtyTable> {
        val tableHeader = parseTableHeader("d${contents.size} $filename")
        return listOf(parseTable(tableHeader, contents))
    }

    private fun parseTableHeader(header: String) =
        tableHeaderParsers.first { parser -> parser.isHeader(header) }.parse(header)

    private fun parseTable(tableHeader: TableHeader, tableContents: TableResults): Table.DirtyTable {
        val results = when (tableHeader.dieSize) {
            tableContents.size -> simpleTableContentParser.parse(tableContents)
            else -> rangeTableContentParser.parse(tableContents)
        }

        return Table.DirtyTable(tableHeader, results)
    }

    override fun canParse(contents: List<String>) = true
}