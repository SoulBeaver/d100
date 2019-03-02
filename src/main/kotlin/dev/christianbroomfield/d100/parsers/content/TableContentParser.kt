package dev.christianbroomfield.d100.parsers.content

/**
 * Parses the contents of a table and returns a list of table results if possible.
 */
interface TableContentParser {
    /**
     * @param tableContents the contents to be parsed
     * @return a list of table results
     */
    fun parse(tableContents: List<String>): List<String>
}