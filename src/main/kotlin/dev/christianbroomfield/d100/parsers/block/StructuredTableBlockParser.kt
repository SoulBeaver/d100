package dev.christianbroomfield.d100.parsers.block

import dev.christianbroomfield.d100.model.RollBehavior
import dev.christianbroomfield.d100.model.Table
import dev.christianbroomfield.d100.model.TableHeader
import dev.christianbroomfield.d100.model.TableResults
import dev.christianbroomfield.d100.parsers.content.TableContentParser
import dev.christianbroomfield.d100.parsers.header.TableHeaderParser
import dev.christianbroomfield.d100.utils.takeWhileUpToMax

private data class TableHeaderBlock(val tableHeader: TableHeader, val tableBlockStart: Int, val linesRead: Int, val headerLine: String)
private data class TableBlock(val table: Table.DirtyTable, val linesRead: Int)

/**
 * Assumes that the file has a structure wherein one or more tables will have a descriptor and a list of die roll results.
 *
 * Example:
 *
 * d% Roll --- Armor Type
 * 01-08 Breastplate
 * 09-18 Chain mail
 *
 * The table is considered structured because it contains a header specifying the number of results (d%).
 */
class StructuredTableBlockParser(
    private val simpleTableContentParser: TableContentParser,
    private val rangeTableContentParser: TableContentParser,
    private val tableHeaderParsers: List<TableHeaderParser>
) : TableBlockParser {

    /**
     * @param contents the list of results and a table header
     * @param filename name of the file being parsed
     * @return the parsed table
     */
    override fun parse(contents: List<String>, filename: String): List<Table.DirtyTable> =
        parseRecursively(contents.filter(String::isNotBlank), filename)

    // We are intentionally not tailrecursive because of a kotlin compiler error in 1.3.X (https://youtrack.jetbrains.com/issue/KT-14961)
    private fun parseRecursively(
        contents: List<String>,
        filename: String,
        tablesAcc: List<Table.DirtyTable> = emptyList()
    ): List<Table.DirtyTable> {
        return parseTableHeaderBlock(contents)?.let { tableHeaderBlock ->
            val (tableHeader, tableBlockStart, _, headerLine) = tableHeaderBlock

            val tableBlock = parseTableBlock(
                tableHeader,
                contents.subList(contents.indexOf(headerLine) + 1, contents.size)
            )

            parseRecursively(
                contents.subList(tableBlockStart + tableBlock.linesRead + tableHeaderBlock.linesRead, contents.size),
                filename,
                tablesAcc + tableBlock.table
            )
        } ?: tablesAcc
    }

    override fun canParse(contents: List<String>) = contents.any { line -> isHeader(line) }

    private fun parseTableBlock(header: TableHeader, contents: List<String>): TableBlock {
        return contents.takeWhileUpToMax(header.dieSize * header.rollsRequired) { line -> !isHeader(line) }
            .let { tableContents -> TableBlock(parseTable(header, tableContents), tableContents.size) }
    }

    private fun parseTable(tableHeader: TableHeader, tableContents: TableResults): Table.DirtyTable {
        val results = when (tableHeader.dieSize) {
            tableContents.size -> simpleTableContentParser.parse(tableContents)
            else -> rangeTableContentParser.parse(tableContents)
        }

        return Table.DirtyTable(tableHeader, results, rollBehavior = if (tableHeader.dieSize == results.size) RollBehavior.REPEAT else RollBehavior.ADD)
    }

    private fun parseTableHeaderBlock(contents: List<String>): TableHeaderBlock? {
        return contents.firstOrNull { line -> isHeader(line) }
            ?.let { headerLine ->
                when {
                    parseTableHeader(headerLine).descriptor.isBlank() -> {
                        val displacedHeaderElements = contents.takeWhile { line -> !isHeader(line) }
                        val joinedHeaderLine = "$headerLine ${displacedHeaderElements.joinToString()}"

                        TableHeaderBlock(
                            parseTableHeader(joinedHeaderLine),
                            contents.indexOf(displacedHeaderElements.first()),
                            displacedHeaderElements.size + 1,
                            headerLine
                        )
                    }
                    else -> TableHeaderBlock(parseTableHeader(headerLine), contents.indexOf(headerLine), 1, headerLine)
                }
            }
    }

    private fun isHeader(line: String) = tableHeaderParsers.any { parser -> parser.isHeader(line) }

    private fun parseTableHeader(header: String) =
        tableHeaderParsers.first { parser -> parser.isHeader(header) }.parse(header)
}