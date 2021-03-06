package bn.blaszczyk.rose.model;

import java.util.Set;

public interface Readable extends Identifyable
{	
	public String getEntityName();
	
	public int getFieldCount();
	public String getFieldName( int index );
	public Object getFieldValue( int index );
	
	public int getEntityCount();
	public Readable getEntityValueOne( int index );
	public Set<? extends Readable> getEntityValueMany( int index );
	public String getEntityName( int index );
	public RelationType getRelationType( int index );
	public Class<? extends Readable> getEntityClass( int index );
}
