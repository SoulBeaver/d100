package dev.christianbroomfield.d100.parsers.content

import dev.christianbroomfield.d100.model.TableResults

/**
 * Parses a table as-is, considers each line to be a separate entry on the die result table.
 *
 * Input:
 * 1	a wren.
 * 2	a moth.
 * 3	a swallow.
 * 4	a bumblebee.
 * 5	a flying fish.
 * 6    	a dragonfly.
 *
 * Output:
 * 1	a wren.
 * 2	a moth.
 * 3	a swallow.
 * 4	a bumblebee.
 * 5	a flying fish.
 * 6    	a dragonfly.
 */
class SimpleTableContentParser : TableContentParser {
    /**
     * @param tableContents the contents to be parsed
     * @return a list of table results
     */
    override fun parse(tableContents: List<String>): TableResults {
        return tableContents
    }
}