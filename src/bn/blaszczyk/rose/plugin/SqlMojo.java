package bn.blaszczyk.rose.plugin;

import org.apache.maven.plugins.annotations.Mojo;

import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rose.creators.Creator;
import bn.blaszczyk.rose.parser.RoseParser;

@Mojo(name = "sql")
public class SqlMojo extends AbstractRoseMojo
{

	@Override
	void doExecute(final RoseParser parser) throws RoseException
	{
		Creator.createSql(parser, parentDir);
	}

}
