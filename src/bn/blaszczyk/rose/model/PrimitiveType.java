package bn.blaszczyk.rose.model;

public enum PrimitiveType {
	VARCHAR("varchar",String.class,"\"\"","%s"),
	CHAR("char",String.class,"\"\"","%s"),
	INT("int",Integer.class,"0","%s"),
	DATE("date",java.util.Date.class," ","new java.util.Date(%s)"),
	NUMERIC("numeric",java.math.BigDecimal.class,"0","new java.math.BigDecimal(%s)"),
	BOOLEAN("boolean",Boolean.class,"false","%s");
	
	private final String sqlname;
	private final String defValue;
	private final String defFormat;
	private final Class<?> javaType;
	
	private PrimitiveType(String sqlname, Class<?> javaType, String defValue, String defFormat )
	{
		this.javaType = javaType;
		this.sqlname = sqlname;
		this.defValue = defValue;
		this.defFormat = defFormat;
	}
	
	public String getSqlname()
	{
		return sqlname;
	}
	
	public Class<?> getJavaType()
	{
		return javaType;
	}

	public String getJavaname()
	{
		return javaType.getName();
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
