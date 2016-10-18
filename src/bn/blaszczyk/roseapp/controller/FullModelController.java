package bn.blaszczyk.roseapp.controller;

import java.util.List;

import bn.blaszczyk.roseapp.model.Entity;
import bn.blaszczyk.roseapp.model.EntityModel;

public interface FullModelController extends BasicModelController {

	public Entity createCopy( Entity entity );
	public EntityModel createCopy(EntityModel entityModel);
	public void commit();
	public  List<EntityModel> getAllModels(Class<?> type);
	public void loadEntities(Class<?>[] types);
}
