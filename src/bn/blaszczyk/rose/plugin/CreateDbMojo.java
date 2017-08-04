
package bn.blaszczyk.rose.plugin;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.creators.CreateException;
import bn.blaszczyk.rose.creators.SQLCreator;
import bn.blaszczyk.rose.model.Entity;
import bn.blaszczyk.rose.parser.RoseParser;
import bn.blaszczyk.rose.tools.DBConnection;

@Mojo(name = "createDb")
public class CreateDbMojo extends AbstractMojo {

	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";	
	
	@Parameter(property="sql.rosefile", required=false)
	private String rosefile;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		if(rosefile == null)
			return;
		final File file = new File(rosefile);
		try
		{
			final RoseParser parser = new RoseParser(file);
			parser.parse();
			final MetaData metadata = parser.getMetadata();
			ensureDbExistence(metadata);
			final List<Entity> entities = parser.getEntities();
			executeCreateTables(metadata, entities);
		}
		catch (final FileNotFoundException e)
		{
			throw new MojoFailureException("rosefile not found: " + rosefile,e);
		}
		catch (CreateException e)
		{
			throw new MojoFailureException("error creating tables",e);
		}
		catch (IOException e)
		{
			throw new MojoFailureException("error generating create script",e);
		}
	}

	private void executeCreateTables(final MetaData metadata, final List<Entity> entities)
			throws CreateException, IOException
	{
		final String connectionString = String.format("jdbc:mysql://%s:%s/%s", metadata.getDbserver(), metadata.getDbport(), metadata.getDbname());
		DBConnection.connectToDatabase(MYSQL_DRIVER, connectionString, metadata.getDbuser(), metadata.getDbpassword());
		for(final Entity entity : entities)
		{
			final Writer writer = new StringWriter();
			SQLCreator.createTable(entity, metadata, writer);
			writer.flush();
			final String sql = writer.toString();
			DBConnection.executeUpdate(sql);
		}
		DBConnection.closeConnection();
	}

	private void ensureDbExistence(final MetaData metadata) throws CreateException
	{
		final String connectionString = String.format("jdbc:mysql://%s:%s", metadata.getDbserver(), metadata.getDbport());
		DBConnection.connectToDatabase(MYSQL_DRIVER, connectionString, metadata.getDbuser(), metadata.getDbpassword());
		final String dbName = metadata.getDbname();
		if(!DBConnection.databaseExists(dbName))
			DBConnection.createDatabase(dbName);
		DBConnection.closeConnection();
	}

}
