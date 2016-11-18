package bn.blaszczyk.roseapp.view.panels;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bn.blaszczyk.roseapp.controller.FullModelController;
import bn.blaszczyk.roseapp.controller.GUIController;
import bn.blaszczyk.roseapp.model.*;
import bn.blaszczyk.roseapp.view.ThemeConstants;
import bn.blaszczyk.roseapp.view.tools.EntityTable;
import bn.blaszczyk.roseapp.view.tools.EntityTableModel;

@SuppressWarnings("serial")
public class FullListPanel extends JPanel implements ThemeConstants, MyPanel {
	
	private Class<?> type;
	
	public FullListPanel(FullModelController modelController, GUIController guiController, Class<?> type)
	{
		this.type = type;
		setLayout(null);
		List<EntityModel> entityModels = modelController.getAllModels(type);
		entityModels.sort((e1,e2) -> Integer.compare(e1.getId(), e2.getId()));
		EntityTableModel tableModel = new EntityTableModel(entityModels,3);
		EntityTable table = new EntityTable(tableModel, FULL_TABLE_WIDTH, PANEL_HEIGHT);
		table.setButtonColumn(0, "view.png", e -> guiController.openEntityTab( e, false ));
		table.setButtonColumn(1, "edit.png", e -> guiController.openEntityTab( e, true ));
		table.setButtonColumn(2, "copy.png", e -> guiController.openEntityTab( modelController.createCopy( e ), true));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(H_SPACING, V_SPACING, FULL_TABLE_WIDTH, PANEL_HEIGHT);
		add(scrollPane);
	}

	@Override
	public Object getShownObject()
	{
		return type;
	}

	@Override
	public JPanel getPanel()
	{
		return this;
	}

	@Override
	public boolean hasChanged()
	{
		return false;
	}
}
