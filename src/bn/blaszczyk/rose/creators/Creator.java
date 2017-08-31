package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rose.model.EntityModel;
import bn.blaszczyk.rose.model.EnumModel;
import bn.blaszczyk.rose.parser.RoseParser;

public class Creator {
	
	public static void createJavaModel(final RoseParser parser) throws RoseException
	{
		final List<EntityModel> entities = parser.getEntities();
		final List<EnumModel> enums = parser.getEnums();
		final MetaData metadata = parser.getMetadata();
		for(final EnumModel enumType : enums)
			JavaEnumCreator.create(enumType, metadata);
		for(final EntityModel entity : entities)
		{
			if(metadata.isUsingInterfaces())
				JavaInterfaceCreator.create(entity, metadata);
			JavaModelCreator.create(entity, metadata);
		}
	}
	
	public static void createJavaParser(final RoseParser parser) throws RoseException
	{
		final List<EntityModel> entities = parser.getEntities();
		final MetaData metadata = parser.getMetadata();
		for(final EntityModel entity : entities)
			JavaParserCreator.create(entity, metadata);
	}

	public static void copyRoseFile(final RoseParser parser, final File origin) throws RoseException
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
			throw new RoseException("error copying rosefile", e);
		}
	}
	
	public static void createPersistence(final RoseParser parser) throws RoseException
	{
		final List<EntityModel> entities = parser.getEntities();
		final MetaData metadata = parser.getMetadata();
		PersistenceCreator.create(entities, metadata);
	}
	
	public static void createSql(final RoseParser parser) throws RoseException
	{
		final List<EntityModel> entities = parser.getEntities();
		final MetaData metadata = parser.getMetadata();
		SQLCreator.create(entities, metadata);
	}

}
