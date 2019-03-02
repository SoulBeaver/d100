package dev.christianbroomfield.d100.parsers.header

import dev.christianbroomfield.d100.model.TableHeader

/**
 * Attempts to parse a table header (the bit with the flavor text and die information) where the expected dice roll
 * is at the beginning of the table, e.g. "d20 - The ogre is doing..."
 */
class BeginningTableHeaderParser : TableHeaderParser("""\(?(\d*)d(\d+|%)\)?(.*)""".toRegex(RegexOption.IGNORE_CASE)) {

    /**
     * Parses a header from the input string
     *
     * @param header the string to be parsed
     * @return a table header
     */
    override fun parse(header: String): TableHeader {
        return headerRegex.find(header)!!.groupValues.let {
            val rollsRequired = if (it[1].isBlank()) 1 else it[1].toInt()
            val dieSize = if (it[2] == "%") 100 else it[2].toInt()
            val descriptor = it[3]

            TableHeader(rollsRequired, dieSize, descriptor)
        }
    }
}