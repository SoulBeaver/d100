package com.christian.dnd.d100.model

enum class RollBehavior {
    REPEAT,
    ADD
}

data class Table(
    val header: TableHeader,

    /**
     * All the possible results of a table. In case a table is formatted such that a result is more likely, e.g.
     *
     *  1-3 Potion of Healing
     *
     * Then this list will contain three entries for Potion of Healing
     */
    val results: List<String>,

    /**
     * When a table requests multiple rolls, do you repeat the roll against the table
     * or add all the rolls and check the table once?
     */
    val rollBehavior: RollBehavior = RollBehavior.REPEAT
)