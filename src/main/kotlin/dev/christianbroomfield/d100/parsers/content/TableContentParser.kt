package dev.christianbroomfield.d100.parsers.content

interface TableContentParser {
    fun parse(tableContents: List<String>): List<String>
}