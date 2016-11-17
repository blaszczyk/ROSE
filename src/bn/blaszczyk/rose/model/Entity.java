package bn.blaszczyk.rose.model;

import java.util.ArrayList;
import java.util.List;

public class Entity {
	private String classname;
	private String javaname;
	private String packagename;
	private String toString;
	private String tableCols;
	private List<Field> fields = new ArrayList<>();
	private List<EntityField> entityFields = new ArrayList<>();
//	private List<EnumField> enummembers = new ArrayList<>();
 
	public Entity(String classname, String packagename)
	{
		this.classname = classname;
		this.packagename = packagename;
		this.javaname = classname.substring(0, 1).toLowerCase() + classname.substring(1);
	}

	public String getObjectName()
	{
		return javaname;
	}
	
	
	public String getClassName()
	{
		if(packagename == null || packagename == "")
			return classname;
		return packagename + "." + classname;
	}
	
	public String getSimpleClassName()
	{
		return classname;
	}

	public String getToString()
	{
		return toString;
	}

	public void setToString(String toString)
	{
		this.toString = toString;
	}

	public String getTableCols()
	{
		return tableCols;
	}

	public void setTableCols(String tableCols)
	{
		this.tableCols = tableCols;
	}	
	
	public List<Field> getFields()
	{
		return fields;
	}

	public List<EntityField> getEntityFields()
	{
		return entityFields;
	}
	
//	public List<EnumField> getEnumMembers()
//	{
//		return enummembers;
//	}
	
	public void addField(Field field)
	{
		fields.add(field);
	}

	public void addEntityField(EntityField entityField, boolean cascade)
	{
		entityFields.add(entityField);
		if( cascade && entityField.getType().isFirstMany() )
		{
			EntityField counterpart = new EntityField(this, entityField);
			entityField.setCouterpart(counterpart);
			entityField.getEntity().addEntityField( counterpart, false );
		}
	}	
	
//	public void addEnumMember(EnumField enummember)
//	{
//		enummembers.add(enummember);
//	}
	
}
