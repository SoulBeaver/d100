package com.christian.dnd.d100.model

/**
 * Representation of a table header, e.g. the line that describes the table and its roll requirements.
 */
data class TableHeader(
    /**
     * How often a table should be rolled. Is given in the form of YdX, e.g.
     *
     *  2d10 Title. This is a...
     *
     * Rolls Required: 2
     */
    val rollsRequired: Int,

    /**
     * The size of the table represented by a dX expression, e.g.
     *
     *  d10 Title. This is a...
     *
     * Die Size: 10
     */
    val dieSize: Int,

    /**
     * The descriptor is the flavor text next to the die size of a table, e.g.
     *
     *  d10 Title. This is a...
     *
     * Descriptor: "This is a..."
     */
    val descriptor: String
)