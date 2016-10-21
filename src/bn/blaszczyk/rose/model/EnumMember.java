package bn.blaszczyk.rose.model;

public class EnumMember {
	
	private EnumType enumType;
	private String name;
	private String capitalName;
	private String defValue;

	public EnumMember(EnumType enumType, String name, String defValue)
	{
		this.name = name;
		this.enumType = enumType;
		this.defValue = defValue;
		this.capitalName = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
	}
	
	
	
	public EnumMember(EnumType enumType, String name)
	{
		this(enumType, name, enumType.iterator().next());
	}



	public EnumType getEnumType()
	{
		return enumType;
	}

	public String getName()
	{
		return name;
	}
	
	public String getCapitalName()
	{
		return capitalName;
	}



	public String getDefValue()
	{
		return defValue;
	}

	
}
