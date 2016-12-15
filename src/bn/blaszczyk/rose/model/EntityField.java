package bn.blaszczyk.rose.model;

public class EntityField  extends AbstractField
{	
	private Entity entity = null;
	
	private final String entityName;
	
	private final RelationType type;
	
	private String counterName = null;
	private String counterCapitalName = null;
	private EntityField couterpart = null;

	public EntityField(String entityName, RelationType type, String name, String counterName)
	{
		super( quantify(name, type) );
		this.entityName = entityName;
		this.type = type;
		setCounterName(counterName);
	}
	
	public EntityField(String entityName, RelationType type, String name )
	{
		this(entityName, type, name, null);
	}
	
	public EntityField(String entityName, RelationType relationType)
	{
		this(entityName, relationType, entityName);
	}
	
	public EntityField( Entity entity, EntityField counterpart )
	{
		super( quantify( counterpart.getCounterName() == null ? entity.getObjectName() : counterpart.getCounterName() ,
				counterpart.getType().getInverse() ) );
		this.entityName = counterpart.getEntity().getObjectName();
		this.type = counterpart.getType().getInverse();			
		this.entity = entity;
		setCounterName(counterpart.getName());
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
	
	private static String quantify(String name, RelationType type)
	{
		return name + (type.isSecondMany() ? "s" : "");
	}
}
