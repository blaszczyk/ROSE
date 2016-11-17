package bn.blaszczyk.rose.model;

import bn.blaszczyk.roseapp.model.RelationType;

public class EntityField implements Field{
	
	private Entity entity;
	private String name;
	private String counterName;
	private String capitalName;
	private String counterCapitalName;
	private RelationType type;
	private EntityField couterpart = null;

	public EntityField(Entity entity, RelationType type, String name, String counterName)
	{
		this.entity = entity;
		this.type = type;
		this.name = name;
		this.counterName = counterName;
		this.capitalName = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);;
		this.counterCapitalName = this.counterName.substring(0, 1).toUpperCase() + this.counterName.substring(1);
	}
	
	public EntityField( Entity entity, EntityField counterpart )
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

	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getCapitalName()
	{
		return capitalName;
	}
	
	@Override
	public String getSqlType()
	{
		return "int";
	}
	
	
	
	public String getCounterName()
	{
		return counterName;
	}

	public String getCounterCapitalName()
	{
		return counterCapitalName;
	}

	public EntityField getCouterpart()
	{
		return couterpart;
	}

	public void setCouterpart(EntityField couterpart)
	{
		this.couterpart = couterpart;
	}

	
	
	
}
