package bn.blaszczyk.rose;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bn.blaszczyk.rose.creators.*;
import bn.blaszczyk.rose.model.*;

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
				parseEntity(split[2],  entities, scanner);
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
			JavaControllerCreator.create(entities, metadata);
			break;
		// TODO: java CRUD, hibernate.cfg.xml
		default:
			System.out.println( "Unknown Agrument: create " + filetype );
				
		}
	}

	private static void parseMetaData(MetaData metadata, String property, String value)
	{
		
	}
	
	private static void parseEntity(String sqlname, List<Entity> entities, Scanner scanner) throws ParseException
	{
		Entity entity = new Entity(sqlname);
		Entity subentity;
		String line;
		String[] split;
		while(scanner.hasNextLine() && !( line = scanner.nextLine().trim() ).startsWith( "end entity" ) )
		{
			split = line.split("\\s+",3); // Split at any number of Whilespaces
			if(split.length > 1 && isMemberType(split[1]) )
				if( split.length > 2)
					entity.addMember(new Member(split[0], split[1], split[2],false));
				else
					entity.addMember(new Member(split[0], split[1],false));
			else if( split.length == 1 && ( subentity = getEntityType(split[0], entities) ) != null )
				entity.addEntityMember(new EntityMember(subentity, null, false));
			else if( split.length > 1 && ( subentity = getEntityType(split[1], entities) ) != null )
				entity.addEntityMember(new EntityMember(subentity, split[0], false));
			else if( split.length > 1 )
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
	
	private static Entity getEntityType(String classname, List<Entity> entities)
	{
		for(Entity entity : entities)
			if( entity.getClassname().equalsIgnoreCase( classname ) )
				return entity;
		return null;
	}
}
