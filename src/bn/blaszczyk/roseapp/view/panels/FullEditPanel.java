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
import bn.blaszczyk.roseapp.view.inputpanels.MyComboBox;
import bn.blaszczyk.roseapp.view.tools.EntityTable;
import bn.blaszczyk.roseapp.view.tools.EntityTableModel;

@SuppressWarnings("serial")
public class FullEditPanel extends AlignPanel {


	private BasicEditPanel basicPanel;
	private List<BasicEditPanel> basicPanels = new ArrayList<>();
	private Map<Integer,MyComboBox<Entity>> entityBoxes = new HashMap<>();
	
	private FullModelController modelController;
	private final Writable entity;

	public FullEditPanel( Writable entity, FullModelController modelController, GUIController guiController, boolean showTitle, ChangeListener listener )
	{
		this(entity, modelController, guiController, showTitle);
		setChangeListener(listener);		
	}
	public FullEditPanel( Writable entity, FullModelController modelController, GUIController guiController, boolean showTitle )
	{
		super(guiController);
		this.modelController = modelController;
		this.entity = entity;
		if(showTitle)
			setTitle( entity.getId() > 0 ? entity.getEntityName() + " " + entity.getId() : "new " + entity.getEntityName() );
		basicPanel = addBasicPanel(entity);
		for(int i = 0; i < entity.getEntityCount(); i++)
		{
			switch( entity.getRelationType(i))
			{
			case ONETOONE:
				if(	entity.getEntityValue(i) instanceof Writable )
					basicPanels.add( addBasicPanel( (Writable) entity.getEntityValue(i) ) );
				break;
			case MANYTOMANY:
			case ONETOMANY:
				addEntityTable( i );
				break;
			case MANYTOONE:
				addSelectionBox( i );
				break;
			}	
		}		
		realign();
	}
	

	private JButton createAddButton( int index )
	{	
		JButton button = null;
		if(entity.getRelationType(index).isSecondMany())
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
			button.addActionListener( e -> guiController.addNew( entity, index ) );
		}
		
		return button;
	}
	
	private BasicEditPanel addBasicPanel( Writable entity )
	{	
		BasicEditPanel basicPanel = new BasicEditPanel(entity) ;
		addPanel(null, null, basicPanel);
		return basicPanel;
	}
	
	@SuppressWarnings("unchecked")
	private void addEntityTable( int index )
	{
		List<Writable> entities = new ArrayList<>();
		entities.addAll((Set<? extends Writable>) entity.getEntityValue(index));

		EntityTableModel<Writable> tableModel = new EntityTableModel<>(entities,3);
		EntityTable<Writable> table = new EntityTable<>( tableModel, BASIC_WIDTH, SUBTABLE_HEIGTH );
		table.setButtonColumn(0, "edit.png", e -> guiController.openEntityTab( e, true ));
		table.setButtonColumn(1, "copy.png", e -> guiController.openEntityTab( modelController.createCopy((Writable) e), true ) );
		table.setButtonColumn(2, "delete.png", e -> guiController.delete((Writable) e) );
		JScrollPane scrollPane = new JScrollPane(table);
		
		super.addPanel( entity.getEntityName(index), createAddButton(index), scrollPane, BASIC_WIDTH, SUBTABLE_HEIGTH);
	}
	
	private void addSelectionBox( int index )
	{
		Entity[] entities = new Entity[modelController.getAllEntites(entity.getEntityClass(index)).size()];
		modelController.getAllEntites(entity.getEntityClass(index)).toArray(entities);
		MyComboBox<Entity> selectBox = new MyComboBox<>(entities, BASIC_WIDTH, true);
		if(entity.getEntityValue(index) != null)
			selectBox.setSelectedItem(entity.getEntityValue(index));
		selectBox.setFont(VALUE_FONT);
		selectBox.setForeground(VALUE_FG);
		entityBoxes.put(index, selectBox);
		
		super.addPanel( entity.getEntityName(index), null, selectBox, BASIC_WIDTH, LBL_HEIGHT);
	}
	
	public void save(FullModelController modelController)
	{
		basicPanel.save(modelController);
		for(BasicEditPanel panel : basicPanels)
			panel.save(modelController);
		for(Integer index : entityBoxes.keySet() )
			modelController.setEntityField(entity, index, ( (Writable)entityBoxes.get(index).getSelectedItem() ) );
	}

	@Override
	public JPanel getPanel()
	{
		return this;
	}

	@Override
	public Object getShownObject()
	{
		return entity;
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
			if( entityBoxes.get(index).getSelectedItem().equals( entity.getEntityValue(index) ) )
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
