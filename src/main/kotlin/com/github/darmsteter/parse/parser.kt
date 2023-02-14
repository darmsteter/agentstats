package com.github.darmsteter.parse

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

fun parseExportData(data: String): Map<String, Any> {
    return try {
        parseExportDataImpl(data)
    } catch (e: Throwable) {
        throw ExportDataParseException(data, e)
    }
}

class ExportDataParseException(rawData: String, cause: Throwable) : Exception(
    "Can't parse the data exported from Ingress: $rawData",
    cause,
)

class ExportDataFieldParseException(fieldName: String, fieldRawValue: String, cause: Throwable) : Exception(
    "Can't parse field '$fieldName' with value '$fieldRawValue'",
    cause,
)

private fun parseExportDataImpl(data: String): Map<String, Any> {
    val lines = data.split("\n")
    val header = lines[0].split("\t")
    val values = lines[1].split("\t")

    return buildMap {
        for ((title, value) in header.zip(values)) {
            val parsedValue = try {
                when (title) {
                    "Time Span", "Agent Name", "Agent Faction" -> value
                    "Date (yyyy-mm-dd)" -> LocalDate.parse(value)
                    "Time (hh:mm:ss)" -> LocalTime.parse(value)
                    else -> value.toInt().also {
                        check(it >= 0 ) {
                            "Negative numbers are not allowed"
                        }
                    }
                }
            } catch (e: Throwable) {
                throw ExportDataFieldParseException(title, value, e)
            }
            put(title, parsedValue)
        }
    }
}