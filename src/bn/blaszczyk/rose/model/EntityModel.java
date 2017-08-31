package bn.blaszczyk.rose.model;

import java.util.ArrayList;
import java.util.List;

public class EntityModel {
	
	private final String classname;
	private final String objectname;
	private final String packagename;
	private final ImplInterface implInterface;
	private final List<Field> fields = new ArrayList<>();
	private final List<EntityField> entityFields = new ArrayList<>();

	private String toString;
 
	public EntityModel(final String classname, final String packagename, final ImplInterface implInterface)
	{
		this.classname = classname;
		this.packagename = packagename;
		this.implInterface = implInterface;
		this.objectname = classname.substring(0, 1).toLowerCase() + classname.substring(1);
	}

	public String getObjectName()
	{
		return objectname;
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

	
	public ImplInterface getImplInterface()
	{
		return implInterface;
	}
	
	public List<Field> getFields()
	{
		return fields;
	}

	public List<EntityField> getEntityFields()
	{
		return entityFields;
	}
	
	public void addField(Field field)
	{
		fields.add(field);
	}

	public void addEntityField(EntityField entityField)
	{
		entityFields.add(entityField);
	}
	
	@Override
	public String toString()
	{
		return classname;
	}

}
