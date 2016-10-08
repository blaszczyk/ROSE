// ROSE : Relational-Object-tranSlator-rosE
package bn.blaszczyk.rose;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import bn.blaszczyk.rose.model.Entity;

public class Rose {
	
	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			System.out.println("Use:java -jar ROSE.jar filename");
			return;
		}
		try
		{
			List<Entity> entities = new ArrayList<>();
			MetaData metadata = new MetaData();
			RoseParser.parse(args[0],entities,metadata);
		}
		catch (FileNotFoundException | ParseException e)
		{
			e.printStackTrace();
		}
	}
}
