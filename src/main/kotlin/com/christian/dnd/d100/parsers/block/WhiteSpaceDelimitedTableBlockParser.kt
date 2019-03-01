package com.christian.dnd.d100.parsers.block

import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.model.TableHeader

class WhiteSpaceDelimitedTableBlockParser : TableBlockParser {
    override fun parse(contents: List<String>, filename: String): List<Table.DirtyTable> {
        return parseRecursively(trimContents(contents), filename)
    }

    private tailrec fun parseRecursively(contents: List<String>, filename: String, tablesAcc: List<Table.DirtyTable> = emptyList()): List<Table.DirtyTable> {
        val tableBlock = contents.takeWhile { it.isNotBlank() }

        val tableHeader = TableHeader(1, tableBlock.size - 1, tableBlock.first())
        val table = Table.DirtyTable(tableHeader, tableBlock.drop(1))

        return if (tableBlock.size == contents.size)
            tablesAcc + table
        else
            parseRecursively(contents.subList(tableBlock.size + 1, contents.size), filename, tablesAcc + table)
    }

    override fun canParse(contents: List<String>): Boolean {
        val tablesFound = trimContents(contents).count { it.isBlank() } + 1

        return tablesFound > 1
    }

    private fun trimContents(contents: List<String>): List<String> {
        var trimmedContents = contents.dropWhile(String::isBlank)
        while (trimmedContents.last().isBlank()) {
            trimmedContents = trimmedContents.subList(0, trimmedContents.size - 1)
        }

        return trimmedContents
    }
}