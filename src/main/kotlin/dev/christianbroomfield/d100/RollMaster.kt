package dev.christianbroomfield.d100

import dev.christianbroomfield.d100.model.RollBehavior
import dev.christianbroomfield.d100.model.Table
import kotlin.random.Random

class RollMaster(private val random: Random = Random.Default) {

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