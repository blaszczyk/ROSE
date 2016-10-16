package bn.blaszczyk.roseapp.controller;

import java.util.List;

import bn.blaszczyk.roseapp.model.Entity;

public interface FullModelController extends BasicModelController {

	public Entity createCopy( Entity entity );
	public void commit();
	public List<?> getAll(Class<?> type);
}
