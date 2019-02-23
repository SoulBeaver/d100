package com.christian.dnd.d100.parsers.block

import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.model.TableHeader
import com.christian.dnd.d100.parsers.content.TableContentParser
import com.christian.dnd.d100.parsers.header.TableHeaderParser
import com.christian.dnd.d100.utils.takeWhileUpToMax

/**
 * Assumes that the file has a structure wherein one or more tables will have a descriptor and a list of die roll results.
 */
class StructuredTableBlockParser(
    simpleTableContentParser: TableContentParser,
    rangeTableContentParser: TableContentParser,
    tableHeaderParsers: List<TableHeaderParser>
) : TableBlockParser(simpleTableContentParser, rangeTableContentParser, tableHeaderParsers) {

    override fun parse(contents: List<String>, filename: String): List<Table.DirtyTable> {
        return parseRecursively(contents, filename)
    }

    // We are intentionally not tailrecursive because of a kotlin compiler error in 1.3.X (https://youtrack.jetbrains.com/issue/KT-14961)
    private fun parseRecursively(contents: List<String>, filename: String, tablesAcc: List<Table.DirtyTable> = emptyList()): List<Table.DirtyTable> {
        return parseTableHeaderBlock(contents)?.let { tableHeaderBlock ->
            val (tableHeader, _, headerLine) = tableHeaderBlock

            val tableBlock = parseTableBlock(
                tableHeader,
                contents.subList(contents.indexOf(headerLine) + 1, contents.size)
            )

            parseRecursively(
                contents.subList(tableBlock.linesRead + tableHeaderBlock.linesRead , contents.size),
                filename,
                tablesAcc + tableBlock.table)
        } ?: tablesAcc
    }

    override fun canParse(contents: List<String>) = contents.any { line -> isHeader(line) }

    private fun parseTableBlock(header: TableHeader, contents: List<String>): TableBlock {
        return contents.takeWhileUpToMax(header.dieSize) { line -> !isHeader(line) }
            .let { tableContents ->
                TableBlock(parseTable(header, tableContents), tableContents.size)
            }
    }

    private fun parseTableHeaderBlock(contents: List<String>): TableHeaderBlock? {
        return contents.firstOrNull { line -> isHeader(line) }
            ?.let { headerLine ->
                return when {
                    parseTableHeader(headerLine).descriptor.isBlank() -> {
                        val displacedHeaderElements = contents.takeWhile { line -> !isHeader(line) }
                        val joinedHeaderLine = "$headerLine ${displacedHeaderElements.joinToString()}"

                        TableHeaderBlock(parseTableHeader(joinedHeaderLine), displacedHeaderElements.size + 1, headerLine)
                    }
                    else -> TableHeaderBlock(parseTableHeader(headerLine), 1, headerLine)
                }
            }
    }
}

private data class TableHeaderBlock(val tableHeader: TableHeader, val linesRead: Int, val headerLine: String)
private data class TableBlock(val table: Table.DirtyTable, val linesRead: Int)