package com.christian.dnd.d100.parsers

class SimpleTableParser: TableParser {
    override fun parse(tableContents: List<String>): List<String> {
        return tableContents
            .asSequence()
            .map(String::trim)
            // 1	Is red. Its touch is burning hot. -> Is red. Its touch is burning hot.
            .map { it.replace("\\d+\\t".toRegex(), "") }
            .toList()
    }
}