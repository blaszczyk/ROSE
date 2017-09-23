package bn.blaszczyk.rose.plugin;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rose.parser.RoseParser;

abstract class AbstractRoseMojo extends AbstractMojo {

	@Parameter
	File rosefile;
	
	String parentDir;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		if(rosefile == null || !rosefile.exists() || !rosefile.isFile())
		{
			getLog().warn("rosefile not found: " + rosefile);
			return;
		}
		try
		{
			final MavenProject project = (MavenProject) getPluginContext().get("project");
			parentDir = project.getBasedir().getAbsolutePath();
			
			final RoseParser parser = RoseParser.forFiles(rosefile.getAbsolutePath());
			parser.parse();
			for(final String file : parser.getRosePaths())
				getLog().info("parsed " + file);
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
