/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.oewntk.grind.yaml2sql

import org.oewntk.model.Serialize.serializeCoreModel
import org.oewntk.sql.out.SerializeNIDs.serializeNIDs
import org.oewntk.yaml.`in`.Factory.Companion.makeModel
import java.io.File
import java.io.IOException

/**
 * Class that serializes the NID maps
 */
object Serializer {

	private const val FILE_MODEL = "model.ser"

	/**
	 * Main entry point
	 *
	 * @param args command-line arguments
	 * ```
	 * yamlDir [outputDir]
	 * ```
	 * @throws IOException io exception
	 */
	@Throws(IOException::class)
	@JvmStatic
	fun main(args: Array<String>) {
		val outDir = File(args[2])
		if (!outDir.isDirectory) {
			outDir.mkdirs()
		}
		val model = makeModel(args)
		serializeCoreModel(model!!, File(outDir, FILE_MODEL))
		serializeNIDs(model, outDir)
	}
}
