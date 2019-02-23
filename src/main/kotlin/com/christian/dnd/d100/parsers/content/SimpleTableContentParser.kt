package com.christian.dnd.d100.parsers.content

import com.christian.dnd.d100.DiceExpressionEvaluator

/**
 * Maps a list of die results to a list with some scrubbing in between.
 */
class SimpleTableContentParser(private val diceExpressionEvaluator: DiceExpressionEvaluator): TableContentParser {

    override fun parse(tableContents: List<String>): List<String> {
        return tableContents
            .asSequence()
            .map(String::trim)
            /*
             * Trims the #-bullet from a result.
             *
             * Input:
             *
             * 1	Is red. Its touch is burning hot.
             *
             * Output:
             *
             * Is red. Its touch is burning hot.
             */
            .map { it.replace("\\d+\\.?\\t".toRegex(), "") }
            .map { diceExpressionEvaluator.evaluate(it) }
            .toList()
    }
}