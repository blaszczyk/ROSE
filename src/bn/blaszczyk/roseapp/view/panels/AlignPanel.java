package bn.blaszczyk.roseapp.view.panels;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bn.blaszczyk.roseapp.controller.*;
import bn.blaszczyk.roseapp.view.ThemeConstants;

@SuppressWarnings("serial")
public abstract class AlignPanel extends JPanel implements MyPanel, ThemeConstants {


	private int width = 2 * H_SPACING;
	private int height = V_SPACING;
	private int h_offset = H_SPACING;
	private int v_offset = V_SPACING;
	
	protected GUIController guiController;

	public AlignPanel( GUIController guiController )
	{
		this.guiController = guiController;
		setLayout(null);
		setBackground(FULL_PNL_BACKGROUND);		
	}

	protected void addTitle( String text )
	{
//		height += V_OFFSET;
		JLabel lblTitle = new JLabel( text );
		lblTitle.setFont(TITLE_FONT);
		lblTitle.setForeground(TITLE_FG);
		lblTitle.setBackground(TITLE_BG);
		lblTitle.setBounds(h_offset, v_offset, TITLE_WIDTH, TITLE_HEIGHT);
		lblTitle.setOpaque(true);
		add(lblTitle);		
		computeDimensions(TITLE_HEIGHT, TITLE_WIDTH);		
	}
	
	protected void addPanel( String title, JButton button,  MyPanel panel )
	{
		addPanel(title, button, panel.getPanel(), panel.getWidth(), panel.getHeight());		
	}
	
	protected void addPanel( String title, JButton button,  JComponent component, int width, int height )
	{
		v_offset += V_OFFSET;
		if( v_offset + height > PANEL_HEIGHT ) 
		{
			v_offset = 2 * V_SPACING + TITLE_HEIGHT;
			h_offset = this.width + 2 * H_SPACING;
		}
		if(title != null)
		{
			JLabel lblSubTitle = new JLabel( title );
			lblSubTitle.setFont(SUBTITLE_FONT);
			lblSubTitle.setForeground(SUBTITLE_FG);
			lblSubTitle.setBackground(SUBTITLE_BG);
			lblSubTitle.setBounds(h_offset, v_offset, SUBTITLE_WIDTH, SUBTITLE_HEIGHT);
			lblSubTitle.setOpaque(true);
			add(lblSubTitle);
			
			if(button != null)
			{
				button.setBounds( h_offset + H_SPACING + SUBTITLE_WIDTH, v_offset , SUBTLTBTN_WIDTH, SUBTITLE_HEIGHT);
				add(button);
			}
			computeDimensions(SUBTITLE_HEIGHT, SUBTITLE_WIDTH);
		}		
		component.setBounds(h_offset, v_offset, width, height);
		add(component);
		computeDimensions(height, width);
	}
	
	private void computeDimensions( int height, int width )
	{
//		System.out.printf( "(%4d,%4d) - (%4d,%4d)- (%4d,%4d)\n",  h_offset, v_offset, this.width, this.height, width, height);	
		this.v_offset += V_SPACING + height;
		this.width = Math.max(this.width, h_offset + H_SPACING + width);
		this.height = Math.max(this.height, v_offset + V_SPACING);
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
