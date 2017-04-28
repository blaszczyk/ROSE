package bn.blaszczyk.rose.model;

public interface Representable extends Writable
{
	public void setEntity( int index, Writable value, Representable representor );
	public void addEntity( int index, Writable value, Representable representor );
	public void removeEntity( int index, Writable value, Representable representor );
}
