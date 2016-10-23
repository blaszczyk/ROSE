package bn.blaszczyk.roseapp.view.panels;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bn.blaszczyk.roseapp.model.EntityModel;
import bn.blaszczyk.roseapp.model.RelationType;
import bn.blaszczyk.roseapp.view.ThemeConstants;

@SuppressWarnings("serial")
public class BasicViewPanel extends JPanel implements MyPanel, ThemeConstants {


	
	private int width = 3 * H_SPACING + PROPERTY_WIDTH + VALUE_WIDTH;
	private int height = V_SPACING;

	private EntityModel entityModel;
	
	public BasicViewPanel( EntityModel entityModel )
	{
		this.entityModel = entityModel;
		setLayout(null);
		setBackground(BASIC_PNL_BACKGROUND);
		for(int i = 0; i < entityModel.getMemberCount(); i++)
			addValue( entityModel.getMemberName(i), entityModel.getMemberValue(i).toString() );
		for(int i = 0; i < entityModel.getEntityCount(); i++)
			if(entityModel.getRelationType(i) == RelationType.ENUM)
				addValue(entityModel.getEntityName(i), entityModel.getEntityMember(i).toString());
	}

	private void addValue(String property, String value)
	{
		JLabel lblProperty = new JLabel(property + ": ", SwingConstants.RIGHT);
		lblProperty.setBounds( H_SPACING, height, PROPERTY_WIDTH, LBL_HEIGHT );
		lblProperty.setFont(PROPERTY_FONT);
		lblProperty.setOpaque(true);
		lblProperty.setForeground(PROPERTY_FG);
		lblProperty.setBackground(PROPERTY_BG);
		add(lblProperty);
		
		JLabel lblValue = new JLabel( " " + value);
		lblValue.setBounds( 2 * H_SPACING + PROPERTY_WIDTH , height, VALUE_WIDTH, LBL_HEIGHT);
		lblValue.setFont(VALUE_FONT);
		lblValue.setOpaque(true);
		lblValue.setForeground(VALUE_FG);
		lblValue.setBackground(VALUE_BG);
		add(lblValue);
				
		height += LBL_HEIGHT + V_SPACING;
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
		return false;
	}
	
}
