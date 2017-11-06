package bn.blaszczyk.rose.model;

public class EntityField  extends AbstractField
{	
	private EntityModel entityModel = null;
	
	private final String entityName;
	
	private final RelationType type;
	
	private String counterName = null;
	private String counterCapitalName = null;
	private EntityField couterpart = null;

	public EntityField(String entityName, RelationType type, String name, String counterName)
	{
		super( quantifedName(name, type) );
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
	
	public EntityField( EntityModel entityModel, EntityField counterpart )
	{
		super( quantifedName( entityModel, counterpart ) );
		this.entityName = entityModel.getSimpleClassName();
		this.type = counterpart.getType().getInverse();			
		this.entityModel = entityModel;
		setCounterName(counterpart.getName());
		setCouterpart(counterpart);
	}

	public EntityModel getEntityModel()
	{
		return entityModel;
	}
	
	public boolean isLinked()
	{
		return entityModel !=  null;
	}
	
	public String getEntityName()
	{
		return entityName;
	}

	public void setEntityModel(EntityModel entityModel)
	{
		this.entityModel = entityModel;
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

	private static String quantifedName(final EntityModel entityModel, final EntityField counterpart)
	{
		final String name = counterpart.getCounterName() == null ? entityModel.getObjectName() : counterpart.getCounterName();
		final RelationType type = counterpart.getType().getInverse();
		return quantifedName(name, type);
	}

	private static String quantifedName(final String name, final RelationType type)
	{
		return name + (type.isSecondMany() ? "s" : "");
	}
}
