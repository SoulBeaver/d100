package com.christian.dnd.d100

import kotlin.random.Random

class RollMaster(private val random: Random = Random.Default) {

    fun rollWithDescriptor(tables: List<Table>): List<String> {
        return roll(tables) { descriptor, result ->
            "$descriptor $result"
        }
    }

    fun rollWithoutDescriptor(tables: List<Table>): List<String> {
        return roll(tables) { _, result ->
            result
        }
    }

    private fun roll(tables: List<Table>, formatter: (String, String) -> String): List<String> {

        return tables.flatMap { table ->
            val (descriptor, dieSize, results, rollsRequired) = table

            Array(rollsRequired) {
                results[random.nextInt(dieSize)]
            }.map { rollResult ->
                formatter(descriptor, rollResult)
            }
        }
    }
}