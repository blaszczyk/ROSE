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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bn.blaszczyk.roseapp.controller.*;
import bn.blaszczyk.roseapp.model.*;
import bn.blaszczyk.roseapp.view.EntityTable;
import bn.blaszczyk.roseapp.view.EntityTableModel;
import bn.blaszczyk.roseapp.view.inputpanels.MyComboBox;

@SuppressWarnings("serial")
public class FullEditPanel extends AlignPanel {


	private BasicEditPanel basicPanel;
	private List<BasicEditPanel> basicPanels = new ArrayList<>();
	private Map<Integer,MyComboBox<Entity>> entityBoxes = new HashMap<>();
	
	private FullModelController modelController;
	private final EntityModel entityModel;

	public FullEditPanel( EntityModel entityModel, FullModelController modelController, GUIController guiController, boolean showTitle, ChangeListener listener )
	{
		this(entityModel, modelController, guiController, showTitle);
		setChangeListener(listener);		
	}
	public FullEditPanel( EntityModel entityModel, FullModelController modelController, GUIController guiController, boolean showTitle )
	{
		super(guiController);
		this.modelController = modelController;
		this.entityModel = entityModel;
		if(showTitle)
			addTitle( entityModel.getId() > 0 ? entityModel.getName() + " " + entityModel.getId() : "new " + entityModel.getName() );
		basicPanel = addBasicPanel(entityModel);
		for(int i = 0; i < entityModel.getEntityCount(); i++)
		{
			switch( entityModel.getRelationType(i))
			{
			case ONETOONE:
				basicPanels.add( addBasicPanel( entityModel.createModel( (Entity) entityModel.getEntityValue(i))));
				break;
			case MANYTOMANY:
			case ONETOMANY:
				List<EntityModel> entityModels = new ArrayList<>();
				Set<?> objects =  (Set<?>) entityModel.getEntityValue(i);
				for(Object object : objects)
					entityModels.add(entityModel.createModel((Entity)object));
				addEntityTable( i );
				break;
			case MANYTOONE:
				addSelectionBox( i );
				break;
			}	
		}		
	}
	

	private JButton createAddButton( int index )
	{	
		JButton button = null;
		if(entityModel.getRelationType(index).isSecondMany())
		{
			button = new JButton("Add");
			try
			{
				button.setIcon( new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("bn/blaszczyk/roseapp/resources/add.png"))) );
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			button.addActionListener( e -> guiController.addNew( entityModel, index ) );
		}
		
		return button;
	}
	
	private BasicEditPanel addBasicPanel( EntityModel entityModel )
	{	
		BasicEditPanel basicPanel = new BasicEditPanel(entityModel) ;
		addPanel(null, null, basicPanel);
		return basicPanel;
	}
	
	private void addEntityTable( int index )
	{
		List<EntityModel> entityModels = new ArrayList<>();
		Set<?> entities =  (Set<?>) entityModel.getEntityValue(index);
		for(Object entity : entities)
			entityModels.add(entityModel.createModel((Entity)entity));

		EntityTableModel tableModel = new EntityTableModel(entityModels,3);
		EntityTable table = new EntityTable( tableModel, BASIC_WIDTH, SUBTABLE_HEIGTH );
		table.setButtonColumn(0, "edit.png", e -> guiController.openEntityTab( e, true ));
		table.setButtonColumn(1, "copy.png", e -> guiController.openEntityTab( modelController.createCopy(e), true ) );
		table.setButtonColumn(2, "delete.png", e -> guiController.delete(e.getEntity()) );
		JScrollPane scrollPane = new JScrollPane(table);
		
		super.addPanel( entityModel.getEntityName(index), createAddButton(index), scrollPane, BASIC_WIDTH, SUBTABLE_HEIGTH);
	}
	
	private void addSelectionBox( int index )
	{
		Entity[] entities = new Entity[modelController.getAllModels(entityModel.getEntityClass(index)).size()];
		int count = 0;
		for( EntityModel entityModel : modelController.getAllModels(entityModel.getEntityClass(index)))
			entities[count++] = entityModel.getEntity();
		MyComboBox<Entity> selectBox = new MyComboBox<>(entities, BASIC_WIDTH, true);
		if(entityModel.getEntityValue(index) != null)
			selectBox.setSelectedItem(entityModel.getEntityValue(index));
		selectBox.setFont(VALUE_FONT);
		selectBox.setForeground(VALUE_FG);
		entityBoxes.put(index, selectBox);
		
		super.addPanel( entityModel.getEntityName(index), null, selectBox, BASIC_WIDTH, LBL_HEIGHT);
	}
	
	public void save(FullModelController modelController)
	{
		basicPanel.save(modelController);
		for(BasicEditPanel panel : basicPanels)
			panel.save(modelController);
		for(Integer index : entityBoxes.keySet() )
			modelController.setEntityField(entityModel.getEntity(), entityModel.getEntityName(index), ( (Entity)entityBoxes.get(index).getSelectedItem() ) );
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
			if( entityBoxes.get(index).getSelectedItem().equals( entityModel.getEntityValue(index) ) )
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
