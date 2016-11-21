package bn.blaszczyk.rose.parser;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import bn.blaszczyk.rose.model.Entity;
import bn.blaszczyk.rose.model.EnumType;

public class ModelProvidingNonCreatingRoseParser extends RoseParser {
	
	@Override
	public void parse(String filename)
	{
		super.parse(filename);
		try
		{
			linkEntities();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public List<Entity> getEntities()
	{
		return Collections.unmodifiableList( super.getEntities() );
	}

	@Override
	public List<EnumType> getEnums()
	{
		return Collections.unmodifiableList( super.getEnums() );
	}

	@Override
	void createFile(String filetype)
	{
	}
	
}
