package bn.blaszczyk.rose.model;

import bn.blaszczyk.rose.interfaces.RelationType;

public class EntityMember {
	
	private Entity entity;
	private String name;
	private String capitalName;
	private RelationType type;
	private EntityMember couterpart = null;

	public EntityMember(Entity entity, RelationType type, String name)
	{
		this.entity = entity;
		this.type = type;
		this.name = name;
		if( name == null )
			this.name = entity.getJavaname();
		this.capitalName = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
	}
	
	public Entity getEntity()
	{
		return entity;
	}
	
	public RelationType getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
	}
	
	public String getCapitalName()
	{
		return capitalName;
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
