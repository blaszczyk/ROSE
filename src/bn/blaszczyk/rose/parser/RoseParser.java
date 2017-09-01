package bn.blaszczyk.rose.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rose.model.*;

public class RoseParser {

	private final MetaData metadata = new MetaData();
	private final List<EntityModel> entities = new ArrayList<>();
	private final List<EnumModel> enums = new ArrayList<>();
	private final List<String> createOptions = new ArrayList<>();

	private final EntityParser entityParser = new EntityParser(metadata);
	private final EnumParser enumParser = new EnumParser(metadata);

	private final InputStream stream;

	public RoseParser(final File file) throws RoseException
	{
		try
		{
			this.stream = new FileInputStream(file);
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
	
	public void parse() throws RoseException
	{
		try(final Scanner scanner = new Scanner(stream))
		{
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
				else if(split.length > 1 && split[0].equalsIgnoreCase("create"))
				{
					createOptions.add(split[1]);
				}
			}
			linkEntities();
		}
		catch (ParseException e)
		{
			throw new RoseException("error parsing rose file", e);
		}
	}
	
	public List<EntityModel> getEntities()
	{
		return entities;
	}

	public List<EnumModel> getEnums()
	{
		return enums;
	}

	public MetaData getMetadata()
	{
		return metadata;
	}
	
	public List<String> getCreateOptions()
	{
		return createOptions;
	}

	private void linkEntities() throws RoseException
	{
		Map<EntityField,EntityModel> originalFields = new LinkedHashMap<>();
		for(EntityModel entity : entities)
		{
			for(Field field : entity.getFields())
				if(field instanceof EnumField)
				{
					EnumField enumField = ((EnumField)field);
					if(!enumField.isLinked())
						enumField.setEnumModel(getEnumModel(enumField.getEnumName()));
				}
			for(EntityField entityField : entity.getEntityFields())
				if(!entityField.isLinked())
					originalFields.put(entityField,entity);
		}
		for(Map.Entry<EntityField, EntityModel> entry : originalFields.entrySet())
		{
			final EntityField entityField = entry.getKey();
			final EntityModel entityModel = entry.getValue();
			entityField.setEntityModel(getEntityModel(entityField.getEntityName()));
			EntityField counterpart = new EntityField(entityModel, entityField);
			entityField.setCouterpart(counterpart);
			entityField.getEntityModel().addEntityField(counterpart);
		}		
	}
	
	protected MetaData getMetaData()
	{
		return metadata;
	}

	private EntityModel getEntityModel(String name) throws RoseException
	{
		for(EntityModel entity : entities)
			if( entity.getSimpleClassName().equalsIgnoreCase( name ) )
				return entity;
		throw new RoseException("Unknown Entity Type: \"" + name + "\"");
	}
	
	private EnumModel getEnumModel(String name) throws RoseException
	{
		for(EnumModel enumModel : enums)
			if( enumModel.getSimpleClassName().equalsIgnoreCase( name ) )
				return enumModel;
		throw new RoseException("Unknown Enum Type: \"" + name + "\"");
	}

}
