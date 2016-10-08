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
	}
	
	public void addEntityMember(EntityMember entitymember)
	{
		entitymembers.add(entitymember);
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
		Set<String> imports = new HashSet<>();
		for(MemberType memberType : MemberType.values())
			for(Member member : members)
				if(member.getType().equals(memberType))
					imports.add(memberType.getJavapackage());
		return imports;
	}
	
}
