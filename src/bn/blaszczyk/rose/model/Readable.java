package bn.blaszczyk.rose.model;

public interface Readable extends Identifyable{
	
	public String getEntityName();
	
	public int getFieldCount();
	public String getFieldName( int index );
	public Object getFieldValue( int index );
	
	public int getEntityCount();
	public Object getEntityValue( int index );
	public String getEntityName( int index );
	public RelationType getRelationType( int index );
	public Class<?> getEntityClass( int index );

}
