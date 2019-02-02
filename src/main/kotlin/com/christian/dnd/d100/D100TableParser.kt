package com.christian.dnd.d100

import java.io.BufferedReader
import java.io.File

object D100TableParser {

    private val headerRegex = """(\d*)d(\d+|%)(.*)""".toRegex(RegexOption.IGNORE_CASE)

    fun parse(d100Table: File): List<Table> {
        val tables = mutableListOf<Table>()

        d100Table.bufferedReader().use { reader ->
            var line = reader.readLine()

            while (line != null) {
                if (isHeader(line)) {
                    val table = parseTable(line, reader)

                    tables.add(table)
                }

                line = reader.readLine()
            }
        }

        return tables
    }


    private fun isHeader(line: String) = headerRegex.matches(line)

    private fun parseTable(header: String, reader: BufferedReader): Table {
        return headerRegex.find(header)!!.groupValues.let {
            val rollsRequired = if (it[1].isBlank()) 1 else it[1].toInt()
            val dieSize = if (it[2] == "%") 100 else it[2].toInt()
            val descriptor = cleanDescriptor(it[3])
            val results = parseResults(reader, dieSize)

            Table(descriptor, dieSize, results, rollsRequired)
        }
    }

    private fun parseResults(reader: BufferedReader, dieSize: Int): List<String> {
        return (0 until dieSize)
            .map { reader.readLine() }
            .asSequence()
            .take(dieSize)
            .map(String::trim)
            // 1	Is red. Its touch is burning hot. -> Is red. Its touch is burning hot.
            .map { it.replace("\\d+\\t".toRegex(), "") }
            .toList()
    }

    private fun cleanDescriptor(descriptor: String): String {
        return descriptor.trim()
            .removePrefix("\t")
    }
}