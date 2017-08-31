package bn.blaszczyk.rose.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnumModel implements Iterable<String>{
	private String classname;
	private String objectname;
	private String packagename;
	private List<String> options = new ArrayList<>();
 
	public EnumModel(String classname, String packagename)
	{
		this.classname = classname;
		this.packagename = packagename;
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

	public boolean addOption(String e)
	{
		return options.add(e);
	}
	
	@Override
	public String toString()
	{
		return classname;
	}

	@Override
	public Iterator<String> iterator()
	{
		return options.iterator();
	}
	
}
