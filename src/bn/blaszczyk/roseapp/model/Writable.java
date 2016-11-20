package bn.blaszczyk.roseapp.model;

public interface Writable extends Readable {
	
	public void setField( int index, Object value );
	public void setEntity( int index, Writable value );
	public void addEntity( int index, Writable value );
	public void removeEntity( int index, Writable value );
	
}
