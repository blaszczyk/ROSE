package bn.blaszczyk.roseapp.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bn.blaszczyk.roseapp.controller.GUIController;
import bn.blaszczyk.roseapp.model.Writable;
import bn.blaszczyk.roseapp.view.panels.FullViewPanel;
import bn.blaszczyk.roseapp.view.panels.MyPanel;

public class Actions implements ChangeListener{
	
	private MainFrame mainFrame;
	
	private final Action actnStart;
	private final Action actnClose;
	private final Action actnCloseAll;
	private final Action actnEdit;
	private final Action actnSave;
	private final Action actnSaveAll;
	private final Action actnDelete;
	private final Action actnNew;
	private final Action actnCopy;
	
	public Actions( MainFrame mainFrame, GUIController guiController)
	{	
		this.mainFrame = mainFrame;
		actnStart = createAction( e -> guiController.openStartTab() );
		actnClose = createAction( e -> guiController.closeCurrent() );
		actnCloseAll = createAction( e -> guiController.closeAll() );
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

	public Action getActnCloseAll()
	{
		return actnCloseAll;
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
	
	public Action getActnStart()
	{
		return actnStart;
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		JTabbedPane tabbedPane = mainFrame.getTabbedPane();
		
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
			else if( o instanceof Writable )
			{
				actnNew.setEnabled(true);
				actnCopy.setEnabled(true);
				actnDelete.setEnabled(true);
				actnEdit.setEnabled(panel instanceof FullViewPanel );
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
