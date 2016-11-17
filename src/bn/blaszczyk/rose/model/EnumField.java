package bn.blaszczyk.rose.model;

public class EnumField implements Field{
	
	private EnumType enumType;
	private String name;
	private String capitalName;
	private String defValue;

	public EnumField(EnumType enumType, String name, String defValue)
	{
		this.name = name;
		this.enumType = enumType;
		this.defValue = defValue;
		this.capitalName = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
	}
	

	
	public EnumField(EnumType enumType, String name)
	{
		this(enumType, name, enumType.iterator().next());
	}
	
	public EnumField(EnumType enumType )
	{
		this(enumType, enumType.getObjectName() );
	}



	public EnumType getEnumType()
	{
		return enumType;
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
