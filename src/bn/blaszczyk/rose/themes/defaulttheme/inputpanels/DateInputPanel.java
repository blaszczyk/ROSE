package bn.blaszczyk.rose.themes.defaulttheme.inputpanels;

import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class DateInputPanel extends AbstractInputPanel<Date> {
	
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy");
	
	private final JLabel label = new JLabel();
	private final JTextField textField = new JTextField();
	
	private Date defValue;
	
	public DateInputPanel( String name, Date defValue )
	{
		this.defValue = defValue;
		
		label.setText(name);
		label.setBounds(0, 0, PROPERTY_WIDTH, LBL_HEIGHT);
		add(label);
		
		setValue(defValue);
		textField.setBounds( PROPERTY_WIDTH + H_SPACING , 0, VALUE_WIDTH, LBL_HEIGHT);
		add(textField);
	}
	
	@Override
	public Date getValue()
	{
		try
		{
			return DATE_FORMAT.parse(textField.getText());
		}
		catch (ParseException e)
		{
			return defValue;
		}
	}
	
	@Override
	public void setValue(Date value)
	{	
		textField.setText( DATE_FORMAT.format(value) );
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
	
}
