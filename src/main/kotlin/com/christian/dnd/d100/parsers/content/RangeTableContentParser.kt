package com.christian.dnd.d100.parsers.content

import com.christian.dnd.d100.model.TableResults

/**
 * Transforms a table's die ranges into a table that has one entry up to its die size.
 *
 * Input:
 *
 * d10 Roll --- Armor Type
 * 01-03 Breastplate
 * 04-06 Chain mail
 * 07-10 Chain shirt
 *
 * Output:
 *
 * Breastplate
 * Breastplate
 * Breastplate
 * Chain mail
 * Chain mail
 * Chain mail
 * Chain shirt
 * Chain shirt
 * Chain shirt
 * Chain shirt
 */
class RangeTableContentParser: TableContentParser {
    private val rangeRegex = """(\d+)\s*[-.]+\s*(\d+)(.*)""".toRegex(RegexOption.IGNORE_CASE)

    override fun parse(tableContents: List<String>): TableResults {
        return tableContents.flatMap { line ->
            val matches = rangeRegex.find(line)!!.groupValues

            val rangeStart = matches[1].toInt()
            val rangeEnd = matches[2].toInt()
            val result = matches[3]

            (rangeStart..rangeEnd).map { result }
        }
    }
}