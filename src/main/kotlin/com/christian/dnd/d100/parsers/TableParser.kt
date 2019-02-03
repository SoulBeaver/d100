package com.christian.dnd.d100.parsers

interface TableParser {
    fun parse(tableContents: List<String>): List<String>
}