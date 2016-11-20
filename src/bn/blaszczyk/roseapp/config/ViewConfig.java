package bn.blaszczyk.roseapp.config;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bn.blaszczyk.roseapp.view.tools.ColumnContent;

public class ViewConfig {
	
	private static final String DELIMITER = "\\;";
	
	private final static Map<Class<?>, List<ColumnContent>> COLUMN_CONTENT_MAP = new HashMap<>();
	private final static Map<Class<?>, int[]> COLUMN_WIDTH_MAP = new HashMap<>();

	
	public static List<ColumnContent> getColumnContents(Class<?> type)
	{
		return COLUMN_CONTENT_MAP.get(type);
	}
	
	/*
	 * Format is "f1;f2;f3;e1;e2;e1.f1;e1,f2"
	 */
	public static void putColumnContentsAsString(Class<?> type, String wString) throws ParseException
	{
		String[] split = wString.split(DELIMITER);
		List<ColumnContent> columnContents = new ArrayList<>();
		for(String ccString : split)
			columnContents.add(new ColumnContent(ccString.trim()));
		COLUMN_CONTENT_MAP.put(type, columnContents);
	}

	public static int[] getColumnWidths(Class<?> type)
	{
		return COLUMN_WIDTH_MAP.get(type);
	}
	
	/*
	 * Format is "150;100;100;40"
	 */
	public static void putColumnWidthsAsString(Class<?> type, String ccsString) throws ParseException
	{
		String[] split = ccsString.split(DELIMITER);
		int[] widths = new int[split.length];
		for(int i = 0; i < split.length; i++)
			widths[i] = Integer.parseInt(split[i].trim());
		COLUMN_WIDTH_MAP.put(type, widths);
	}
}
