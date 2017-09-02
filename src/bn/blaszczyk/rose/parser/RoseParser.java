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
			throw new RoseException("error parsing meta data file", e);
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
		for(EntityModel entityModel : entities)
		{
			for(Field field : entityModel.getFields())
				if(field instanceof EnumField)
				{
					final EnumField enumField = ((EnumField)field);
					if(!enumField.isLinked())
						enumField.setEnumModel(getEnumModel(enumField.getEnumName()));
				}
			for(EntityField entityField : entityModel.getEntityFields())
				if(!entityField.isLinked())
				{
					entityField.setEntityModel(getEntityModel(entityField.getEntityName()));
					final EntityField counterpart = new EntityField(entityModel, entityField);
					entityField.setCouterpart(counterpart);
					entityField.getEntityModel().addEntityField(counterpart);
				}
		}
	}

	private EntityModel getEntityModel(final String name) throws RoseException
	{
		for(final EntityModel entityModel : entities)
			if( entityModel.getSimpleClassName().equalsIgnoreCase( name ) )
				return entityModel;
		throw new RoseException("unknown entity type: \"" + name + "\"");
	}
	
	private EnumModel getEnumModel(final String name) throws RoseException
	{
		for(final EnumModel enumModel : enums)
			if( enumModel.getSimpleClassName().equalsIgnoreCase( name ) )
				return enumModel;
		throw new RoseException("unknown enum type: \"" + name + "\"");
	}

}
