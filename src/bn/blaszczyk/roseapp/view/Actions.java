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
	private Action actnSaveAll;
	private Action actnDelete;
	private Action actnNew;
	private Action actnCopy;
	
	public Actions(FullModelController modelController, GUIController guiController)
	{
//		this.modelController = modelController;
//		this.guiController = guiController;
		
		actnClose = createAction( e -> guiController.closeCurrent() );
		actnEdit = createAction( e -> guiController.editCurrent() );
		actnSave = createAction( e -> guiController.saveCurrent() );
		actnSaveAll = createAction( e -> guiController.saveAll() );
		actnCopy = createAction( e -> guiController.copyCurrent() );
		actnDelete = createAction( e -> guiController.deleteCurrent() );
		actnNew = createAction( e -> guiController.openNew(  ) );
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
	
	public Action getActnSaveAll()
	{
		return actnSaveAll;
	}

	public Action getActnDelete()
	{
		return actnDelete;
	}

	public Action getActnCopy()
	{
		return actnCopy;
	}

	public Action getActnNew()
	{
		return actnNew;
	}
	
	@Override
	public void stateChanged(ChangeEvent e)
	{
		if( e.getSource() instanceof JTabbedPane )
		{
			JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
			
			actnSaveAll.setEnabled(false);
			for(Component c : tabbedPane.getComponents())
				if( c instanceof MyPanel)
				{
					MyPanel panel = (MyPanel) c;
					if(panel.hasChanged())
						actnSaveAll.setEnabled(true);
				}
			
			Component c = tabbedPane.getSelectedComponent();
			actnClose.setEnabled( c instanceof MyPanel );
			if( c instanceof MyPanel)
			{
				MyPanel panel = (MyPanel) c;
				Object o = panel.getShownObject();
				if( o instanceof Class<?>)
				{
					actnNew.setEnabled(true);
					actnSave.setEnabled(false);
					actnCopy.setEnabled(false);
					actnDelete.setEnabled(false);
					actnEdit.setEnabled(false);
				}
				else if( o instanceof EntityModel )
				{
					actnNew.setEnabled(true);
					actnCopy.setEnabled(true);
					actnDelete.setEnabled(true);
					actnEdit.setEnabled(panel instanceof FullPanel );
					actnSave.setEnabled(panel.hasChanged());
				}
			}
			else
			{
				actnNew.setEnabled(false);
				actnSave.setEnabled(false);
				actnCopy.setEnabled(false);
				actnDelete.setEnabled(false);
				actnEdit.setEnabled(false);
			}
		}
	}
	
}
