package bn.blaszczyk.roseapp.view.panels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bn.blaszczyk.roseapp.controller.GUIController;
import bn.blaszczyk.roseapp.model.*;
import bn.blaszczyk.roseapp.view.tools.EntityTable;
import bn.blaszczyk.roseapp.view.tools.EntityTableModel;

@SuppressWarnings("serial")
public class FullViewPanel extends AlignPanel {
	
	private EntityModel entityModel;

	
	public FullViewPanel( EntityModel entityModel, GUIController guiController, boolean showTitle )
	{
		super( guiController);
		this.entityModel = entityModel;
		if(showTitle)
			setTitle( entityModel.getId() > 0 ? entityModel.getName() + " " + entityModel.getId() : "new " + entityModel.getName() );
		addBasicPanel(null, null, entityModel);
		for(int i = 0; i < entityModel.getEntityCount(); i++)
		{
			if( entityModel.getEntityValue(i) == null )
				continue;
			switch( entityModel.getRelationType(i) )
			{
			case MANYTOMANY:
			case ONETOMANY:
				if(!((Set<?>)entityModel.getEntityValue(i)).isEmpty())
					addEntityTable(i);
				break;
			case MANYTOONE:
				if(entityModel.getEntityValue(i)!= null)
					addBasicPanel( entityModel.getEntityName(i), createViewButton(i), entityModel.createModel( (Entity) entityModel.getEntityValue(i) ) );
				break;
			case ONETOONE:
				if(entityModel.getEntityValue(i)!= null)
					addFullPanel( null, null, entityModel.createModel( (Entity) entityModel.getEntityValue(i) ) );
				break;
			}
		}
		realign();
		
	}

	private JButton createViewButton( int index )
	{	
		JButton button = null;
		if(entityModel.getRelationType(index).equals(RelationType.MANYTOONE))
		{
			button = new JButton("View");
			try
			{
				button.setIcon( new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("bn/blaszczyk/roseapp/resources/view.png"))) );
			}
			catch (IOException e)
			{	
				e.printStackTrace();
			}
			button.addActionListener( e -> guiController.openEntityTab(entityModel.createModel( (Entity) entityModel.getEntityValue(index) ), false) );
		}		
		return button;
	}
	
	
	private void addBasicPanel( String title, JButton button,  EntityModel entityModel )
	{	
		super.addPanel( title, button, new BasicViewPanel(entityModel));
	}
	
	private void addFullPanel( String title, JButton button, EntityModel entityModel )
	{
		super.addPanel( title, button, new BasicViewPanel(entityModel) );
	}
	
	private void addEntityTable( int index )
	{
		List<EntityModel> entityModels = new ArrayList<>();
		Set<?> entities =  (Set<?>) entityModel.getEntityValue(index);
		for(Object entity : entities)
			entityModels.add(entityModel.createModel((Entity)entity));
		
		EntityTableModel tableModel = new EntityTableModel(entityModels,1);
		EntityTable table = new EntityTable( tableModel, BASIC_WIDTH, SUBTABLE_HEIGTH );
		table.setButtonColumn(0, "view.png", e -> guiController.openEntityTab( e, false ));
		JScrollPane scrollPane = new JScrollPane(table);
		
		super.addPanel( entityModel.getEntityName(index), createViewButton(index), scrollPane, BASIC_WIDTH, SUBTABLE_HEIGTH);
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
		return false;
	}
	
	
}
