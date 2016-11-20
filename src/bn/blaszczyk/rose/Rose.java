// ROSE : Relational-Object-tranSlator-rosE
package bn.blaszczyk.rose;

import java.io.FileNotFoundException;
import java.text.ParseException;

public class Rose {
<<<<<<< HEAD

	public static void main(String[] args) {
		for (String roseFile : args) {
			try {
				RoseParser.parse(roseFile);
			} catch (FileNotFoundException | ParseException e) {
				e.printStackTrace();
			}
		}
=======
	
	public static void main(String[] args)
	{
		for(String arg : args )
			try
			{
				RoseParser.parse(arg);
			}
			catch (FileNotFoundException | ParseException  e)
			{
				e.printStackTrace();
			}
>>>>>>> e3450006b7e223e8bbf0cfa2314c3e939c431667
	}
}
