package com.christian.dnd.d100.parsers.block

import com.christian.dnd.d100.parsers.content.TableContentParser

class FileIsTableBlockParser(
    simpleTableContentParser: TableContentParser,
    rangeTableContentParser: TableContentParser
): TableBlockParser(simpleTableContentParser, rangeTableContentParser) {

    override fun parse(contents: List<String>, filename: String) =
        listOf(parseTable("d${contents.size} $filename", contents))
}