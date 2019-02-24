package com.christian.dnd.d100.parsers.block

import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.model.TableHeader

class WideTableBlockParser : TableBlockParser {

    private val diceHeaderExpression = """^\d*d\d+.*""".toRegex(RegexOption.IGNORE_CASE)
    private val wideTableDelimiters = arrayOf("\t", ",", "|", ";")

    override fun parse(contents: List<String>, filename: String): List<Table.DirtyTable> {
        /*
         * Contents:
         *   listOf(
         *        "d12	Meat	Bread	Pickled Vegetable	Nuts / Seeds	Dried Fruit	Boil and Serve",
         *        "1.	Beef jerky	Beer bread	Asparagus	Cashews	Apples	Beets"
         *   )
         *
         * Matrix:
         *   listOf(
         *        listOf("d12", "Meat", "Bread", "Pickled Vegetable", "Nuts / Seeds", "Dried Fruit", "Boil and Serve"),
         *        listOf("1.", "Beef jerky", "Beer bread", "Asparagus", "Cashews", "Apples", "Beets"
         *   )
         *
         * TableColumns:
         *   arrayOf(
         *     listOf("d12", "1.", ...)
         *     listOf("Meat", "Beef jerky", ...)
         *     listOf("Bread", "Beer bread", ...)
         *     listOf("Pickled Vegetable", "Asparagus", ...)
         *     listOf("Nuts / Seeds", "Cashews", ...)
         *     listOf("Dried Fruit", "Apples", ...)
         *     listOf("Boil and Serve", "Beets", ...)
         *   )
         *
         * Each separate list is a table with the first entry being the header. The dieSize is the number of elements (ignore dice column- filter if exists)
         */
        val matrix: List<List<String>> = contents
            .asSequence()
            .filterNot { isSeparatorRow(it) }
            .map { it.split(*wideTableDelimiters) }
            .toList()

        val tableColumns: Array<List<String>> = Array(matrix[0].size) { idx ->
            matrix.map { row -> row[idx] }
        }

        return tableColumns
            .asSequence()
            .filter { column -> !isDieRollColumn(column) }
            .map { column: List<String> ->
                val descriptor = column[0]
                val tableResults = column.drop(1)

                val tableHeader = TableHeader(1, tableResults.size, "$descriptor is")
                Table.DirtyTable(tableHeader, tableResults)
            }
            .toList()
    }

    override fun canParse(contents: List<String>): Boolean {
        // Given a list of tables in a column format (think csv but not as nicely structured)
        val matrix: List<List<String>> = contents
            .asSequence()
            .filterNot { isSeparatorRow(it) }
            .map { it.split(*wideTableDelimiters) }
            .toList()

        // Grab the first row which "should" be the header row (not guaranteed but whatever)
        val headerRow = matrix.first()
        if (headerRow.size == 1) {
            return false
        }

        // Transform the first column into a list of (header, value1, value2, ...)
        val firstColumn: List<String> = matrix.map { it[0] }
        /* If the table has a Dice column we will ignore it when counting the wide table, e.g.
         * D20  Results
         * 1    Foo
         * 2    Bar
         * ...  ...
         */
        val columnSize = when {
            isDieRollColumn(firstColumn) -> headerRow.size - 1
            else -> headerRow.size
        }

        // If we can find the same number of columns in every row, then we're a wide table!
        // Note that this still works for tables with empty columns as long as they keep the delimiter.
        return columnSize > 1
                && matrix.all { row -> row.size == headerRow.size }
    }

    private fun isDieRollColumn(column: List<String>): Boolean {
        val header = column[0]
        return diceHeaderExpression.matches(header)
    }

    private fun isSeparatorRow(row: String): Boolean {
        return row.none { it.isLetterOrDigit() }
    }
}