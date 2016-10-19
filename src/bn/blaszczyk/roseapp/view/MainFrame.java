package bn.blaszczyk.roseapp.view;

import java.awt.*;

import javax.swing.*;

import bn.blaszczyk.roseapp.controller.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ThemeConstants {

	private JTabbedPane tabbedPane = new JTabbedPane();
	private Actions actions;
	
	public MainFrame(FullModelController modelController, GUIController guiController, String title)
	{
		super(title);
		actions = new Actions(modelController, guiController);
		setLayout(new BorderLayout());
		
		ToolBar toolBar = new ToolBar(actions);
		add(toolBar,BorderLayout.PAGE_START);

		tabbedPane.addChangeListener(actions);
		add(tabbedPane,BorderLayout.CENTER);
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
	
	public JTabbedPane getTabbedPane()
	{
		return tabbedPane;
	}
	
//	public int getTabCount()
//	{
//		return tabbedPane.getTabCount();
//	}
//
//	public Component getComponentAt(int index)
//	{
//		return tabbedPane.getComponentAt(index);
//	}
	
}
