package bn.blaszczyk.rose.model;

public class EntityMember {
	
	private Entity entity;
	private String sqlname;
	private String javaname;
	private boolean many = false;
	private EntityMember couterpart = null;

	public EntityMember(Entity entity, String sqlname, boolean many)
	{
		this.entity = entity;
		this.many = many;
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
	}
	
	public EntityMember(Entity entity, String sqlname, String javaname, boolean many)
	{
		this(entity, sqlname, many);
		this.javaname = javaname;
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

	public EntityMember getCouterpart()
	{
		return couterpart;
	}

	public void setCouterpart(EntityMember couterpart)
	{
		this.couterpart = couterpart;
	}
	
	
	
}
