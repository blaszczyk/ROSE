package bn.blaszczyk.rose;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bn.blaszczyk.rose.creators.*;
import bn.blaszczyk.rose.model.*;
import bn.blaszczyk.roseapp.model.RelationType;

public class RoseParser {

	private static List<Entity> entities;
	private static List<EnumType> enums;
	private static MetaData metadata;
	
	public static void parse(String filename) throws FileNotFoundException, ParseException
	{
		entities = new ArrayList<>();
		enums = new ArrayList<>();
		metadata = new MetaData();
		Scanner scanner = new Scanner(new FileInputStream(filename));
		String[] split;
		while(scanner.hasNextLine())
		{
			split = scanner.nextLine().trim().split("\\s+",3);
			if(split.length > 2 && split[0].equalsIgnoreCase("set") )
				MetaDataParser.parseField(metadata, split[1], split[2]);
			else if(split.length > 2 && split[0].equalsIgnoreCase("begin") && split[1].equalsIgnoreCase("entity"))
				parseEntity(split[2], scanner);
			else if(split.length > 2 && split[0].equalsIgnoreCase("begin") && split[1].equalsIgnoreCase("enum"))
				parseEnum(split[2], scanner);
			else if(split.length > 1 && split[0].equalsIgnoreCase("create"))
				createFile(split[1]);				
		}
	}
	
	private static void createFile( String filetype )
	{
		switch(filetype.toLowerCase())
		{
		case "sqlcreate":
			SQLCreator.create(entities, metadata);
			break;
		case "hibernate":
			HibernateCreator.create(entities, enums, metadata);
			break;
		case "javamodels":
			for(EnumType enumType : enums)
				JavaEnumCreator.create(enumType, metadata);
			for(Entity entity : entities)
				JavaModelCreator.create(entity, metadata);
			break;
		case "javaparser":
			for(Entity entity : entities)
				JavaParserCreator.create(entity, metadata);
			break;
		case "javamain":
			JavaMainCreator.create(entities, metadata);
			break;
		default:
			System.out.println( "Unknown Agrument: create " + filetype );
				
		}
	}

	private static void parseEntity(String args, Scanner scanner ) throws ParseException
	{
		String[] split = args.split(":");
		ImplInterface implInterface = ImplInterface.Entity;
		if(split.length > 1)
			if(split[1].toLowerCase().contains("w"))
				implInterface = ImplInterface.Writable;
			else if(split[1].toLowerCase().contains("r"))
				implInterface = ImplInterface.Readable;
		Entity entity = new Entity(split[0].trim(),metadata.getModelpackage(), implInterface);
		String line;
		while(scanner.hasNextLine() && !( line = scanner.nextLine().trim() ).startsWith( "end entity" ) )
		{
			split = line.split("\\s+",2);
			String command = split[0];
			EnumType subenum;
			if( (subenum = getEnumType(command)) != null )
			{
				if( split.length == 1) 
					entity.addField(new EnumField(subenum));
				else 
				{
					split = line.split("\\s+",2);
					if( split.length == 1)
						entity.addField(new EnumField(subenum, split[0]));
					else
						entity.addField(new EnumField(subenum, split[0],split[1]));
				}
				continue;
			}
			else if(split.length == 1 )
				continue;
			if( isPrimitiveType(command) )
			{
				split = split[1].split("\\s+",2);
				if(split.length == 2)
					entity.addField(new PrimitiveField(command, split[0], split[1]));
				else
					entity.addField(new PrimitiveField(command, split[0]));
			}
			else if( isRelationType(command) )
			{
				split = split[1].split("\\s+", 3);
				Entity subentity;
				if( (subentity = getEntityType(split[0])) != null )
					if(split.length == 3)
						entity.addEntityField(new EntityField(subentity, getRelationType(command), split[1],split[2]), true);	
					else if(split.length == 2)
						entity.addEntityField(new EntityField(subentity, getRelationType(command), split[1],split[1]), true);					
					else
						entity.addEntityField(new EntityField(subentity, getRelationType(command),subentity.getObjectName(),entity.getObjectName()), true);			
			}
			else if( command.equalsIgnoreCase("tostring"))
			{
				if(split.length == 2)
					entity.setToString(split[1]);
			}
			else 
				System.out.println("Invalid Field: " + line);
		}
		entities.add(entity);
	}

	private static void parseEnum(String sqlname, Scanner scanner ) throws ParseException
	{
		EnumType enumType = new EnumType(sqlname,metadata.getModelpackage());
		String line;
		while(scanner.hasNextLine() && !( line = scanner.nextLine().trim() ).startsWith( "end enum" ) )
		{
			enumType.addOption(line);
		}
		enums.add(enumType);
	}

	private static boolean isPrimitiveType(String sqltype)
	{
		for(PrimitiveType primitiveType : PrimitiveType.values())
			if( sqltype.toLowerCase().startsWith( primitiveType.getSqlname().toLowerCase() ) )
				return true;
		return false;
	}
	
	private static boolean isRelationType(String name)
	{
		for(RelationType type : RelationType.values())
			if( name .equalsIgnoreCase( type.name() ) )
				return true;
		return false;
	}

	private static Entity getEntityType(String classname )
	{
		for(Entity entity : entities)
			if( entity.getSimpleClassName().equalsIgnoreCase( classname ) )
				return entity;
		return null;
	}
	
	private static EnumType getEnumType(String classname)
	{
		for(EnumType enumType : enums)
			if( enumType.getSimpleClassName().equalsIgnoreCase( classname ) )
				return enumType;
		return null;
	}
	
	private static RelationType getRelationType(String typeName)
	{
		for( RelationType type : RelationType.values() )
			if(type.name().equalsIgnoreCase(typeName))
				return type;
		return null;
	}
}
