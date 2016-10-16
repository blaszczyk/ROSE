package bn.blaszczyk.roseapp.themes.defaulttheme;

import java.util.List;

import javax.swing.Icon;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import bn.blaszczyk.roseapp.model.EntityModel;

public class MemberTableModel implements TableModel {
	
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

	
}
