package bn.blaszczyk.roseapp.controller;

import java.util.List;

public interface FullModelController extends BasicModelController {

	public void commit();
	public List<?> getAll(Class<?> type);
}
