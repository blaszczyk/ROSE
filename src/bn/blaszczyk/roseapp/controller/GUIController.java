package bn.blaszczyk.roseapp.controller;

import java.awt.Component;
import java.util.Set;

import bn.blaszczyk.roseapp.model.Entity;
import bn.blaszczyk.roseapp.model.EntityModel;
import bn.blaszczyk.roseapp.view.*;
import bn.blaszczyk.roseapp.view.panels.FullEditPanel;
import bn.blaszczyk.roseapp.view.panels.FullListPanel;
import bn.blaszczyk.roseapp.view.panels.FullViewPanel;
import bn.blaszczyk.roseapp.view.panels.MyPanel;
import bn.blaszczyk.roseapp.view.panels.StartPanel;

public class GUIController {

	private FullModelController modelController;
	private Class<?>[] types;
	private MainFrame mainFrame;
		
	public GUIController(FullModelController modelController)
	{
		this.modelController = modelController;
	}
	
	public void editCurrent()
	{
		openEntityTab( ((EntityModel) ((MyPanel)mainFrame.getTabbedPane().getSelectedComponent()).getShownObject()), true );
	}
	
	public void createMainFrame(Class<?>[] types, String title)
	{
		mainFrame = new MainFrame ( this, title);
		this.types = types;
		openStartTab();
	}
	
	public void openStartTab()
	{
		for(int i = 0; i < mainFrame.getTabbedPane().getTabCount(); i++)
		{
			Component c = mainFrame.getTabbedPane().getComponentAt(i);
			if( c instanceof StartPanel )
			{
				mainFrame.getTabbedPane().setSelectedIndex(i);
				return;
			}
		}
		mainFrame.addTab(new StartPanel(this, types), "Start", "start.png");
	}
	
	public void openFullListTab( Class<?> type )
	{
		for(int i = 0; i < mainFrame.getTabbedPane().getTabCount(); i++)
		{
			Component c = mainFrame.getTabbedPane().getComponentAt(i);
			if( c instanceof MyPanel && ((MyPanel)c).getShownObject().equals(type) )
			{
				mainFrame.getTabbedPane().setSelectedIndex(i);
				return;
			}
		}
		String title = type.getSimpleName() + "s";
		mainFrame.addTab( new FullListPanel(modelController, this, type), title , "applist.png" );
	}
	
	public void openEntityTab(EntityModel entityModel, boolean edit )
	{
		String iconFile = edit ? "edit.png" : "view.png";
		String title = entityModel.getId() > 0 ? entityModel.getName() + " " + entityModel.getId() : "new " + entityModel.getName();
		for(int i = 0; i < mainFrame.getTabbedPane().getTabCount(); i++)
		{
			Component c = mainFrame.getTabbedPane().getComponentAt(i);
			if( c instanceof MyPanel && ((MyPanel)c).getShownObject().equals(entityModel) )
			{
				if( edit ^ c instanceof FullViewPanel )
					mainFrame.getTabbedPane().setSelectedIndex(i);
				else
				{
					if(edit)
						mainFrame.replaceTab(i, new FullEditPanel(entityModel, modelController, this, mainFrame.getActions()) , title, iconFile );
					else
						mainFrame.replaceTab(i, new FullViewPanel(entityModel, this) , title, iconFile );
				}
				return;
			}
		}
		if(edit)
			mainFrame.addTab( new FullEditPanel(entityModel, modelController, this, mainFrame.getActions()) , title, iconFile );
		else
			mainFrame.addTab( new FullViewPanel(entityModel, this) , title, iconFile );
	}
	
	public void saveCurrent()
	{
		Component c = mainFrame.getTabbedPane().getSelectedComponent();
		if( c instanceof FullEditPanel)
		{
			FullEditPanel panel = (FullEditPanel) c;
			panel.save(modelController);
			modelController.commit();
			openEntityTab((EntityModel) panel.getShownObject(),false);
		}
	}
	
	public void closeCurrent()
	{
		Component c = mainFrame.getTabbedPane().getSelectedComponent();
			mainFrame.getTabbedPane().remove(c);
	}

	public void deleteCurrent()
	{
		Component c = mainFrame.getTabbedPane().getSelectedComponent();
		if( c instanceof MyPanel && ((MyPanel)c).getShownObject() instanceof EntityModel )
			delete( ((EntityModel) ((MyPanel)c).getShownObject()).getEntity() );
	}
	
	public void copyCurrent()
	{
		Component c = mainFrame.getTabbedPane().getSelectedComponent();
		if( c instanceof MyPanel && ((MyPanel)c).getShownObject() instanceof EntityModel )
			openEntityTab( modelController.createCopy( (EntityModel) ((MyPanel)c).getShownObject() ), true );
	}

	public void openNew()
	{
		Component c = mainFrame.getTabbedPane().getSelectedComponent();
		if( !(c instanceof MyPanel) )
			return;
		Object o = ((MyPanel)c).getShownObject();
		Class<?> type;
		if( o instanceof Class<?>)
			type = (Class<?>) o;
		else if( o instanceof EntityModel )
			type = ((EntityModel)o).getEntity().getClass();
		else
			return;
		openEntityTab( modelController.createModel( modelController.createNew( type.getSimpleName() ) ), true );
	}

	public void addNew(EntityModel entityModel, int index)
	{
		Entity entity = modelController.createNew( entityModel.getEntityClass(index).getSimpleName() );
		modelController.addEntityMember(entityModel.getEntity(), entityModel.getEntityName(index), entity);
		openEntityTab( modelController.createModel(entity), true);
	}

	public void saveAll()
	{
		int current = mainFrame.getTabbedPane().getSelectedIndex();
		for(Component c : mainFrame.getTabbedPane().getComponents())
			if( c instanceof FullEditPanel )
			{
				FullEditPanel panel = (FullEditPanel) c;
				if(panel.hasChanged())
					panel.save(modelController);
				openEntityTab((EntityModel) panel.getShownObject(),false);
			}
		modelController.commit();
		mainFrame.getTabbedPane().setSelectedIndex(current);
	}

	public void delete(Entity entity)
	{
		EntityModel entityModel = modelController.createModel(entity);
		for(int i = 0; i < entityModel.getEntityCount(); i++)
		{
			switch(entityModel.getRelationType(i))
			{
			case MANYTOMANY:
				break;
			case MANYTOONE:
				break;
			case ONETOMANY:
				Set<?> set = (Set<?>) entityModel.getEntityMember(i);
				for(Object o : set.toArray())
					delete((Entity) o);
				break;
			case ONETOONE:
//				delete((Entity) entityModel.getEntityMember(i));
				break;
			case ENUM:
				break;
			}
		}		
		for(Component c : mainFrame.getTabbedPane().getComponents() )
			if(c instanceof MyPanel && ((MyPanel)c).getShownObject() instanceof EntityModel )
				if( ((EntityModel) ((MyPanel)c).getShownObject()).getEntity().equals(entity))
					mainFrame.getTabbedPane().remove(c);
		modelController.delete(entity);
	}

	public void closeAll()
	{
		while(mainFrame.getTabbedPane().getSelectedComponent() != null)
			closeCurrent();
	}
}
