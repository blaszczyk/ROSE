package sqltojava;

public enum MemberType {
	VARCHAR("varchar","String"),
	CHAR("char","String"),
	INT("int","int"),
	DATE("date","Date","java.util.Date"),
	NUMERIC("numeric","BigDecimal","java.math.BigDecimal");
	
	private String sqlname;
	private String javaname;
	private String javapackage = null;
	
	private MemberType(String sqlname, String javaname)
	{
		this.sqlname = sqlname;
		this.javaname = javaname;
	}

	private MemberType(String sqlname, String javaname, String javapackage)
	{
		this.sqlname = sqlname;
		this.javaname = javaname;
		this.javapackage = javapackage;
	}

	public String getSqlname()
	{
		return sqlname;
	}

	public String getJavaname()
	{
		return javaname;
	}

	public String getJavapackage()
	{
		return javapackage;
	}
	
	
	
}
