package com.christian.dnd.d100.parsers.content

interface TableContentParser {
    fun parse(tableContents: List<String>): List<String>
}