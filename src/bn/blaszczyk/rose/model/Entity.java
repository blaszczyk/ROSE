package bn.blaszczyk.rose.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Entity {
	private String classname;
	private String javaname;
	private Set<String> imports = new HashSet<>();
	private List<Member> members = new ArrayList<>();
	private List<EntityMember> entitymembers = new ArrayList<>();
 
	public Entity(String classname)
	{
		this.classname = classname;
		this.javaname = classname.substring(0, 1).toLowerCase() + classname.substring(1);
	}

	public String getJavaname()
	{
		return javaname;
	}
	
	public String getClassname()
	{
		return classname;
	}

//	public Member getPrimary()
//	{
//		return primary;
//	}

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
		for(MemberType memberType : MemberType.values())
			if(member.getType().equals(memberType))
				imports.add(memberType.getJavapackage());
	}
	
	public void addEntityMember(EntityMember entitymember)
	{
		entitymembers.add(entitymember);
		switch(entitymember.getType())
		{
		case ONETOONE:
			break;
		case MANYTOONE:
			EntityMember counterpart = new EntityMember(this, entitymember.getType().getInverse() , getJavaname());
			counterpart.setCouterpart(entitymember);
			entitymember.setCouterpart(counterpart);
			entitymember.getEntity().addEntityMember( counterpart );
			break;
		case ONETOMANY:
			imports.add("java.util.Set");
			imports.add("java.util.TreeSet");
			break;
		default:
			break;
		}
	}
	
	public Set<String> getImports()
	{
		return imports;
	}
	
}
