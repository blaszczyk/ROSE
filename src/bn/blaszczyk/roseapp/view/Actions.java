package bn.blaszczyk.roseapp.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bn.blaszczyk.roseapp.controller.FullModelController;
import bn.blaszczyk.roseapp.controller.GUIController;
import bn.blaszczyk.roseapp.model.EntityModel;

public class Actions implements ChangeListener{
	
//	private FullModelController modelController;
//	private GUIController guiController;
	
	private Action actnClose;
	private Action actnEdit;
	private Action actnSave;
	
	public Actions(FullModelController modelController, GUIController guiController)
	{
//		this.modelController = modelController;
//		this.guiController = guiController;
		
		actnClose = createAction( e -> guiController.closeCurrent() );
		actnEdit = createAction( e -> guiController.editCurrent() );
		actnSave = createAction( e -> guiController.saveCurrent() );
	}

	private Action createAction(ActionListener l)
	{
		@SuppressWarnings("serial")
		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				l.actionPerformed(e);
			}
		};
		return action;
	}
	
	public Action getActnClose()
	{
		return actnClose;
	}

	public Action getActnEdit()
	{
		return actnEdit;
	}

	public Action getActnSave()
	{
		return actnSave;
	}
	


	@Override
	public void stateChanged(ChangeEvent e)
	{
		if( e.getSource() instanceof JTabbedPane )
		{
			JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
			Component c = tabbedPane.getSelectedComponent();
			actnClose.setEnabled( c instanceof MyPanel );
			if( c instanceof MyPanel)
			{
				MyPanel panel = (MyPanel) c;
				actnEdit.setEnabled(panel.getShownObject() instanceof EntityModel);
				actnSave.setEnabled(panel.hasChanged());
			}
			else
			{
				actnEdit.setEnabled(false);
				actnSave.setEnabled(false);
			}
		}
	}
	
}
