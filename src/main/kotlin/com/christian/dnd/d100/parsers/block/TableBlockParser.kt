package com.christian.dnd.d100.parsers.block

import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.parsers.content.TableContentParser
import com.christian.dnd.d100.parsers.header.TableHeaderParser

abstract class TableBlockParser(
    private val simpleTableContentParser: TableContentParser,
    private val rangeTableContentParser: TableContentParser,
    private val tableHeaderParsers: List<TableHeaderParser>
) {
    abstract fun parse(contents: List<String>, filename: String): List<Table>

    abstract fun canParse(contents: List<String>): Boolean

    protected fun isHeader(line: String) = tableHeaderParsers.any { parser -> parser.isHeader(line) }

    protected fun parseTable(header: String, tableContents: List<String>): Table {
        val tableHeader = tableHeaderParsers
            .first { parser -> parser.isHeader(header) }
            .parse(header)

        val results = when (tableHeader.dieSize) {
            tableContents.size -> simpleTableContentParser.parse(tableContents)
            else -> rangeTableContentParser.parse(tableContents)
        }

        return Table(tableHeader, results)
    }
}