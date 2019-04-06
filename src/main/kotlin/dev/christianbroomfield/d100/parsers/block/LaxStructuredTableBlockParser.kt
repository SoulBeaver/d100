package dev.christianbroomfield.d100.parsers.block

import dev.christianbroomfield.d100.parsers.content.TableContentParser
import dev.christianbroomfield.d100.parsers.header.TableHeaderParser

class LaxStructuredTableBlockParser(
    simpleTableContentParser: TableContentParser,
    rangeTableContentParser: TableContentParser,
    tableHeaderParsers: List<TableHeaderParser>
) : StructuredTableBlockParser(simpleTableContentParser, rangeTableContentParser, tableHeaderParsers) {

    override fun isValidTableBlock(headerBlock: TableHeaderBlock, contents: List<String>) = true
}