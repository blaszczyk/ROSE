package bn.blaszczyk.rose.model;

public class EnumField implements Field{
	
	private EnumType enumType;
	private String enumName;
	private String name;
	private String capitalName;
	private String defValue;

	public EnumField(String enumName, String name, String defValue)
	{
		this.enumName = enumName;
		this.defValue = defValue;
		this.name = name.substring(0, 1).toLowerCase() + name.substring(1);
		this.capitalName = name.substring(0, 1).toUpperCase() + name.substring(1);
	}
		
	public EnumField(String enumName, String name)
	{
		this(enumName, name, null);
	}
	
	public EnumField( String enumName )
	{
		this(enumName, enumName );
	}

	public EnumType getEnumType()
	{
		return enumType;
	}

	public String getEnumName()
	{
		return enumName;
	}

	public void setEnumType(EnumType enumType)
	{
		this.enumType = enumType;
		if(defValue == null)
			defValue = enumType.iterator().next();
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



	public String getDefValue()
	{
		return defValue;
	}



	@Override
	public String getSqlType()
	{
		return "int";
	}

	
}
