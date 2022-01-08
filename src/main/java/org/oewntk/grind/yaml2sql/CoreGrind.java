/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.grind.yaml2sql;

import org.oewntk.model.CoreModel;
import org.oewntk.sql.out.CoreModelConsumer;
import org.oewntk.yaml.in.CoreFactory;

import java.io.File;

/**
 * Main class that generates the WN database in the SQL format
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
public class CoreGrind
{
	/**
	 * Argument switches processing
	 *
	 * @param args command-line arguments
	 * @return int[0]=flags, int[1]=next arg to process
	 */
	public static int flags(String[] args)
	{
		int i = 0;
		for (; i < args.length; i++)
		{
			if ("-traceTime".equals(args[i])) // if left and is "-traceTime"
			{
				Tracing.traceTime = true;
			}
			else if ("-traceHeap".equals(args[i])) // if left and is "-traceHeap"
			{
				Tracing.traceHeap = true;
			}
			else
			{
				break;
			}
		}
		return i;
	}

	/**
	 * Main entry point
	 *
	 * @param args command-line arguments yamlDir yamlDir2 [outputDir]
	 */
	public static void main(String[] args)
	{
		int iArg = CoreGrind.flags(args);

		// Tracing
		final long startTime = Tracing.start();

		// Input
		File inDir = new File(args[iArg]);
		Tracing.psInfo.println("[Input] " + inDir.getAbsolutePath());

		// Input2
		File inDir2 = new File(args[iArg + 1]);
		Tracing.psInfo.println("[Input2] " + inDir2.getAbsolutePath());

		// Output
		File outDir = new File(args[iArg + 2]);
		if (!outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			outDir.mkdirs();
		}
		Tracing.psInfo.println("[Output] " + outDir.getAbsolutePath());

		// Supply model
		Tracing.progress("before model is supplied,", startTime);
		CoreModel model = new CoreFactory(inDir).get();
		//Tracing.psInfo.printf("[CoreModel] %s%n%s%n%n", model.getSource(), model.info());
		Tracing.progress("after model is supplied,", startTime);

		// Consume model
		Tracing.progress("before model is consumed,", startTime);
		new CoreModelConsumer(outDir).accept(model);
		Tracing.progress("after model is consumed,", startTime);

		// End
		Tracing.progress("total,", startTime);
	}
}
