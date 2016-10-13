package bn.blaszczyk.rose.model;

import java.text.ParseException;

public class Member {
	private String name;
	private String capitalName;
	private String sqltype;
	private MemberType type = null;
	private String defValue = null;
	
	public Member(String name, String sqltype, String defvalue) throws ParseException
	{
		this.name = name;
		this.capitalName = name.substring(0,1).toUpperCase() + name.substring(1);
		this.sqltype = sqltype;
		this.defValue = defvalue;
		for(MemberType memberType : MemberType.values() )
			if( sqltype.toLowerCase().startsWith( memberType.getSqlname().toLowerCase() ) )
				this.type = memberType;
		if(type == null)
			throw new ParseException("Unknown SQL type: " + sqltype, 0);
	}

	public Member(String name, String sqltype) throws ParseException
	{
		this(name,sqltype,null);
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
	
	
	
	
}
