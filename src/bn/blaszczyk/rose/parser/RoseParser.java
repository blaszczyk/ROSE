package bn.blaszczyk.rose.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.creators.*;
import bn.blaszczyk.rose.model.*;

public class RoseParser {

	private final List<Entity> entities = new ArrayList<>();
	private final List<EnumType> enums = new ArrayList<>();
	private final MetaData metadata = new MetaData();

	private final EntityParser entityParser = new EntityParser(metadata,this);
	private final EnumParser enumParser = new EnumParser(metadata);
	
	
	public void parse(String filename)
	{
		try(Scanner scanner = new Scanner(new FileInputStream(filename)))
		{
			String[] split;
			while(scanner.hasNextLine())
			{
				split = scanner.nextLine().trim().split("\\s+",3);
				if(split.length > 2 && split[0].equalsIgnoreCase("set") )
					MetaDataParser.parseField(metadata, split[1], split[2]);
				else if(split.length > 2 && split[0].equalsIgnoreCase("begin") && split[1].equalsIgnoreCase("entity"))
					entities.add(entityParser.parseEntity(split[2], scanner));
				else if(split.length > 2 && split[0].equalsIgnoreCase("begin") && split[1].equalsIgnoreCase("enum"))
					enums.add(enumParser.parseEnum(split[2], scanner));
				else if(split.length > 1 && split[0].equalsIgnoreCase("create"))
					createFile(split[1]);				
			}
		}
		catch (FileNotFoundException | ParseException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void createFile( String filetype )
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

	public Entity getEntityType(String classname )
	{
		for(Entity entity : entities)
			if( entity.getSimpleClassName().equalsIgnoreCase( classname ) )
				return entity;
		return null;
	}
	
	public EnumType getEnumType(String classname)
	{
		for(EnumType enumType : enums)
			if( enumType.getSimpleClassName().equalsIgnoreCase( classname ) )
				return enumType;
		return null;
	}

}
