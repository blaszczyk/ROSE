package sqltojava;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

public class RoseParser {
	
	private static List<Entity> entites;
	
	public static void parse(String filename, List<Entity> entities, MetaData metadata) throws FileNotFoundException, ParseException
	{
		RoseParser.entites = entities;
		Scanner scanner = new Scanner(new FileInputStream(filename));
		String[] split;
		while(scanner.hasNextLine())
		{
			split = scanner.nextLine().trim().split("\\s+");
			if(split.length > 2 && split[0].equalsIgnoreCase("set") )
				switch(split[1].toLowerCase())
				{
				case "modelpackage":
					metadata.setModelpackage( split[2] );
					break;
				case "annotations":
					metadata.setUsingAnnotations( Boolean.parseBoolean( split[2] ) );
					break;
				case "modelpath":
					metadata.setModelpath( split[2] );
					break;
				case "sqlpath":
					metadata.setSqlpath( split[2] );
					break;
				// more to come
				}
			else if(split.length > 2 && split[0].equalsIgnoreCase("begin") && split[1].equalsIgnoreCase("entity"))
				entities.add( parseEntity(split[2], scanner));
			else if(split.length > 1 && split[0].equalsIgnoreCase("create"))
				switch(split[1].toLowerCase())
				{
				case "sqlcreate":
					CreateSQL.createCreateTables(entities, metadata);
					break;
				case "sqldelete":
					CreateSQL.createDeleteTables(entities, metadata);
					break;
				case "javamodels":
					for(Entity entity : entities)
						CreateJavaModel.createModel(entity, metadata);
					break;
				// TODO: java CRUD, hibernate.cfg.xml
				}
		}
	}
	
	
	private static Entity parseEntity(String sqlname, Scanner scanner) throws ParseException
	{
		Entity entity = new Entity(sqlname);
		Entity subentity;
		String line;
		String[] split;
		while(scanner.hasNextLine() && !( line = scanner.nextLine().trim() ).startsWith( "end entity" ) )
		{
			split = line.split("\\s+"); // Split at any number of Whilespaces
			if(split.length > 1 && isMemberType(split[1]) )
				if( split.length > 2)
					entity.addMember(new Member(split[0], split[1], split[2],false));
				else
					entity.addMember(new Member(split[0], split[1],false));
			else if( split.length == 1 && ( subentity = getEntityType(split[0], RoseParser.entites) ) != null )
				entity.addEntityMember(new EntityMember(subentity, null, false));
			else if( split.length > 1 && ( subentity = getEntityType(split[1], RoseParser.entites) ) != null )
				entity.addEntityMember(new EntityMember(subentity, split[0], false));
			else
				throw new ParseException("Invalid Member: " + line, 0);
		}
		return entity;
	}
	
	private static boolean isMemberType(String sqltype)
	{
		for(MemberType memberType : MemberType.values())
			if( sqltype.toLowerCase().startsWith( memberType.getSqlname().toLowerCase() ) )
				return true;
		return false;
	}
	
	private static Entity getEntityType(String sqltype, List<Entity> entities)
	{
		for(Entity entity : entities)
			if( entity.getSqlname().equalsIgnoreCase( sqltype ) )
				return entity;
		return null;
	}
}
