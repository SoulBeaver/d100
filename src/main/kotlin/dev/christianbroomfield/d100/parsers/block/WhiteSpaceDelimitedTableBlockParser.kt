package dev.christianbroomfield.d100.parsers.block

import dev.christianbroomfield.d100.model.Table
import dev.christianbroomfield.d100.model.TableHeader

/**
 * Parses a file wherein all tables are separated by whitespace.
 *
 * E.g.:
 *
 * Dominant race:
 * Aett-raths (Goblins)
 * Huldur (Elves)
 * Raths (Dwarves)
 *
 * The village is located...
 * On an ancient dragonwork stone-bridge
 * On a network of lianas
 */
class WhiteSpaceDelimitedTableBlockParser : TableBlockParser {
    /**
     * @param contents the list of results and a table header
     * @param filename name of the file being parsed
     * @return the parsed table
     */
    override fun parse(contents: List<String>, filename: String): List<Table.DirtyTable> {
        return parseRecursively(trimLeadingAndTrailingBlankLines(contents), filename)
    }

    private tailrec fun parseRecursively(contents: List<String>, filename: String, tablesAcc: List<Table.DirtyTable> = emptyList()): List<Table.DirtyTable> {
        val tableBlock = contents.takeWhile { it.isNotBlank() }
        if (tableBlock.isEmpty()) {
            return parseRecursively(contents.subList(1, contents.size), filename, tablesAcc)
        }

        val tableHeader = TableHeader(1, tableBlock.size - 1, tableBlock.first())
        val table = Table.DirtyTable(tableHeader, tableBlock.drop(1))

        return when {
            tableBlock.size == contents.size -> addNonEmptyTable(tablesAcc, table)
            else -> parseRecursively(contents.subList(tableBlock.size + 1, contents.size), filename, addNonEmptyTable(tablesAcc, table))
        }
    }

    private fun addNonEmptyTable(tables: List<Table.DirtyTable>, tableToAdd: Table.DirtyTable): List<Table.DirtyTable> {
        return when {
            tableToAdd.header.dieSize > 1 -> tables + tableToAdd
            else -> tables
        }
    }

    override fun canParse(contents: List<String>): Boolean {
        val tables = parse(contents, "")

        return tables.size > 1
    }

    private fun trimLeadingAndTrailingBlankLines(contents: List<String>): List<String> {
        return contents.dropWhile(String::isBlank).dropLastWhile(String::isBlank)
    }
}