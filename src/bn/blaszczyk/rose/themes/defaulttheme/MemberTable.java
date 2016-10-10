package bn.blaszczyk.rose.themes.defaulttheme;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.*;
import java.util.Date;
import java.util.Locale;

import javax.swing.*;
import javax.swing.table.*;

import bn.blaszczyk.rose.interfaces.MyPanel;

@SuppressWarnings("serial")
public class MemberTable extends JTable implements MyPanel {

	private static final int CELL_WIDTH = 100;

	private static final DateFormat  DATE_FORMAT = new SimpleDateFormat("dd.MM.YY");
	private static final NumberFormat INT_FORMAT = NumberFormat.getIntegerInstance();
	private static final NumberFormat DOUBLE_FORMAT = new DecimalFormat("0.000",DecimalFormatSymbols.getInstance(Locale.GERMAN));
	
	private static final Color ODD_BG = Color.DARK_GRAY;
	private static final Color ODD_FG = Color.RED;
	private static final Font ODD_FONT = new Font("Arial",Font.PLAIN,16);
	
	private static final Color EVEN_BG = Color.BLACK;
	private static final Color EVEN_FG = Color.GREEN;
	private static final Font EVEN_FONT = new Font("Arial",Font.PLAIN,16);

	private static final Font HEADER_FONT = new Font("Arial",Font.BOLD,16);
	private static final Color HEADER_BG = Color.LIGHT_GRAY;
	
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
		return 5 * CELL_WIDTH + 16;
	}

	@Override
	public int getHeight()
	{
		return 150;
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
			int width = CELL_WIDTH;
			if( width >= 0 )
			{
				getColumnModel().getColumn(i).setPreferredWidth(width);
				getColumnModel().getColumn(i).setMinWidth(width);
				getColumnModel().getColumn(i).setMaxWidth(width);
			}
		}
	}
	
}
