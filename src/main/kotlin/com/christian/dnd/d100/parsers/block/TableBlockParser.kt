package com.christian.dnd.d100.parsers.block

import com.christian.dnd.d100.D100TableParser.Companion.HEADER_REGEX
import com.christian.dnd.d100.Table
import com.christian.dnd.d100.parsers.content.TableContentParser

abstract class TableBlockParser(
    private val simpleTableContentParser: TableContentParser,
    private val rangeTableContentParser: TableContentParser
) {

    abstract fun parse(contents: List<String>, filename: String): List<Table>

    protected fun isHeader(line: String) = HEADER_REGEX.matches(line)

    protected fun parseTable(header: String, tableContents: List<String>) = HEADER_REGEX.find(header)!!.groupValues.let {
        val rollsRequired = if (it[1].isBlank()) 1 else it[1].toInt()
        val dieSize = if (it[2] == "%") 100 else it[2].toInt()
        val descriptor = cleanDescriptor(it[3])

        val results = parseResults(tableContents, dieSize)

        Table(descriptor, dieSize, results, rollsRequired)
    }

    private fun parseResults(tableContents: List<String>, dieSize: Int) = when (dieSize) {
        tableContents.size -> simpleTableContentParser.parse(tableContents)
        else -> rangeTableContentParser.parse(tableContents)
    }

    private fun cleanDescriptor(descriptor: String) = descriptor
        .trim()
        .replace("...", "")
        .replace("…", "")
        .removePrefix("\t")
        .removePrefix("|")
        .trim()

    companion object {
        protected data class TableBlock(val table: Table, val linesRead: Int)
    }
}