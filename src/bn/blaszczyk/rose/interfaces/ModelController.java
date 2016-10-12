package bn.blaszczyk.rose.interfaces;

import java.text.ParseException;

public interface ModelController {
	
	public void setMember( Object entity, String name, Object value) throws ParseException;
	public void setEntityMember( Object entity, String name, Object value) throws ParseException;
	public void addEntityMember( Object entity, String name, Object value) throws ParseException;
	public void deleteEntityMember( Object entity, String name, Object value) throws ParseException;
	public void commit();
	
}
