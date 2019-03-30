package dev.christianbroomfield.d100.parsers.block

import dev.christianbroomfield.d100.model.Table
import dev.christianbroomfield.d100.model.TableHeader
import dev.christianbroomfield.d100.model.TableResults

/**
 * Parses a file using both the {@see StructuredTableBlockParser} and {@see WhiteSpaceDelimitedTableBlockParser} and combines
 * the results into a single table list.
 */
class MixedTableBlockParser(
    private val structuredTableBlockParser: StructuredTableBlockParser,
    private val laxStructuredTableBlockParser: LaxStructuredTableBlockParser,
    private val whiteSpaceDelimitedTableBlockParser: WhiteSpaceDelimitedTableBlockParser
) : TableBlockParser {

    /**
     * @param contents the list of results and a table header
     * @param filename name of the file being parsed
     * @return the parsed table
     */
    override fun parse(contents: List<String>, filename: String): List<Table.DirtyTable> {
        val whiteSpaceTables = when {
            whiteSpaceDelimitedTableBlockParser.canParse(contents) -> whiteSpaceDelimitedTableBlockParser.parse(contents, filename)
            else -> emptyList()
        }

        val structuredTables = when {
            laxStructuredTableBlockParser.canParse(contents) && whiteSpaceTables.isNotEmpty() -> laxStructuredTableBlockParser.parse(contents, filename)
            structuredTableBlockParser.canParse(contents) -> structuredTableBlockParser.parse(contents, filename)
            else -> emptyList()
        }

        return when {
            structuredTables.isNotEmpty() && whiteSpaceTables.isNotEmpty() -> aggregateTables(structuredTables, whiteSpaceTables)
            structuredTables.isNotEmpty() -> structuredTables
            whiteSpaceTables.isNotEmpty() -> whiteSpaceTables
            else -> emptyList()
        }
    }

    /*
     * Some files unfortunately mix structured tables with unstructured tables. Basically, structured tables are those
     * with a die size so that we can safely eliminate whitespace and count table elements. Unstructured tables don't have a die size
     * and we simply count non-blank lines until we reach a blank line or the end of the file.
     *
     * A structured table is considered more trustworthy because it has the die information to tell it how large a table is supposed to be. I can't
     * verify that an unstructured table is supposed to have X results.
     *
     * So when we are dealing with mixed files, we want to only include those whitespace tables that are completely disparate from the structured tables, i.e.
     * neither the header nor the table results are contained in any parsed structured table. If we find that the header or any results are part of a structured table,
     * then I'll assume the whitespace parsing wasn't successful and ignore the table. I can also infer that, if a structured table has a result contained in a whitespace table,
     * that the structured table is correct and contains the table completely.
     */
    private fun aggregateTables(
        structuredTables: List<Table.DirtyTable>,
        whiteSpaceTables: List<Table.DirtyTable>
    ): List<Table.DirtyTable> {
        val eligibleWhiteSpaceTables = determineEligibleWhiteSpaceTables(structuredTables, whiteSpaceTables)
        val unusedStructuredTables = determinedUnusedTables(structuredTables, eligibleWhiteSpaceTables)

        return eligibleWhiteSpaceTables + unusedStructuredTables
    }

    private fun determineEligibleWhiteSpaceTables(
        structuredTables: List<Table.DirtyTable>,
        whiteSpaceTables: List<Table.DirtyTable>
    ): List<Table.DirtyTable> {
        return whiteSpaceTables.mapNotNull { table ->
            when {
                isStructuredHeader(table.header) -> getIdenticalStructuredTable(structuredTables, table)
                isCompletelySeparateTable(structuredTables, table) -> table
                else -> null
            }
        }
    }

    private fun isStructuredHeader(tableHeader: TableHeader): Boolean {
        return structuredTableBlockParser.canParse(listOf(tableHeader.descriptor))
    }

    private fun isCompletelySeparateTable(structuredTables: List<Table.DirtyTable>, table: Table.DirtyTable): Boolean {
        val isNotAnEmptyTable = table.header.dieSize > 1
        val isUniqueTable = isAGenuineHeader(structuredTables, table.header) && hasUniqueResults(structuredTables, table.results)

        return isNotAnEmptyTable && isUniqueTable
    }

    private fun isAGenuineHeader(structuredTables: List<Table.DirtyTable>, tableHeader: TableHeader): Boolean {
        val headerIsUnique = structuredTables.map { it.header }.none { tableHeader.descriptor.contains(it.descriptor) }
        val headerIsNotAResult = structuredTables.map { it.results }.none { it.contains(tableHeader.descriptor) }

        return headerIsUnique && headerIsNotAResult
    }

    private fun hasUniqueResults(structuredTables: List<Table.DirtyTable>, tableResults: TableResults): Boolean {
        return structuredTables.none { it.results.any(tableResults::contains) }
    }

    private fun determinedUnusedTables(
        structuredTables: List<Table.DirtyTable>,
        eligibleWhiteSpaceTables: List<Table.DirtyTable>
    ): List<Table.DirtyTable> {
        return structuredTables.filter { isCompletelySeparateTable(eligibleWhiteSpaceTables, it) }
    }

    private fun getIdenticalStructuredTable(
        structuredTables: List<Table.DirtyTable>,
        table: Table.DirtyTable
    ): Table.DirtyTable? {
        return structuredTables.find { table.header.descriptor.contains(it.header.descriptor) }
    }

    override fun canParse(contents: List<String>): Boolean {
        return structuredTableBlockParser.canParse(contents) || whiteSpaceDelimitedTableBlockParser.canParse(contents)
    }
}