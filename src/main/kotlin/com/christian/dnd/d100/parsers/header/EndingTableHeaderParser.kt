package com.christian.dnd.d100.parsers.header

import com.christian.dnd.d100.model.TableHeader

class EndingTableHeaderParser: TableHeaderParser("""(.*?)\(?(\d*)d(\d+|%)\)?:?\s*""".toRegex(RegexOption.IGNORE_CASE)) {
    override fun parse(header: String): TableHeader {
        return headerRegex.find(header)!!.groupValues.let {
            val rollsRequired = if (it[2].isBlank()) 1 else it[2].toInt()
            val dieSize = if (it[3] == "%") 100 else it[3].toInt()
            val descriptor = cleanDescriptor(it[1])

            TableHeader(rollsRequired, dieSize, descriptor)
        }
    }

    override fun cleanDescriptor(descriptor: String): String {
        return super.cleanDescriptor(descriptor)
            .removeSuffix(")")
            .removeSuffix("(")
            .removeSuffix("-")
            .removeSuffix(":")
            .trim()
    }
}