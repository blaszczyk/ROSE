package bn.blaszczyk.rose.controller;

import java.text.ParseException;

import bn.blaszczyk.rose.interfaces.Entity;

public interface ModelController {
	
	public void setMember( Entity entity, String name, Object value) throws ParseException;
	public void setEntityMember( Entity entity, String name, Entity value) throws ParseException;
	public void addEntityMember( Entity entity, String name, Entity value) throws ParseException;
	public void deleteEntityMember( Entity entity, String name, Entity value) throws ParseException;
	public void commit();
	
}
