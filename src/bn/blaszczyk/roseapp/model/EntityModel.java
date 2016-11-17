package bn.blaszczyk.roseapp.model;


public interface EntityModel {
	
	public String getName();
	public int getId();
	public Entity getEntity();
	
	public int getFieldCount();
	public String getFieldName( int index );
	public Object getFieldValue( int index );
	
	public int getEntityCount();
	public Object getEntityValue( int index );
	public String getEntityName( int index );
	public RelationType getRelationType( int index );
	public Class<?> getEntityClass( int index );
	
	public String getTableCols();

	public int getLength1( int index );	
	public int getLength2( int index );	
	
	// to create instances in Factory
	public EntityModel createModel( Entity entity );

}
