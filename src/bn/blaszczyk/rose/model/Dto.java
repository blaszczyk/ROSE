package bn.blaszczyk.rose.model;

public interface Dto extends Identifyable
{

	public Object getFieldValue(final String fieldName);

	public int getEntityId(final String fieldName);

	public int[] getEntityIds(final String fieldName);
	
	public void setFieldValue(final String fieldName, final Object value);
	
	public void setEntityId(final String fieldName, final int id);
	
	public void setEntityIds(final String fieldName, final int[] ids);

}