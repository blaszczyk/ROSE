package bn.blaszczyk.roseapp.view;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import bn.blaszczyk.roseapp.controller.FullModelController;
import bn.blaszczyk.roseapp.controller.GUIController;

public class MainFrame extends JFrame implements ThemeConstants {
	

	JTabbedPane tabbedPane = new JTabbedPane();
	
	public MainFrame(FullModelController modelController, GUIController guiController, Class<?>... types)
	{
		super("Overview");
		for(Class<?> type : types)
		{
			FullListPanel panel = new FullListPanel(modelController, guiController, type);
			panel.setBounds(0, 0, MF_WIDTH-10, MF_HEIGTH-45);
			addTab(panel, type.getSimpleName() + "s",  false);
		}
		add(tabbedPane);
		setSize( MF_WIDTH, MF_HEIGTH );
		setLocationRelativeTo(null);
		setVisible(true);	
	}
	
	public void addTab( Component component, String name, boolean closable)
	{
		tabbedPane.addTab(name, component);
	}
	
	public void replaceTab( int index, Component component )
	{
		tabbedPane.setTabComponentAt(index, component);
	}
	
}
