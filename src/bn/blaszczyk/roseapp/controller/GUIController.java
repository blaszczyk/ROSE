package bn.blaszczyk.roseapp.controller;

import java.awt.Component;

import bn.blaszczyk.roseapp.model.EntityModel;
import bn.blaszczyk.roseapp.view.*;

public class GUIController {

	private FullModelController modelController;
	private MainFrame mainFrame;
		
	public GUIController(FullModelController modelController)
	{
		this.modelController = modelController;
	}

	public void openView(EntityModel entityModel)
	{
		MyPanel panel = new FullPanel(entityModel,this);
		openTab(panel, entityModel, "view.png");
	}

	public void openEdit( EntityModel entityModel)
	{
		MyPanel panel = new FullEditPanel(entityModel,modelController, this);
		openTab(panel, entityModel, "edit.png");
		
//
//		as Dialog:
//		JDialog dialog = new JDialog(mainFrame, entityModel.getName(), ModalityType.APPLICATION_MODAL);
//		dialog.add(panel.getPanel());
//		dialog.setSize( panel.getWidth() + 16, panel.getHeight() + 46 );
//		dialog.setLocationRelativeTo(mainFrame);
//		dialog.setVisible(true);				
//
	}
	
	public void editCurrent()
	{
		openEdit( ((EntityModel) ((MyPanel)mainFrame.getTabbedPane().getSelectedComponent()).getShownObject()) );
	}
	
	public void createMainFrame(Class<?>[] types, String title)
	{
		mainFrame = new MainFrame (modelController, this, title);
		for(Class<?> type : types)
		{
			FullListPanel panel = new FullListPanel(modelController, this, type);
			mainFrame.addTab(panel, type.getSimpleName() + "s", "applist.png" ,  false);
		}		
	}
	
	private void openTab(MyPanel panel, EntityModel entityModel, String iconFile )
	{
		int index = 0;
		for(int i = 0; i < mainFrame.getTabbedPane().getTabCount(); i++)
		{
			Component c = mainFrame.getTabbedPane().getComponentAt(i);
			if( c instanceof MyPanel && ((MyPanel)c).getShownObject().equals(entityModel) )
				mainFrame.replaceTab(index, panel.getPanel(), entityModel.getName() + " " + entityModel.getId(), iconFile,  true);				
			index++;
		}
		if(index >= mainFrame.getTabbedPane().getTabCount())
		{
			mainFrame.addTab(panel.getPanel(), entityModel.getName() + " " + entityModel.getId(), iconFile,  true);
		}
		
	}
	
	public void saveCurrent()
	{
		Component c = mainFrame.getTabbedPane().getSelectedComponent();
		if( c instanceof FullEditPanel)
		{
			FullEditPanel panel = (FullEditPanel) c;
			panel.save(modelController);
			modelController.commit();
			openView((EntityModel) panel.getShownObject());
		}
		
		
	}
	
	public void closeCurrent()
	{
		Component c = mainFrame.getTabbedPane().getSelectedComponent();
		if( c instanceof MyPanel )
		{
			if(!((MyPanel) c).hasChanged())
				mainFrame.getTabbedPane().remove(c);
		}
	}
}
