package bn.blaszczyk.rose.model;

public enum PrimitiveType {
	VARCHAR("varchar",String.class,String.class,"\"\"","%s"),
	CHAR("char",String.class,String.class,"\"\"","%s"),
	INT("int",Integer.class,int.class,"0","%s"),
	DATE("date",java.util.Date.class,long.class," ","new java.util.Date(%s)"),
	NUMERIC("numeric",java.math.BigDecimal.class,String.class,"0","new java.math.BigDecimal(%s)"),
	BOOLEAN("boolean",Boolean.class,boolean.class,"false","%s");
	
	private final String sqlname;
	private final String defValue;
	private final String defFormat;
	private final Class<?> javaType;
	private final Class<?> dtoType;
	
	private PrimitiveType(final String sqlname, final Class<?> javaType, final Class<?> dtoType, final String defValue, final String defFormat )
	{
		this.javaType = javaType;
		this.dtoType = dtoType;
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

	public Class<?> getDtoType()
	{
		return dtoType;
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
