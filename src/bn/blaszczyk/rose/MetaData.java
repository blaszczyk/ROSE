/*
 * MetaData.java
 * generated by rose
 */

package bn.blaszczyk.rose;


public class MetaData
{
	private Integer metaData_id = 0;

	private String modelpackage = null;

	private String panelpackage = null;

	private String fullpanelformat = "%sFullPanel";

	private String basicpanelformat = "%sBasicPanel";

	private String srcpath = "src/";

	private String sqlpath = "sql/";

	private Boolean usingForeignKeys = false;

	private Boolean usingAnnotations = false;

	private String database = null;

	private String basicpanelclass = "bn.blaszczyk.rose.themes.defaulttheme.DefaultBasicPanel";

	private String fullpanelclass = "bn.blaszczyk.rose.themes.defaulttheme.DefaultFullPanel";


	public MetaData()
	{
	}

	public MetaData( String modelpackage, String panelpackage, String fullpanelformat, String basicpanelformat, String srcpath, String sqlpath, Boolean usingForeignKeys, Boolean usingAnnotations, String database, String basicpanelclass, String fullpanelclass )
	{
		this.modelpackage = modelpackage;
		this.panelpackage = panelpackage;
		this.fullpanelformat = fullpanelformat;
		this.basicpanelformat = basicpanelformat;
		this.srcpath = srcpath;
		this.sqlpath = sqlpath;
		this.usingForeignKeys = usingForeignKeys;
		this.usingAnnotations = usingAnnotations;
		this.database = database;
		this.basicpanelclass = basicpanelclass;
		this.fullpanelclass = fullpanelclass;
	}

	public Integer getMetaData_id()
	{
		return metaData_id;
	}

	public void setMetaData_id( Integer metaData_id )
	{
		this.metaData_id = metaData_id;
	}

	public String getModelpackage()
	{
		return modelpackage;
	}

	public void setModelpackage( String modelpackage )
	{
		this.modelpackage = modelpackage;
	}

	public String getPanelpackage()
	{
		return panelpackage;
	}

	public void setPanelpackage( String panelpackage )
	{
		this.panelpackage = panelpackage;
	}

	public String getFullpanelformat()
	{
		return fullpanelformat;
	}

	public void setFullpanelformat( String fullpanelformat )
	{
		this.fullpanelformat = fullpanelformat;
	}

	public String getBasicpanelformat()
	{
		return basicpanelformat;
	}

	public void setBasicpanelformat( String basicpanelformat )
	{
		this.basicpanelformat = basicpanelformat;
	}

	public String getSrcpath()
	{
		return srcpath;
	}

	public void setSrcpath( String srcpath )
	{
		this.srcpath = srcpath;
	}

	public String getSqlpath()
	{
		return sqlpath;
	}

	public void setSqlpath( String sqlpath )
	{
		this.sqlpath = sqlpath;
	}

	public Boolean isUsingForeignKeys()
	{
		return usingForeignKeys;
	}

	public void setUsingForeignKeys( Boolean usingForeignKeys )
	{
		this.usingForeignKeys = usingForeignKeys;
	}

	public Boolean isUsingAnnotations()
	{
		return usingAnnotations;
	}

	public void setUsingAnnotations( Boolean usingAnnotations )
	{
		this.usingAnnotations = usingAnnotations;
	}

	public String getDatabase()
	{
		return database;
	}

	public void setDatabase( String database )
	{
		this.database = database;
	}

	public String getBasicpanelclass()
	{
		return basicpanelclass;
	}

	public void setBasicpanelclass( String basicpanelclass )
	{
		this.basicpanelclass = basicpanelclass;
	}

	public String getFullpanelclass()
	{
		return fullpanelclass;
	}

	public void setFullpanelclass( String fullpanelclass )
	{
		this.fullpanelclass = fullpanelclass;
	}

}
