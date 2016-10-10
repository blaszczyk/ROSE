package bn.blaszczyk.rose.model;

public class EntityMember {
	
	private Entity entity;
	private String name;
	private String capitalName;
	private boolean many = false;
	private EntityMember couterpart = null;

	public EntityMember(Entity entity, String name, boolean many)
	{
		this.entity = entity;
		this.many = many;
		this.name = name;
		if( name == null )
			this.name = entity.getJavaname();
		this.capitalName = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
	}
	
	public Entity getEntity()
	{
		return entity;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getCapitalName()
	{
		return capitalName;
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
