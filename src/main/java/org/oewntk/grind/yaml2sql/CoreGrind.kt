/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.grind.yaml2sql

import org.oewntk.grind.yaml2sql.Tracing.progress
import org.oewntk.grind.yaml2sql.Tracing.start
import org.oewntk.sql.out.CoreModelConsumer
import org.oewntk.yaml.`in`.CoreFactory
import java.io.File

/**
 * Main class that generates the WN database in the SQL format
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
object CoreGrind {

    /**
     * Argument switches processing
     *
     * @param args command-line arguments
     * @return int[0]=flags, int[1]=next arg to process
     */
    fun flags(args: Array<String>): Int {
        var i = 0
        while (i < args.size) {
            if ("-traceTime" == args[i]) // if left and is "-traceTime"
            {
                Tracing.traceTime = true
            } else if ("-traceHeap" == args[i]) // if left and is "-traceHeap"
            {
                Tracing.traceHeap = true
            } else {
                break
            }
            i++
        }
        return i
    }

    /**
     * Main entry point
     *
     * @param args command-line arguments
     * ```
     * yamlDir yamlDir2 [outputDir]
     * ```
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val iArg = flags(args)

        // Tracing
        val startTime = start()

        // Input
        val inDir = File(args[iArg])
        Tracing.psInfo.println("[Input] " + inDir.absolutePath)

        // Input2
        val inDir2 = File(args[iArg + 1])
        Tracing.psInfo.println("[Input2] " + inDir2.absolutePath)

        // Output
        val outDir = File(args[iArg + 2])
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
        Tracing.psInfo.println("[Output] " + outDir.absolutePath)

        // Supply model
        progress("before model is supplied,", startTime)
        val model = CoreFactory(inDir).get()
        //Tracing.psInfo.printf("[CoreModel] %s%n%s%n%n", model.getSource(), model.info());
        progress("after model is supplied,", startTime)

        // Consume model
        progress("before model is consumed,", startTime)
        CoreModelConsumer(outDir).accept(model!!)
        progress("after model is consumed,", startTime)

        // End
        progress("total,", startTime)
    }
}
