package com.christian.dnd.d100

import com.christian.dnd.d100.parsers.block.FileIsTableBlockParser
import com.christian.dnd.d100.parsers.block.StructuredTableBlockParser
import java.io.File

class D100TableParser(
    private val structuredTableBlockParser: StructuredTableBlockParser,
    private val fileIsTableBlockParser: FileIsTableBlockParser
) {
    fun parse(d100Table: File): List<Table> {
        val filename = d100Table.nameWithoutExtension
        val contents = d100Table.readLines()
            .map { line -> line.replace("\u200B", "") }
            .filter(String::isNotBlank)

        return when {
            contents.any(HEADER_REGEX::matches) -> structuredTableBlockParser.parse(contents, filename)
            else -> fileIsTableBlockParser.parse(contents, filename)
        }
    }

    companion object {
        val HEADER_REGEX = """(\d*)d(\d+|%)(.*)""".toRegex(RegexOption.IGNORE_CASE)
    }
}