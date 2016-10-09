package bn.blaszczyk.rose.themes;

import javax.swing.JPanel;

public interface FullPanel {
	
	public void addTitle(String title);
	public void addSubTitle(String subTitle);
	public void addBasicPanel(BasicPanel basicPanel);
	public int getWidth();
	public int getHeight();
	public JPanel getPanel();
}
