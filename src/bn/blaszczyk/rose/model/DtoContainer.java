package bn.blaszczyk.rose.model;

import java.util.Collection;
import java.util.Set;

public interface DtoContainer {

	public Dto get(final String type, final int id);
	public Collection<? extends Dto> getAll(final String type);
	public Set<Integer> getAllIds(final String type);
	
	public void put(final Dto dto);
	public void putAll(final DtoContainer container);

}
