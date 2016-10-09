package bn.blaszczyk.rose.themes;

import javax.swing.JPanel;

public interface BasicPanel {
	public void addValue( String property, String value );
	public int getWidth();
	public int getHeight();
	public JPanel getPanel();
}
