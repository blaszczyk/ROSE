package bn.blaszczyk.rose.model;

public enum DBType {
	MYSQL("mysql","com.mysql.jdbc.Driver","org.hibernate.dialect.MySQLDialect");
	
	private String name;
	private String jdbcDriver;
	private String dialect;
	
	private DBType(String name, String jdbcDriver, String dialect)
	{
		this.name = name;
		this.jdbcDriver = jdbcDriver;
		this.dialect = dialect;
	}
	

	public String getName()
	{
		return name;
	}

	public String getJdbcDriver()
	{
		return jdbcDriver;
	}

	public String getDialect()
	{
		return dialect;
	}

	public static DBType getType(String name)
	{
		for(DBType type : values())
			if(type.getName().equalsIgnoreCase(name))
				return type;
		return null;
	}
	
}
