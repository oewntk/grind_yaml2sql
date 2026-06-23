/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */

package org.oewntk.grind.yaml2sql

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.grind.yaml2sql.LibTestsYamlCommon.model
import org.oewntk.model.Key
import org.oewntk.model.KeyF
import org.oewntk.model.Lex
import org.oewntk.model.NIDs.lookup
import kotlin.test.assertEquals

class TestsYamlNIDMap {

    // first
    private val hoodLex = Lex("'hood", "n")

    // last
    private val zymurgyLex = Lex("zymurgy", "n")

    private fun testLookupByKey(lex: Lex, expectedNID: Int) {
        val lexK = Key.UsingPronunciation.of(lex, Lex::lemma) { it.type.toCategory() }
        val r1 = lookup(lexKeyToNIDByKey, lexK)
        assertEquals(expectedNID, r1)

        val lexK2 = Key.UsingPronunciation.of(lex)
        val r2 = lookup(lexKeyToNIDByKey, lexK2)
        assertEquals(expectedNID, r2)
    }

    private fun testLookupByKeyF(lex: Lex, expectedNID: Int) {
        val lexK = KeyF.FuncUsingPronunciation.Mono.of(Lex::lemma, { it.type.toCategory() }, lex)
        val r = lookup(lexKeyToNIDByKeyF, lexK)
        assertEquals(expectedNID, r)
    }

    @Test
    fun testLookupByKey() {
        testLookupByKey(hoodLex, 1) // first
        testLookupByKey(zymurgyLex, lexKeyToNIDByKey.size) // last
    }

    @Test
    fun testLookupByKeyF() {
        testLookupByKeyF(hoodLex, 1) // first
        testLookupByKeyF(zymurgyLex, lexKeyToNIDByKeyF.size) // last
    }

    @Test
    fun failingTestLookupByKey() {
        val lex = hoodLex
        val k = Key.UsingPronunciation.of(lex)
        lookup(lexKeyToNIDByKeyF, k)
    }

    @Test
    fun failingTestLookupByKeyF() {
        val lex = hoodLex
        val k = KeyF.FuncUsingPronunciation.Mono.of(Lex::lemma, { it.type.toCategory() }, lex)
        lookup(lexKeyToNIDByKey, k)
    }

    companion object {

        private lateinit var lexKeyToNIDByKey: Map<Key, Int>

        private lateinit var lexKeyToNIDByKeyF: Map<Key, Int>

        @JvmStatic
        @BeforeClass
        fun init() {
            model

            // lex key to NID
            lexKeyToNIDByKey = model.lexes
                .asSequence()
                .map { Key.UsingPronunciation.of(it) }
                .sorted()
                .withIndex()
                .associate { it.value to it.index + 1 } // map(of_t(lex), nid)

            // lex keyf to NID
            lexKeyToNIDByKeyF = model.lexes
                .asSequence()
                .map { KeyF.FuncUsingPronunciation.Mono.of(Lex::lemma, { lex -> lex.type.toCategory() }, it) }
                .sorted()
                .withIndex()
                .associate { it.value to it.index + 1 } // map(of_t(lex), nid)
        }
    }
}