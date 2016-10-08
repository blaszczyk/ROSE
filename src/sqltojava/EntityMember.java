package sqltojava;

public class EntityMember {
	
	private Entity entity;
	private String sqlname;
	private String javaname;
	private boolean many = false;
	
	public EntityMember(Entity entity, String sqlname, boolean many)
	{
		this.entity = entity;
		if(sqlname != null)
		{
			this.sqlname = sqlname;
			this.javaname = sqlname.toLowerCase();
		}
		else
		{
			this.sqlname = entity.getPrimary().getSqlname();
			this.javaname = entity.getJavaname();
		}
		this.many = many;
	}
	
	public Entity getEntity()
	{
		return entity;
	}
	
	public String getSqlname()
	{
		return sqlname;
	}
	
	public String getJavaname()
	{
		return javaname;
	}
	
	public boolean isMany()
	{
		return many;
	}
	
}
