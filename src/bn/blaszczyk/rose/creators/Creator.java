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
	private static final String OPTION_JAVADTOS = "javadtos";
	private static final String OPTION_RETROFIT = "retrofit";
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

	public static void createJavaDtos(final RoseParser parser, final String parentDir) throws RoseException
	{
		final List<EntityModel> entities = parser.getEntities();
		final MetaData metadata = parser.getMetadata();
		for(final EntityModel entity : entities)
		{
			JavaDtoCreator.create(entity, metadata, parentDir);
		}
	}

	public static void createRetrofit(final RoseParser parser, final String parentDir) throws RoseException
	{
		RetrofitCreator.create(parser.getEntities(), parser.getMetadata(), parentDir);
	}
	
	public static void createJavaParser(final RoseParser parser, final String parentDir) throws RoseException
	{
		final List<EntityModel> entities = parser.getEntities();
		final MetaData metadata = parser.getMetadata();
		for(final EntityModel entity : entities)
			JavaParserCreator.create(entity, metadata, parentDir);
	}

	public static void copyRoseFiles(final RoseParser parser, final String parentDir) throws RoseException
	{
		final MetaData metadata = parser.getMetadata();
		final List<String> rosePaths = parser.getRosePaths();
		for(final String origin : rosePaths)
		{
			final String copyPath = new StringBuilder()
					.append(parentDir)
					.append("/")
					.append(metadata.getResourcepath())
					.append(metadata.getResourcepackage().replaceAll("\\.", "/"))
					.append("/")
					.append(origin.substring(origin.replaceAll("\\\\", "/").lastIndexOf('/') + 1))
					.toString();
			final File copyFile = new File(copyPath);
			copyFile.getParentFile().mkdirs();
			try
			{
				Files.copy(new File(origin).toPath(), copyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			catch (final IOException e)
			{
				throw new RoseException("error copying rosefile " + origin + " to " + copyPath, e);
			}
			System.out.println("File created: " + copyPath);
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
	
	public static void createAll(final RoseParser parser, final String parentDir) throws RoseException
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
			case OPTION_JAVADTOS:
				createJavaDtos(parser, parentDir);
				break;
			case OPTION_RETROFIT:
				createRetrofit(parser, parentDir);
				break;
			case OPTION_JAVAPARSER:
				createJavaParser(parser, parentDir);
				break;
			case OPTION_ROSEFILECOPY:
				copyRoseFiles(parser, parentDir);
				break;
			default:
				throw new RoseException("unknown option: create " + createOption);
			}
	}

}
