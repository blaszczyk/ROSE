package bn.blaszczyk.roseapp.view.panels;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import bn.blaszczyk.roseapp.controller.*;
import bn.blaszczyk.roseapp.model.Entity;
import bn.blaszczyk.roseapp.model.EntityModel;
import bn.blaszczyk.roseapp.model.RelationType;
import bn.blaszczyk.roseapp.view.ThemeConstants;
import bn.blaszczyk.roseapp.view.inputpanels.*;

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
			addInputPanel( i );
		for(int i = 0; i < entityModel.getEntityCount(); i++)
			if(entityModel.getRelationType(i).equals(RelationType.ENUM))
				addEnumPanel( i );
	}

	private void addInputPanel(int index)
	{
		InputPanel<?> panel = null;
		String name = entityModel.getMemberName(index);
		Object value = entityModel.getMemberValue(index);
		if( value instanceof String )
			panel = new StringInputPanel( name, (String) value, entityModel.getLength1(index) );
		else if( value instanceof Boolean )
			panel = new BooleanInputPanel( name, (Boolean) value );
		else if( value instanceof Integer)
			panel = new IntegerInputPanel( name, (Integer) value );
		else if( value instanceof Date)
			panel = new DateInputPanel( name, (Date) value );
		else if( value instanceof BigDecimal)
			panel = new BigDecimalInputPanel( name, (BigDecimal) value, entityModel.getLength1(index), entityModel.getLength2(index) );
		else
		{
			System.out.printf( "Unknown type %s \n", value);
			return;
		}
		panel.getPanel().setBounds( H_SPACING, height, PROPERTY_WIDTH + VALUE_WIDTH, LBL_HEIGHT );
		panels.add(panel);
		add(panel.getPanel());
		height += LBL_HEIGHT + V_SPACING;
	}
	private void addEnumPanel(int index)
	{
		InputPanel<?> panel = new EnumInputPanel(entityModel.getEntityName(index), (Enum<?>) entityModel.getEntityMember(index));
		panel.getPanel().setBounds( H_SPACING, height, PROPERTY_WIDTH + VALUE_WIDTH, LBL_HEIGHT );
		panels.add(panel);
		add(panel.getPanel());
		height += LBL_HEIGHT + V_SPACING;
	}
	
	public void save(FullModelController controller)
	{
		int i;
		for(i = 0 ; i < entityModel.getMemberCount(); i++ )
			controller.setMember(entityModel.getEntity(), entityModel.getMemberName(i), panels.get(i).getValue() );
		for( int j = 0; j < entityModel.getEntityCount(); j++)
			if(entityModel.getRelationType(j).equals(RelationType.ENUM))
				controller.setEntityMember(entityModel.getEntity(), entityModel.getEntityName(j), (Entity) panels.get(i++).getValue());
	}
	
	public void setChangeListener(ChangeListener l)
	{
		for(InputPanel<?> panel : panels)
			panel.setChangeListener(l);
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

	@Override
	public Object getShownObject()
	{
		return entityModel.getEntity();
	}
	

	@Override
	public boolean hasChanged()
	{
		for(InputPanel<?> panel : panels)
			if(panel.hasChanged())
				return true;
		return false;
	}
	
}
