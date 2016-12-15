package bn.blaszczyk.rose.model;

public class EnumField  extends AbstractField
{
	private EnumType enumType;
	private String enumName;
	private String defValue;

	public EnumField(String enumName, String name, String defValue)
	{
		super(name);
		this.enumName = enumName;
		this.defValue = defValue;
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
	
	public String getDefValue()
	{
		return defValue;
	}

}
