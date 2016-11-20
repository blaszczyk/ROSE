// ROSE : Relational-Object-tranSlator-rosE
package bn.blaszczyk.rose;

import java.io.FileNotFoundException;
import java.text.ParseException;

public class Rose {
	public static void main(String[] args) {
		for (String roseFile : args) {
			try {
				RoseParser.parse(roseFile);
			} catch (FileNotFoundException | ParseException e) {
				e.printStackTrace();
			}
		}
	}
}
