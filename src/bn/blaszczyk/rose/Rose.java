// ROSE : Relational-Object-tranSlator-rosE
package bn.blaszczyk.rose;

import java.io.File;
import java.io.FileNotFoundException;

import bn.blaszczyk.rose.parser.RoseParser;

public class Rose {
	
	public static final String VERSION_ID = "0.108";
	
	public static void main(String[] args) 
	{
		for (String roseFile : args) 			
			try
			{
				new	RoseParser(new File(roseFile)).parseAndCreate();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
	}
}
