package com.christian.dnd.d100.parsers.header

import com.christian.dnd.d100.model.TableHeader

class BeginningTableHeaderParser: TableHeaderParser("""\(?(\d*)d(\d+|%)\)?(.*)""".toRegex(RegexOption.IGNORE_CASE)) {

    override fun parse(header: String): TableHeader {
        return headerRegex.find(header)!!.groupValues.let {
            val rollsRequired = if (it[1].isBlank()) 1 else it[1].toInt()
            val dieSize = if (it[2] == "%") 100 else it[2].toInt()
            val descriptor = cleanDescriptor(it[3])

            TableHeader(rollsRequired, dieSize, descriptor)
        }
    }

    override fun cleanDescriptor(descriptor: String): String {
        return super.cleanDescriptor(descriptor)
            .removePrefix(":")
            .removePrefix("(")
            .removePrefix(")")
            .removePrefix("-")
            .trim()
    }
}