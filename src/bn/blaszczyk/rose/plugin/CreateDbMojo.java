package bn.blaszczyk.rose.plugin;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.apache.maven.plugins.annotations.Mojo;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rose.creators.SQLCreator;
import bn.blaszczyk.rose.model.Entity;
import bn.blaszczyk.rose.parser.RoseParser;
import bn.blaszczyk.rose.tools.DBConnection;

@Mojo(name = "createDb")
public class CreateDbMojo extends AbstractRoseMojo
{
	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";	
	
	@Override
	void doExecute(RoseParser parser) throws RoseException
	{
		parser.parse();
		final MetaData metadata = parser.getMetadata();
		ensureDbExistence(metadata);
		final List<Entity> entities = parser.getEntities();
		executeCreateTables(metadata, entities);
	}

	private void executeCreateTables(final MetaData metadata, final List<Entity> entities) throws RoseException
	{
		final String connectionString = String.format("jdbc:mysql://%s:%s/%s", metadata.getDbserver(), metadata.getDbport(), metadata.getDbname());
		DBConnection.connectToDatabase(MYSQL_DRIVER, connectionString, metadata.getDbuser(), metadata.getDbpassword());
		for(final Entity entity : entities)
		{
			final Writer writer = new StringWriter();
			SQLCreator.createTable(entity, metadata, writer);
			try
			{
				writer.flush();
			}
			catch (IOException e)
			{
				throw new RuntimeException("unexpected error", e);
			}
			final String sql = writer.toString();
			DBConnection.executeUpdate(sql);
		}
		DBConnection.closeConnection();
	}

	private void ensureDbExistence(final MetaData metadata) throws RoseException
	{
		final String connectionString = String.format("jdbc:mysql://%s:%s", metadata.getDbserver(), metadata.getDbport());
		DBConnection.connectToDatabase(MYSQL_DRIVER, connectionString, metadata.getDbuser(), metadata.getDbpassword());
		final String dbName = metadata.getDbname();
		if(!DBConnection.databaseExists(dbName))
			DBConnection.createDatabase(dbName);
		DBConnection.closeConnection();
	}

}
