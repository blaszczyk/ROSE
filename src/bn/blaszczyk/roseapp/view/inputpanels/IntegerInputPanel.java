package bn.blaszczyk.roseapp.view.inputpanels;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class IntegerInputPanel extends AbstractInputPanel<Integer> implements KeyListener {
	
	private final JLabel label = new JLabel();
	private final JTextField textField = new JTextField();
	
	public IntegerInputPanel( String name, Integer defvalue )
	{
		
		label.setText(name);
		label.setBounds(0, 0, PROPERTY_WIDTH, LBL_HEIGHT);
		add(label);
		
		textField.setText( "" + defvalue);
		textField.setBounds( PROPERTY_WIDTH + H_SPACING , 0, VALUE_WIDTH, LBL_HEIGHT);
		textField.addKeyListener(this);
		add(textField);
	}
	
	@Override
	public Integer getValue()
	{
		return Integer.parseInt(textField.getText());
	}
	
	@Override
	public void setValue(Integer value)
	{	
		textField.setText("" + value);
	}
		
	@Override
	public String getName()
	{
		return label.getText();
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

	private void shiftReverenceValue(int diff)
	{
		int newValue = getValue() + diff;
		textField.setText( newValue + "" );
	}	

	/*
	 * KeyListener Methods
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_UP:
			shiftReverenceValue(1);
			break;
		case KeyEvent.VK_DOWN:
			shiftReverenceValue(-1);
			break;
		}
		textField.requestFocusInWindow();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		textField.requestFocusInWindow();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		textField.replaceSelection(null);
		char c = e.getKeyChar();
		if (!Character.isISOControl(c) && !Character.isDigit(c) && c!='-')
		{
			Toolkit.getDefaultToolkit().beep();
			e.consume();
		}
	}
	
}
