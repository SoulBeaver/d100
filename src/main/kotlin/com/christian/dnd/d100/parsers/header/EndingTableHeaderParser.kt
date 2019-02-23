package com.christian.dnd.d100.parsers.header

import com.christian.dnd.d100.model.TableHeader

/**
 * Attempts to parse a table header (the bit with the flavor text and die information) where the expected dice roll
 * is at the end of the table, e.g. "You have reached the... (d20)"
 */
class EndingTableHeaderParser: TableHeaderParser("""(.*?)\(?(\d*)d(\d+|%)\)?:?\s*""".toRegex(RegexOption.IGNORE_CASE)) {
    override fun parse(header: String): TableHeader {
        return headerRegex.find(header)!!.groupValues.let {
            val rollsRequired = if (it[2].isBlank()) 1 else it[2].toInt()
            val dieSize = if (it[3] == "%") 100 else it[3].toInt()
            val descriptor = it[1]

            TableHeader(rollsRequired, dieSize, descriptor)
        }
    }
}