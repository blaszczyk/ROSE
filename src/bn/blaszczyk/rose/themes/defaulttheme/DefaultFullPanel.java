package bn.blaszczyk.rose.themes.defaulttheme;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import bn.blaszczyk.rose.interfaces.*;

@SuppressWarnings("serial")
public abstract class DefaultFullPanel extends JPanel implements FullPanel {

	private static final int H_SPACING = 10;
	private static final int V_SPACING = 10;
	
	private static final int TITLE_WIDTH = 200;
	private static final int TITLE_HEIGHT = 35;
	private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 30);
	private static final Color TITLE_BG = Color.WHITE;
	private static final Color TITLE_FG = Color.RED;
	
	private static final int SUBTITLE_WIDTH = 200;
	private static final int SUBTITLE_HEIGHT = 30;
	private static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 25);
	private static final Color SUBTITLE_BG = Color.CYAN;
	private static final Color SUBTITLE_FG = Color.BLUE;
	
	private static final Color BACKGROUND = Color.LIGHT_GRAY;
	
	private static final int H_OFFSET = 20;	
	private int width = 2 * H_SPACING;
	private int height = V_SPACING;

	private GUIController controller;
	private Object object;
	
	public DefaultFullPanel( Object object, GUIController controller )
	{
		this.controller = controller;
		this.object = object;
		setLayout(null);
		setBackground(BACKGROUND);
		addTitle( getName() );
		addBasicPanel(object);
		for(int i = 0; i < getEntityCount(); i++)
		{
			if( getEntityMember(i) == null)
				continue;
			addSubTitle( getEntityName(i) );
			addBasicPanel( getEntityMember(i) );
		}
		
	}

	private void addTitle( String title )
	{
		height += H_OFFSET;
		JLabel lblTitle = new JLabel( title );
		lblTitle.setFont(TITLE_FONT);
		lblTitle.setForeground(TITLE_FG);
		lblTitle.setBackground(TITLE_BG);
		lblTitle.setBounds(H_SPACING, height, TITLE_WIDTH, TITLE_HEIGHT);
		lblTitle.setOpaque(true);
		add(lblTitle);
		computeDimensions(TITLE_HEIGHT, TITLE_WIDTH);		
	}
	
	private void addSubTitle( String subtitle )
	{
		height += H_OFFSET;
		JLabel lblSubTitle = new JLabel( subtitle );
		lblSubTitle.setFont(SUBTITLE_FONT);
		lblSubTitle.setForeground(SUBTITLE_FG);
		lblSubTitle.setBackground(SUBTITLE_BG);
		lblSubTitle.setBounds(H_SPACING, height, SUBTITLE_WIDTH, SUBTITLE_HEIGHT);
		lblSubTitle.setOpaque(true);
		add(lblSubTitle);
		computeDimensions(TITLE_HEIGHT, TITLE_WIDTH);		
	}
	
	private void addBasicPanel( Object object )
	{	
		BasicPanel basicPanel = controller.createBasicPanel(object);
		JPanel panel = basicPanel.getPanel();
		panel.setBounds(H_SPACING, height, basicPanel.getWidth() , basicPanel.getHeight() );
		add(panel);
		computeDimensions( basicPanel.getHeight(), basicPanel.getWidth() );
	}
	
	private void computeDimensions( int height, int width )
	{
		this.width = Math.max(this.width, 2 * H_SPACING + width);
		this.height += V_SPACING + height;
	}

	
	
	@Override
	public Object getObject()
	{
		return object;
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	public JPanel getPanel()
	{
		return this;
	}
	
}
