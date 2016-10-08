package sqltojava;

import java.text.ParseException;

public class Member {
	private String sqlname;
	private String javaname;
	private MemberType type = null;
	private boolean primary = false;
	private String defvalue = null;
	
	public Member(String sqlname, String sqltype, boolean primary) throws ParseException
	{
		this.sqlname = sqlname;
		this.primary = primary;
		javaname = sqlname.toLowerCase();
		for(MemberType memberType : MemberType.values() )
			if( sqltype.toLowerCase().startsWith( memberType.getSqlname().toLowerCase() ) )
				this.type = memberType;
		if(type == null)
			throw new ParseException("Unknown SQL type: " + sqltype, 0);
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

	public String getSqlname()
	{
		return sqlname;
	}

	public String getJavaname()
	{
		return javaname;
	}

	public MemberType getType()
	{
		return type;
	}
	
	
	
	
}
