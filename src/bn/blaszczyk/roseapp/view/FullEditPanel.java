package bn.blaszczyk.roseapp.view;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bn.blaszczyk.roseapp.controller.*;
import bn.blaszczyk.roseapp.model.*;
import bn.blaszczyk.roseapp.view.inputpanels.MyComboBox;

@SuppressWarnings("serial")
public class FullEditPanel extends JPanel implements MyPanel, ThemeConstants {


	private int width = 2 * H_SPACING;
	private int height = V_SPACING;

	private BasicEditPanel basicPanel;
	private List<BasicEditPanel> basicPanels = new ArrayList<>();
	
	private FullModelController modelController;
	private GUIController guiController;
	private EntityModel entityModel;
	
	public FullEditPanel( EntityModel entityModel, FullModelController modelController, GUIController guiController )
	{
		this.modelController = modelController;
		this.entityModel = entityModel;
		this.guiController = guiController;
		setLayout(null);
		setBackground(FULL_PNL_BACKGROUND);
		addTitle( entityModel );
		basicPanel = addBasicPanel(entityModel);
		for(int i = 0; i < entityModel.getEntityCount(); i++)
		{
			if( entityModel.getEntityMember(i) == null )
				continue;
			switch( entityModel.getRelationType(i))
			{
			case ONETOONE:
				addSubTitle(entityModel.getEntityName(i));
				basicPanels.add( addBasicPanel( entityModel.createModel( (Entity) entityModel.getEntityMember(i))));
				break;
			case ONETOMANY:
				List<EntityModel> entityModels = new ArrayList<>();
				Set<?> objects =  (Set<?>) entityModel.getEntityMember(i);
				for(Object object : objects)
					entityModels.add(entityModel.createModel((Entity)object));
				addSubTitle( entityModel.getEntityName(i) );
				addMemberTable( entityModels, entityModel.getEntity(),entityModel.getEntityName(i) );
				break;
			case MANYTOONE:
				addSubTitle( entityModel.getEntityName(i));
				addSelectionBox( entityModel.getEntity(), (Entity) entityModel.getEntityMember(i) );
				break;
			case MANYTOMANY:
				break;
			}
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
		}
		addButtonPanel();
		
	}

	private void addTitle( EntityModel entityModel )
	{
		height += V_OFFSET;

		JLabel lblTitle = new JLabel( entityModel.getId() > 0 ? entityModel.getName() + " " + entityModel.getId() : "new " + entityModel.getName() );
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
	
	private BasicEditPanel addBasicPanel( EntityModel entityModel )
	{	
		BasicEditPanel basicPanel = new BasicEditPanel(entityModel) ;
		JPanel panel = basicPanel.getPanel();
		panel.setBounds(H_SPACING, height, basicPanel.getWidth() , basicPanel.getHeight() );
		add(panel);
		computeDimensions( basicPanel.getHeight(), basicPanel.getWidth() );
		return basicPanel;
	}
	
	private void addMemberTable( List<EntityModel> entityModels, Entity entity, String name )
	{
		MemberTableModel tableModel = new MemberTableModel(entityModels,3);
		MemberTable table = new MemberTable( tableModel, guiController );
		table.setButtonColumn(0, "edit.png", e -> guiController.openEdit( e ));
		table.setButtonColumn(1, "copy.png", e -> {
			try
			{
				EntityModel copy = modelController.createCopy(e);
				modelController.addEntityMember(entity, name, copy.getEntity());
				guiController.openEdit(copy);
			}
			catch (ParseException e1)
			{
				e1.printStackTrace();
			}
		});
		table.setButtonColumn(2, "delete.png", e -> {
			try
			{
				modelController.deleteEntityMember(entity, name, e.getEntity());
			}
			catch (ParseException e1)
			{
				e1.printStackTrace();
			}
		} );
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(2 * H_SPACING, height, table.getWidth(), table.getHeight());
		add(scrollPane);
		computeDimensions( scrollPane.getHeight(), scrollPane.getWidth() );
	}
	
	private void addSelectionBox( Entity entity, Entity value )
	{
//		Entity[] entities = new Entity[modelController.getAllModels(value.getClass()).size()];
//		modelController.getAllModels(value.getClass()).toArray(entities);
		List<EntityModel> entities = modelController.getAllModels(value.getClass());
		MyComboBox<EntityModel> selectBox = new MyComboBox<>(entities, 300, true); 
		selectBox.setSelectedItem(value);
		selectBox.setBounds( 2* H_SPACING, height, 300, 30);
		add(selectBox);
		computeDimensions(30, 300);
	}
	
	private void addButtonPanel()
	{
		JButton btnSave = new JButton("Save");
		btnSave.setBounds( H_SPACING , height + V_OFFSET, 100, SUBTITLE_HEIGHT);
		btnSave.addActionListener( e -> save() );		
		add(btnSave);
		computeDimensions(SUBTITLE_HEIGHT + V_OFFSET, 100);
	}
	
	private void save()
	{
		basicPanel.save(modelController);
		for(BasicEditPanel panel : basicPanels)
			panel.save(modelController);
		modelController.commit();
		guiController.openView(entityModel);
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

	@Override
	public Object getShownObject()
	{
		return entityModel.getEntity();
	}
}
