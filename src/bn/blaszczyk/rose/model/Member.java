package bn.blaszczyk.rose.model;

import java.text.ParseException;

public class Member {
	private String name;
	private String capitalName;
	private String sqltype;
	private MemberType type = null;
	private boolean primary = false;
	private String defvalue = null;
	
	public Member(String name, String sqltype, String defvalue, boolean primary) throws ParseException
	{
		this.name = name;
		this.capitalName = name.substring(0,1).toUpperCase() + name.substring(1);
		this.sqltype = sqltype;
		this.primary = primary;
		this.defvalue = defvalue;
		for(MemberType memberType : MemberType.values() )
			if( sqltype.toLowerCase().startsWith( memberType.getSqlname().toLowerCase() ) )
				this.type = memberType;
		if(type == null)
			throw new ParseException("Unknown SQL type: " + sqltype, 0);
	}

	public Member(String name, String sqltype, boolean primary) throws ParseException
	{
		this(name,sqltype,null,primary);
	}
	
	public boolean isPrimary()
	{
		return primary;
	}

	public void setPrimary(boolean primary)
	{
		this.primary = primary;
	}

	public String getDefvalue()
	{
		return defvalue;
	}

	public void setDefvalue(String defvalue)
	{
		this.defvalue = defvalue;
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
