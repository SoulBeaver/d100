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
        val tableRolls = mutableListOf<String>()

        for (table in tables) {
            val (descriptor, dieSize, results, rollsRequired) = table

            val rollResults = Array(rollsRequired) { results[random.nextInt(dieSize)] }

            (0 until rollsRequired).map {
                formatter(descriptor, rollResults[it])
            }.forEach {
                tableRolls.add(it)
            }
        }

        return tableRolls
    }
}