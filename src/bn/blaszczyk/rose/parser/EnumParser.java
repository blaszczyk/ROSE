package bn.blaszczyk.rose.parser;

import java.text.ParseException;
import java.util.Scanner;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.model.EnumModel;

public class EnumParser {

	private final MetaData metadata;
	
	public EnumParser(final MetaData metadata)
	{
		this.metadata = metadata;
	}

	public EnumModel parseEnum(final String sqlname, final Scanner scanner ) throws ParseException
	{
		final EnumModel enumType = new EnumModel(sqlname,metadata.getModelpackage());
		String line;
		while(scanner.hasNextLine() && !( line = scanner.nextLine().trim() ).startsWith( "end enum" ) )
		{
			enumType.addOption(line);
		}
		return enumType;
	}
	
}
