/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.grind.yaml2sql;

import org.oewntk.model.Model;
import org.oewntk.model.Sense;
import org.oewntk.model.VerbTemplate;
import org.oewntk.sql.out.Names;
import org.oewntk.sql.out.Senses;
import org.oewntk.sql.out.Utils;
import org.oewntk.yaml.in.Factory;
import org.oewntk.yaml.in.Memory;
import org.oewntk.yaml.in.Memory.Unit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

/**
 * Main class that generates the WN database in the SQL format
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
public class Grinder extends BaseGrinder
{
	/**
	 * Extra dir
	 */
	private final File inDir2;

	public Grinder(File inDir, File inDir2)
	{
		super(inDir);
		this.inDir2 = inDir2;
	}

	public Model makeModel() throws IOException
	{
		return Factory.makeModel(inDir, inDir2);
	}

	public void process(Model model, File outDir) throws FileNotFoundException
	{
		// Process
		processBase(model, outDir);
		templates(outDir, model.sensesById, model.verbTemplatesById);
	}

	private void templates(final File outDir, final Map<String, Sense> sensesById, final Map<Integer, VerbTemplate> verbTemplatesById) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SENSES_VTEMPLATES.FILE))), true, StandardCharsets.UTF_8))
		{
			Senses.generateVerbTemplates(ps, sensesById, synsetIdToNID, lexToNID, wordToNID);
		}
		final Function<Map.Entry<Integer, VerbTemplate>, String> toString = entry -> String.format("%d, '%s'", entry.getKey(), Utils.escape(entry.getValue().getTemplate()));
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.VTEMPLATES.FILE))), true, StandardCharsets.UTF_8))
		{
			Utils.generateTable(ps, Names.VTEMPLATES.TABLE, String.join(",", Names.VTEMPLATES.templateid, Names.VTEMPLATES.template), verbTemplatesById, toString);
		}
	}

	/**
	 * Main entry point
	 *
	 * @param args command-line arguments yamlDir yamlDir2 [outputDir]
	 * @throws IOException io
	 */
	public static void main(String[] args) throws IOException
	{
		// Argument switches processing
		int nArg = args.length; // left
		int iArg = 0; // current

		// Switches
		boolean traceHeap = false;
		if ("-h".equalsIgnoreCase(args[iArg]))
		{
			nArg--; // left: decrement
			iArg++; // current: move to next
			traceHeap = true;
		}

		// Input
		File inDir = new File(args[iArg]);
		nArg--; // left: decrement
		iArg++; // current: move to next

		// Input2
		File inDir2 = new File(args[iArg]);
		nArg--; // left: decrement
		iArg++; // current: move to next

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
		System.err.println("[Output] " + outDir.getAbsolutePath());

		// Timing
		final long startTime = System.currentTimeMillis();

		// Heap
		if (traceHeap)

		{
			System.err.println(Memory.heapInfo("before maps,", Unit.M));
		}

		// Model
		Grinder grinder = new Grinder(inDir, inDir2);
		Model model = grinder.makeModel();
		System.err.printf("model %s\n%s%n", Arrays.toString(model.getSources()), model.info());

		// Timing
		final long midTime = System.currentTimeMillis();
		System.err.println("[Time] " + (midTime - startTime) / 1000 + "s");

		// Heap
		if (traceHeap)

		{
			System.err.println(Memory.heapInfo("after maps,", Unit.M));
		}

		// Process
		grinder.process(model, outDir);

		// Timing
		final long endTime = System.currentTimeMillis();
		System.err.println("[Time] " + (endTime - startTime) / 1000 + "s");
	}
}
