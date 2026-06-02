/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.grind.yaml2sql

import org.junit.Assert
import org.oewntk.model.CoreModel
import org.oewntk.model.ModelInfo
import org.oewntk.yaml.`in`.CoreFactory
import java.io.File
import java.io.PrintStream

object LibTestsYamlCommon {

    private val source: String? = System.getProperty("SOURCE")

    val silent = if (System.getProperties().containsKey("VERBOSE")) false
    else if (System.getProperties().containsKey("SILENT")) true
    else true

    val ps: PrintStream = if (!silent) Tracing.psInfo else Tracing.psNull

    val model: CoreModel by lazy {
        if (source == null) {
            Tracing.psErr.println("Define serialized source file dir with -DSOURCE=path")
            throw AssertionError("SOURCE not defined")
        }
        val inDir = File(source)
        Tracing.psInfo.printf("source=%s%n", inDir.absolutePath)
        if (!inDir.exists()) {
            Tracing.psErr.println("Define YAML source dir that exists")
            Assert.fail()
        }
        val result = CoreFactory(inDir, verbose = !silent).get()!!
        ps.println(result.info())
        ps.println(ModelInfo.counts(result))
        result
    }
}
