package bn.blaszczyk.roseapp.view.inputpanels;

import javax.swing.JPanel;

import bn.blaszczyk.roseapp.view.ThemeConstants;

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
