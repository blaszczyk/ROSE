package sqltojava;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

public class RoseParser {
	
	public static void parse(String filename, List<? super Entity> entities, MetaData metadata) throws FileNotFoundException, ParseException
	{
		Scanner scanner = new Scanner(new FileInputStream(filename));
		String[] split;
		while(scanner.hasNextLine())
		{
			split = scanner.nextLine().trim().split("\\s+");
			if(split.length > 2 && split[0].equalsIgnoreCase("set") )
			{
				switch(split[1].toLowerCase())
				{
				case "modelpackage":
					metadata.setModelpackage( split[2] );
					break;
				case "annotations":
					metadata.setUsingAnnotations( Boolean.parseBoolean( split[2] ) );
					break;
				case "modelpath":
					metadata.setModelpath( split[2] );
					break;
				case "sqlpath":
					metadata.setSqlpath( split[2] );
					break;
				// more to come
				}
			}
			else if(split.length > 2 && split[0].equalsIgnoreCase("begin") && split[1].equalsIgnoreCase("entity"))
				entities.add( parseEntity(split[2], scanner));
		}
	}
	
	
	private static Entity parseEntity(String sqlname, Scanner scanner) throws ParseException
	{
		Entity Entity = new Entity(sqlname);
		String line;
		String[] split;
		while(scanner.hasNextLine() && !( line = scanner.nextLine().trim() ).startsWith( "end entity" ) )
		{
			split = line.split("\\s+"); // Split at any number of Whilespaces
			if(split.length < 2)
				throw new ParseException("Invalid Member: " + line, 0);
			Entity.addMember(new Member(split[0], split[1],false));
		}
		return Entity;
	}
	
}
