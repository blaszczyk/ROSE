package bn.blaszczyk.roseapp.controller;

import bn.blaszczyk.roseapp.model.Entity;
import bn.blaszczyk.roseapp.model.EntityModel;

public interface BasicModelController {
	
	public void setField( Entity entity, String name, Object value);
	public void setEntityField( Entity entity, String name, Entity value);
	public void addEntityField( Entity entity, String name, Entity value);
	public void delete( Entity entity );
	
	public Entity createNew( String className);
	
	public EntityModel createModel( Entity entity );
	
}
