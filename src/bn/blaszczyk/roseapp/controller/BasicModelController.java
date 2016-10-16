package bn.blaszczyk.roseapp.controller;

import java.text.ParseException;

import bn.blaszczyk.roseapp.model.Entity;
import bn.blaszczyk.roseapp.model.EntityModel;

public interface BasicModelController {
	
	public void setMember( Entity entity, String name, Object value) throws ParseException;
	public void setEntityMember( Entity entity, String name, Entity value) throws ParseException;
	public void addEntityMember( Entity entity, String name, Entity value) throws ParseException;
	public void deleteEntityMember( Entity entity, String name, Entity value) throws ParseException;
	
	public Entity createNew( String className);
	
	public EntityModel createModel( Entity entity );
	
}
