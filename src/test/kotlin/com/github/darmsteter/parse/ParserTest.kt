package com.github.darmsteter.parse

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.io.BufferedReader
import kotlin.test.*

class ParserTest {
    private fun getTestDataStream(fileName: String) = ParserTest::class.java.classLoader.getResourceAsStream("ingress-export-data/$fileName")
        ?: error("No test data for $fileName")
    private fun getTestData(fileName: String) = buildString {
        BufferedReader(getTestDataStream(fileName).reader()).useLines { lines ->
            lines.forEach(::appendLine)
        }
    }

    private fun notCorrectField(fileName: String, fieldName: String) {
        val data = getTestData(fileName)
        val exception = assertFailsWith(ExportDataParseException::class) {
            parseExportData(data)
        }
        val cause = exception.cause
        assertNotNull(cause)
        assertEquals(cause::class, ExportDataFieldParseException::class)
        val causeMessage = cause.message
        assertNotNull(causeMessage)
        assertContains(causeMessage, "Can't parse field '${fieldName}' with value")
    }

    @Test
    fun correctDataTest() {
        val data = getTestData("correctData.txt")
        val parseIngressExportData = parseExportData(data)
        assertEquals("player", parseIngressExportData["Agent Name"])
        assertEquals(LocalDate(2023, 2, 12), parseIngressExportData["Date (yyyy-mm-dd)"])
        assertEquals(LocalTime(6, 13, 48), parseIngressExportData["Time (hh:mm:ss)"])
        assertEquals(15, parseIngressExportData["Level"])
    }

    @Test
    fun notCorrectDateTest() {
        notCorrectField("notCorrectDate.txt", "Date (yyyy-mm-dd)")
    }

    @Test
    fun notCorrectTimeTest() {
        notCorrectField("notCorrectTime.txt", "Time (hh:mm:ss)")
    }

    @Test
    fun negativeLevel() {
        notCorrectField("negativeLevel.txt", "Level")
    }

    @Test
    fun notIntegerLevel() {
        notCorrectField("notIntegerLevel.txt", "Level")
    }
}

