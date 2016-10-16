package bn.blaszczyk.roseapp.controller;

import java.awt.Dialog.ModalityType;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;

import bn.blaszczyk.roseapp.model.EntityModel;
import bn.blaszczyk.roseapp.themes.defaulttheme.FullEditPanel;
import bn.blaszczyk.roseapp.themes.defaulttheme.FullListPanel;
import bn.blaszczyk.roseapp.themes.defaulttheme.FullPanel;
import bn.blaszczyk.roseapp.themes.defaulttheme.MainFrame;
import bn.blaszczyk.roseapp.themes.defaulttheme.MyPanel;

public class GUIController {

	private FullModelController modelController;
	private MainFrame mainFrame;
		
	public GUIController(FullModelController modelController)
	{
		this.modelController = modelController;
	}

	public void createFullPanelDialog(EntityModel entityModel)
	{
		JDialog dialog = new JDialog(mainFrame, entityModel.getName(), ModalityType.APPLICATION_MODAL);
		MyPanel panel = new FullPanel(entityModel,this);
		dialog.add(panel.getPanel());
		dialog.setSize( panel.getWidth() + 16, panel.getHeight() + 46 );
		dialog.setLocationRelativeTo(mainFrame);
		dialog.setVisible(true);				
	}

	public void createEditPanelDialog( EntityModel entityModel)
	{
		JDialog dialog = new JDialog(mainFrame, entityModel.getName(), ModalityType.APPLICATION_MODAL);
		MyPanel panel = new FullEditPanel(entityModel,modelController);
		dialog.add(panel.getPanel());
		dialog.setSize( panel.getWidth() + 16, panel.getHeight() + 46 );
		dialog.setLocationRelativeTo(mainFrame);
		dialog.setVisible(true);				
	}
	
	public void createMainFrame(Window owner, Class<?>... types)
	{
		mainFrame = new MainFrame (modelController, this, types);		
	}
}
