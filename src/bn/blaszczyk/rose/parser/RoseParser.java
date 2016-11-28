package bn.blaszczyk.rose.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.creators.*;
import bn.blaszczyk.rose.model.*;

public class RoseParser {

	private final List<Entity> entities = new ArrayList<>();
	private final List<EnumType> enums = new ArrayList<>();
	private final MetaData metadata = new MetaData();

	private final EntityParser entityParser = new EntityParser(metadata);
	private final EnumParser enumParser = new EnumParser(metadata);
	
	private final InputStream stream;
	private File file;
	
	public RoseParser(File file) throws FileNotFoundException
	{
		this(new FileInputStream(file));
		this.file = file;
	}
	
	RoseParser(InputStream stream)
	{
		this.stream = stream;
	}
	
	public void parse()
	{
		Scanner scanner = new Scanner(stream);
		try
		{
			boolean requiresLink = false;
			while(scanner.hasNextLine())
			{
				String[] split = scanner.nextLine().trim().split("\\s+",3);
				if(split.length > 2 && split[0].equalsIgnoreCase("set") )
				{
					MetaDataParser.parseField(metadata, split[1], split[2]);
				}
				else if(split.length > 2 && split[0].equalsIgnoreCase("begin") && split[1].equalsIgnoreCase("entity"))
				{
					entities.add(entityParser.parseEntity(split[2], scanner));
					requiresLink = true;
				}
				else if(split.length > 2 && split[0].equalsIgnoreCase("begin") && split[1].equalsIgnoreCase("enum"))
				{
					enums.add(enumParser.parseEnum(split[2], scanner));
					requiresLink = true;
				}
				else if(split.length > 1 && split[0].equalsIgnoreCase("create"))
				{
					if(requiresLink)
						linkEntities();
					requiresLink = false;
					createFile(split[1]);
				}
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		finally 
		{
			scanner.close();
		}
	}
	
	List<Entity> getEntities()
	{
		return entities;
	}

	List<EnumType> getEnums()
	{
		return enums;
	}

	void createFile( String filetype )
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
		case "roseapplauncher":
			RoseAppLauncherCreator.createMain(entities, metadata);
			RoseAppLauncherCreator.createInitializer(entities, metadata);
			RoseAppLauncherCreator.copyRose(file,metadata);
			break;
		default:
			System.out.println( "Unknown Agrument: create " + filetype );
				
		}
	}
	
	protected void linkEntities() throws ParseException
	{
		Map<EntityField,Entity> originalFields = new HashMap<>();
		for(Entity entity : entities)
		{
			for(Field field : entity.getFields())
				if(field instanceof EnumField)
				{
					EnumField eField = ((EnumField)field);
					eField.setEnumType(getEnumType(eField.getEnumName()));
				}
			for(EntityField entityField : entity.getEntityFields())
				originalFields.put(entityField,entity);
		}
		for(EntityField entityField : originalFields.keySet())
		{
			entityField.setEntity(getEntityType(entityField.getEntityName()));
			EntityField counterpart = new EntityField(originalFields.get(entityField), entityField);
			entityField.setCouterpart(counterpart);
			entityField.getEntity().addEntityField(counterpart);
		}		
	}
	
	protected MetaData getMetaData()
	{
		return metadata;
	}

	private Entity getEntityType(String name) throws ParseException
	{
		for(Entity entity : entities)
			if( entity.getSimpleClassName().equalsIgnoreCase( name ) )
				return entity;
		throw new ParseException("Unknown Entity Type: \"" + name + "\"", 0);
	}
	
	private EnumType getEnumType(String name) throws ParseException
	{
		for(EnumType enumType : enums)
			if( enumType.getSimpleClassName().equalsIgnoreCase( name ) )
				return enumType;
		throw new ParseException("Unknown Enum Type: \"" + name + "\"", 0);
	}

}
