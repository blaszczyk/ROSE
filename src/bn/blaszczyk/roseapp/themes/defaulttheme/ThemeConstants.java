package bn.blaszczyk.roseapp.themes.defaulttheme;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public interface ThemeConstants {
	
	// Basic Panel
	
	public static final int LBL_HEIGHT = 30;
	public static final int H_SPACING = 10;
	public static final int V_SPACING = 10;
	
	public static final int PROPERTY_WIDTH = 200;
	public static final Font PROPERTY_FONT = new Font("Arial", Font.BOLD, 20);
	public static final Color PROPERTY_BG = Color.LIGHT_GRAY;
	public static final Color PROPERTY_FG = Color.BLACK;
	
	public static final int VALUE_WIDTH = 500;
	public static final Font VALUE_FONT = new Font("Arial", Font.BOLD, 20);
	public static final Color VALUE_BG = Color.LIGHT_GRAY;
	public static final Color VALUE_FG = Color.BLACK;
	
	public static final Color BASIC_PNL_BACKGROUND = Color.DARK_GRAY;
	
	
	// Full Panel
	
	public static final int TITLE_WIDTH = 200;
	public static final int TITLE_HEIGHT = 35;
	public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 30);
	public static final Color TITLE_BG = Color.LIGHT_GRAY;
	public static final Color TITLE_FG = Color.BLACK;
	
	public static final int SUBTITLE_WIDTH = 200;
	public static final int SUBTITLE_HEIGHT = 30;
	public static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 25);
	public static final Color SUBTITLE_BG = Color.LIGHT_GRAY;
	public static final Color SUBTITLE_FG = Color.BLACK;
	
	public static final Color FULL_PNL_BACKGROUND = Color.DARK_GRAY;
	
	public static final int V_OFFSET = 20;	
	
	// Table
	
	public static final int CELL_HEIGTH = 25;
	public static final int CELL_WIDTH = 100;
	public static final int TABLE_HEIGHT = 150;
	public static final int TABLE_WIDTH = 800;

	public static final DateFormat  DATE_FORMAT = new SimpleDateFormat("dd.MM.YY");
	public static final NumberFormat INT_FORMAT = NumberFormat.getIntegerInstance();
	public static final NumberFormat DOUBLE_FORMAT = new DecimalFormat("0.000",DecimalFormatSymbols.getInstance(Locale.GERMAN));
	
	public static final Color ODD_BG = Color.WHITE;
	public static final Color ODD_FG = Color.BLACK;
	public static final Font ODD_FONT = new Font("Arial",Font.PLAIN,16);
	
	public static final Color EVEN_BG = Color.LIGHT_GRAY;
	public static final Color EVEN_FG = Color.BLACK;
	public static final Font EVEN_FONT = new Font("Arial",Font.PLAIN,16);

	public static final Font HEADER_FONT = new Font("Arial",Font.BOLD,16);
	public static final Color HEADER_BG = new Color(238,238,238);
	
	// Main Frame
	
	public static final int MF_WIDTH = 1000;
	public static final int MF_HEIGTH = 800;
}
