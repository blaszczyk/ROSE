package bn.blaszczyk.rose.themes.defaulttheme;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bn.blaszczyk.rose.interfaces.*;

@SuppressWarnings("serial")
public class FullEditPanel extends JPanel implements MyPanel, ThemeConstants {


	private int width = 2 * H_SPACING;
	private int height = V_SPACING;

	private BasicEditPanel basicPanel;
	
	private ModelController controller;
	
	private EntityModel entityModel;
	
	public FullEditPanel( EntityModel entityModel, ModelController controller )
	{
		this.controller = controller;
		this.entityModel = entityModel;
		setLayout(null);
		setBackground(FULL_PNL_BACKGROUND);
		addTitle( "Edit " + entityModel.getName() );
		addBasicPanel(entityModel);
		addButtonPanel();
//		for(int i = 0; i < entityModel.getEntityCount(); i++)
//		{
//			if( entityModel.getEntityMember(i) == null )
//				continue;
//			else if( entityModel.isEntityMany(i))
//			{
//				List<EntityModel> entityModels = new ArrayList<>();
//				Set<?> objects =  (Set<?>) entityModel.getEntityMember(i);
//				for(Object object : objects)
//					entityModels.add(entityModel.createModel(object));
//				addSubTitle( entityModel.getEntityName(i) );
//				addMemberTable( entityModels );
//			}
//			else
//			{
//				EntityModel subEntityModel = entityModel.createModel( entityModel.getEntityMember(i) );
//				addSubTitle( entityModel.getEntityName(i), subEntityModel );
//				addBasicPanel( subEntityModel );
//			}
//		}
		
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

//	private void addSubTitle( String subtitle, EntityModel entityModel )
//	{	
//		JButton btnView = new JButton("View");
//		btnView.setBounds(2 * H_SPACING + SUBTITLE_WIDTH, height + V_OFFSET, 100, SUBTITLE_HEIGHT);
//		final EntityModel entityModelCpy = entityModel;
//		btnView.addActionListener( e -> GUIController.createFullPanelDialog(null, entityModelCpy) );
//		add(btnView);
//		
//		addSubTitle(subtitle);		
//	}
//	private void addSubTitle( String subtitle )
//	{
//		height += V_OFFSET;
//		JLabel lblSubTitle = new JLabel( subtitle );
//		lblSubTitle.setFont(SUBTITLE_FONT);
//		lblSubTitle.setForeground(SUBTITLE_FG);
//		lblSubTitle.setBackground(SUBTITLE_BG);
//		lblSubTitle.setBounds(H_SPACING, height, SUBTITLE_WIDTH, SUBTITLE_HEIGHT);
//		lblSubTitle.setOpaque(true);
//		add(lblSubTitle);
//		
//		computeDimensions(TITLE_HEIGHT, TITLE_WIDTH);		
//	}
	
	private void addBasicPanel( EntityModel entityModel )
	{	
		basicPanel = new BasicEditPanel(entityModel) ;
		JPanel panel = basicPanel.getPanel();
		panel.setBounds(H_SPACING, height, basicPanel.getWidth() , basicPanel.getHeight() );
		add(panel);
		computeDimensions( basicPanel.getHeight(), basicPanel.getWidth() );
	}
	
	private void addButtonPanel()
	{
		JButton btnSave = new JButton("Save");
		btnSave.setBounds( H_SPACING , height + V_OFFSET, 100, SUBTITLE_HEIGHT);
		btnSave.addActionListener( e -> save() );		
		add(btnSave);
		computeDimensions(SUBTITLE_HEIGHT + V_OFFSET, 100);
	}
	
//	private void addMemberTable( List<EntityModel> entityModels )
//	{
//		MemberTableModel tableModel = new MemberTableModel(entityModels);
//		MemberTable table = new MemberTable( tableModel );
//		JPanel panel = table.getPanel();
//		panel.setBounds(H_SPACING, height, table.getWidth(), table.getHeight());
//		add(panel);
//		computeDimensions( panel.getHeight(), panel.getWidth() );
//	}
	
	private void save()
	{
		for(int i = 0; i < entityModel.getMemberCount(); i++ )
		{
			try
			{
				controller.setMember(entityModel.getEntity(), entityModel.getMemberName(i), basicPanel.getPanel(i).getValue() );
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
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
