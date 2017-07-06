
package bn.blaszczyk.rose.plugin;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import bn.blaszczyk.rose.creators.CreateException;
import bn.blaszczyk.rose.creators.Creator;
import bn.blaszczyk.rose.parser.RoseParser;

@Mojo(name = "parser")
public class ParserMojo extends AbstractMojo {

	@Parameter(property="parser.rosefile", required=true)
	private String rosefile;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		final File file = new File(rosefile);
		try
		{
			final RoseParser parser = new RoseParser(file);
			parser.parse();
			parser.linkEntities();
			Creator.createJavaParser(parser);
		}
		catch (final FileNotFoundException e)
		{
			throw new MojoFailureException("rosefile not found: " + rosefile,e);
		}
		catch (final ParseException e)
		{
			throw new MojoFailureException("error parsing rosefile: " + rosefile,e);
		}
		catch (CreateException e)
		{
			throw new MojoFailureException("error creating javafile: " + rosefile,e);
		}
	}

}
