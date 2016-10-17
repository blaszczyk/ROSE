package bn.blaszczyk.roseapp.view.inputpanels;

import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class BooleanInputPanel extends AbstractInputPanel<Boolean> {
	
	private JCheckBox checkBox = new JCheckBox();
	
	public BooleanInputPanel(String name, Boolean defValue)
	{
		checkBox.setSelected(defValue);
		checkBox.setText(name);
		checkBox.setBounds(0, 0, PROPERTY_WIDTH, LBL_HEIGHT);
		add(checkBox);
	}
	
	@Override
	public Boolean getValue()
	{
		return checkBox.isSelected();
	}
	
	@Override
	public void setValue(Boolean value)
	{
		checkBox.setSelected(value);
	}
	
	@Override
	public void addActionListener(ActionListener l)
	{
		checkBox.addActionListener(l);
	}
	
	@Override
	public void removeActionListener(ActionListener l)
	{
		checkBox.removeActionListener(l);
	}
	
}
