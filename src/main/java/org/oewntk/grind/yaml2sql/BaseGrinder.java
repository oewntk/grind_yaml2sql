/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.grind.yaml2sql;

import org.oewntk.model.CoreModel;
import org.oewntk.model.Lex;
import org.oewntk.model.Sense;
import org.oewntk.model.Synset;
import org.oewntk.sql.out.BuiltIn;
import org.oewntk.sql.out.Lexes;
import org.oewntk.sql.out.Names;
import org.oewntk.sql.out.Senses;
import org.oewntk.sql.out.Synsets;
import org.oewntk.yaml.in.Factory;
import org.oewntk.yaml.in.Memory;
import org.oewntk.yaml.in.Memory.Unit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Main class that generates the WN database in the SQL format
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
public class BaseGrinder
{
	/**
	 * NID maps
	 */
	protected Map<Lex, Integer> lexToNID = null;
	protected Map<String, Integer> wordToNID = null;
	protected Map<String, Integer> casedWordToNID = null;
	protected Map<String, Integer> synsetIdToNID = null;
	@SuppressWarnings("FieldCanBeLocal")
	private Map<String, Integer> morphToNID = null;
	@SuppressWarnings("FieldCanBeLocal")
	private Map<String, Integer> pronunciationToNID = null;
	@SuppressWarnings("FieldCanBeLocal")
	private Map<String, Integer> sensekeyToNID = null;

	protected final File inDir;

	public BaseGrinder(File inDir)
	{
		this.inDir = inDir;
	}

	public CoreModel makeCoreModel() throws IOException
	{
		return Factory.makeCoreModel(inDir);
	}

	public void processBase(CoreModel model, File outDir) throws FileNotFoundException
	{
		// Process
		lexes(outDir, model.lexesByLemma);
		synsets(outDir, model.synsetsById);
		senses(outDir, model.sensesById);
		builtins(outDir);
	}

	private void lexes(final File outDir, final Map<String, List<Lex>> lexesByLemma) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.WORDS.FILE))), true, StandardCharsets.UTF_8))
		{
			wordToNID = Lexes.generateWords(ps, lexesByLemma);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.CASEDWORDS.FILE))), true, StandardCharsets.UTF_8))
		{
			casedWordToNID = Lexes.generateCasedWords(ps, lexesByLemma, wordToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.LEXES.FILE))), true, StandardCharsets.UTF_8))
		{
			lexToNID = Lexes.generateLexes(ps, lexesByLemma, wordToNID, casedWordToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.MORPHS.FILE))), true, StandardCharsets.UTF_8))
		{
			morphToNID = Lexes.generateMorphs(ps, lexesByLemma);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.LEXES_MORPHS.FILE))), true, StandardCharsets.UTF_8))
		{
			Lexes.generateMorphMaps(ps, lexesByLemma, lexToNID, wordToNID, morphToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.PRONUNCIATIONS.FILE))), true, StandardCharsets.UTF_8))
		{
			pronunciationToNID = Lexes.generatePronunciations(ps, lexesByLemma);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.LEXES_PRONUNCIATIONS.FILE))), true, StandardCharsets.UTF_8))
		{
			Lexes.generatePronunciationMaps(ps, lexesByLemma, lexToNID, wordToNID, pronunciationToNID);
		}
	}

	private void synsets(final File outDir, final Map<String, Synset> synsetsById) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SYNSETS.FILE))), true, StandardCharsets.UTF_8))
		{
			synsetIdToNID = Synsets.generateSynsets(ps, synsetsById);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SAMPLES.FILE))), true, StandardCharsets.UTF_8))
		{
			Synsets.generateSamples(ps, synsetsById, synsetIdToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SYNSETS_SYNSETS.FILE))), true, StandardCharsets.UTF_8))
		{
			Synsets.generateSynsetRelations(ps, synsetsById, synsetIdToNID);
		}
	}

	private void senses(final File outDir, final Map<String, Sense> sensesById) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SENSES.FILE))), true, StandardCharsets.UTF_8))
		{
			sensekeyToNID = Senses.generateSenses(ps, sensesById, synsetIdToNID, lexToNID, wordToNID, casedWordToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SENSES_SENSES.FILE))), true, StandardCharsets.UTF_8))
		{
			Senses.generateSenseRelations(ps, sensesById, synsetIdToNID, lexToNID, wordToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SENSES_VFRAMES.FILE))), true, StandardCharsets.UTF_8))
		{
			Senses.generateVerbFrames(ps, sensesById, synsetIdToNID, lexToNID, wordToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SENSES_ADJPOSITIONS.FILE))), true, StandardCharsets.UTF_8))
		{
			Senses.generateAdjPositions(ps, sensesById, synsetIdToNID, lexToNID, wordToNID);
		}
	}

	public static void builtins(final File outDir) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.DOMAINS.TABLE))), true, StandardCharsets.UTF_8))
		{
			BuiltIn.generateDomains(ps);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.VFRAMES.FILE))), true, StandardCharsets.UTF_8))
		{
			BuiltIn.generateVerbFrames(ps);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.POSES.FILE))), true, StandardCharsets.UTF_8))
		{
			BuiltIn.generatePosTypes(ps);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.ADJPOSITIONS.FILE))), true, StandardCharsets.UTF_8))
		{
			BuiltIn.generateAdjectivePositionTypes(ps);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.RELS.FILE))), true, StandardCharsets.UTF_8))
		{
			BuiltIn.generateRelationTypes(ps);
		}
	}

	static protected String makeFilename(String name)
	{
		String fileName = name + ".sql";
		System.err.println(fileName);
		return fileName;
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
		BaseGrinder grinder = new BaseGrinder(inDir);
		CoreModel coreModel = grinder.makeCoreModel();
		System.err.printf("model %s\n%s%n", coreModel.getSource(), coreModel.info());

		// Timing
		final long midTime = System.currentTimeMillis();
		System.err.println("[Time] " + (midTime - startTime) / 1000 + "s");

		// Heap
		if (traceHeap)
		{
			System.err.println(Memory.heapInfo("after maps,", Unit.M));
		}

		grinder.processBase(coreModel, outDir);

		// Timing
		final long endTime = System.currentTimeMillis();
		System.err.println("[Time] " + (endTime - startTime) / 1000 + "s");
	}
}
