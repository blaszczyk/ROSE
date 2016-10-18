package bn.blaszczyk.roseapp.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.*;

import bn.blaszczyk.roseapp.controller.GUIController;
import bn.blaszczyk.roseapp.model.*;

@SuppressWarnings("serial")
public class MemberTable extends JTable implements ThemeConstants {

	
	public interface EntityAction
	{
		public void performAction(EntityModel entityModel);
	}

	
	/*
	 * Custom Cell Renderer
	 */
	private final TableCellRenderer cellRenderer = new TableCellRenderer(){
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			String text = "";
			if(value instanceof Icon)
			{
				JButton button = new JButton((Icon)value);
				button.setBorderPainted(false);
				button.addActionListener(e -> buttonActions[column].performAction(tableModel.getEntityModel(row)  ));
				button.setBackground( row % 2 == 0 ? EVEN_BG : ODD_BG);
				return button;
			}
			else if(value instanceof Date)
				text = DATE_FORMAT.format(value);
			else if(value instanceof Double)
				text = DOUBLE_FORMAT.format(value);
			else if(value instanceof Integer)
				text = INT_FORMAT.format(value);
			else if(value instanceof BigDecimal)
				text = DOUBLE_FORMAT.format(value);
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
//	private GUIController controller;
	private EntityAction[] buttonActions;
	private MemberTableModel tableModel;
	
	private int width = TABLE_WIDTH;
	private int height = TABLE_HEIGHT;
//	private final TableRowSorter<TableModel> sorter = new TableRowSorter<>();
	
	public MemberTable(MemberTableModel tableModel, GUIController controller)
	{
		super(tableModel);
		this.tableModel = tableModel;
//		this.controller = controller;
		buttonActions = new EntityAction[tableModel.getButtonCount()];

		

		setShowGrid(false);
		setIntercellSpacing(new Dimension(CELL_SPACING, CELL_SPACING));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().setFont(HEADER_FONT);
//		setRowSorter(sorter);
		
		addMouseListener( new MouseAdapter() 
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1 )
				{
					int row = rowAtPoint(e.getPoint());
					int col = columnAtPoint(e.getPoint());
					if(col < tableModel.getButtonCount() )
						buttonActions[col].performAction(tableModel.getEntityModel(row));						
				}
				else if(e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1 )
				{
					int row = rowAtPoint( e.getPoint() );
					controller.createFullPanelDialog(tableModel.getEntityModel(row));
				}
			}
			
		});
		
		setRowHeight(ODD_FONT.getSize() + 10);
		
		setCellRenderer();
		setDimns();
	}
	
	
	public void setButtonColumn( int columnIndex, String iconFile,  EntityAction action)
	{
		if(columnIndex < 0 || columnIndex >= buttonActions.length)
			return;
		buttonActions[columnIndex] = action;
		tableModel.setButtonIcon(columnIndex, new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("../resources/" + iconFile))));
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
	
	public void setHeight( int height )
	{
		this.height = height;
	}

	
	private void setCellRenderer()
	{
		getTableHeader().setDefaultRenderer(cellRenderer);
		for(int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++)
			getColumnModel().getColumn(columnIndex).setCellRenderer( cellRenderer );
	}
	
	private void setDimns()
	{
		this.width = 0;
		for(int i = 0 ; i < this.getColumnCount(); i++)
		{
//			int width = i < tableModel.getButtonCount() ? BUTTON_WIDTH : Math.max( CELL_WIDTH, TABLE_WIDTH / getColumnCount() );
			int width = tableModel.getColumnWidth(i);
			if( width >= 0 )
			{
				getColumnModel().getColumn(i).setPreferredWidth(width);
				getColumnModel().getColumn(i).setMinWidth(width);
				getColumnModel().getColumn(i).setMaxWidth(width);
				this.width += width + CELL_SPACING;
			}
		}
	}
	
}
