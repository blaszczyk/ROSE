package bn.blaszczyk.roseapp.model;

public enum RelationType {
	ONETOONE("onetoone",false,false),
	ONETOMANY("onetomany",false,true),
	MANYTOONE("manytoone",true,false),
	MANYTOMANY("manytomany",true,true);
	
	private String name;
	private boolean firstMany;
	private boolean secondMany;
	
	

	private RelationType(String name, boolean firstMany, boolean secondMany)
	{
		this.name = name;
		this.firstMany = firstMany;
		this.secondMany = secondMany;
	}

	public String getName()
	{
		return name;
	}

	public boolean isFirstMany()
	{
		return firstMany;
	}

	public boolean isSecondMany()
	{
		return secondMany;
	}
	
	public RelationType getInverse()
	{
		if( this == ONETOMANY )
			return MANYTOONE;
		if( this == MANYTOONE )
			return ONETOMANY;
		return this;
	}
	
}
