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
	
	public void createMainFrame(Class<?>[] types, String title)
	{
		mainFrame = new MainFrame (modelController, this, types, title);		
	}
	
	private void openTab(MyPanel panel, EntityModel entityModel, String iconFile )
	{
		int index = 0;
		for(int i = 0; i < mainFrame.getTabCount(); i++)
		{
			Component c = mainFrame.getComponentAt(i);
			if( c instanceof MyPanel && ((MyPanel)c).getShownObject().equals(entityModel.getEntity()) )
				mainFrame.replaceTab(index, panel.getPanel(), entityModel.getName() + " " + entityModel.getId(), iconFile,  true);				
			index++;
		}
		if(index >= mainFrame.getTabCount())
		{
			mainFrame.addTab(panel.getPanel(), entityModel.getName() + " " + entityModel.getId(), iconFile,  true);
		}
		
	}
}
