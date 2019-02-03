package com.christian.dnd.d100

import com.christian.dnd.d100.parsers.TableParser
import java.io.File

class D100TableParser(
    private val simpleTableParser: TableParser,
    private val rangeTableParser: TableParser
) {

    private enum class ParseStrategy {
        REGULAR,
        BLOCK
    }

    private data class TableBlock(val table: Table, val linesRead: Int)

    private val headerRegex = """(\d*)d(\d+|%)(.*)""".toRegex(RegexOption.IGNORE_CASE)

    fun parse(d100Table: File): List<Table> {
        val contents = d100Table.readLines()
            .filter(String::isNotBlank)

        return when (detectParseStrategy(contents)) {
            ParseStrategy.REGULAR -> parseRegularTableContents(contents)
            ParseStrategy.BLOCK -> parseTableBlockFile(contents, d100Table.nameWithoutExtension)
        }
    }

    private fun detectParseStrategy(contents: List<String>): ParseStrategy = if (contents.any(this::isHeader)) ParseStrategy.REGULAR else ParseStrategy.BLOCK

    private fun isHeader(line: String) = headerRegex.matches(line)

    private fun parseRegularTableContents(contents: List<String>): List<Table> {
        val tables = mutableListOf<Table>()

        var i = 0
        while (i < contents.size) {
            val line = contents[i]

            if (isHeader(line)) {
                val tableBlock = parseTableBlock(line, contents.subList(i+1, contents.size))

                tables.add(tableBlock.table)

                i += tableBlock.linesRead
            } else {
                i++
            }
        }

        return tables
    }

    private fun parseTableBlockFile(contents: List<String>, tableName: String): List<Table> {
        return listOf(parseTable("d${contents.size} $tableName", contents))
    }

    private fun parseTableBlock(header: String, contents: List<String>): TableBlock {
        val tableContents = contents.takeWhile { line -> !isHeader(line) }
        val table = parseTable(header, tableContents)

        return TableBlock(table, tableContents.size)
    }

    private fun parseTable(header: String, tableContents: List<String>): Table {
        return headerRegex.find(header)!!.groupValues.let {
            val rollsRequired = if (it[1].isBlank()) 1 else it[1].toInt()
            val dieSize = if (it[2] == "%") 100 else it[2].toInt()
            val descriptor = cleanDescriptor(it[3])

            val results = parseResults(tableContents, dieSize)

            Table(descriptor, dieSize, results, rollsRequired)
        }
    }

    private fun parseResults(tableContents: List<String>, dieSize: Int): List<String> {
        return if (tableContents.size == dieSize) {
            simpleTableParser.parse(tableContents)
        } else {
            rangeTableParser.parse(tableContents)
        }
    }

    private fun cleanDescriptor(descriptor: String): String {
        return descriptor.trim()
            .removePrefix("\t")
            .replace("...", "")
            .replace("…", "")
            .removePrefix("|")
            .trim()
    }
}