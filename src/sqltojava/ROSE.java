// ROSE : Relational-Object-tranSlator-rosE

package sqltojava;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

public class ROSE {

	
	public static void main(String... args)
	{
		if(args.length == 0)
		{
			System.out.println("Use:java -jar ROSE.jar filename");
			return;
		}
		try
		{
			List<Entity> entities = SQLParser.parseEntities(args[0]);
			for(Entity entity : entities)
				CreateJavaModel.createModel(entity, "src/",true);
		}
		catch (FileNotFoundException | ParseException e)
		{
			e.printStackTrace();
		}
	}
}
