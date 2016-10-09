package bn.blaszczyk.rose.themes.defaulttheme;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import bn.blaszczyk.rose.interfaces.EntityModel;

public class MemberTableModel implements TableModel {
	
	private List<EntityModel> entityModels;
	private boolean empty;
	private EntityModel first;
	
	public MemberTableModel(List<EntityModel> entityModels)
	{
		this.entityModels = entityModels;
		this.empty = entityModels.isEmpty();
		if(!empty)
			first = entityModels.get(0);
	}

	@Override
	public int getRowCount()
	{
		return entityModels.size();
	}
	
	@Override
	public int getColumnCount()
	{
		return empty ? 0 : first.getMemberCount();
	}
	
	@Override
	public String getColumnName(int columnIndex)
	{
		return first.getMemberName(columnIndex);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return first.getMemberValue(columnIndex).getClass();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return entityModels.get(rowIndex).getMemberValue(columnIndex);
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
	
}
