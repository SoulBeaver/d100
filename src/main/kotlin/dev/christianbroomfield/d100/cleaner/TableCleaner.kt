package dev.christianbroomfield.d100.cleaner

import dev.christianbroomfield.d100.model.Table
import dev.christianbroomfield.d100.model.TableHeader
import dev.christianbroomfield.d100.utils.removeAllPrefixesAndSuffixesOf

/**
 * Removes characters from the table header and results.
 */
class TableCleaner {
    /**
     * Cleans a table by removing trailing or leading characters.
     *
     * @return a cleaned table, one which has been stripped of any unnecessary characters.
     */
    fun clean(table: Table.DirtyTable): Table.CleanedTable {
        val cleanedTabledHeader = cleanTableHeader(table.header)
        val cleanedTableResults = cleanTableResults(table.results)

        return Table.CleanedTable(cleanedTabledHeader, cleanedTableResults, table.rollBehavior)
    }

    private fun cleanTableHeader(tableHeader: TableHeader): TableHeader {
        val cleanedDescriptor = tableHeader.descriptor
            .trim()
            .removeAllPrefixesAndSuffixesOf("()|=-_:;")
            .replace("...", "")
            .replace("…", "")
            .trim()

        return tableHeader.copy(descriptor = cleanedDescriptor)
    }

    private fun cleanTableResults(tableResults: List<String>): List<String> {
        return tableResults
            .asSequence()
            .map { line ->
                line.trim()
                    .replace("...", "")
                    .replace("…", "")
                    /*
                     * Trims the #-bullet from a result.
                     *
                     * Input:
                     * 1	Is red. Its touch is burning hot.
                     * Output:
                     * Is red. Its touch is burning hot.
                     */
                    .replace("""^\d+[.:;\-)\t]\s*""".toRegex(), "")
                    .removePrefix("or less")
                    .removePrefix("or more")
                    .trim()
            }.toList()
    }
}