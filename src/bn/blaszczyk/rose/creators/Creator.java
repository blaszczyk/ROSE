package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.model.Entity;
import bn.blaszczyk.rose.model.EnumType;
import bn.blaszczyk.rose.parser.RoseParser;

public class Creator {
	
	public static void createJavaModel(final RoseParser parser) throws CreateException
	{
		final List<Entity> entities = parser.getEntities();
		final List<EnumType> enums = parser.getEnums();
		final MetaData metadata = parser.getMetadata();
		for(final EnumType enumType : enums)
			JavaEnumCreator.create(enumType, metadata);
		for(final Entity entity : entities)
		{
			if(metadata.isUsingInterfaces())
				JavaInterfaceCreator.create(entity, metadata);
			JavaModelCreator.create(entity, metadata);
		}
	}
	
	public static void createJavaParser(final RoseParser parser) throws CreateException
	{
		final List<Entity> entities = parser.getEntities();
		final MetaData metadata = parser.getMetadata();
		for(final Entity entity : entities)
			JavaParserCreator.create(entity, metadata);
	}

	public static void copyRoseFile(final RoseParser parser, final File origin) throws CreateException
	{
		final MetaData metadata = parser.getMetadata();
		final String copyName = metadata.getResourcepath() + metadata.getResourcepackage().replaceAll("\\.", "/") + "/" + origin.getName();
		final File copy = new File(copyName);
		try
		{
			copy.getParentFile().mkdirs();
			Files.copy(origin.toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("File created: " + copyName);
		}
		catch (final IOException e)
		{
			throw new CreateException("error copying rosefile", e);
		}
	}
	
	public static void createPersistence(final RoseParser parser) throws CreateException
	{
		final List<Entity> entities = parser.getEntities();
		final MetaData metadata = parser.getMetadata();
		PersistenceCreator.create(entities, metadata);
	}
	
	public static void createSql(final RoseParser parser) throws CreateException
	{
		final List<Entity> entities = parser.getEntities();
		final MetaData metadata = parser.getMetadata();
		SQLCreator.create(entities, metadata);
	}

}
