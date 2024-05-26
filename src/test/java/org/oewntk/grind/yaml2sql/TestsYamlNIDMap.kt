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
import org.oewntk.sql.out.Lexes.makeLexesNIDs
import org.oewntk.sql.out.NIDMaps
import kotlin.test.assertEquals

class TestsYamlNIDMap {

    // first
    private val hoodLex = Lex("'hood", "n")

    // last
    private val zymurgyLex = Lex("zymurgy", "n")

    private fun testLookupByKey(lex: Lex, expectedNID: Int) {
        val lexK = Key.KeyLCP.of(lex, Lex::lemma, Lex::type)
        val r1 = NIDMaps.lookup(lexKeyToNIDByKey, lexK)
        assertEquals(expectedNID, r1)

        val lexK2 = Key.KeyLCP.of_t(lex)
        val r2 = NIDMaps.lookup(lexKeyToNIDByKey, lexK2)
        assertEquals(expectedNID, r2)
    }

    private fun testLookupByKeyF(lex: Lex, expectedNID: Int) {
        val lexK = KeyF.FuncKeyLCP.Mono.of(Lex::lemma, Lex::type, lex)
        val r = NIDMaps.lookup(lexKeyToNIDByKeyF, lexK)
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

    @Test(expected = NullPointerException::class)
    fun failingTestLookupByKey() {
        val lex = hoodLex
        val k = Key.KeyLCP.of_t(lex)
        NIDMaps.lookup(lexKeyToNIDByKeyF, k)
    }

    @Test(expected = NullPointerException::class)
    fun failingTestLookupByKeyF() {
        val lex = hoodLex
        val k = KeyF.FuncKeyLCP.Mono.of(Lex::lemma, Lex::type, lex)
        NIDMaps.lookup(lexKeyToNIDByKey, k)
    }

    companion object {

        private lateinit var lexKeyToNIDByKey: Map<Key, Int>
        private lateinit var lexKeyToNIDByKeyF: Map<Key, Int>

        @JvmStatic
        @BeforeClass
        fun init() {
            LibTestsYamlCommon.init()
            checkNotNull(model)

            // lex key to NID
            lexKeyToNIDByKey = makeLexesNIDs(model!!.lexes)

            // lex keyf to NID
            lexKeyToNIDByKeyF = model!!.lexes
                .asSequence()
                .map { KeyF.FuncKeyLCP.Mono.of(Lex::lemma, Lex::type, it) }
                .sorted()
                .withIndex()
                .associate { it.value to it.index + 1 } // map(of_t(lex), nid)
        }
    }
}