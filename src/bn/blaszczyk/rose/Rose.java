// ROSE : Relational-Object-tranSlator-rosE
package bn.blaszczyk.rose;

import bn.blaszczyk.rose.creators.Creator;
import bn.blaszczyk.rose.parser.RoseParser;

public class Rose {
	
	public static final String VERSION_ID = "0.133";
	
	public static void main(final String[] args) 
	{
		for (final String arg : args) 			
			try
			{
				final RoseParser parser = RoseParser.forFiles(arg);
				parser.parse();
				for(final String file : parser.getRosePaths())
					System.out.println("File parsed: " + file);
				Creator.createAll(parser, "");
			}
			catch (RoseException e)
			{
				e.printStackTrace();
			}
	}
}
