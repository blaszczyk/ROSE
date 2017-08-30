package bn.blaszczyk.rose.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.RoseException;
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

	public RoseParser(final File file) throws RoseException
	{
		try
		{
			this.stream = new FileInputStream(file);
			this.file = file;
		}
		catch (final FileNotFoundException e)
		{
			throw new RoseException("rosefile not found", e);
		}
	}

	public RoseParser(final InputStream stream)
	{
		this.stream = stream;
	}
	
	public void parseAndCreate() throws RoseException
	{
		parseCreateOptional(true);
	}
	
	public void parse() throws RoseException
	{
		parseCreateOptional(false);
	}
	
	private void parseCreateOptional(final boolean executeCreate) throws RoseException
	{
		try(final Scanner scanner = new Scanner(stream))
		{
			boolean requiresLink = true;
			while(scanner.hasNextLine())
			{
				final String[] split = scanner.nextLine().trim().split("\\s+",3);
				if(split.length > 2 && split[0].equalsIgnoreCase("set") )
				{
					MetaDataParser.parseField(metadata, split[1], split[2]);
				}
				else if(split.length > 2 && split[0].equalsIgnoreCase("begin") && split[1].equalsIgnoreCase("entity"))
				{
					entities.add(entityParser.parseEntity(split[2], scanner));
				}
				else if(split.length > 2 && split[0].equalsIgnoreCase("begin") && split[1].equalsIgnoreCase("enum"))
				{
					enums.add(enumParser.parseEnum(split[2], scanner));
				}
				else if(split.length > 1 && split[0].equalsIgnoreCase("create") && executeCreate)
				{
					if(requiresLink)
						linkEntities();
					requiresLink = false;
					createFile(split[1]);
				}
			}
			if(requiresLink)
				linkEntities();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}
	
	public List<Entity> getEntities()
	{
		return entities;
	}

	public List<EnumType> getEnums()
	{
		return enums;
	}

	public MetaData getMetadata()
	{
		return metadata;
	}

	private void createFile( String filetype ) throws RoseException
	{
		switch(filetype.toLowerCase())
		{
		case "sqlcreate":
			Creator.createSql(this);
			break;
		case "persistence":
			Creator.createPersistence(this);
			break;
		case "javamodels":
			Creator.createJavaModel(this);
			break;
		case "javaparser":
			Creator.createJavaParser(this);
			break;
		case "rosefilecopy":
			Creator.copyRoseFile(this, file);
			break;
		default:
			throw new RoseException("Unknown Agrument: create " + filetype);
		}
	}
	
	public void linkEntities() throws RoseException
	{
		Map<EntityField,Entity> originalFields = new LinkedHashMap<>();
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

	private Entity getEntityType(String name) throws RoseException
	{
		for(Entity entity : entities)
			if( entity.getSimpleClassName().equalsIgnoreCase( name ) )
				return entity;
		throw new RoseException("Unknown Entity Type: \"" + name + "\"");
	}
	
	private EnumType getEnumType(String name) throws RoseException
	{
		for(EnumType enumType : enums)
			if( enumType.getSimpleClassName().equalsIgnoreCase( name ) )
				return enumType;
		throw new RoseException("Unknown Enum Type: \"" + name + "\"");
	}

}
