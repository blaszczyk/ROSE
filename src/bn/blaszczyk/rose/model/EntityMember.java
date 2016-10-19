package bn.blaszczyk.rose.model;

import bn.blaszczyk.roseapp.model.RelationType;

public class EntityMember {
	
	private Entity entity;
	private String name;
	private String counterName;
	private String capitalName;
	private String counterCapitalName;
	private RelationType type;
	private EntityMember couterpart = null;

	public EntityMember(Entity entity, RelationType type, String name, String counterName)
	{
		this.entity = entity;
		this.type = type;
		this.name = name;
		this.counterName = counterName;
		this.capitalName = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);;
		this.counterCapitalName = this.counterName.substring(0, 1).toUpperCase() + this.counterName.substring(1);
	}
	
	public EntityMember( Entity entity, EntityMember counterpart )
	{
		this(entity, counterpart.getType().getInverse(), counterpart.getCounterName(), counterpart.getName() );
		setCouterpart(counterpart);
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
	
	
	
	public String getCounterName()
	{
		return counterName;
	}

	public String getCounterCapitalName()
	{
		return counterCapitalName;
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
