package sqltojava;

public class MetaData {
	private String modelpackage = "";
	private String modelpath = "";
	private String sqlpath = "";
	private boolean usingAnnotations = false;
	
	public MetaData()
	{
		super();
	}

	public String getModelpackage()
	{
		return modelpackage;
	}

	public void setModelpackage(String modelpackage)
	{
		this.modelpackage = modelpackage;
	}

	public String getModelpath()
	{
		return modelpath;
	}

	public void setModelpath(String modelpath)
	{
		this.modelpath = modelpath;
	}

	public String getSqlpath()
	{
		return sqlpath;
	}

	public void setSqlpath(String sqlpath)
	{
		this.sqlpath = sqlpath;
	}

	public boolean isUsingAnnotations()
	{
		return usingAnnotations;
	}

	public void setUsingAnnotations(boolean usingAnnotations)
	{
		this.usingAnnotations = usingAnnotations;
	}
	
}
