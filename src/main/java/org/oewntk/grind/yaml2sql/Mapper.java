/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.grind.yaml2sql;

import org.oewntk.model.Model;
import org.oewntk.sql.out.NIDMaps;
import org.oewntk.yaml.in.Factory;

import java.io.File;
import java.io.IOException;

public class Mapper
{
	static public void main(String[] args) throws IOException
	{
		File outDir = new File(args[2]);
		if (!outDir.isDirectory())
		{
			//noinspection ResultOfMethodCallIgnored
			outDir.mkdirs();
		}

		final Model model = Factory.makeModel(args);
		NIDMaps.printMaps(model, outDir);
	}
}
