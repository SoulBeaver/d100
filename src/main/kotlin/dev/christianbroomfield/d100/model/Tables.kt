package dev.christianbroomfield.d100.model

/**
 * Representation of a list of table results
 */
typealias TableResults = List<String>

/**
 * Representation of a table in various states.
 *
 * Only a PreppedTable should be used for rolling, other tables are intermediate results during parsing.
 */
sealed class Table {
    /**
     * A table that has not been cleaned, i.e. no trimming, replacements, etc.
     *
     * @property header the TableHeader describing this table
     * @property results the results this table contains
     * @property rollBehavior how to roll tables with multiple rolls.
     */
    data class DirtyTable(
        val header: TableHeader,
        val results: TableResults,
        val rollBehavior: RollBehavior = RollBehavior.REPEAT
    ) : Table()

    /**
     * A table with its header and all of its results cleaned
     *
     * @property header the TableHeader describing this table
     * @property results the results this table contains
     * @property rollBehavior how to roll tables with multiple rolls.
     */
    data class CleanedTable(
        val header: TableHeader,
        val results: TableResults,
        val rollBehavior: RollBehavior = RollBehavior.REPEAT
    ) : Table()

    /**
     * A table that has been cleaned and had its dice expressions within the TableResults evaluated.
     *
     * @property header the TableHeader describing this table
     * @property results the results this table contains
     * @property rollBehavior how to roll tables with multiple rolls.
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

/**
 * Represents the operation necessary to correctly determine the results of a table.
 */
enum class RollBehavior {
    /**
     * Roll on the table multiple times
     */
    REPEAT,
    /**
     * Sum each roll for a single total
     */
    ADD
}