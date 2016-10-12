package bn.blaszczyk.rose.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Entity {
	private String classname;
	private String javaname;
	private Member primary;
	private Set<String> imports = new HashSet<>();
	private List<Member> members = new ArrayList<>();
	private List<EntityMember> entitymembers = new ArrayList<>();
 
	public Entity(String classname)
	{
		this.classname = classname;
		this.javaname = classname.substring(0, 1).toLowerCase() + classname.substring(1);
		try
		{
			addMember( primary = new Member(javaname+"_id", "int",true) );
		}
		catch (ParseException e)
		{
		}
	}

	public String getJavaname()
	{
		return javaname;
	}
	
	public String getClassname()
	{
		return classname;
	}

	public Member getPrimary()
	{
		return primary;
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
		for(MemberType memberType : MemberType.values())
			if(member.getType().equals(memberType))
				imports.add(memberType.getJavapackage());
	}
	
	public void addEntityMember(EntityMember entitymember)
	{
		entitymembers.add(entitymember);
		if(entitymember.getType().isSecondMany())
		{
			imports.add("java.util.Set");
			imports.add("java.util.TreeSet");
		}
		else
		{
			EntityMember counterpart = new EntityMember(this, entitymember.getType().getInverse() , getJavaname());
			counterpart.setCouterpart(entitymember);
			entitymember.setCouterpart(counterpart);
			entitymember.getEntity().addEntityMember( counterpart );
		}
	}
	
//	public void setPrimaryKey(String membername)
//	{
//		for(Member member : members)
//			if(membername.toLowerCase().contains(member.getSqlname().toLowerCase()))
//				member.setPrimary(true);
//			else
//				member.setPrimary(false);
//	}
	
	public Set<String> getImports()
	{
		return imports;
	}
	
}
