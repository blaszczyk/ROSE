// ROSE : Relational-Object-tranSlator-rosE
package bn.blaszczyk.rose;

import java.io.File;

import bn.blaszczyk.rose.creators.Creator;
import bn.blaszczyk.rose.parser.RoseParser;

public class Rose {
	
	public static final String VERSION_ID = "0.115";
	
	public static void main(final String[] args) 
	{
		for (final String arg : args) 			
			try
			{
				final File roseFile = new File(arg);
				final RoseParser parser = new RoseParser(roseFile);
				parser.parse();
				Creator.createAll(parser, "", roseFile);
			}
			catch (RoseException e)
			{
				e.printStackTrace();
			}
	}
}
