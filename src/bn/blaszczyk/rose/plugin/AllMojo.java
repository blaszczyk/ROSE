
package bn.blaszczyk.rose.plugin;
import java.io.File;
import java.io.FileNotFoundException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import bn.blaszczyk.rose.parser.RoseParser;

@Mojo(name = "all")
public class AllMojo extends AbstractMojo {

	@Parameter(property="all.rosefile", required=true)
	private String rosefile;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		final File file = new File(rosefile);
		try
		{
			final RoseParser parser = new RoseParser(file);
			parser.parseAndCreate();
		}
		catch (final FileNotFoundException e)
		{
			throw new MojoFailureException("rosefile not found: " + rosefile,e);
		}
	}

}
