package bn.blaszczyk.rose.model;

public interface Dto extends Identifyable
{

	public Object getFieldValue(final String fieldName);

	public Integer getEntityId(final String fieldName);

	public Integer[] getEntityIds(final String fieldName);
	
	public Dto getEntity(final String fieldName);
	
	public Integer getEntityCount(final String fieldName);
	
	public void setFieldValue(final String fieldName, final Object value);
	
	public void setEntityId(final String fieldName, final Integer id);
	
	public void setEntityIds(final String fieldName, final Integer[] ids);

	public void setEntity(final String fieldName, final Dto entity);
	
	public void setEntityCount(final String fieldName, final Integer count);

}
