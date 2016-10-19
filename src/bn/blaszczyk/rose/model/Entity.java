package bn.blaszczyk.rose.model;

import java.util.ArrayList;
import java.util.List;

public class Entity {
	private String classname;
	private String javaname;
	private String packagename;
	private String toString;
	private List<Member> members = new ArrayList<>();
	private List<EntityMember> entitymembers = new ArrayList<>();
 
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

	public List<Member> getMembers()
	{
		return members;
	}

	public List<EntityMember> getEntityMembers()
	{
		return entitymembers;
	}
	
	public void addMember(Member member)
	{
		members.add(member);
	}
	
	public void addEntityMember(EntityMember entitymember)
	{
		entitymembers.add(entitymember);
		switch(entitymember.getType())
		{
		case ONETOONE:
			break;
		case MANYTOONE:
			EntityMember counterpart = new EntityMember(this, entitymember);
			entitymember.setCouterpart(counterpart);
			entitymember.getEntity().addEntityMember( counterpart );
			break;
		case ONETOMANY:
			break;
		default:
			break;
		}
	}
	
	
}
