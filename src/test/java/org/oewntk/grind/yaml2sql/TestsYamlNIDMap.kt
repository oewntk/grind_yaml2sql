/*
 * Copyright (c) 2024. Bernard Bou.
 */

package org.oewntk.grind.yaml2sql

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.grind.yaml2sql.LibTestsYamlCommon.model
import org.oewntk.model.Key
import org.oewntk.model.KeyF
import org.oewntk.model.Lex
import org.oewntk.sql.out.NIDMaps
import kotlin.test.assertEquals

class TestsYamlNIDMap {

	@Test
	fun testLookupByKey() {
		val lex = Lex("C", "n", null)
		val k = Key.W_P_A.of_t(lex)
		NIDMaps.lookup(lexKeyToNIDByKey, k)
		val k2 = Key.W_P_A.of_t(lex)
		val r = NIDMaps.lookup(lexKeyToNIDByKey, k2)
		assertEquals(r, 1)
	}

	@Test(expected = NullPointerException::class)
	fun failingTestLookupByKey() {
		val lex = Lex("C", "n", null)
		val k = Key.W_P_A.of_t(lex)
		NIDMaps.lookup(lexKeyToNIDByKey, k)
		val k2 = Key.W_P_A.of_t(lex)
		NIDMaps.lookup(lexKeyToNIDByKeyF, k2)
	}

	@Test
	fun testLookupByKeyF() {
		val lex = Lex("C", "n", null)
		val k = KeyF.F_W_P_A.Mono.of(Lex::lemma, Lex::type, lex)
		val r = NIDMaps.lookup(lexKeyToNIDByKeyF, k)
		assertEquals(r, 1)
	}

	@Test(expected = NullPointerException::class)
	fun failingTestLookupByKeyF() {
		val lex = Lex("C", "n", null)
		val k = KeyF.F_W_P_A.Mono.of(Lex::lemma, Lex::type, lex)
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
			lexKeyToNIDByKey = model!!.lexes
				.asSequence()
				.map { Key.W_P_A.of_t(it) }
				.withIndex()
				.associate { it.value to it.index + 1 } // map(of_t(lex), nid)

			// lex keyf to NID
			lexKeyToNIDByKeyF = model!!.lexes
				.asSequence()
				.map { KeyF.F_W_P_A.Mono.of(Lex::lemma, Lex::type, it) }
				.withIndex()
				.associate { it.value to it.index + 1 } // map(of_t(lex), nid)
		}
	}
}