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
			split = scanner.nextLine().trim().split("\\s+");
			if(split.length > 2 && split[0].equalsIgnoreCase("set") )
				MetaDataParser.parseMember(metadata, split[1], split[2]);
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
				JavaModelCreator.create(enumType, metadata);
			for(Entity entity : entities)
				JavaModelCreator.create(entity, metadata);
			break;
		case "javaparser":
			for(Entity entity : entities)
				JavaParserCreator.create(entity, metadata);
			break;
		case "javaentitymodels":
			for(Entity entity : entities)
				JavaEntityModelCreator.createModel(entity, metadata);
			JavaEntityModelCreator.createFactory(entities, metadata);
			break;
		case "javacontroller":
			for(Entity entity : entities)
				JavaControllerCreator.create(entity, metadata);
			JavaControllerCreator.create(entities, metadata);
			break;
		case "javamain":
			JavaMainCreator.create(entities, metadata);
			break;
		default:
			System.out.println( "Unknown Agrument: create " + filetype );
				
		}
	}

	private static void parseEntity(String sqlname, Scanner scanner ) throws ParseException
	{
		Entity entity = new Entity(sqlname,metadata.getModelpackage());
		Entity subentity;
		EnumType subenum;
		String line, command;
		String[] split;
		while(scanner.hasNextLine() && !( line = scanner.nextLine().trim() ).startsWith( "end entity" ) )
		{
			split = line.split("\\s+",2);
			command = split[0];
			if( (subenum = getEnumType(command)) != null )
			{
				if( split.length == 1) 
					entity.addEnumMember(new EnumMember(subenum, command));
				else 
				{
					split = line.split("\\s+",2);
					if( split.length == 1)
						entity.addEnumMember(new EnumMember(subenum, split[0]));
					else
						entity.addEnumMember(new EnumMember(subenum, split[0],split[1]));
				}
				continue;
			}
			else if(split.length == 1 )
				continue;
			if( isMemberType(command) )
			{
				split = split[1].split("\\s+",2);
				if(split.length == 2)
					entity.addMember(new Member(command, split[0], split[1]));
				else
					entity.addMember(new Member(command, split[0]));
			}
			else if( isRelationType(command) )
			{
				split = split[1].split("\\s+", 3);
				if( (subentity = getEntityType(split[0])) != null )
					if(split.length == 3)
						entity.addEntityMember(new EntityMember(subentity, getRelationType(command), split[1],split[2]));	
					else if(split.length == 2)
						entity.addEntityMember(new EntityMember(subentity, getRelationType(command), split[1],split[1]));					
					else
						entity.addEntityMember(new EntityMember(subentity, getRelationType(command),subentity.getObjectName(),entity.getObjectName()));			
			}
			else if( command.equalsIgnoreCase("tostring"))
			{
				if(split.length == 2)
					entity.setToString(split[1]);
			}
			else if( command.equalsIgnoreCase("tablecols"))
			{
				if(split.length == 2)
					entity.setTableCols(split[1]);
			}
			else 
				System.out.println("Invalid Member: " + line);
		}
		if(entity.getTableCols() == null)
		{
			if(entity.getMembers().size() > 0)
				entity.setTableCols("%" + entity.getMembers().get(0).getName());
			for(int i = 1; i < entity.getMembers().size(); i++)
				entity.setTableCols(entity.getTableCols() +";%" + entity.getMembers().get(i).getName());
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

	private static boolean isMemberType(String sqltype)
	{
		for(MemberType memberType : MemberType.values())
			if( sqltype.toLowerCase().startsWith( memberType.getSqlname().toLowerCase() ) )
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
