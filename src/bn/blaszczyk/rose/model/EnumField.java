package bn.blaszczyk.rose.model;

public class EnumField  extends AbstractField
{
	private EnumModel enumModel;
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

	public EnumModel getEnumType()
	{
		return enumModel;
	}

	public String getEnumName()
	{
		return enumName;
	}

	public void setEnumModel(EnumModel enumModel)
	{
		this.enumModel = enumModel;
		if(defValue == null)
			defValue = enumModel.iterator().next();
	}
	
	public String getDefValue()
	{
		return defValue;
	}

	public boolean isLinked()
	{
		return enumModel != null;
	}

}
