package com.christian.dnd.d100.parsers.block

import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.model.TableHeader

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

        return if (tableBlock.size == contents.size)
            tablesAcc + table
        else
            parseRecursively(contents.subList(tableBlock.size + 1, contents.size), filename, tablesAcc + table)
    }

    override fun canParse(contents: List<String>): Boolean {
        val tablesFound = trimLeadingAndTrailingBlankLines(contents).count { it.isBlank() } + 1

        return tablesFound > 1
    }

    private fun trimLeadingAndTrailingBlankLines(contents: List<String>): List<String> {
        return contents.dropWhile(String::isBlank).dropLastWhile(String::isBlank)
    }
}