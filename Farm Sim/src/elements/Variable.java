package elements;

public class Variable extends interfaces.Element {
	
	public enum Flag { ReadOnly, CheatProtected, Latched, External, Configuration, Modifiable }
	
	protected final String Name;
	protected final String Description;
	protected Flag CurrentFlag;
	
	public Variable(String N, String Description, Flag DefaultValue)
	{
		Name = N;
		CurrentFlag = DefaultValue;
		this.Description = Description;
	}

}


