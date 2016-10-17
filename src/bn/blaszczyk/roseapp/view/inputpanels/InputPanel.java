package bn.blaszczyk.roseapp.view.inputpanels;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

public interface InputPanel<T> {
	
	public T getValue();
	public void setValue( T value );
	public String getName();
	public JPanel getPanel();
	public void addActionListener(ActionListener l);
	public void removeActionListener(ActionListener l);
	
}
