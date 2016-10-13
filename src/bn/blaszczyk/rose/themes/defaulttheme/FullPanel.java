package bn.blaszczyk.rose.themes.defaulttheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bn.blaszczyk.rose.controller.GUIController;
import bn.blaszczyk.rose.interfaces.*;

@SuppressWarnings("serial")
public class FullPanel extends JPanel implements MyPanel, ThemeConstants {


	private int width = 2 * H_SPACING;
	private int height = V_SPACING;
	
	private GUIController controller;

	
	public FullPanel( EntityModel entityModel, GUIController controller )
	{
		this.controller = controller;
		setLayout(null);
		setBackground(FULL_PNL_BACKGROUND);
		addTitle( entityModel.getName(), entityModel );
		addBasicPanel(entityModel);
		for(int i = 0; i < entityModel.getEntityCount(); i++)
		{
			if( entityModel.getEntityMember(i) == null )
				continue;
			else if( entityModel.getRelationType(i).isSecondMany())
			{
				List<EntityModel> entityModels = new ArrayList<>();
				Set<?> objects =  (Set<?>) entityModel.getEntityMember(i);
				for(Object object : objects)
					entityModels.add(entityModel.createModel(object));
				addSubTitle( entityModel.getEntityName(i) );
				addMemberTable( entityModels );
			}
			else
			{
				EntityModel subEntityModel = entityModel.createModel( entityModel.getEntityMember(i) );
				addSubTitle( entityModel.getEntityName(i), subEntityModel );
				addBasicPanel( subEntityModel );
			}
		}
		
	}

	private void addTitle( String title )
	{
		height += V_OFFSET;
		JLabel lblTitle = new JLabel( title );
		lblTitle.setFont(TITLE_FONT);
		lblTitle.setForeground(TITLE_FG);
		lblTitle.setBackground(TITLE_BG);
		lblTitle.setBounds(H_SPACING, height, TITLE_WIDTH, TITLE_HEIGHT);
		lblTitle.setOpaque(true);
		add(lblTitle);
		computeDimensions(TITLE_HEIGHT, TITLE_WIDTH);		
	}
	
	private void addTitle( String title, EntityModel entityModel )
	{
		JButton btnEdit = new JButton("Edit");
		btnEdit.setBounds(2 * H_SPACING + SUBTITLE_WIDTH, height + V_OFFSET, 100, SUBTITLE_HEIGHT);
		final EntityModel entityModelCpy = entityModel;
		btnEdit.addActionListener( e -> {
			controller.createEditPanelDialog(null, entityModelCpy);
		} );
		add(btnEdit);
		
		addTitle(title);				
	}

	private void addSubTitle( String subtitle, EntityModel entityModel )
	{	
		JButton btnView = new JButton("View");
		btnView.setBounds(2 * H_SPACING + SUBTITLE_WIDTH, height + V_OFFSET, 100, SUBTITLE_HEIGHT);
		final EntityModel entityModelCpy = entityModel;
		btnView.addActionListener( e -> controller.createFullPanelDialog(null, entityModelCpy) );
		add(btnView);
		
		addSubTitle(subtitle);		
	}
	private void addSubTitle( String subtitle )
	{
		height += V_OFFSET;
		JLabel lblSubTitle = new JLabel( subtitle );
		lblSubTitle.setFont(SUBTITLE_FONT);
		lblSubTitle.setForeground(SUBTITLE_FG);
		lblSubTitle.setBackground(SUBTITLE_BG);
		lblSubTitle.setBounds(H_SPACING, height, SUBTITLE_WIDTH, SUBTITLE_HEIGHT);
		lblSubTitle.setOpaque(true);
		add(lblSubTitle);
		
		computeDimensions(TITLE_HEIGHT, TITLE_WIDTH);		
	}
	
	private void addBasicPanel( EntityModel entityModel )
	{	
		MyPanel myPanel = new BasicPanel(entityModel) ;
		JPanel panel = myPanel.getPanel();
		panel.setBounds(H_SPACING, height, myPanel.getWidth() , myPanel.getHeight() );
		add(panel);
		computeDimensions( myPanel.getHeight(), myPanel.getWidth() );
	}
	
	private void addMemberTable( List<EntityModel> entityModels )
	{
		MemberTableModel tableModel = new MemberTableModel(entityModels);
		MemberTable table = new MemberTable( tableModel, controller );
		JPanel panel = table.getPanel();
		panel.setBounds(H_SPACING, height, table.getWidth(), table.getHeight());
		add(panel);
		computeDimensions( panel.getHeight(), panel.getWidth() );
	}
	
	private void computeDimensions( int height, int width )
	{
		this.width = Math.max(this.width, 2 * H_SPACING + width);
		this.height += V_SPACING + height;
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
