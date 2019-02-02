package com.christian.dnd.d100

data class Table(
    /**
     * The descriptor is the flavor text next to the die size of a table, e.g.
     *
     *  d10 Title. This is a...
     *
     * Descriptor: "This is a..."
     */
    val descriptor: String,

    /**
     * The size of the table represented by a dX expression, e.g.
     *
     *  d10 Title. This is a...
     *
     * Die Size: 10
     */
    val dieSize: Int,

    /**
     * All the possible results of a table. In case a table is formatted such that a result is more likely, e.g.
     *
     *  1-3 Potion of Healing
     *
     * Then this list will contain three entries for Potion of Healing
     */
    val results: List<String>,

    /**
     * How often a table should be rolled. Is given in the form of YdX, e.g.
     *
     *  2d10 Title. This is a...
     *
     * Rolls Required: 2
     */
    val rollsRequired: Int
)