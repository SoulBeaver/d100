package com.christian.dnd.d100.parsers.block

import com.christian.dnd.d100.model.Table

interface TableBlockParser {

    fun parse(contents: List<String>, filename: String): List<Table.DirtyTable>

    fun canParse(contents: List<String>): Boolean
}