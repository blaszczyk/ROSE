package bn.blaszczyk.roseapp.view;

import java.awt.Font;
import java.awt.Toolkit;


import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ToolBar extends JPanel implements ThemeConstants {


	public ToolBar(Actions actions)
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		addButton("Close", "appclose.png", actions.getActnClose() );
		addButton("Edit", "appedit.png", actions.getActnEdit() );
		addButton("Save", "save.png", actions.getActnSave());
	}
	
	private JButton addButton(String text, String iconFile, Action action)
	{
		JButton button = new JButton();
		button.setFont(new Font("Arial", Font.PLAIN, 16));
		button.setAction(action);
		button.setText(text);
		button.setIcon( new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("../resources/" + iconFile))) );
		add(button);
		return button;
	}

}
