package bn.blaszczyk.roseapp.view;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.swing.Icon;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.mysql.fabric.xmlrpc.base.Data;

import bn.blaszczyk.roseapp.model.EntityModel;

public class MemberTableModel implements TableModel, ThemeConstants {
	
	private List<EntityModel> entityModels;
	private boolean empty;
	private EntityModel first;
	private int buttonCount = 0;
	private Icon[] buttonIcons;
	
	public MemberTableModel(List<EntityModel> entityModels, int buttonCount)
	{
		this.entityModels = entityModels;
		this.empty = entityModels.isEmpty();
		if(!empty)
			first = entityModels.get(0);
		this.buttonCount = buttonCount > 0 ? buttonCount : 0;
		buttonIcons = new Icon[buttonCount];
	}

	public EntityModel getEntityModel(int row)
	{
		return entityModels.get(row);
	}
	
	public void setButtonIcon(int columnIndex, Icon icon)
	{
		buttonIcons[columnIndex] = icon;
	}
	
	@Override
	public int getRowCount()
	{
		return entityModels.size();
	}
	
	@Override
	public int getColumnCount()
	{
		return empty ? 0 : ( first.getMemberCount() + buttonCount );
	}
	
	@Override
	public String getColumnName(int columnIndex)
	{
		if(columnIndex < buttonCount)
			return "";
		return first.getMemberName(columnIndex - buttonCount);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if(columnIndex < buttonCount)
			return Icon.class;
		return first.getMemberValue(columnIndex - buttonCount).getClass();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return columnIndex < buttonCount;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if(columnIndex < buttonCount)
			return buttonIcons[columnIndex];
		return entityModels.get(rowIndex).getMemberValue(columnIndex - buttonCount);
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
		if( getColumnClass(columnIndex) == String.class )
			return 7 * first.getLength1(columnIndex-buttonCount);
		else if( getColumnClass(columnIndex) == BigDecimal.class )
			return 15 * first.getLength1(columnIndex-buttonCount);
		else if( getColumnClass(columnIndex) == Icon.class )
			return BUTTON_WIDTH;
		else 
			return CELL_WIDTH;
	}

	
}
