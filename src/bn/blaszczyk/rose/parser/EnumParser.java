package bn.blaszczyk.rose.parser;

import java.text.ParseException;
import java.util.Scanner;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.model.EnumType;

public class EnumParser {

	private final MetaData metadata;
	
	public EnumParser(MetaData metadata)
	{
		this.metadata = metadata;
	}

	public EnumType parseEnum(String sqlname, Scanner scanner ) throws ParseException
	{
		EnumType enumType = new EnumType(sqlname,metadata.getModelpackage());
		String line;
		while(scanner.hasNextLine() && !( line = scanner.nextLine().trim() ).startsWith( "end enum" ) )
		{
			enumType.addOption(line);
		}
		return enumType;
	}
	
}
