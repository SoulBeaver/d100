package com.christian.dnd.d100.cleaner

import com.christian.dnd.d100.model.Table
import com.christian.dnd.d100.model.TableHeader
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek

class TableCleanerSpec: Spek({
    val tableCleaner = TableCleaner()

    group("cleaning header junk ") {
        val testCases = listOf(
            "( header",
            ") header",
            ": header",
            "- header",
            " - header",
            "   header  ",
            "header ",
            "header (",
            "header )",
            "header:",
            "header :     ",
            "header -   "
        )

        testCases.forEach { expression ->
            val dirtyTable = Table.DirtyTable(TableHeader(1, 10, expression), listOf())

            test("the expression '$expression' should be cleaned to 'header'") {
                tableCleaner.clean(dirtyTable).header.descriptor shouldEqual "header"
            }
        }
    }

    group("cleaning table result junk") {
        val testCases = mapOf(
            " Hoard: …you might escape with a/an…" to "Hoard: you might escape with a/an",
            "\tcobwebs." to "cobwebs.",
            "    Awaken Plants. " to "Awaken Plants.",
            "1\ta wren." to "a wren.",
            "1- a wren." to "a wren.",
            "1. a wren." to "a wren.",
            "1.a wren." to "a wren.",
            "1) a wren." to "a wren.",
            "1: a wren." to "a wren.",
            "1:a wren." to "a wren."
        )

        testCases.forEach { expression, expected ->
            val dirtyTable = Table.DirtyTable(TableHeader(1, 10, ""), listOf(expression))

            test("the expression '$expression' should be cleaned to '$expected'") {
                tableCleaner.clean(dirtyTable).results.first() shouldEqual expected
            }
        }
    }
})