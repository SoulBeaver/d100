package com.christian.dnd.d100.parsers

class RangeTableParser: TableParser {

    private val rangeRegex = """(\d+)\s*-\s*(\d+)(.*)""".toRegex(RegexOption.IGNORE_CASE)

    override fun parse(tableContents: List<String>): List<String> {
        val results = mutableListOf<String>()

        for (line in tableContents) {
            val matches = rangeRegex.find(line)!!.groupValues

            val rangeStart = matches[1].toInt()
            val rangeEnd = matches[2].toInt()
            val result = clean(matches[3])

            (rangeStart..rangeEnd).forEach { _ -> results.add(result) }
        }

        return results
    }

    private fun clean(result: String) = result
        .trim()
        .removePrefix("\t")
        .removeSuffix("\t")
}