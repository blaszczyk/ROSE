package bn.blaszczyk.rose.model;

public abstract class AbstractField implements Field {
	
	private final String name;
	private final String capitalName;
	
	public AbstractField(String name)
	{
		this.name = name.substring(0, 1).toLowerCase() + name.substring(1);
		this.capitalName = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getCapitalName()
	{
		return capitalName;
	}
	
	@Override
	public String getSqlType()
	{
		return "int";
	}

	@Override
	public String toString()
	{
		return name;
	}
	
}
