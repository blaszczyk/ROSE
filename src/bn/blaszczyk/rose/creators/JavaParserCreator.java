package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.model.*;

public class JavaParserCreator {
	
	public static final String PARSE_METHOD = "parseMember";
	
	public static String getParserName(Entity entity, MetaData metadata)
	{
		return String.format(metadata.getParserformat(), entity.getSimpleClassName());
	}
	
	public static void create(Entity entity, MetaData metadata)
	{
		String classname = getParserName(entity, metadata);
		String fullpath = metadata.getSrcpath() + metadata.getParserpackage().replaceAll("\\.", "/") + "/" + classname + ".java";
		File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(FileWriter writer = new FileWriter(file))
		{
			// initial comment
			writer.write("/*\n * " + classname + ".java\n * generated by rose\n */\n\n");
			
			// package declaration
			writer.write("package " + metadata.getParserpackage() + ";\n\n");
			
			// class declaration
			writer.write("\npublic class " + classname + "\n{\n");
			
			// parseMember
			writer.write("\tpublic static void " + PARSE_METHOD + "( " + entity.getClassName() + " " + entity.getObjectName() 
						+ ", String name, String value ) throws java.text.ParseException\n\t{\n" );
			writer.write("\t\tswitch( name.toLowerCase() )\n\t\t{\n");
			for(Field field : entity.getFields())
				if( field instanceof PrimitiveField)
				{
					PrimitiveField primitiveField = (PrimitiveField) field;
					writer.write("\t\tcase \"" + primitiveField.getName().toLowerCase() + "\":\n\t\t\t" + entity.getObjectName() + "." + JavaModelCreator.getSetterName(primitiveField) + "( " );
					switch(primitiveField.getType())
					{
					case VARCHAR:
					case CHAR:
						writer.write( "value" );
						break;
					case INT:
						writer.write( "Integer.parseInt( value )" );
						break;
					case DATE:
						writer.write( "java.text.DateFormat.getDateInstance().parse( value )" );
						break;
					case NUMERIC:
						writer.write( "new java.math.BigDecimal( value )" );
						break;
					case BOOLEAN:
						writer.write( "Boolean.parseBoolean( value )" ) ;
					}	
					writer.write( " );\n\t\t\tbreak;\n" );
				}		
			writer.write("\t\tdefault:\n\t\t\tSystem.out.println( \"Unknown Primitive Field: \" + name + \" in " + entity.getSimpleClassName() + "\");\n" );
			writer.write("\t\t}\n\t}\n\n");

			
			
			writer.write("}\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	
}
