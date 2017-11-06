package bn.blaszczyk.rose.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rose.model.*;

public abstract class RoseParser
{
	private static final ClassLoader CLASS_LOADER = RoseParser.class.getClassLoader();

	public static RoseParser forFiles(final String parentPath)
	{
		return new RoseParser(parentPath)
		{
			@Override
			InputStream stream(final String path) throws RoseException
			{
				try
				{
					return new FileInputStream(path);
				}
				catch (final FileNotFoundException e)
				{
					throw new RoseException("rosefile not found: " + path, e);
				}
			}
		};
	}

	public static RoseParser forResources(final String parentPath)
	{
		return new RoseParser(parentPath)
		{
			@Override
			InputStream stream(final String path) throws RoseException
			{
				final InputStream stream = CLASS_LOADER.getResourceAsStream(path);
				if(stream == null)
					throw new RoseException("resource not found: " + path);
				return stream;
			}
		};
	}
	

	private final MetaData metadata = new MetaData();
	private final List<EntityModel> entities = new ArrayList<>();
	private final List<EnumModel> enums = new ArrayList<>();
	private final List<String> createOptions = new ArrayList<>();
	private final List<String> rosePaths = new ArrayList<>();

	private final EntityParser entityParser = new EntityParser(metadata);
	private final EnumParser enumParser = new EnumParser(metadata);

	private final String parentPath;
	
	private RoseParser(final String path)
	{
		final String normalizedPath = path.replaceAll("\\\\","/");
		this.parentPath = normalizedPath.substring(0, normalizedPath.lastIndexOf('/') + 1);
		rosePaths.add(normalizedPath);
	}
	
	public void parse() throws RoseException
	{
		parse(rosePaths.get(0));
		linkEntities();
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

	public List<String> getRosePaths()
	{
		return rosePaths;
	}
	
	abstract InputStream stream(final String path) throws RoseException;

	private void parse(final String path) throws RoseException
	{
		try(final Scanner scanner = new Scanner(stream(path)))
		{
			while(scanner.hasNextLine())
			{
				final String[] split = scanner.nextLine().trim().split("\\s+",3);
				if(matches(split,3,"set"))
				{
					MetaDataParser.parseField(metadata, split[1], split[2]);
				}
				else if(matches(split,3,"begin","entity"))
				{
					entities.add(entityParser.parseEntity(split[2], scanner));
				}
				else if(matches(split,3,"begin","enum"))
				{
					enums.add(enumParser.parseEnum(split[2], scanner));
				}
				else if(matches(split,2,"create"))
				{
					createOptions.add(split[1]);
				}
				else if(matches(split, 2, "import"))
				{
					parseImport(split[1]);
				}
			}
		}
	}

	private void parseImport(final String subPath) throws RoseException
	{
		final String newPath =  parentPath + subPath;
		if(rosePaths.contains(newPath))
			return;
		rosePaths.add(newPath);
		parse(newPath);
	}
	
	private void linkEntities() throws RoseException
	{
		final Map<EntityField,EntityModel> entityFieldsToLink = new LinkedHashMap<>();
		for(final EntityModel entityModel : entities)
		{
			for(final Field field : entityModel.getFields())
				if(field instanceof EnumField)
				{
					final EnumField enumField = ((EnumField)field);
					if(!enumField.isLinked())
						enumField.setEnumModel(getEnumModel(enumField.getEnumName()));
				}
			for(final EntityField entityField : entityModel.getEntityFields())
				if(!entityField.isLinked())
					entityFieldsToLink.put(entityField, entityModel);
		}
		//actual linking delayed to prevent ConcurrentModificationException at self-relations
		for(final Entry<EntityField, EntityModel> entry : entityFieldsToLink.entrySet())
		{
			final EntityField entityField = entry.getKey();
			final EntityModel entityModel = entry.getValue();
			entityField.setEntityModel(getEntityModel(entityField.getEntityName()));
			final EntityField counterpart = new EntityField(entityModel, entityField);
			entityField.setCouterpart(counterpart);
			entityField.getEntityModel().addEntityField(counterpart);
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
	
	private boolean matches(final String[] array, final int minLenght, final String... args)
	{
		return array.length >= minLenght && matches(array, args);
	}
	
	private boolean matches(final String[] array, final String... args )
	{
		if(array.length < args.length)
			return false;
		for(int i = 0; i < args.length; i++)
			if(!args[i].equalsIgnoreCase(array[i]))
				return false;
		return true;
	}

}
