package bn.blaszczyk.roseapp.view.panels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bn.blaszczyk.roseapp.controller.*;
import bn.blaszczyk.roseapp.model.*;
import bn.blaszczyk.roseapp.view.MemberTable;
import bn.blaszczyk.roseapp.view.MemberTableModel;
import bn.blaszczyk.roseapp.view.ThemeConstants;
import bn.blaszczyk.roseapp.view.inputpanels.MyComboBox;

@SuppressWarnings("serial")
public class FullEditPanel extends JPanel implements MyPanel, ThemeConstants {


	private int width = 2 * H_SPACING;
	private int height = V_SPACING;

	private BasicEditPanel basicPanel;
	private List<BasicEditPanel> basicPanels = new ArrayList<>();
	private Map<Integer,MyComboBox<Entity>> entityBoxes = new HashMap<>();
	
	private FullModelController modelController;
	private GUIController guiController;
	private EntityModel entityModel;

	public FullEditPanel( EntityModel entityModel, FullModelController modelController, GUIController guiController, ChangeListener listener )
	{
		this(entityModel, modelController, guiController);
		setChangeListener(listener);		
	}
	public FullEditPanel( EntityModel entityModel, FullModelController modelController, GUIController guiController )
	{
		this.modelController = modelController;
		this.guiController = guiController;
		this.entityModel = entityModel;
		setLayout(null);
		setBackground(FULL_PNL_BACKGROUND);
		addTitle( entityModel );
		basicPanel = addBasicPanel(entityModel);
		for(int i = 0; i < entityModel.getEntityCount(); i++)
		{
			addSubTitle( i );
			switch( entityModel.getRelationType(i))
			{
			case ONETOONE:
				basicPanels.add( addBasicPanel( entityModel.createModel( (Entity) entityModel.getEntityMember(i))));
				break;
			case ONETOMANY:
				List<EntityModel> entityModels = new ArrayList<>();
				Set<?> objects =  (Set<?>) entityModel.getEntityMember(i);
				for(Object object : objects)
					entityModels.add(entityModel.createModel((Entity)object));
				addMemberTable( entityModels, entityModel.getEntity(),entityModel.getEntityName(i) );
				break;
			case MANYTOONE:
				addSelectionBox( i );
				break;
			case MANYTOMANY:
				break;
			case ENUM:
				break;
			}	
		}
		
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
	
	private void addSubTitle( int index )
	{
		height += V_OFFSET;
		JLabel lblSubTitle = new JLabel( entityModel.getEntityName(index) );
		lblSubTitle.setFont(SUBTITLE_FONT);
		lblSubTitle.setForeground(SUBTITLE_FG);
		lblSubTitle.setBackground(SUBTITLE_BG);
		lblSubTitle.setBounds(H_SPACING, height, SUBTITLE_WIDTH, SUBTITLE_HEIGHT);
		lblSubTitle.setOpaque(true);
		add(lblSubTitle);
		
		if(entityModel.getRelationType(index).isSecondMany())
		{
			JButton btnView = new JButton("Add");
			try
			{
				btnView.setIcon( new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("bn/blaszczyk/roseapp/resources/add.png"))) );
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			btnView.setBounds(2 * H_SPACING + SUBTITLE_WIDTH, height , 100, SUBTITLE_HEIGHT);
			final EntityModel entityModelCpy = entityModel;
			btnView.addActionListener( e -> guiController.addNew( entityModelCpy, index ) );
			add(btnView);
		}
		
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
		MemberTable table = new MemberTable( tableModel );
		table.setButtonColumn(0, "edit.png", e -> guiController.openEntityTab( e, true ));
		table.setButtonColumn(1, "copy.png", e -> guiController.openEntityTab( modelController.createCopy(e), true ) );
		table.setButtonColumn(2, "delete.png", e -> guiController.delete(e.getEntity()) );
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(2 * H_SPACING, height, table.getWidth(), table.getHeight());
		add(scrollPane);
		computeDimensions( scrollPane.getHeight(), scrollPane.getWidth() );
	}
	
	private void addSelectionBox( int index )
	{
		Entity[] entities = new Entity[modelController.getAllModels(entityModel.getEntityClass(index)).size()];
		int count = 0;
		for( EntityModel entityModel : modelController.getAllModels(entityModel.getEntityClass(index)))
			entities[count++] = entityModel.getEntity();
		MyComboBox<Entity> selectBox = new MyComboBox<>(entities, 300, true);
		if(entityModel.getEntityMember(index) != null)
			selectBox.setSelectedItem(entityModel.getEntityMember(index));
		selectBox.setBounds( 2* H_SPACING, height, 600, 30);
		add(selectBox);
		entityBoxes.put(index, selectBox);
		computeDimensions(30, 600);
	}
	
	public void save(FullModelController modelController)
	{
		basicPanel.save(modelController);
		for(BasicEditPanel panel : basicPanels)
			panel.save(modelController);
		for(Integer index : entityBoxes.keySet() )
			modelController.setEntityMember(entityModel.getEntity(), entityModel.getEntityName(index), ( (Entity)entityBoxes.get(index).getSelectedItem() ) );
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
		return entityModel;
	}
	

	@Override
	public boolean hasChanged()
	{
		if(basicPanel.hasChanged())
			return true;
		for(BasicEditPanel panel : basicPanels)
			if(panel.hasChanged())
				return true;
		for(Integer index : entityBoxes.keySet() )
			if( entityModel.getEntityMember(index).equals( entityBoxes.get(index).getSelectedItem() ) )
				return true;
		return false;
	}
	
	public void setChangeListener(ChangeListener l)
	{
		basicPanel.setChangeListener(l);
		for(BasicEditPanel panel : basicPanels)
			panel.setChangeListener(l);
		for(Integer index : entityBoxes.keySet() )
			entityBoxes.get(index).addItemListener(e -> l.stateChanged(new ChangeEvent(entityBoxes.get(index))));
	}
}
