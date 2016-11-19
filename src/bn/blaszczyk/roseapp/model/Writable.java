package bn.blaszczyk.roseapp.model;

import bn.blaszczyk.roseapp.model.Entity;

public interface Writable extends Readable {
	
	public void setField( int index, Object value );
	public void setEntity( int index, Entity value );
	public void addEntity( int index, Entity value );
	public void removeEntity( int index, Entity value );
	
//	public Entity createNew( ); -> Factory
	
}
