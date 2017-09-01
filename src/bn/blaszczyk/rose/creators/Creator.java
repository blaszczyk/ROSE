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

public class Creator
{
	
	private static final String OPTION_ROSEFILECOPY = "rosefilecopy";
	private static final String OPTION_JAVAPARSER = "javaparser";
	private static final String OPTION_JAVAMODELS = "javamodels";
	private static final String OPTION_PERSISTENCE = "persistence";
	private static final String OPTION_SQLCREATE = "sqlcreate";

	public static void createJavaModel(final RoseParser parser, final String parentDir) throws RoseException
	{
		final List<EntityModel> entities = parser.getEntities();
		final List<EnumModel> enums = parser.getEnums();
		final MetaData metadata = parser.getMetadata();
		for(final EnumModel enumType : enums)
			JavaEnumCreator.create(enumType, metadata, parentDir);
		for(final EntityModel entity : entities)
		{
			if(metadata.isUsingInterfaces())
				JavaInterfaceCreator.create(entity, metadata, parentDir);
			JavaModelCreator.create(entity, metadata, parentDir);
		}
	}
	
	public static void createJavaParser(final RoseParser parser, final String parentDir) throws RoseException
	{
		final List<EntityModel> entities = parser.getEntities();
		final MetaData metadata = parser.getMetadata();
		for(final EntityModel entity : entities)
			JavaParserCreator.create(entity, metadata, parentDir);
	}

	public static void copyRoseFile(final RoseParser parser, final String parentDir, final File origin) throws RoseException
	{
		final MetaData metadata = parser.getMetadata();
		final String copyName = parentDir + "/" + metadata.getResourcepath() + metadata.getResourcepackage().replaceAll("\\.", "/") + "/" + origin.getName();
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
	
	public static void createPersistence(final RoseParser parser, final String parentDir) throws RoseException
	{
		final List<EntityModel> entities = parser.getEntities();
		final MetaData metadata = parser.getMetadata();
		PersistenceCreator.create(entities, metadata, parentDir);
	}
	
	public static void createSql(final RoseParser parser, final String parentDir) throws RoseException
	{
		final List<EntityModel> entities = parser.getEntities();
		final MetaData metadata = parser.getMetadata();
		SQLCreator.create(entities, metadata, parentDir);
	}
	
	public static void createAll(final RoseParser parser, final String parentDir, final File roseFile) throws RoseException
	{
		for(final String createOption : parser.getCreateOptions())
			switch(createOption.toLowerCase())
			{
			case OPTION_SQLCREATE:
				createSql(parser, parentDir);
				break;
			case OPTION_PERSISTENCE:
				createPersistence(parser, parentDir);
				break;
			case OPTION_JAVAMODELS:
				createJavaModel(parser, parentDir);
				break;
			case OPTION_JAVAPARSER:
				createJavaParser(parser, parentDir);
				break;
			case OPTION_ROSEFILECOPY:
				copyRoseFile(parser, parentDir, roseFile);
				break;
			default:
				throw new RoseException("unknown option: create " + createOption);
			}
	}	

}
