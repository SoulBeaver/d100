package com.christian.dnd.d100

import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.parsers.block.FileIsTableBlockParser
import com.christian.dnd.d100.parsers.block.StructuredTableBlockParser
import com.christian.dnd.d100.parsers.block.TableBlockParser
import com.christian.dnd.d100.parsers.header.TableHeaderParser
import java.io.File

class D100TableParser(
    private val tableBlockParsers: List<TableBlockParser>
) {
    fun parse(d100Table: File): List<Table> {
        val filename = d100Table.nameWithoutExtension
        val contents = d100Table.readLines()
            .map { line -> line.replace("\u200B", "") }
            .filter(String::isNotBlank)

        val tableBlockParser = tableBlockParsers.first { parser -> parser.canParse(contents) }
        return tableBlockParser.parse(contents, filename)
    }
}
