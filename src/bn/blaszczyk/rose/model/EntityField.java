package bn.blaszczyk.rose.model;

public class EntityField implements Field{
	
	private Entity entity = null;
	
	private final String entityName;
	private final String name;
	private final String capitalName;
	
	private final RelationType type;
	
	private String counterName = null;
	private String counterCapitalName = null;
	private EntityField couterpart = null;

	public EntityField(String entityName, RelationType type, String name, String counterName)
	{
		this.entityName = entityName;
		this.type = type;
		this.name = name.substring(0, 1).toLowerCase() + name.substring(1);
		this.capitalName = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
		setCounterName(counterName);
	}
	
	public EntityField(String entityName, RelationType type, String name )
	{
		this(entityName, type, entityName, null);
	}
	
	public EntityField(String entityName, RelationType relationType)
	{
		this(entityName, relationType, entityName);
	}
	
	public EntityField( Entity entity, EntityField counterpart )
	{
		this.entityName = counterpart.getEntity().getObjectName();
		this.type = counterpart.getType().getInverse();		
		if(counterpart.getCounterName() == null)
			this.name = entity.getObjectName();
		else
			this.name = counterpart.getCounterName();
		this.capitalName = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);		
		setCounterName(counterpart.getName());
		this.entity = entity;
		setCouterpart(counterpart);
	}

	public Entity getEntity()
	{
		return entity;
	}
	
	public String getEntityName()
	{
		return entityName;
	}

	public void setEntity(Entity entity)
	{
		this.entity = entity;
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

	public void setCouterpart(EntityField counterpart)
	{
		this.couterpart = counterpart;
		if(counterName == null)
			setCounterName(counterpart.getName());
	}

	private void setCounterName(String counterName)
	{
		this.counterName = counterName;
		this.counterCapitalName = counterName == null ? null : this.counterName.substring(0, 1).toUpperCase() + this.counterName.substring(1);
	}	

	@Override
	public String toString()
	{
		return capitalName;
	}
}
