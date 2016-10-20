package bn.blaszczyk.roseapp.controller;

import java.awt.Component;

import bn.blaszczyk.roseapp.model.Entity;
import bn.blaszczyk.roseapp.model.EntityModel;
import bn.blaszczyk.roseapp.view.*;

public class GUIController {

	private FullModelController modelController;
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
		mainFrame = new MainFrame (modelController, this, title);
		for(Class<?> type : types)
		{
			FullListPanel panel = new FullListPanel(modelController, this, type);
			mainFrame.addTab(panel, type.getSimpleName() + "s", "applist.png" );
		}		
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
				if( edit ^ c instanceof FullPanel )
					mainFrame.getTabbedPane().setSelectedIndex(i);
				else
				{
					if(edit)
						mainFrame.replaceTab(i, new FullEditPanel(entityModel, modelController, this) , title, iconFile );
					else
						mainFrame.replaceTab(i, new FullPanel(entityModel, this) , title, iconFile );
				}
				return;
			}
		}
		if(edit)
			mainFrame.addTab( new FullEditPanel(entityModel, modelController, this) , title, iconFile );
		else
			mainFrame.addTab( new FullPanel(entityModel, this) , title, iconFile );
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
		{
			EntityModel entityModel = (EntityModel) ((MyPanel)c).getShownObject();
			modelController.delete( entityModel.getEntity() );
			closeCurrent();
		}
	}
	
	public void copyCurrent()
	{
		Component c = mainFrame.getTabbedPane().getSelectedComponent();
		if( c instanceof MyPanel && ((MyPanel)c).getShownObject() instanceof EntityModel )
		{
			EntityModel entityModel = (EntityModel) ((MyPanel)c).getShownObject();
			openEntityTab( modelController.createCopy( entityModel ), true );
		}
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
		for(Component c : mainFrame.getTabbedPane().getComponents() )
			if(c instanceof MyPanel && ((MyPanel)c).getShownObject() instanceof EntityModel )
			{
				EntityModel entityModel = (EntityModel) ((MyPanel)c).getShownObject();
				if(entityModel.getEntity().equals(entity))
					mainFrame.getTabbedPane().remove(c);
			}
		modelController.delete(entity);
	}
}
