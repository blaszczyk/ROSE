package bn.blaszczyk.rose.plugin;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rose.parser.RoseParser;

abstract class AbstractRoseMojo extends AbstractMojo {

	@Parameter
	File rosefile;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		if(rosefile == null || !rosefile.exists() || !rosefile.isFile())
			return;
		try
		{
			final RoseParser parser = new RoseParser(rosefile);
			doExecute(parser);
		}
		catch(final RoseException e)
		{
			throw new MojoFailureException("failure executing rose",e);
		}
		catch(final Exception e)
		{
			throw new MojoExecutionException("error executing rose", e);
		}
	}

	abstract void doExecute(final RoseParser parser) throws RoseException;

}
