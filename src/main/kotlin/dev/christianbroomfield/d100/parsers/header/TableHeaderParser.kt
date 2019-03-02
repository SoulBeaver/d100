package dev.christianbroomfield.d100.parsers.header

import dev.christianbroomfield.d100.model.TableHeader

/**
 * Parses a {@see TableHeader} from a header using the given regex, if possible.
 *
 * @property headerRegex the regex with which to validate and parse a header
 */
abstract class TableHeaderParser(protected val headerRegex: Regex) {
    /**
     * Parses the header, failing if not able.
     *
     * @param header the header to be parsed
     * @return a {@see TableHeader}
     */
    abstract fun parse(header: String): TableHeader

    /**
     * Determines if the TableHeaderParser implementation is capable of parsing the given header.
     */
    fun isHeader(line: String) = headerRegex.matches(line)
}
