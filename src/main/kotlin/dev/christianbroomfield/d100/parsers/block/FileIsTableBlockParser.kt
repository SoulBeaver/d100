package dev.christianbroomfield.d100.parsers.block

import dev.christianbroomfield.d100.model.Table
import dev.christianbroomfield.d100.model.TableHeader
import dev.christianbroomfield.d100.model.TableResults
import dev.christianbroomfield.d100.parsers.content.TableContentParser
import dev.christianbroomfield.d100.parsers.header.TableHeaderParser

/**
 * The FileIsTableBlockParser considers the given file and its entire contents to be one single table.
 */
class FileIsTableBlockParser(
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
        contents.filter(String::isNotBlank).let { filteredContents ->
            val tableHeader = parseTableHeader("d${filteredContents.size} $filename")
            listOf(parseTable(tableHeader, filteredContents))
        }

    private fun parseTableHeader(header: String) =
        tableHeaderParsers.first { parser -> parser.isHeader(header) }.parse(header)

    private fun parseTable(tableHeader: TableHeader, tableContents: TableResults): Table.DirtyTable {
        val results = when (tableHeader.dieSize) {
            tableContents.size -> simpleTableContentParser.parse(tableContents)
            else -> rangeTableContentParser.parse(tableContents)
        }

        return Table.DirtyTable(tableHeader, results)
    }

    override fun canParse(contents: List<String>) = true
}