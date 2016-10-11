package bn.blaszczyk.rose.themes.defaulttheme;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import bn.blaszczyk.rose.interfaces.MyPanel;
import bn.blaszczyk.rose.themes.defaulttheme.inputpanels.*;
import bn.blaszczyk.rose.interfaces.EntityModel;
import bn.blaszczyk.rose.interfaces.InputPanel;

@SuppressWarnings("serial")
public class BasicEditPanel extends JPanel implements MyPanel, ThemeConstants {
	
	private int width = 3 * H_SPACING + PROPERTY_WIDTH + VALUE_WIDTH;
	private int height = V_SPACING;

	private EntityModel entityModel;
	private List<InputPanel<?>> panels = new ArrayList<>();
	
	public BasicEditPanel( EntityModel entityModel )
	{
		this.entityModel = entityModel;
		setLayout(null);
		setBackground(BASIC_PNL_BACKGROUND);
		for(int i = 0; i < entityModel.getMemberCount(); i++)
			addInputPanel( entityModel.getMemberName(i), entityModel.getMemberValue(i) );
	}

	private void addInputPanel(String name, Object value)
	{
		InputPanel<?> panel = null;
		if( value instanceof String )
			panel = new StringInputPanel( name, (String) value );
		else if( value instanceof Boolean )
			panel = new BooleanInputPanel( name, (Boolean) value );
		else if( value instanceof Integer)
			panel = new IntegerInputPanel( name, (Integer) value );
		else if( value instanceof Date)
			panel = new DateInputPanel( name, (Date) value );
		else if( value instanceof BigDecimal)
			panel = new BigDecimalInputPanel( name, (BigDecimal) value );
		else
			return;
		panel.getPanel().setBounds( H_SPACING, height, PROPERTY_WIDTH + VALUE_WIDTH, LBL_HEIGHT );
		panels.add(panel);
		add(panel.getPanel());
		height += LBL_HEIGHT + V_SPACING;
	}
	
	public InputPanel<?> getPanel( int index )
	{
		return panels.get(index);
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	public JPanel getPanel()
	{
		return this;
	}
	
}
