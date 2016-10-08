package bn.blaszczyk.rose;

public class MetaData {
	private String modelpackage = "";
	private String srcpath = "src/";
	private String sqlpath = "sql/";
	private boolean usingForeignKeys = false;
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

	public String getSrcpath()
	{
		return srcpath;
	}

	public void setSrcpath(String srcpath)
	{
		this.srcpath = srcpath;
	}

	public String getSqlpath()
	{
		return sqlpath;
	}

	public void setSqlpath(String sqlpath)
	{
		this.sqlpath = sqlpath;
	}
	
	public boolean isUsingForeignKeys()
	{
		return usingForeignKeys;
	}

	public void setUsingForeignKeys(boolean usingForeignKeys)
	{
		this.usingForeignKeys = usingForeignKeys;
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
