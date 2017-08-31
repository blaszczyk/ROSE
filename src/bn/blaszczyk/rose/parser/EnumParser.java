package bn.blaszczyk.rose.parser;

import java.text.ParseException;
import java.util.Scanner;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.model.EnumModel;

public class EnumParser {

	private final MetaData metadata;
	
	public EnumParser(MetaData metadata)
	{
		this.metadata = metadata;
	}

	public EnumModel parseEnum(String sqlname, Scanner scanner ) throws ParseException
	{
		EnumModel enumType = new EnumModel(sqlname,metadata.getModelpackage());
		String line;
		while(scanner.hasNextLine() && !( line = scanner.nextLine().trim() ).startsWith( "end enum" ) )
		{
			enumType.addOption(line);
		}
		return enumType;
	}
	
}
