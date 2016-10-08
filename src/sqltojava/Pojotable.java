package sqltojava;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Pojotable implements Iterable<Member>{
	private String sqlname;
	private String javaname;
	private String javapackage;
	private List<Member> members = new ArrayList<>();
		
	public Pojotable(String sqlname, String javapackage)
	{
		this.sqlname = sqlname;
		this.javaname = sqlname.toLowerCase();
		this.javapackage = javapackage;
	}
	
	public String getSqlname()
	{
		return sqlname;
	}

	public String getJavaname()
	{
		return javaname;
	}

	public String getJavapackage()
	{
		return javapackage;
	}

	public void addMember(Member member)
	{
		members.add(member);
	}
	
	public void setPrimaryKey(String membername)
	{
		for(Member member : members)
			if(membername.toLowerCase().contains(member.getSqlname()))
				member.setPrimary(true);
			else
				member.setPrimary(false);
	}
	
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
