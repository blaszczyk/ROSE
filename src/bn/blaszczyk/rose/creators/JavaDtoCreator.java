package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import bn.blaszczyk.rose.*;
import bn.blaszczyk.rose.model.*;

public class JavaDtoCreator {

	public static String getGetterName(final Field field)
	{
		if(field instanceof PrimitiveField && ((PrimitiveField)field).getType().equals(PrimitiveType.BOOLEAN))
			return "is" + field.getCapitalName();
		if(field instanceof EntityField)
			return "get" + field.getCapitalName() + optionalPlural(field);
		return "get" + field.getCapitalName();
	}
	
	public static String getIdGetterName(final EntityField field)
	{
		return "get" + field.getCapitalName() + "Id" + optionalPlural(field);
	}
	
	public static String getCountGetterName(final EntityField field)
	{
		return "get" + field.getCapitalName() + "Count" + optionalPlural(field);
	}
	
	public static String getSetterName(final Field field)
	{
		if(field instanceof EntityField)
			return "set" + field.getCapitalName() + optionalPlural(field);
		return "set" + field.getCapitalName();
	}
	
	public static String getIdSetterName(final EntityField field)
	{
		return "set" + field.getCapitalName() + "Id" + optionalPlural(field);
	}
	
	public static String getCountSetterName(final EntityField field)
	{
		return "set" + field.getCapitalName() + "Count" + optionalPlural(field);
	}
	
	private static String optionalPlural(final Field field)
	{
		if(field instanceof EntityField && ((EntityField)field).getType().isSecondMany())
			return "s";
		return "";
	}
	
	/*
	 * create Entity
	 */
	public static void create(final EntityModel entityModel, final MetaData metadata, final String parentDir) throws RoseException
	{
		final String fullpath = getFullPath(entityModel, metadata, parentDir);
		final File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(final Writer writer = new FileWriter(file))
		{
			// initial comment
			writer.write("/*\r\n * " + entityModel.getSimpleClassName() + "Dto.java\r\n * generated by rose\r\n */\r\n");
			
			// package declaration
			writer.write("package " + metadata.getDtopackage() + ";\r\n\r\n");
			
			if(metadata.isUsingAnnotations())
				writeAnnotationHeader(writer);
			
			writeClassDeclaration(entityModel, metadata.isUsingTimestamp(), metadata.isImplementDto(), writer);			
			
			writer.write("{\r\n");
			
			writeIdDeclarations(writer);
			writeFieldDeclarations(entityModel, writer, metadata.isUsingTimestamp());
			
			writeConstructors(entityModel, writer);
			
			writeIdGettersSetters(entityModel, metadata.isImplementDto(), writer);
			writeGettersSetters(entityModel,  writer);
			if(metadata.isImplementDto())
				writeDtoMethods(entityModel, writer);
			writeToString(entityModel, writer);

			if(entityModel.getImplInterface().doesExtend(ImplInterface.IDENTIFYABLE))
				writeCompareById(entityModel, writer);

			writer.write("}\r\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			throw new RoseException("error creating java model classes", e);
		}
	}

	private static String getFullPath(final EntityModel entityModel, final MetaData metadata, final String parentDir) {
		final String fullpath = parentDir + "/" + metadata.getSrcpath() + metadata.getDtopackage().replaceAll("\\.", "/") + "/" + entityModel.getSimpleClassName() + "Dto.java";
		return fullpath;
	}

	private static void writeAnnotationHeader( Writer writer) throws IOException
	{
		writer.write("import com.google.gson.annotations.*;\r\n\r\n");
	}
	
	private static void writeClassDeclaration( EntityModel entity, boolean timestamped, boolean implementsDto, Writer writer) throws IOException
	{
		writer.write("public class " + entity.getSimpleClassName() + "Dto implements Comparable<" + entity.getSimpleClassName() + "Dto>");
		if(implementsDto)
			writer.write(", bn.blaszczyk.rose.model.Dto");
		else if(entity.getImplInterface().doesExtend(ImplInterface.IDENTIFYABLE))
			writer.write(", bn.blaszczyk.rose.model.Identifyable");			
//		if(timestamped)
//			writer.write(", bn.blaszczyk.rose.model.Timestamped");
		writer.write( "\r\n");
	}
	
	private static void writeIdDeclarations(  Writer writer) throws IOException
	{
		writer.write("\t@SerializedName(\"id\")\r\n"
				+ "\tprivate int id = -1;\r\n"
				+ "\r\n");
	}
		
	private static void writeFieldDeclarations(final EntityModel entity, final Writer writer, final boolean usingTimestamp) throws IOException
	{
		for(final Field field : entity.getFields())
		{
			writer.write("\t@SerializedName(\"" + field.getName().toLowerCase() + "\")\r\n");
			if(field instanceof PrimitiveField)
			{
				final PrimitiveField primitiveField = (PrimitiveField) field;
				writer.write("\tprivate " + primitiveField.getType().getDtoType().getName() + " " + primitiveField.getName() + ";\r\n"
						+ "\r\n");
			}
			else if( field instanceof EnumField)
			{
				final EnumField enumField = (EnumField) field;
				writer.write("\tprivate " + enumField.getEnumType().getClassName() + " " + enumField.getName() + ";\r\n"
						+ "\r\n" );
			}
		}
		for(EntityField entityfield : entity.getEntityFields())
		{
			if(entityfield.getType().isSecondMany())
				writer.write("\t@SerializedName(\"" + entityfield.getName().toLowerCase() + "_ids\")\r\n"
						+ "\tprivate Integer[] " + entityfield.getName() + "Ids = null;\r\n"
						+ "\r\n"
						+ "\t@SerializedName(\"" + entityfield.getName().toLowerCase() + "_count\")\r\n"
						+ "\tprivate Integer " + entityfield.getName() + "Count = null;\r\n"
						+ "\r\n");
			else
				writer.write("\t@SerializedName(\"" + entityfield.getName().toLowerCase() + "_id\")\r\n"
						+ "\tprivate Integer " + entityfield.getName() + "Id = null;\r\n"
						+ "\r\n"
						+ "\t@SerializedName(\"" + entityfield.getName().toLowerCase() + "\")\r\n"
						+ "\tprivate " + entityfield.getEntityName() + "Dto " + entityfield.getName() + " = null;\r\n"
						+ "\r\n");
		}
//		writer.write("\t@SerializedName(\"type\")\r\n" 
//				+ "\tprivate String type = \"" + entity.getSimpleClassName() + "\";\r\n\r\n");
//		if(usingTimestamp)
//			writer.write("\t@SerializedName(\"timestamp\")\r\n" 
//					+ "\tprivate long timestamp;\r\n");
	}
	
	private static void writeConstructors(EntityModel entity, Writer writer) throws IOException
	{
		writer.write("\r\n\tpublic " + entity.getSimpleClassName() + "Dto()\r\n"
				+ "\t{\r\n"
				+ "\t}\r\n\r\n");
	}
	
	private static void writeIdGettersSetters(final EntityModel entity, final boolean implementsDto, final Writer writer) throws IOException
	{
		final String optionalOverride = ( implementsDto || entity.getImplInterface().doesExtend(ImplInterface.IDENTIFYABLE) ) 
				? "\t@Override\r\n" : "";
		writer.write(optionalOverride
				+ "\tpublic int getId()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn id;\r\n"
				+ "\t}\r\n\r\n" );
		writer.write(optionalOverride
				+ "\tpublic void setId( int id )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis.id = id;\r\n"
				+ "\t}\r\n\r\n");
	}
	
	private static void writeGettersSetters(EntityModel entity, Writer writer) throws IOException
	{
		// primitive and enum Fields
		for(Field field : entity.getFields())
			if( field instanceof PrimitiveField)
				writePrimitiveGetterSetter((PrimitiveField) field, writer);
			else if( field instanceof EnumField )
				writeEnumGetterSetter((EnumField) field, writer);
		
		// entity Fields
		for(EntityField entityField : entity.getEntityFields())
			if(entityField.getType().isSecondMany())
				writeEntityManyGetterSetter(entityField, writer);
			else
				writeEntityOneGetterSetter(entityField, writer);
//		writeTimestampGetterSetter(writer);
	}

	private static void writePrimitiveGetterSetter(final PrimitiveField primitiveField, final Writer writer) throws IOException
	{
		final String dtoType = primitiveField.getType().getDtoType().getName();
		writer.write("\tpublic " + dtoType + " " + getGetterName(primitiveField) + "()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn " + primitiveField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
		writer.write("\tpublic void " + getSetterName(primitiveField) + "( " + dtoType + " " + primitiveField.getName() + " )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis." + primitiveField.getName() + " = " + primitiveField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
	}

	private static void writeEnumGetterSetter(final EnumField enumField, final Writer writer) throws IOException
	{
		writer.write("\tpublic " + enumField.getEnumType().getClassName() + " " + getGetterName(enumField) + "()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn " + enumField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
		writer.write("\tpublic void " + getSetterName(enumField) + "( " + enumField.getEnumType().getClassName() + " " + enumField.getName() + " )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis." + enumField.getName() + " = " + enumField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
	}
	
	private static void writeEntityOneGetterSetter(final EntityField entityField, final Writer writer ) throws IOException
	{
		writer.write("\tpublic " + entityField.getEntityName() + "Dto " + getGetterName(entityField) + "()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn " + entityField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
		writer.write("\tpublic void " + getSetterName(entityField) + "( final " + entityField.getEntityName() + "Dto "	+ entityField.getName() + " )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis." + entityField.getName() + " = " + entityField.getName() + ";\r\n"
				+ "\t}\r\n\r\n" );
		writer.write("\tpublic Integer " + getIdGetterName(entityField) + "()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn " + entityField.getName() + "Id;\r\n"
				+ "\t}\r\n\r\n" );
		writer.write("\tpublic void " + getIdSetterName(entityField) + "( final Integer " 	+ entityField.getName() + "Id )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis." + entityField.getName() + "Id = " + entityField.getName() + "Id;\r\n"
				+ "\t}\r\n\r\n" );
	}
	
	private static void writeEntityManyGetterSetter(final EntityField entityField, final Writer writer ) throws IOException
	{
		writer.write("\tpublic Integer[] " + getIdGetterName(entityField) + "()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn " + entityField.getName() + "Ids;\r\n"
				+ "\t}\r\n\r\n" );
		writer.write("\tpublic void " + getIdSetterName(entityField) + "( Integer[] " 	+ entityField.getName() + "Ids )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis." + entityField.getName() + "Ids = " + entityField.getName() + "Ids;\r\n"
				+ "\t}\r\n\r\n" );
		writer.write("\tpublic Integer " + getCountGetterName(entityField) + "()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn " + entityField.getName() + "Count;\r\n"
				+ "\t}\r\n\r\n" );
		writer.write("\tpublic void " + getCountSetterName(entityField) + "( Integer " 	+ entityField.getName() + "Count )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis." + entityField.getName() + "Count = " + entityField.getName() + "Count;\r\n"
				+ "\t}\r\n\r\n" );
	}

	private static void writeTimestampGetterSetter(final Writer writer) throws IOException
	{
		writer.write("\tpublic long getTimestamp()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn timestamp;\r\n"
				+ "\t}\r\n\r\n" );
		writer.write("\tpublic void setTimestamp( long timestamp )\r\n"
				+ "\t{\r\n"
				+ "\t\tthis.timestamp = timestamp;\r\n"
				+ "\t}\r\n\r\n");
	}

	private static void writeCompareById(final EntityModel entity, final Writer writer ) throws IOException
	{
		writer.write("\t@Override\r\n"
				+ "\tpublic int compareTo(" + entity.getSimpleClassName() + "Dto that)\r\n"
				+ "\t{\r\n"
				+ "\t\treturn Integer.compare( this.getId(), that.getId() );\r\n"
				+ "\t}\r\n\r\n");
	}
	
	private static void writeToString(final EntityModel entity, final Writer writer ) throws IOException
	{
		final StringBuilder sb = new StringBuilder( entity.getSimpleClassName() + " : {");
		sb.append("id:\" + id + \"");
		for(Field field : entity.getFields() )
			sb.append(", " + field.getName() +  ":\" + " + field.getName() + " + \"");
		for(EntityField field : entity.getEntityFields() )
			if(field.getType().isSecondMany())
				sb.append(", " + field.getName() +  ":\" + java.util.Arrays.toString(" + field.getName() + "Ids" + ") + \"");
			else
				sb.append(", " + field.getName() +  ":\" + " + field.getName() + "Id" + " + \"");
		sb.append("}");
		writer.write("\t@Override\r\n"
				+ "\tpublic String toString()\r\n"
				+ "\t{\r\n"
				+ "\t\treturn \"" + sb.toString() + "\";\r\n"
				+ "\t}\r\n\r\n");
	}
	
	private static void writeDtoMethods(final EntityModel entity, final Writer writer) throws IOException
	{
		writer.write("\t@Override\r\n"
				+ "\tpublic Object getFieldValue(String fieldName)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(fieldName.toLowerCase())\r\n"
				+ "\t\t{\r\n");
		for(final Field field : entity.getFields())
			writer.write("\t\tcase \"" +field.getName().toLowerCase() + "\":\r\n"
					+ "\t\t\treturn " + field.getName() + ";\r\n");
		writer.write("\t\tdefault:\r\n"
				+ "\t\t\treturn null;\r\n"
				+ "\t\t}\r\n"
				+ "\t}\r\n\r\n");

		writer.write("\t@Override\r\n"
				+ "\tpublic Integer getEntityId(String fieldName)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(fieldName.toLowerCase())\r\n"
				+ "\t\t{\r\n");
		for(final EntityField field : entity.getEntityFields())
			if(!field.getType().isSecondMany())
				writer.write("\t\tcase \"" +field.getName().toLowerCase() + "\":\r\n"
					+ "\t\t\treturn " + field.getName() + "Id;\r\n");
		writer.write("\t\tdefault:\r\n"
				+ "\t\t\treturn null;\r\n"
				+ "\t\t}\r\n"
				+ "\t}\r\n\r\n");

		writer.write("\t@Override\r\n"
				+ "\tpublic Integer[] getEntityIds(String fieldName)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(fieldName.toLowerCase())\r\n"
				+ "\t\t{\r\n");
		for(final EntityField field : entity.getEntityFields())
			if(field.getType().isSecondMany())
				writer.write("\t\tcase \"" +field.getName().toLowerCase() + "\":\r\n"
					+ "\t\t\treturn " + field.getName() + "Ids;\r\n");
		writer.write("\t\tdefault:\r\n"
				+ "\t\t\treturn null;\r\n"
				+ "\t\t}\r\n"
				+ "\t}\r\n\r\n");

		writer.write("\t@Override\r\n"
				+ "\tpublic bn.blaszczyk.rose.model.Dto getEntity(String fieldName)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(fieldName.toLowerCase())\r\n"
				+ "\t\t{\r\n");
		for(final EntityField field : entity.getEntityFields())
			if(!field.getType().isSecondMany())
				writer.write("\t\tcase \"" +field.getName().toLowerCase() + "\":\r\n"
					+ "\t\t\treturn " + field.getName() + ";\r\n");
		writer.write("\t\tdefault:\r\n"
				+ "\t\t\treturn null;\r\n"
				+ "\t\t}\r\n"
				+ "\t}\r\n\r\n");

		writer.write("\t@Override\r\n"
				+ "\tpublic Integer getEntityCount(String fieldName)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(fieldName.toLowerCase())\r\n"
				+ "\t\t{\r\n");
		for(final EntityField field : entity.getEntityFields())
			if(field.getType().isSecondMany())
				writer.write("\t\tcase \"" +field.getName().toLowerCase() + "\":\r\n"
					+ "\t\t\treturn " + field.getName() + "Count;\r\n");
		writer.write("\t\tdefault:\r\n"
				+ "\t\t\treturn null;\r\n"
				+ "\t\t}\r\n"
				+ "\t}\r\n\r\n");

		writer.write("\t@Override\r\n"
				+ "\tpublic void setFieldValue(String fieldName, Object value)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(fieldName.toLowerCase())\r\n"
				+ "\t\t{\r\n");
		for(final Field field : entity.getFields())
		{
			writer.write("\t\tcase \"" +field.getName().toLowerCase() + "\":\r\n");
			if(field instanceof PrimitiveField)
						writer.write("\t\t\tthis." + field.getName() + " = (" + ((PrimitiveField)field).getType().getDtoType().getName() + ") value;\r\n");
			else // EnumField
				writer.write("\t\t\tthis." + field.getName() + " = (" + ((EnumField)field).getEnumType().getClassName() + ") value;\r\n");
			writer.write("\t\t\tbreak;\r\n");
		}
		writer.write("\t\t}\r\n"
				+ "\t}\r\n\r\n");

		writer.write("\t@Override\r\n"
				+ "\tpublic void setEntityId(String fieldName, Integer id)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(fieldName.toLowerCase())\r\n"
				+ "\t\t{\r\n");
		for(final EntityField field : entity.getEntityFields())
			if(!field.getType().isSecondMany())
				writer.write("\t\tcase \"" +field.getName().toLowerCase() + "\":\r\n"
						+ "\t\t\tthis." + field.getName() + "Id = id;\r\n"
						+ "\t\t\tbreak;\r\n");
		writer.write("\t\t}\r\n"
				+ "\t}\r\n\r\n");

		writer.write("\t@Override\r\n"
				+ "\tpublic void setEntityIds(String fieldName, Integer[] ids)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(fieldName.toLowerCase())\r\n"
				+ "\t\t{\r\n");
		for(final EntityField field : entity.getEntityFields())
			if(field.getType().isSecondMany())
				writer.write("\t\tcase \"" +field.getName().toLowerCase() + "\":\r\n"
						+ "\t\t\tthis." + field.getName() + "Ids = ids;\r\n"
						+ "\t\t\tbreak;\r\n");
		writer.write("\t\t}\r\n"
				+ "\t}\r\n\r\n");

		writer.write("\t@Override\r\n"
				+ "\tpublic void setEntity(String fieldName, bn.blaszczyk.rose.model.Dto entity)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(fieldName.toLowerCase())\r\n"
				+ "\t\t{\r\n");
		for(final EntityField field : entity.getEntityFields())
			if(!field.getType().isSecondMany())
				writer.write("\t\tcase \"" +field.getName().toLowerCase() + "\":\r\n"
						+ "\t\t\tthis." + field.getName() + " = (" + field.getEntityName() + "Dto) entity;\r\n"
						+ "\t\t\tbreak;\r\n");
		writer.write("\t\t}\r\n"
				+ "\t}\r\n\r\n");

		writer.write("\t@Override\r\n"
				+ "\tpublic void setEntityCount(String fieldName, Integer count)\r\n"
				+ "\t{\r\n"
				+ "\t\tswitch(fieldName.toLowerCase())\r\n"
				+ "\t\t{\r\n");
		for(final EntityField field : entity.getEntityFields())
			if(field.getType().isSecondMany())
				writer.write("\t\tcase \"" +field.getName().toLowerCase() + "\":\r\n"
						+ "\t\t\tthis." + field.getName() + "Count = count;\r\n"
						+ "\t\t\tbreak;\r\n");
		writer.write("\t\t}\r\n"
				+ "\t}\r\n\r\n");
	}

	public static void clear(final EntityModel entity, final MetaData metadata, final String parentDir)
	{
		final String fullPath = getFullPath(entity, metadata, parentDir);
		final File file = new File(fullPath);
		if(file.exists())
			file.delete();
	}
}
