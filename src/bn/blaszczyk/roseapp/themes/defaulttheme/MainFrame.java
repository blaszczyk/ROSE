package bn.blaszczyk.roseapp.themes.defaulttheme;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import bn.blaszczyk.roseapp.controller.FullModelController;
import bn.blaszczyk.roseapp.controller.GUIController;

public class MainFrame extends JFrame implements ThemeConstants {
	
	public MainFrame(FullModelController modelController, GUIController guiController, Class<?>... types)
	{
		super("Overview");
		JTabbedPane tabbedPane = new JTabbedPane();
		for(Class<?> type : types)
		{
			FullListPanel panel = new FullListPanel(modelController, guiController, type);
			panel.setBounds(0, 0, MF_WIDTH-10, MF_HEIGTH-45);
			tabbedPane.addTab(type.getSimpleName() + "s",  panel);
		}
		add(tabbedPane);
		setSize( MF_WIDTH, MF_HEIGTH );
		setLocationRelativeTo(null);
		setVisible(true);	
	}
	
	
}
