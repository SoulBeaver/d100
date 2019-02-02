package com.christian.dnd.d100

import kotlin.random.Random

class RollMaster(private val random: Random = Random.Default) {

    fun roll(tables: List<Table>): List<String> {
        val tableRolls = mutableListOf<String>()

        for (table in tables) {
            val (descriptor, dieSize, results, rollsRequired) = table

            val rollResults = Array(rollsRequired) { results[random.nextInt(dieSize)] }

            (0 until rollsRequired).map {
                "$descriptor ${rollResults[it]}"
            }.forEach {
                tableRolls.add(it)
            }
        }

        return tableRolls
    }
}