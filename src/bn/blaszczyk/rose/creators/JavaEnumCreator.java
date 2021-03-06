package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import bn.blaszczyk.rose.*;
import bn.blaszczyk.rose.model.*;


public class JavaEnumCreator {

	/*
	 * create Enum
	 */
	public static void create(final EnumModel enumModel, final MetaData metadata, final String parentDir) throws RoseException
	{
		final String fullpath = getFullPath(enumModel, metadata, parentDir);
		final File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(final FileWriter writer = new FileWriter(file))
		{
			// initial comment
			writer.write("/*\r\n * " + enumModel.getSimpleClassName() + ".java\r\n * generated by rose\r\n */\r\n");
			
			// package declaration
			writer.write("package " + metadata.getModelpackage() + ";\r\n\r\n");
			
			// class declaration
			writer.write("public enum " + enumModel.getSimpleClassName() + "\r\n"
					+ "{");
			
			// options variables
			boolean first = true;
			for(String option : enumModel)
			{
				if(first)
					first = false;
				else
					writer.write( "," );
				writer.write("\r\n\t" + option  );
			}
			// fin
			writer.write(";\r\n"
					+ "}\r\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			throw new RoseException("Error creating enum java files", e);
		}
		
	}

	private static String getFullPath(final EnumModel enumModel, final MetaData metadata, final String parentDir) {
		final String fullpath = parentDir + "/" + metadata.getSrcpath() + metadata.getModelpackage().replaceAll("\\.", "/") + "/" + enumModel.getSimpleClassName() + ".java";
		return fullpath;
	}

	public static void clear(final EnumModel enumModel, final MetaData metadata, final String parentDir)
	{
		final String fullPath = getFullPath(enumModel, metadata, parentDir);
		final File file = new File(fullPath);
		if(file.exists())
			file.delete();
	}
	
}
