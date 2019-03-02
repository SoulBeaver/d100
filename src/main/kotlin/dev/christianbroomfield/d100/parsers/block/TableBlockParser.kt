package dev.christianbroomfield.d100.parsers.block

import dev.christianbroomfield.d100.model.Table

interface TableBlockParser {

    fun parse(contents: List<String>, filename: String): List<Table.DirtyTable>

    fun canParse(contents: List<String>): Boolean
}