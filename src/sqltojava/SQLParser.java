package sqltojava;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SQLParser {

	private static String javapackage;
	
	public static List<Entity> parseEntities(String filename) throws FileNotFoundException, ParseException
	{
		Scanner scanner = new Scanner(new FileInputStream(filename));
		String[] split;
		List<Entity> Entities = new ArrayList<>();
		while(scanner.hasNextLine())
		{
			split = scanner.nextLine().split("\\s+");
			if(split[0].startsWith("--@") && split.length > 2 )
			{
				switch(split[1].toLowerCase())
				{
				case "package":
					javapackage = split[2];
					break;
				// more to come
				}
			}
			else if(split.length > 2 && split[0].equalsIgnoreCase("create") && split[1].equalsIgnoreCase("table"))
				Entities.add( parseEntity(split[2], scanner));			
		}
		return Entities;
	}
	
	
	private static Entity parseEntity(String sqlname, Scanner scanner) throws ParseException
	{
		Entity Entity = new Entity(sqlname,javapackage);
		String line;
		String[] split;
		while(scanner.hasNextLine() && !( line = scanner.nextLine() ).trim().startsWith( ")" ) )
		{
			split = line.trim().split("\\s+"); // Split at any number of Whilespaces
			if(split.length < 2)
				throw new ParseException("Invalid Column: " + line, 0);
			if(split[0].toLowerCase().equals("constraint"))
			{
				if(split.length < 5)
					throw new ParseException("Invalid Constraint: " + line,0);
				switch((split[2]+split[3]).toLowerCase())
				{
				case "primarykey":
					Entity.setPrimaryKey(split[4]);
					break;
				case "foreignkey":
					break;
				default:
					throw new ParseException("Invalid Constraint: " + line,0);
				}				
			}
			else
				Entity.addMember(new Member(split[0], split[1]));
		}
		return Entity;
	}
	
}
