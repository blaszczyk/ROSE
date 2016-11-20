// ROSE : Relational-Object-tranSlator-rosE
package bn.blaszczyk.rose;

import bn.blaszczyk.rose.parser.RoseParser;

public class Rose {
	public static void main(String[] args) {
		for (String roseFile : args) {
			new	RoseParser().parse(roseFile);
		}
	}
}
