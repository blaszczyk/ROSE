package bn.blaszczyk.rose.themes.defaulttheme.inputpanels;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class BigDecimalInputPanel extends AbstractInputPanel<BigDecimal> implements KeyListener {

	private static final DecimalFormat BIG_DEC_FORMAT = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
	
	static{
		BIG_DEC_FORMAT.setParseBigDecimal(true);
	}
	
	private final JLabel label = new JLabel();
	private final JTextField textField = new JTextField();
	
	public BigDecimalInputPanel( String name, BigDecimal defvalue )
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
	public BigDecimal getValue()
	{
		try
		{
			return (BigDecimal) BIG_DEC_FORMAT.parse(textField.getText());
		}
		catch (ParseException e)
		{
			return BigDecimal.ZERO;
		}
	}
	
	@Override
	public void setValue(BigDecimal value)
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


	/*
	 * KeyListener Methods
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{
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
		if (!Character.isISOControl(c) && !Character.isDigit(c) && !"-.,".contains("" + c) )
		{
			Toolkit.getDefaultToolkit().beep();
			e.consume();
		}
	}
	
}
