package bn.blaszczyk.rose.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Entity {
	private String sqlname;
	private String javaname;
	private Member primary;
	private Set<String> imports = new HashSet<>();
	private List<Member> members = new ArrayList<>();
	private List<EntityMember> entitymembers = new ArrayList<>();
 
	public Entity(String sqlname)
	{
		this.sqlname = sqlname;
		this.javaname = sqlname.toLowerCase();
		try
		{
			addMember( primary = new Member(sqlname+"_ID", "int",true) );
		}
		catch (ParseException e)
		{
		}
	}
	
	public String getSqlname()
	{
		return sqlname;
	}

	public String getJavaname()
	{
		return javaname;
	}
	
	public String getClassname()
	{
		return javaname.substring(0, 1).toUpperCase() + javaname.substring(1);
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
		if(entitymember.isMany())
		{
			imports.add("java.util.Set");
			imports.add("java.util.HashSet");
		}
		else
		{
			EntityMember counterpart = new EntityMember(this, getSqlname(), getJavaname(), true);
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
