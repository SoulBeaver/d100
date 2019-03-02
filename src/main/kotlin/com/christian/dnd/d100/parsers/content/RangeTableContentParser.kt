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
class RangeTableContentParser : TableContentParser {
    private val singleValueRegex = """(\d+)\s+(.*)""".toRegex(RegexOption.IGNORE_CASE)
    private val rangeRegex = """(\d+)\s*[-.]+\s*(\d+)(.*)""".toRegex(RegexOption.IGNORE_CASE)

    override fun parse(tableContents: List<String>): TableResults {
        return tableContents.flatMap { line ->
            when {
                rangeRegex.find(line) != null -> rangeList(rangeRegex.find(line)!!)
                singleValueRegex.find(line) != null -> singleValueList(singleValueRegex.find(line)!!)
                else -> emptyList()
            }
        }
    }

    private fun rangeList(match: MatchResult): List<String> {
        val matches = match.groupValues

        val rangeStart = matches[1].toInt()
        val rangeEnd = (if (matches[2] == "00") "100" else matches[2]) .toInt()
        val result = matches[3]

        return (rangeStart..rangeEnd).map { result }
    }

    private fun singleValueList(match: MatchResult): List<String> {
        val matches = match.groupValues

        val value = matches[1].toInt()
        val result = matches[2]

        return listOf(result)
    }
}