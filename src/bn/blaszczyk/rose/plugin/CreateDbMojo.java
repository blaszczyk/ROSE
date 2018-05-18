package bn.blaszczyk.rose.plugin;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.apache.maven.plugins.annotations.Mojo;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rose.creators.SQLCreator;
import bn.blaszczyk.rose.model.EntityField;
import bn.blaszczyk.rose.model.EntityModel;
import bn.blaszczyk.rose.parser.RoseParser;
import bn.blaszczyk.rose.tools.DBConnection;

@Mojo(name = "createDb")
public class CreateDbMojo extends AbstractRoseMojo
{
	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";	
	
	@Override
	void doExecute(final RoseParser parser) throws RoseException
	{
		final MetaData metadata = parser.getMetadata();
		ensureDbExistence(metadata);
		final List<EntityModel> entities = parser.getEntities();
		executeCreateScripts(metadata, entities);
	}

	private void executeCreateScripts(final MetaData metadata, final List<EntityModel> entities) throws RoseException
	{
		final String connectionString = String.format("jdbc:mysql://%s:%s/%s", metadata.getDbserver(), metadata.getDbport(), metadata.getDbname());
		getLog().info("connecting to " + connectionString);
		DBConnection.connectToDatabase(MYSQL_DRIVER, connectionString, metadata.getDbuser(), metadata.getDbpassword());
		for(final EntityModel entity : entities)
		{
			executeSql(w -> SQLCreator.createTable(entity, metadata, w));
			for(final EntityField field : entity.getEntityFields())
			{
				if(SQLCreator.needsManyToManyTable(field))
					executeSql(w -> SQLCreator.createManyToManyTable(field, w));
				if(SQLCreator.hasColumn(field))
					executeSql(w -> SQLCreator.createIndex(field, w));
			}
		}
		DBConnection.closeConnection();
	}
	
	private void executeSql(final WriterFiller writerFiller)
	{
		try
		{
			final Writer writer = new StringWriter();
			writerFiller.fill(writer);
			writer.flush();
			final String sql = writer.toString();
			getLog().info("executing: " + sql.replaceAll("\\s+", " ").trim());
			DBConnection.executeUpdate(sql);
		}
		catch (IOException e)
		{
			throw RoseException.wrap(e, "");
		}
	}

	private void ensureDbExistence(final MetaData metadata) throws RoseException
	{
		final String connectionString = String.format("jdbc:mysql://%s:%s", metadata.getDbserver(), metadata.getDbport());
		getLog().info("connecting to " + connectionString);
		DBConnection.connectToDatabase(MYSQL_DRIVER, connectionString, metadata.getDbuser(), metadata.getDbpassword());
		final String dbName = metadata.getDbname();
		if(!DBConnection.databaseExists(dbName))
		{
			getLog().info("creating schema: " + dbName);
			DBConnection.createDatabase(dbName);
		}
		else
			getLog().info("schema exists: " + dbName);
		DBConnection.closeConnection();
	}

	private static interface WriterFiller
	{
		public void fill(final Writer writer) throws IOException;
	}

}
