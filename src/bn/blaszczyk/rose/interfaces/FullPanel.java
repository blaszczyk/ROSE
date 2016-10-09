package bn.blaszczyk.rose.interfaces;

import javax.swing.JPanel;

public interface FullPanel {

	// To be provided by created Classes
	public String getName();
	public int getEntityCount();
	public Object getEntityMember( int index );
	public String getEntityName( int index );
	public boolean isEntityMany( int index );
	
	// To be provided by abstract Class
	public Object getObject();
	public int getWidth();
	public int getHeight();
	public JPanel getPanel();
}
