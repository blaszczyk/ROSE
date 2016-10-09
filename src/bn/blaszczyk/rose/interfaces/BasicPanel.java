package bn.blaszczyk.rose.interfaces;

import javax.swing.JPanel;

public interface BasicPanel {
	public void addValue( String property, String value );
	public int getWidth();
	public int getHeight();
	public JPanel getPanel();
}
