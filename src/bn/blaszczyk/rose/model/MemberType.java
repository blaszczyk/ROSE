package bn.blaszczyk.rose.model;

public enum MemberType {
	VARCHAR("varchar","String","\"\"","%s"),
	CHAR("char","String","\"\"","%s"),
	INT("int","Integer","0","%s"),
	DATE("date","Date"," ","new Date(%s)","java.util.Date"),
	NUMERIC("numeric","BigDecimal","0","new BigDecimal(%s)","java.math.BigDecimal"),
	BOOLEAN("boolean","Boolean","false","%s");
	
	private String sqlname;
	private String javaname;
	private String defValue;
	private String defFormat;
	private String javapackage;
	
	private MemberType(String sqlname, String javaname, String defValue, String defFormat, String javapackage)
	{
		this.sqlname = sqlname;
		this.javaname = javaname;
		this.defValue = defValue;
		this.defFormat = defFormat;
		this.javapackage = javapackage;
	}

	private MemberType(String sqlname, String javaname, String defValue, String defFormat)
	{
		this(sqlname, javaname, defValue, defFormat, null);
	}

	public String getSqlname()
	{
		return sqlname;
	}

	public String getJavaname()
	{
		return javaname;
	}

	public String getDefValue()
	{
		return defValue;
	}

	public String getDefFormat()
	{
		return defFormat;
	}

	public String getJavapackage()
	{
		return javapackage;
	}
	
	

	
	
	
}
