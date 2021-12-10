/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.grind.yaml2sql;

import java.io.File;
import java.io.IOException;

/**
 * Main class that generates the WN database in the WNDB format as per wndb(5WN)
 *
 * @author Bernard Bou
 * @see "https://wordnet.princeton.edu/documentation/wndb5wn"
 */
public class GrinderBuiltIn
{
	/**
	 * Main entry point
	 *
	 * @param args command-line arguments [-compat:lexid] [-compat:pointer] yamlDir [outputDir]
	 * @throws IOException io
	 */
	public static void main(String[] args) throws IOException
	{
		// Timing
		final long startTime = System.currentTimeMillis();

		// Argument switches processing
		int nArg = args.length; // left
		int iArg = 0; // current

		// Output
		File outDir;
		if (nArg > 0) // if left
		//noinspection CommentedOutCode
		{
			outDir = new File(args[iArg]);
			// nArg--; // left: decrement
			// iArg++; // current: move to next
			if (!outDir.exists())
			{
				//noinspection ResultOfMethodCallIgnored
				outDir.mkdirs();
			}
		}
		else
		{
			outDir = new File(".");
		}
		System.err.println("Output " + outDir.getAbsolutePath());

		// Process
		Grinder.builtins(outDir);

		// Timing
		final long endTime = System.currentTimeMillis();
		System.err.println("Total execution time: " + (endTime - startTime) / 1000 + "s");
	}
}
