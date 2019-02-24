package com.christian.dnd.d100.parsers.content

import com.christian.dnd.d100.model.TableResults

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
    override fun parse(tableContents: List<String>): TableResults {
        return tableContents
    }
}