// ROSE : Relational-Object-tranSlator-rosE
package bn.blaszczyk.rose;

import java.io.FileNotFoundException;
import java.text.ParseException;


public class Rose {
	
	public static void main(String[] args)
	{
		String roseFile = "roland.rose";
//		if(args.length == 0)
//			System.out.println("Use:java -jar rose.jar filename");
//		for(String roseFile : args)
			try
			{
				RoseParser.parse(roseFile);
			}
			catch (FileNotFoundException | ParseException  e)
			{
				e.printStackTrace();
			}
	}
}
