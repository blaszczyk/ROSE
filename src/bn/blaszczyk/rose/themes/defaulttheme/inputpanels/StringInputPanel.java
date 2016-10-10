package bn.blaszczyk.rose.themes.defaulttheme.inputpanels;

import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bn.blaszczyk.rose.interfaces.InputPanel;
import bn.blaszczyk.rose.themes.defaulttheme.ThemeConstants;

@SuppressWarnings("serial")
public class StringInputPanel extends JPanel implements InputPanel<String>, ThemeConstants {
	
	private final JLabel label = new JLabel();
	private final JTextField textField = new JTextField();
	
	public StringInputPanel( String name, String defvalue )
	{
		label.setText(name);
		label.setBounds(0, 0, PROPERTY_WIDTH, LBL_HEIGHT);
		add(label);
		
		textField.setText(defvalue);
		textField.setBounds( PROPERTY_WIDTH + H_SPACING , 0, VALUE_WIDTH, LBL_HEIGHT);
		add(textField);
	}
	
	@Override
	public String getValue()
	{
		return textField.getText();
	}
	
	@Override
	public void setValue(String value)
	{	
		textField.setText(value);
	}
		
	@Override
	public String getName()
	{
		return label.getText();
	}

	@Override
	public JPanel getPanel()
	{
		return this;
	}
	
	@Override
	public void addActionListener(ActionListener l)
	{
		textField.addActionListener(l);
	}
	
	@Override
	public void removeActionListener(ActionListener l)
	{
		textField.removeActionListener(l);
	}
	
}
