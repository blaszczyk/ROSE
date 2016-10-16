package bn.blaszczyk.roseapp.themes.defaulttheme.inputpanels;

import javax.swing.JPanel;

import bn.blaszczyk.roseapp.themes.defaulttheme.ThemeConstants;

@SuppressWarnings("serial")
public abstract class AbstractInputPanel<T> extends JPanel implements InputPanel<T>, ThemeConstants {
	
	public AbstractInputPanel()
	{
		setLayout(null);
	}
	
	@Override
	public JPanel getPanel()
	{
		return this;
	}
	
}
