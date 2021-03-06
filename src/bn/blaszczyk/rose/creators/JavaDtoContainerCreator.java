package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import bn.blaszczyk.rose.*;
import bn.blaszczyk.rose.model.*;

public class JavaDtoContainerCreator {
	
	public static void create(final List<EntityModel> entityModels, final MetaData metadata, final String parentDir) throws RoseException
	{
		final String fullpath = getFullPath(metadata, parentDir);
		final File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(final Writer writer = new FileWriter(file))
		{
			// initial comment
			writer.write("/*\r\n * " + metadata.getDtocontainername() + ".java\r\n * generated by rose\r\n */\r\n");
			
			// package declaration
			writer.write("package " + metadata.getDtopackage() + ";\r\n\r\n");
			
			if(metadata.isUsingAnnotations())
				writeAnnotationHeader(writer);
			
			writeClassDeclaration( metadata.getDtocontainername(), metadata.isImplementDto(), writer);			
			
			writer.write("{\r\n");
			
			writeFieldDeclarations(entityModels, writer);
			
			writeConstructors(metadata.getDtocontainername(), writer);
			
			writeGettersSetters(entityModels,  writer);
			
			if(metadata.isImplementDto())
				writeDtoContainerMethods(entityModels, metadata, writer);

			writer.write("}\r\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			throw new RoseException("error creating java dto container", e);
		}
	}

	private static String getFullPath(final MetaData metadata, final String parentDir) {
		final String fullpath = parentDir + "/" + metadata.getSrcpath() + metadata.getDtopackage().replaceAll("\\.", "/") + "/" + metadata.getDtocontainername() + ".java";
		return fullpath;
	}

	private static void writeAnnotationHeader(Writer writer) throws IOException
	{
		writer.write("import com.google.gson.annotations.*;\r\n\r\n");
	}
	
	private static void writeClassDeclaration( final String dtoContainerName, boolean implementsDto, Writer writer) throws IOException
	{
		writer.write("public class " + dtoContainerName);
		if(implementsDto)
			writer.write(" implements bn.blaszczyk.rose.model.DtoContainer");
		writer.write( "\r\n");
	}

	private static void writeFieldDeclarations(final List<EntityModel> entityModels, final Writer writer) throws IOException
	{
		for(final EntityModel entityModel : entityModels)
			writer.write("\t@SerializedName(\"" + entityModel.getObjectName().toLowerCase() + "\")\r\n"
					+ "\tprivate java.util.Map<Integer," + entityModel.getSimpleClassName() + "Dto> " + entityModel.getObjectName() + "s = new java.util.TreeMap<>();\r\n");
	}
	
	private static void writeConstructors(final String dtoContainername, Writer writer) throws IOException
	{
		writer.write("\r\n\tpublic " + dtoContainername + "()\r\n"
				+ "\t{\r\n"
				+ "\t}\r\n\r\n");
	}
	
	private static void writeGettersSetters(final List<EntityModel> entityModels, final Writer writer) throws IOException
	{
		for(final EntityModel entityModel : entityModels)
		{
			writer.write("\tpublic java.util.Map<Integer," + entityModel.getSimpleClassName() + "Dto> get" + entityModel.getSimpleClassName() + "s()\r\n"
					+ "\t{\r\n"
					+ "\t\treturn " + entityModel.getObjectName() + "s;\r\n"
					+ "\t}\r\n\r\n" );
			writer.write("\tpublic void set" + entityModel.getSimpleClassName() + "s( java.util.Map<Integer," + entityModel.getSimpleClassName() + "Dto> " + entityModel.getObjectName() + "s )\r\n"
					+ "\t{\r\n"
					+ "\t\tthis." + entityModel.getObjectName() + "s = " + entityModel.getObjectName() + "s;\r\n"
					+ "\t}\r\n\r\n" );
		}
	}

	private static void writeDtoContainerMethods(final List<EntityModel> entityModels, final MetaData metadata, final Writer writer) throws IOException
	{
		final String dtoContainerName = metadata.getDtocontainername();
		writer.write("\t@Override\r\n"
				+ "\tpublic bn.blaszczyk.rose.model.Dto get(final String type, final int id)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(type.toLowerCase())\r\n"
				+ "\t\t{\r\n");
		for(final EntityModel entityModel : entityModels)
			writer.write("\t\tcase \"" +entityModel.getObjectName().toLowerCase() + "\":\r\n"
				+ "\t\t\treturn " + entityModel.getObjectName() + "s.get(id);\r\n");
		writer.write("\t\tdefault:\r\n"
				+ "\t\t\treturn null;\r\n"
				+ "\t\t}\r\n"
				+ "\t}\r\n\r\n");

		writer.write("\t@Override\r\n"
				+ "\tpublic java.util.Collection<? extends bn.blaszczyk.rose.model.Dto> getAll(final String type)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(type.toLowerCase())\r\n"
				+ "\t\t{\r\n");
		for(final EntityModel entityModel : entityModels)
			writer.write("\t\tcase \"" +entityModel.getObjectName().toLowerCase() + "\":\r\n"
				+ "\t\t\treturn " + entityModel.getObjectName() + "s.values();\r\n");
		writer.write("\t\tdefault:\r\n"
				+ "\t\t\treturn null;\r\n"
				+ "\t\t}\r\n"
				+ "\t}\r\n\r\n");

		writer.write("\t@Override\r\n"
				+ "\tpublic java.util.Set<Integer> getAllIds(final String type)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(type.toLowerCase())\r\n"
				+ "\t\t{\r\n");
		for(final EntityModel entityModel : entityModels)
			writer.write("\t\tcase \"" +entityModel.getObjectName().toLowerCase() + "\":\r\n"
				+ "\t\t\treturn " + entityModel.getObjectName() + "s.keySet();\r\n");
		writer.write("\t\tdefault:\r\n"
				+ "\t\t\treturn null;\r\n"
				+ "\t\t}\r\n"
				+ "\t}\r\n\r\n");
		
		writer.write("\t@Override\r\n"
				+ "\tpublic void put(final bn.blaszczyk.rose.model.Dto dto)\r\n"
				+ "\t{\r\n");
		for(final EntityModel entityModel : entityModels)
			writer.write("\t\tif(dto instanceof " + entityModel.getSimpleClassName() + "Dto)\r\n"
					+ "\t\t{\r\n"
					+ "\t\t\t" + entityModel.getObjectName() + "s.put(dto.getId(),(" + entityModel.getSimpleClassName() + "Dto)dto);\r\n"
					+ "\t\t\treturn;\r\n"
					+ "\t\t}\r\n");
		writer.write("\t}\r\n\r\n");

		writer.write("\t@Override\r\n"
				+ "\tpublic void putAll(final bn.blaszczyk.rose.model.DtoContainer container)\r\n"
				+ "\t{\r\n"
				+ "\t\tif(container instanceof " + dtoContainerName + ")\r\n"
				+ "\t\t{\r\n"
				+ "\t\t\t" + dtoContainerName + " c = (" + dtoContainerName + ")container;\r\n");
		for(final EntityModel entityModel : entityModels)
			writer.write("\t\t\t" + entityModel.getObjectName() + "s.putAll(c." + entityModel.getObjectName() + "s);\r\n");
		writer.write("\t\t}\r\n"
				+ "\t}\r\n\r\n");
	}

	public static void clear(final MetaData metadata, final String parentDir)
	{
		final String fullPath = getFullPath(metadata, parentDir);
		final File file = new File(fullPath);
		if(file.exists())
			file.delete();
	}
}
