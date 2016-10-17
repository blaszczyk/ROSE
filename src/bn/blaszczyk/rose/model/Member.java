package bn.blaszczyk.rose.model;

import java.text.ParseException;

public class Member {
	private String name;
	private String capitalName;
	private String sqltype;
	private MemberType type = null;
	private String defValue = null;
	
	private int length1 = 1;
	private int length2 = 0;
	
	public Member( String sqltype, String name, String defvalue) throws ParseException
	{
		this.name = name;
		this.capitalName = name.substring(0,1).toUpperCase() + name.substring(1);
		this.sqltype = sqltype;
		this.defValue = defvalue;
		for(MemberType memberType : MemberType.values() )
			if( sqltype.toLowerCase().startsWith( memberType.getSqlname().toLowerCase() ) )
				this.type = memberType;
		String[] split;
		switch(type)
		{
		case VARCHAR:
		case CHAR:
			split = sqltype.split("\\(|\\)");
			if(split.length > 1)
				length1 = Integer.parseInt(split[1]);
			break;
		case NUMERIC:
			split = sqltype.split("\\(|\\)|\\,|\\.");
			if(split.length > 2)
			{
				length1 = Integer.parseInt(split[1]);
				length2 = Integer.parseInt(split[2]);
			}
			break;
		case DATE:
		case BOOLEAN:
		case INT:
			break;
		}
		if(type == null)
			throw new ParseException("Unknown SQL type: " + sqltype, 0);
	}

	public Member( String sqltype, String name ) throws ParseException
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

	public String getName()
	{
		return name;
	}
	
	public String getCapitalName()
	{
		return capitalName;
	}

	public String getSqltype()
	{
		return sqltype;
	}

	public MemberType getType()
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
	
	
	
	
	
	
}
