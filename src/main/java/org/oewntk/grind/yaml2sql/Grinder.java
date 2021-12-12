/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.grind.yaml2sql;

import org.oewntk.model.Model;
import org.oewntk.sql.out.ModelConsumer;
import org.oewntk.yaml.in.Factory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Main class that generates the WN database in the SQL format
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
public class Grinder
{
	/**
	 * Main entry point
	 *
	 * @param args command-line arguments [-compat:lexid] [-compat:pointer] yamlDir [outputDir]
	 * @throws IOException io
	 */
	public static void main(String[] args) throws IOException
	{
		int iArg = BaseGrinder.flags(args);

		// Tracing
		final long startTime = Tracing.start();

		// Input
		File inDir = new File(args[iArg]);
		System.err.println("[Input] " + inDir.getAbsolutePath());

		// Input2
		File inDir2 = new File(args[iArg + 1]);
		System.err.println("[Input2] " + inDir2.getAbsolutePath());

		// Output
		File outDir = new File(args[iArg + 2]);
		if (!outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			outDir.mkdirs();
		}
		System.err.println("[Output] " + outDir.getAbsolutePath());

		// Supply model
		Tracing.progress("before model is supplied,", startTime);
		Model model = new Factory(inDir, inDir2).get();
		System.err.printf("[Model] %s\n%s%n", Arrays.toString(model.getSources()), model.info());
		Tracing.progress("after model is supplied,", startTime);

		// Consume model
		Tracing.progress("before model is consumed,", startTime);
		new ModelConsumer(outDir).accept(model);
		Tracing.progress("after model is consumed,", startTime);

		// End
		Tracing.progress("total,", startTime);
	}
}
