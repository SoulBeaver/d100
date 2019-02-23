package com.christian.dnd.d100

import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.parsers.block.TableBlockParser
import java.io.File

class D100TableParser(private val tableBlockParsers: List<TableBlockParser>) {
    fun parse(d100Table: File): List<Table> {
        val filename = d100Table.nameWithoutExtension
        val contents = d100Table.readLines()
            .asSequence()
            .map { line -> line.replace("\u200B", "") }
            .filter(String::isNotBlank)
            .toList()

        val tableBlockParser = tableBlockParsers.first { parser -> parser.canParse(contents) }
        return tableBlockParser.parse(contents, filename)
    }
}
