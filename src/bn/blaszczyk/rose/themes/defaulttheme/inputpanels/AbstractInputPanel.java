package bn.blaszczyk.rose.themes.defaulttheme.inputpanels;

import javax.swing.JPanel;

import bn.blaszczyk.rose.interfaces.InputPanel;
import bn.blaszczyk.rose.themes.defaulttheme.ThemeConstants;

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
