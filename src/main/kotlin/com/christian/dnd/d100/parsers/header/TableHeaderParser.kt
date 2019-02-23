package com.christian.dnd.d100.parsers.header

import com.christian.dnd.d100.model.TableHeader

abstract class TableHeaderParser(protected val headerRegex: Regex) {
    abstract fun parse(header: String): TableHeader

    fun isHeader(line: String) = headerRegex.matches(line)
}
