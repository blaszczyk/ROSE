package bn.blaszczyk.roseapp.model;

public enum RelationType {
	ONETOONE(false,false),
	ONETOMANY(false,true),
	MANYTOONE(true,false),
	MANYTOMANY(true,true);
	
	private boolean firstMany;
	private boolean secondMany;
	
	private RelationType(boolean firstMany, boolean secondMany)
	{
		this.firstMany = firstMany;
		this.secondMany = secondMany;
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
