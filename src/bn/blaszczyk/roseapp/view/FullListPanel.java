package bn.blaszczyk.roseapp.view;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bn.blaszczyk.roseapp.controller.FullModelController;
import bn.blaszczyk.roseapp.controller.GUIController;
import bn.blaszczyk.roseapp.model.*;

@SuppressWarnings("serial")
public class FullListPanel extends JPanel implements ThemeConstants {
	
	public FullListPanel(FullModelController modelController, GUIController guiController, Class<?> type)
	{
		setLayout(null);
		List<EntityModel> entityModels = modelController.getAllModels(type);
		entityModels.sort((e1,e2) -> Integer.compare(e1.getId(), e2.getId()));
		MemberTableModel tableModel = new MemberTableModel(entityModels,3);
		MemberTable table = new MemberTable(tableModel, guiController);
		table.setHeight(FULL_TABLE_HEIGHT);
		table.setButtonColumn(0, "view.png", e -> guiController.createFullPanelDialog( e ));
		table.setButtonColumn(1, "edit.png", e -> guiController.createEditPanelDialog( e ));
		table.setButtonColumn(2, "copy.png", e -> guiController.createEditPanelDialog( modelController.createCopy( e )));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(H_SPACING, V_SPACING, table.getWidth(), FULL_TABLE_HEIGHT);
		add(scrollPane);
		
		JButton btnNew = new JButton("New " + type.getSimpleName() );
		btnNew.setBounds(H_SPACING, 2 * V_SPACING + FULL_TABLE_HEIGHT, 150, LBL_HEIGHT );
		btnNew.addActionListener(e -> guiController.createEditPanelDialog( modelController.createModel( modelController.createNew( type.getSimpleName() ) ) ));
		add(btnNew);
		
		
	}
}
