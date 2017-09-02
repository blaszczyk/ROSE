package bn.blaszczyk.rose.model;

import bn.blaszczyk.rose.RoseException;

public class PrimitiveField extends AbstractField
{
	private String sqlType;
	private PrimitiveType type = null;
	private String defValue = null;
	
	private int length1 = 1;
	private int length2 = 0;
	
	public PrimitiveField( String sqltype, String name, String defvalue) throws RoseException
	{
		super(name);
		this.sqlType = sqltype;
		this.defValue = defvalue;
		for(PrimitiveType primitiveType : PrimitiveType.values() )
			if( sqltype.toLowerCase().startsWith( primitiveType.getSqlname().toLowerCase() ) )
				this.type = primitiveType;
		String[] split;
		switch(type)
		{
		case VARCHAR:
		case CHAR:
			split = sqltype.split("\\(|\\)");
			if(split.length > 1)
				length1 = parseInt(split[1]);
			break;
		case NUMERIC:
			split = sqltype.split("\\(|\\)|\\,|\\.");
			if(split.length > 2)
			{
				length1 = parseInt(split[1]);
				length2 = parseInt(split[2]);
			}
			break;
		case DATE:
		case BOOLEAN:
		case INT:
			break;
		}
		if(type == null)
			throw new RoseException("Unknown SQL type: " + sqltype);
	}

	public PrimitiveField( String sqltype, String name ) throws RoseException
	{
		this(sqltype,name,null);
	}

	public String getDefValue()
	{
		if(defValue == null || defValue == "")
			return getType().getDefValue();
		return defValue;
	}

	public void setDefValue(String defValue)
	{
		this.defValue = defValue;
	}

	@Override
	public String getSqlType()
	{
		return sqlType;
	}

	public PrimitiveType getType()
	{
		return type;
	}

	public int getLength1()
	{
		return length1;
	}

	public int getLength2()
	{
		return length2;
	}
	
	private static int parseInt(final String number) throws RoseException
	{
		try
		{
			return Integer.parseInt(number);
		}
		catch(final NumberFormatException e)
		{
			throw new RoseException("error parsing primitive type int property from " + number, e);
		}
	}
	
}
