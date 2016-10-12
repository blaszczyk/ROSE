package bn.blaszczyk.rose.themes.defaulttheme;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.*;

import bn.blaszczyk.rose.interfaces.MyPanel;

@SuppressWarnings("serial")
public class MemberTable extends JTable implements MyPanel, ThemeConstants {


	
	/*
	 * Custom Cell Renderer
	 */
	private final TableCellRenderer cellRenderer = new TableCellRenderer(){
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			String text = "";
			if(value instanceof Date)
				text = DATE_FORMAT.format(value);
			else if(value instanceof Double)
				text = DOUBLE_FORMAT.format(value);
			else if(value instanceof Integer)
				text = INT_FORMAT.format(value);
			else if(value instanceof String)
				text = (String) value;
			
			JLabel c = new JLabel( text );
			c.setOpaque(true);
			if(row < 0 )
			{
				c.setText(" " + c.getText() + " ");
				c.setFont(HEADER_FONT);
				c.setBackground(HEADER_BG);
				c.setBorder(BorderFactory.createEtchedBorder());
			}
			else
				if( (row % 2) == 1)
				{
					c.setBackground(ODD_BG);
					c.setFont( ODD_FONT );
					c.setForeground(ODD_FG);
				}
				else
				{
					c.setBackground(EVEN_BG);
					c.setFont( EVEN_FONT );
					c.setForeground(EVEN_FG);
				}
			return c;
		}
	};
	
//	private MemberTableModel tableModel;
	private JPanel panel = new JPanel();
	private GUIController controller;
//	private final TableRowSorter<TableModel> sorter = new TableRowSorter<>();
	
	public MemberTable(MemberTableModel tableModel, GUIController controller)
	{
		super(tableModel);
		this.controller = controller;
//		this.tableModel = tableModel;
		panel.setLayout(null);
		JScrollPane scrollPane = new JScrollPane(this);
		scrollPane.setBounds(0, 0, getWidth(), getHeight());
		panel.add(scrollPane);
		

		setShowGrid(false);
		setIntercellSpacing(new Dimension(0, 0));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().setFont(HEADER_FONT);
//		setRowSorter(sorter);
		
		addMouseListener( new MouseAdapter() 
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1 )
				{
					int row = rowAtPoint( e.getPoint() );
					controller.createFullPanelDialog(null, tableModel.getEntityModel(row));
				}
			}
			
		});
		
		setRowHeight(ODD_FONT.getSize() + 10);
		
		setCellRenderer();
		setWidths();
	}

	@Override
	public int getWidth()
	{
		return Math.max(getColumnCount()* CELL_WIDTH + 16, TABLE_WIDTH);
	}

	@Override
	public int getHeight()
	{
		return Math.min( (getRowCount()+1) * CELL_HEIGTH + 6, TABLE_HEIGHT);
	}
	
	@Override
	public JPanel getPanel()
	{
		return panel;
	}
	
	private void setCellRenderer()
	{
		getTableHeader().setDefaultRenderer(cellRenderer);
		for(int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++)
			getColumnModel().getColumn(columnIndex).setCellRenderer( cellRenderer );
	}
	
	private void setWidths()
	{
		for(int i = 0 ; i < this.getColumnCount(); i++)
		{
			int width = Math.max( CELL_WIDTH, TABLE_WIDTH / getColumnCount() );
			if( width >= 0 )
			{
				getColumnModel().getColumn(i).setPreferredWidth(width);
				getColumnModel().getColumn(i).setMinWidth(width);
				getColumnModel().getColumn(i).setMaxWidth(width);
			}
		}
	}
	
}
