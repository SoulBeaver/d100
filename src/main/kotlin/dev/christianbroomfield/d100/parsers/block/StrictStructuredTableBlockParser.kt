package dev.christianbroomfield.d100.parsers.block

import dev.christianbroomfield.d100.parsers.content.TableContentParser
import dev.christianbroomfield.d100.parsers.header.TableHeaderParser
import dev.christianbroomfield.d100.utils.takeWhileUpToMax

class StrictStructuredTableBlockParser(
    simpleTableContentParser: TableContentParser,
    rangeTableContentParser: TableContentParser,
    tableHeaderParsers: List<TableHeaderParser>
) : StructuredTableBlockParser(simpleTableContentParser, rangeTableContentParser, tableHeaderParsers) {

    override fun isValidTableBlock(headerBlock: TableHeaderBlock, contents: List<String>): Boolean {
        val (header) = headerBlock
        val maxResultsPossible = header.dieSize * header.rollsRequired
        val resultsRead = contents.takeWhileUpToMax(maxResultsPossible) { line -> !isHeader(line) }

        return headerBlock.linesRead > 1 ||
                resultsRead.size < maxResultsPossible ||
                resultsRead.size == contents.size ||
                isHeader(contents[maxResultsPossible])
    }
}