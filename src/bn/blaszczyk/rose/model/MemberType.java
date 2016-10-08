package bn.blaszczyk.rose.model;

public enum MemberType {
	VARCHAR("varchar","String","\"\"","%s"),
	CHAR("char","String","\"\"","%s"),
	INT("int","int","0","%s"),
	DATE("date","Date","new Date()","new Date(%s)","java.util.Date"),
	NUMERIC("numeric","BigDecimal","BigDecimal.ZERO","new BigDecimal(%s)","java.math.BigDecimal"),
	BOOLEAN("tinyint","boolean","false","%s");
// works like this:
//	@Column(columnDefinition = "TINYINT(1)")
	
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
