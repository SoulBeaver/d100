package dev.christianbroomfield.d100.parsers.block

import dev.christianbroomfield.d100.model.Table

/**
 * Accepts a list of contents and constructs a table out of
 */
interface TableBlockParser {

    /**
     * Transforms a block of lines into a {@see DiryTable}
     */
    fun parse(contents: List<String>, filename: String): List<Table.DirtyTable>

    /**
     * Determines if the TableBlockParser implementation is capable of parsing the block.
     */
    fun canParse(contents: List<String>): Boolean
}