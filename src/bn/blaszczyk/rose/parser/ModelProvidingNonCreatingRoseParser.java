package bn.blaszczyk.rose.parser;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import bn.blaszczyk.rose.model.Entity;
import bn.blaszczyk.rose.model.EnumType;

public class ModelProvidingNonCreatingRoseParser extends RoseParser {
	
	private boolean isLinked = false;
	
	public ModelProvidingNonCreatingRoseParser(InputStream stream)
	{
		super(stream);
	}
	
	public String getMainClassAsString()
	{
		return getMetaData().getMainpackage() + "." + getMetaData().getMainname();
	}
	
	

	@Override
	protected void linkEntities() throws ParseException
	{
		if(!isLinked)
			super.linkEntities();
		isLinked = true;
	}

	@Override
	public void parse()
	{
		super.parse();
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
