package bn.blaszczyk.roseapp.view.tools;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import bn.blaszczyk.roseapp.config.ViewConfig;
import bn.blaszczyk.roseapp.model.Readable;
import bn.blaszczyk.roseapp.view.ThemeConstants;

public class EntityTableModel implements TableModel, ThemeConstants {
	
	private final List<? extends Readable> entites;
	private boolean empty;
	private Readable first;
	private final int buttonCount;
	private final List<ColumnContent> colContents = new ArrayList<>();
	
	
	public EntityTableModel(List<? extends Readable> entities, int buttonCount)
	{
		this.entites = entities;
		this.empty = entities.isEmpty();
		for( int i = 0; i < buttonCount; i++)
			colContents.add(new ColumnContent());
		if(!empty)
		{
			first = entities.get(0);
			colContents.addAll(ViewConfig.getColumnContents(first.getClass()));
//			for( String col : first.getTableCols().replaceAll(" ", "").split(";") )
//				if(col.substring(0, 1).equalsIgnoreCase("m") )
//					colContents.add(new ColumnContent(ColType.MEMBER, Integer.parseInt(col.substring(1))));
//				else 
//					if(col.substring(0, 1).equalsIgnoreCase("e") )
//						colContents.add(new ColumnContent(ColType.ENTITY, Integer.parseInt(col.substring(1))));
		}
		this.buttonCount = buttonCount > 0 ? buttonCount : 0;
	}

	public Readable getEntity(int row)
	{
		return entites.get(row);
	}
	
	public void setButtonIcon(int columnIndex, Icon icon)
	{
		colContents.get(columnIndex).setIcon(icon);
	}
	
	@Override
	public int getRowCount()
	{
		return entites.size();
	}
	
	@Override
	public int getColumnCount()
	{
		return colContents.size();
	}
	
	@Override
	public String getColumnName(int columnIndex)
	{
		return colContents.get(columnIndex).getName(first);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return colContents.get(columnIndex).getClass(first);
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return colContents.get(columnIndex).getContent(getEntity(rowIndex));
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
	}
	
	@Override
	public void addTableModelListener(TableModelListener l)
	{
	}
	
	@Override
	public void removeTableModelListener(TableModelListener l)
	{
	}

	public int getButtonCount()
	{
		return buttonCount;
	}
	
	public int getColumnWidth( int columnIndex )
	{
		if( columnIndex < buttonCount)
			return BUTTON_WIDTH;
		if(empty)
			return 0;
		return ViewConfig.getColumnWidths(first.getClass())[columnIndex-buttonCount];
//		if( getColumnClass(columnIndex) == String.class )
//			return 7 * first.getLength1(colContents.get(columnIndex).getIndex());
//		else if( getColumnClass(columnIndex) == BigDecimal.class )
//			return 15 * first.getLength1(colContents.get(columnIndex).getIndex());
//		else if( getColumnClass(columnIndex) == Icon.class )
//			return BUTTON_WIDTH;
//		else 
//			return CELL_WIDTH;
	}

	
}
