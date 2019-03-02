package com.christian.dnd.d100

import com.christian.dnd.d100.model.Table
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
            val (rollsRequired, dieSize, descriptor) = table.header

            Array(rollsRequired) {
                table.results[random.nextInt(dieSize)]
            }.map { rollResult ->
                formatter(descriptor, rollResult)
            }
        }
    }
}