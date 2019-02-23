package com.christian.dnd.d100.parsers.block

import com.christian.dnd.d100.parsers.content.TableContentParser
import com.christian.dnd.d100.parsers.header.TableHeaderParser

/**
 * The FileIsTableBlockParser considers the given file and its entire contents to be one single table.
 */
class FileIsTableBlockParser(
    simpleTableContentParser: TableContentParser,
    rangeTableContentParser: TableContentParser,
    tableHeaderParsers: List<TableHeaderParser>
): TableBlockParser(simpleTableContentParser, rangeTableContentParser, tableHeaderParsers) {

    override fun parse(contents: List<String>, filename: String) =
        listOf(parseTable("d${contents.size} $filename", contents))

    override fun canParse(contents: List<String>) = true
}