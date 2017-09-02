package bn.blaszczyk.rose.parser;

import java.util.Scanner;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rose.model.EnumModel;

public class EnumParser {

	private final MetaData metadata;
	
	public EnumParser(final MetaData metadata)
	{
		this.metadata = metadata;
	}

	public EnumModel parseEnum(final String sqlname, final Scanner scanner ) throws RoseException
	{
		final EnumModel enumType = new EnumModel(sqlname,metadata.getModelpackage());
		while(scanner.hasNextLine())
		{
			final String line = scanner.nextLine().trim();
			if(line.startsWith( "end enum" ))
				return enumType;
			enumType.addOption(line);
		}
		throw new RoseException("missing end enum for " + sqlname);
	}
	
}
