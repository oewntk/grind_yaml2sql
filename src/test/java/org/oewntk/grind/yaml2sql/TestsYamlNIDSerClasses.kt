/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */

package org.oewntk.grind.yaml2sql

import junit.framework.TestCase.assertNull
import org.junit.Test
import org.oewntk.grind.yaml2sql.Checker.loadClassesOf

class TestsYamlNIDSerClasses {

    private fun testLoadedClassesOf(file: String): String? {
        val result = loadClassesOf(file)
        println("Deserialized object is: ${result.first}")
        result.second.sorted().forEach { println("Loaded class: $it") }
        return result.second.firstOrNull { it.contains("kotlin") }
    }

    @Test
    fun testLoadedClassesSensekeysWordsSynsets() {
        val r = testLoadedClassesOf("sers/sensekeys_words_synsets.ser")
        assertNull(r)
    }

    @Test
    fun testLoadedClassesSynsets() {
        val r = testLoadedClassesOf("sers/nid_synsets.ser")
        assertNull(r)
    }

    @Test
    fun testLoadedClassesSenses() {
        val r = testLoadedClassesOf("sers/nid_senses.ser")
        assertNull(r)
    }

    @Test
    fun testLoadedClassesWords() {
        val r = testLoadedClassesOf("sers/nid_words.ser")
        assertNull(r)
    }
}