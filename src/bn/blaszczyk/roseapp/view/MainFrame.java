package bn.blaszczyk.roseapp.view;

import java.awt.*;

import javax.swing.*;

import bn.blaszczyk.roseapp.controller.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ThemeConstants {

	JTabbedPane tabbedPane = new JTabbedPane();
	
	public MainFrame(FullModelController modelController, GUIController guiController, Class<?>... types)
	{
		super("Overview");
		for(Class<?> type : types)
		{
			FullListPanel panel = new FullListPanel(modelController, guiController, type);
			panel.setBounds(0, 0, MF_WIDTH-10, MF_HEIGTH-45);
			addTab(panel, type.getSimpleName() + "s", "applist.png" ,  false);
		}
		add(tabbedPane);
		setSize( MF_WIDTH, MF_HEIGTH );
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);	
	}
	
	public int addTab( Component component, String name, String iconFile, boolean closable)
	{
		tabbedPane.addTab(name, component);
		int index = tabbedPane.getTabCount() - 1;
		JLabel tabLabel = new JLabel(name,  new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("../resources/" + iconFile))), SwingConstants.LEFT);
		tabLabel.setFont(new Font("Arial",Font.PLAIN, 18));
		tabLabel.setBounds(0, 0, 70, 20);
		tabbedPane.setTabComponentAt(index,tabLabel);
		tabbedPane.setSelectedIndex(index);
		return index;
	}
	
	public void replaceTab( int index, Component component, String name, String iconFile, boolean closable)
	{
		tabbedPane.setComponentAt(index, component);
		JLabel tabLabel = new JLabel(name,  new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("../resources/" + iconFile))), SwingConstants.LEFT);
		tabLabel.setFont(new Font("Arial",Font.PLAIN, 18));
		tabLabel.setBounds(0, 0, 70, 20);
		tabbedPane.setTabComponentAt(index, tabLabel);
		tabbedPane.setSelectedIndex(index);
	}
	
	
	
	public int getTabCount()
	{
		return tabbedPane.getTabCount();
	}

	public Component getComponentAt(int index)
	{
		return tabbedPane.getComponentAt(index);
	}
	
}
