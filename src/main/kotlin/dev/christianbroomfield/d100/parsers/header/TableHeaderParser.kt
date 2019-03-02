package dev.christianbroomfield.d100.parsers.header

import dev.christianbroomfield.d100.model.TableHeader

abstract class TableHeaderParser(protected val headerRegex: Regex) {
    abstract fun parse(header: String): TableHeader

    fun isHeader(line: String) = headerRegex.matches(line)
}
