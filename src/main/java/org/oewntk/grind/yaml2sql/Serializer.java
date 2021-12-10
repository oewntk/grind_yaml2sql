/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.grind.yaml2sql;

import java.io.File;
import java.io.IOException;

import org.oewntk.model.Model;
import org.oewntk.sql.out.SerializeNIDs;
import org.oewntk.yaml.in.Factory;

public class Serializer
{
	static final String FILE_MODEL = "model";

	public static void main(String[] args) throws IOException
	{
		File outDir = new File(args[2]);
		if (!outDir.isDirectory())
		{
			outDir.mkdirs();
		}

		final Model model = Factory.makeModel(args);
		org.oewntk.model.Serialize.serializeModel(model, new File(outDir, FILE_MODEL));
		SerializeNIDs.serializeNIDs(model, outDir);
	}
}
