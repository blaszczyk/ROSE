package bn.blaszczyk.rose.interfaces;


public interface EntityModel {

	public String getName();
	public int getId();
	
	public int getMemberCount();
	public String getMemberName( int index );
	public Object getMemberValue( int index );
	
	public int getEntityCount();
	public Object getEntityMember( int index );
	public String getEntityName( int index );
	public boolean isEntityMany( int index );
	
	// to create instances is Factory
	public EntityModel createModel( Object object);
	
	/*
	 *  TODO: setters (parser?, controller?)
	 */
}
