package dev.christianbroomfield.d100.model

typealias TableResults = List<String>

sealed class Table {
    /**
     * A table that has not been cleaned, i.e. no trimming, replacements, etc.
     */
    data class DirtyTable(
        val header: TableHeader,
        val results: TableResults,
        val rollBehavior: RollBehavior = RollBehavior.REPEAT
    ) : Table()

    /**
     * A table with its header and all of its results cleaned
     */
    data class CleanedTable(
        val header: TableHeader,
        val results: TableResults,
        val rollBehavior: RollBehavior = RollBehavior.REPEAT
    ) : Table()

    /**
     * A table that has been cleaned and had its dice expressions within the TableResults evaluated.
     */
    data class PreppedTable(
        val header: TableHeader,
        val results: TableResults,
        val rollBehavior: RollBehavior = RollBehavior.REPEAT
    ) : Table()
}

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

enum class RollBehavior {
    REPEAT,
    ADD
}