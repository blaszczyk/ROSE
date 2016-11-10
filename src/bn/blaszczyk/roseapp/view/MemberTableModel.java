package bn.blaszczyk.roseapp.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import bn.blaszczyk.roseapp.model.EntityModel;
import bn.blaszczyk.roseapp.view.inputpanels.FileInputPanel;

public class MemberTableModel implements TableModel, ThemeConstants {
	
	private enum ColType {
		ICON,
		MEMBER,
		ENTITY;
	}
	
	private class ColContent {
		private ColType colType;
		private int index;
		private Icon icon;
		
		public ColContent(ColType colType, int index)
		{
			this.colType = colType;
			this.index = index;
		}
		
		public ColContent()
		{
			this.colType = ColType.ICON;
		}
		public ColType getColType()
		{
			return colType;
		}
		public int getIndex()
		{
			return index;
		}
		public Icon getIcon()
		{
			return icon;
		}
		public void setIcon(Icon icon)
		{
			this.icon = icon;
		}
	}
	
	private final List<EntityModel> entityModels;
	private boolean empty;
	private EntityModel first;
	private final int buttonCount;
	private final List<ColContent> colContents = new ArrayList<>();
	
	
	public MemberTableModel(List<EntityModel> entityModels, int buttonCount)
	{
		this.entityModels = entityModels;
		this.empty = entityModels.isEmpty();
		for( int i = 0; i < buttonCount; i++)
			colContents.add(new ColContent() );
		if(!empty)
		{
			first = entityModels.get(0);
			for( String col : first.getTableCols().replaceAll(" ", "").split(";") )
				if(col.substring(0, 1).equalsIgnoreCase("m") )
					colContents.add(new ColContent(ColType.MEMBER, Integer.parseInt(col.substring(1))));
				else 
					if(col.substring(0, 1).equalsIgnoreCase("e") )
						colContents.add(new ColContent(ColType.ENTITY, Integer.parseInt(col.substring(1))));
		}
		this.buttonCount = buttonCount > 0 ? buttonCount : 0;
	}

	public EntityModel getEntityModel(int row)
	{
		return entityModels.get(row);
	}
	
	public void setButtonIcon(int columnIndex, Icon icon)
	{
		colContents.get(columnIndex).setIcon(icon);
	}
	
	@Override
	public int getRowCount()
	{
		return entityModels.size();
	}
	
	@Override
	public int getColumnCount()
	{
		return colContents.size();
	}
	
	@Override
	public String getColumnName(int columnIndex)
	{
		switch (colContents.get(columnIndex).getColType())	
		{
		case MEMBER:
			return first.getMemberName(colContents.get(columnIndex).getIndex());
		case ENTITY:
			return first.getEntityName(colContents.get(columnIndex).getIndex());
		default:
			return "";
		}		
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		switch (colContents.get(columnIndex).getColType())
		{
		case ICON:
			return Icon.class;
		case MEMBER:
			return first.getMemberValue(colContents.get(columnIndex).getIndex()).getClass();
		case ENTITY:
			return first.getEntityMember(colContents.get(columnIndex).getIndex()).getClass();
		default:
			return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return colContents.get(columnIndex).getColType().equals(ColType.ICON);
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		switch (colContents.get(columnIndex).getColType())
		{
		case ICON:
			return colContents.get(columnIndex).getIcon();
		case MEMBER:
			Object o =  entityModels.get(rowIndex).getMemberValue(colContents.get(columnIndex).getIndex());
			if( o instanceof String && FileInputPanel.isFileName(o.toString()))
				return o.toString().substring( o.toString().lastIndexOf("/")+1);
			return o;
		case ENTITY:
			return entityModels.get(rowIndex).getEntityMember(colContents.get(columnIndex).getIndex());
		default:
			return null;
		}
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
			return 7 * first.getLength1(colContents.get(columnIndex).getIndex());
		else if( getColumnClass(columnIndex) == BigDecimal.class )
			return 15 * first.getLength1(colContents.get(columnIndex).getIndex());
		else if( getColumnClass(columnIndex) == Icon.class )
			return BUTTON_WIDTH;
		else 
			return CELL_WIDTH;
	}

	
}
