package dev.christianbroomfield.d100

import dev.christianbroomfield.d100.model.RollBehavior
import dev.christianbroomfield.d100.model.Table
import kotlin.random.Random

/**
 * Rolls on {@see PreppedTable}s and returns a list of results.
 */
class RollMaster(private val random: Random = Random.Default) {

    /**
     * Rolls on every table and returns a list of results.
     *
     * @param tables the tables to be rolled.
     * @param hideDescriptor hide the descriptor that is part of the table header.
     * @return a list of results
     */
    fun roll(tables: List<Table.PreppedTable>, hideDescriptor: Boolean = false): List<String> {
        return when {
            hideDescriptor -> roll(tables) { _, result ->
                result
            }
            else -> roll(tables) { descriptor, result ->
                "$descriptor $result"
            }
        }
    }

    private fun roll(tables: List<Table.PreppedTable>, formatter: (String, String) -> String): List<String> {

        return tables.flatMap { table ->
            val rollBehavior = table.rollBehavior
            val (rollsRequired, dieSize, descriptor) = table.header

            when (rollBehavior) {
                RollBehavior.REPEAT -> Array(rollsRequired) {
                    table.results[random.nextInt(dieSize)]
                }.map { rollResult ->
                    formatter(descriptor, rollResult)
                }

                RollBehavior.ADD -> {
                    val result = (0 until rollsRequired).sumBy { random.nextInt(dieSize) }
                    listOf(formatter(descriptor, table.results[result]))
                }
            }
        }
    }
}