package sqltojava;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Entity implements Iterable<Member>{
	private String sqlname;
	private String javaname;
	private List<Member> members = new ArrayList<>();
 
	public Entity(String sqlname)
	{
		this.sqlname = sqlname;
		this.javaname = sqlname.toLowerCase();
		try
		{
			addMember( new Member(sqlname+"_ID", "int",true) );
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

	public void addMember(Member member)
	{
		members.add(member);
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

	@Override
	public Iterator<Member> iterator()
	{
		return members.iterator();
	}
	
	
}
