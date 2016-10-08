package bn.blaszczyk.rose;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bn.blaszczyk.rose.creators.CreateJavaModel;
import bn.blaszczyk.rose.creators.CreateSQL;
import bn.blaszczyk.rose.model.Entity;
import bn.blaszczyk.rose.model.EntityMember;
import bn.blaszczyk.rose.model.Member;
import bn.blaszczyk.rose.model.MemberType;

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
				setProperty(split[1], split[2], metadata);
			else if(split.length > 2 && split[0].equalsIgnoreCase("begin") && split[1].equalsIgnoreCase("entity"))
				parseEntity(split[2],  entities, scanner);
			else if(split.length > 1 && split[0].equalsIgnoreCase("create"))
				createFile(split[1], entities, metadata);				
		}
	}
	
	private static void setProperty( String propertyName, String propertyValue, MetaData metadata)
	{switch(propertyName.toLowerCase())
		{
		case "modelpackage":
			metadata.setModelpackage( propertyValue );
			break;
		case "annotations":
			metadata.setUsingAnnotations( Boolean.parseBoolean( propertyValue ) );
			break;
		case "srcpath":
			metadata.setSrcpath( propertyValue );
			break;
		case "sqlpath":
			metadata.setSqlpath( propertyValue );
			break;
		case "foreignkeys":
			metadata.setUsingForeignKeys( Boolean.parseBoolean(propertyValue));
			break;
		case "database":
			metadata.setDatabase( propertyValue );
			break;
		// more to come
		}		
	}
	
	private static void createFile( String filetype, List<Entity> entities, MetaData metadata )
	{
		switch(filetype.toLowerCase())
		{
		case "sqlcreate":
			CreateSQL.createCreateTables(entities, metadata);
			break;
		case "javamodels":
			for(Entity entity : entities)
				CreateJavaModel.createModel(entity, metadata);
			break;
		// TODO: java CRUD, hibernate.cfg.xml
		}
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
			else
				throw new ParseException("Invalid Member: " + line, 0);
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
