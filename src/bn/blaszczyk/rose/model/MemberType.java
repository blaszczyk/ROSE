package bn.blaszczyk.rose.model;

public enum MemberType {
	VARCHAR("varchar","String","\"\"","%s"),
	CHAR("char","String","\"\"","%s"),
	INT("int","Integer","0","%s"),
	DATE("date","java.util.Date"," ","new java.util.Date(%s)"),
	NUMERIC("numeric","java.math.BigDecimal","0","new java.math.BigDecimal(%s)"),
	BOOLEAN("boolean","Boolean","false","%s");
	// TODO:
	// FILE("file","java.io.File","", "new java.io.File(%s)")
	// ENUM  -> EntityMembers
	// DATETIME
	
	private String sqlname;
	private String javaname;
	private String defValue;
	private String defFormat;
	
	private MemberType(String sqlname, String javaname, String defValue, String defFormat)
	{
		this.sqlname = sqlname;
		this.javaname = javaname;
		this.defValue = defValue;
		this.defFormat = defFormat;
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


	
	
	
}
