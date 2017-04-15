package bn.blaszczyk.rose.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum ImplInterface {
	NONE(""),
	IDENTIFYABLE("Identifyable"),
	READABLE("Readable","Identifyable"),
	WRITABLE("Writable","Readable","Identifyable");
	
	private final String identifyer;
	private final Set<String> extend;
	
	private ImplInterface(String identifyer, String... extend)
	{
		this.identifyer = identifyer;
		this.extend = new HashSet<>();
		Collections.addAll(this.extend, extend);
	}

	public String getIdentifyer()
	{
		return identifyer;
	}
	
	public boolean doesExtend(ImplInterface that)
	{
		return this.equals(that) || extend.contains(that.getIdentifyer());
	}
	
}
