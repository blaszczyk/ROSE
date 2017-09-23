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
		executeCreateTables(metadata, entities);
	}

	private void executeCreateTables(final MetaData metadata, final List<EntityModel> entities) throws RoseException
	{
		final String connectionString = String.format("jdbc:mysql://%s:%s/%s", metadata.getDbserver(), metadata.getDbport(), metadata.getDbname());
		getLog().info("connecting to " + connectionString);
		DBConnection.connectToDatabase(MYSQL_DRIVER, connectionString, metadata.getDbuser(), metadata.getDbpassword());
		for(final EntityModel entity : entities)
		{
			final Writer writer = new StringWriter();
			SQLCreator.createTable(entity, metadata, writer);
			executeCreateTable(writer);
			for(final EntityField field : entity.getEntityFields())
				if(SQLCreator.needsManyToManyTable(field))
				{
					final Writer writer2 = new StringWriter();
					SQLCreator.createManyToManyTable(field, writer2);
					executeCreateTable(writer2);
				}
					
		}
		DBConnection.closeConnection();
	}
	
	private void executeCreateTable(final Writer writer) throws RoseException
	{
		try
		{
			writer.flush();
		}
		catch (IOException e)
		{
			throw new RuntimeException("unexpected error", e);
		}
		final String sql = writer.toString();
		getLog().info("executing: " + sql.replaceAll("\\s+", " ").trim());
		DBConnection.executeUpdate(sql);
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

}
