package com.christian.dnd.d100.cleaner

import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.model.TableHeader
import com.christian.dnd.d100.utils.removeAllPrefixesAndSuffixesOf

class TableCleaner {
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
                    .trim()
            }.toList()
    }
}