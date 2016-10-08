// ROSE : Relational-Object-tranSlator-rosE

package sqltojava;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

public class SQLtoJava {


	
	public static void main(String... args)
	{
		if(args.length == 0)
		{
			System.out.println("Use: SQLtoJava.jar filename");
			return;
		}
		try
		{
			List<Pojotable> pojotables = SQLParser.parsePojotables(args[0]);
			for(Pojotable pojotable : pojotables)
				CreateJavaModel.createModel(pojotable, "src/" , false);
		}
		catch (FileNotFoundException | ParseException e)
		{
			e.printStackTrace();
		}
	}
}
