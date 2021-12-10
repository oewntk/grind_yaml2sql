/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.grind.yaml2sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.oewntk.model.CoreModel;
import org.oewntk.model.Model;
import org.oewntk.sql.out.NIDMaps;
import org.oewntk.sql.out.Names;
import org.oewntk.yaml.in.Factory;

public class Mapper
{
	static public void print(final CoreModel model, final File outDir) throws IOException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, Names.WORDS.FILE)), true, StandardCharsets.UTF_8))
		{
			NIDMaps.printWords(ps, model.lexesByLemma);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, Names.CASEDWORDS.FILE)), true, StandardCharsets.UTF_8))
		{
			NIDMaps.printCasedWords(ps, model.lexesByLemma);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, Names.MORPHS.FILE)), true, StandardCharsets.UTF_8))
		{
			NIDMaps.printMorphs(ps, model.lexesByLemma);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, Names.PRONUNCIATIONS.FILE)), true, StandardCharsets.UTF_8))
		{
			NIDMaps.printPronunciations(ps, model.lexesByLemma);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, Names.SYNSETS.FILE)), true, StandardCharsets.UTF_8))
		{
			NIDMaps.printSynsets(ps, model.synsetsById);
		}
	}

	static public void main(String[] args) throws IOException
	{
		File outDir = new File(args[2]);
		if (!outDir.isDirectory())
		{
			outDir.mkdirs();
		}

		final Model model = Factory.makeModel(args);
		print(model, outDir);
	}
}
