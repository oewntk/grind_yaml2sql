/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.grind.yaml2sql;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.oewntk.model.Model;
import org.oewntk.sql.out.Names;

import static org.oewntk.sql.out.DeSerializeNIDs.deserializeNIDs;

public class DeSerializer
{
	static public void main(String[] args) throws IOException, ClassNotFoundException
	{
		File inDir = new File(args[0]);
		if (!inDir.isDirectory())
		{
			System.exit(1);
		}

		final Model model = org.oewntk.model.DeSerialize.deSerializeModel(new File(inDir, Serializer.FILE_MODEL));
		System.out.println(model.info());

		Map<String, Map<String, Integer>> maps = deserializeNIDs(inDir);
		System.out.printf("%s %d%n", Names.WORDS.FILE, maps.get(Names.WORDS.FILE).size());
		System.out.printf("%s %d%n", Names.CASEDWORDS.FILE, maps.get(Names.CASEDWORDS.FILE).size());
		System.out.printf("%s %d%n", Names.MORPHS.FILE, maps.get(Names.MORPHS.FILE).size());
		System.out.printf("%s %d%n", Names.PRONUNCIATIONS.FILE, maps.get(Names.PRONUNCIATIONS.FILE).size());
		System.out.printf("%s %d%n", Names.SYNSETS.FILE, maps.get(Names.SYNSETS.FILE).size());
	}
}
