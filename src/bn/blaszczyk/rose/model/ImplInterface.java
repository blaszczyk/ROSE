package bn.blaszczyk.rose.model;

import java.util.Arrays;

public enum ImplInterface {
	NONE(""),
	IDENTIFYABLE("Identifyable"),
	READABLE("Readable","Identifyable"),
	WRITABLE("Writable","Readable","Identifyable");
	
	private final String identifyer;
	private final String[] extend;
	
	private ImplInterface(String identifyer, String... extend)
	{
		this.identifyer = identifyer;
		this.extend = extend;
	}

	public String getIdentifyer()
	{
		return identifyer;
	}
	
	public boolean doesExtend(ImplInterface that)
	{
		return this.equals(that) || Arrays.asList(extend).contains(that.getIdentifyer());
	}
	
}
