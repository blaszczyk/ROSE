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
	
	public static void parse(String filename) throws FileNotFoundException, ParseException
	{
		List<Entity> entities = new ArrayList<>();
		MetaData metadata = new MetaData();
		Scanner scanner = new Scanner(new FileInputStream(filename));
		String[] split;
		while(scanner.hasNextLine())
		{
			split = scanner.nextLine().trim().split("\\s+");
			if(split.length > 2 && split[0].equalsIgnoreCase("set") )
				MetaDataParser.parseMember(metadata, split[1], split[2]);
			else if(split.length > 2 && split[0].equalsIgnoreCase("begin") && split[1].equalsIgnoreCase("entity"))
				parseEntity(split[2],  entities, scanner,metadata);
			else if(split.length > 1 && split[0].equalsIgnoreCase("create"))
				createFile(split[1], entities, metadata);				
		}
	}
	
	private static void createFile( String filetype, List<Entity> entities, MetaData metadata )
	{
		switch(filetype.toLowerCase())
		{
		case "sqlcreate":
			SQLCreator.create(entities, metadata);
			break;
		case "hibernate":
			HibernateCreator.create(entities, metadata);
			break;
		case "javamodels":
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
		// TODO: java CRUD
		default:
			System.out.println( "Unknown Agrument: create " + filetype );
				
		}
	}
	
	private static void parseEntity(String sqlname, List<Entity> entities, Scanner scanner, MetaData metadata) throws ParseException
	{
		Entity entity = new Entity(sqlname,metadata.getModelpackage());
		Entity subentity;
		String line, command;
		String[] split;
		while(scanner.hasNextLine() && !( line = scanner.nextLine().trim() ).startsWith( "end entity" ) )
		{
			split = line.split("\\s+",2);
			if(split.length == 1 )
				continue;
			command = split[0];
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
				split = split[1].split("\\s+", 2);
				if( (subentity = getEntityType(split[0], entities)) != null )
					if(split.length == 2)
						entity.addEntityMember(new EntityMember(subentity, getRelationType(command), split[1]));					
					else
						entity.addEntityMember(new EntityMember(subentity, getRelationType(command),null));			
			}
			else if( command.equalsIgnoreCase("tostring"))
			{
				if(split.length == 2)
					entity.setToString(split[1]);
			}
			else 
				System.out.println("Invalid Member: " + line);
		}
		entities.add(entity);
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
			if( name .equalsIgnoreCase( type.getName() ) )
				return true;
		return false;
	}

	private static Entity getEntityType(String classname, List<Entity> entities)
	{
		for(Entity entity : entities)
			if( entity.getSimpleClassName().equalsIgnoreCase( classname ) )
				return entity;
		return null;
	}
	
	private static RelationType getRelationType(String typeName)
	{
		for( RelationType type : RelationType.values() )
			if(type.getName().equalsIgnoreCase(typeName))
				return type;
		return null;
	}
}
