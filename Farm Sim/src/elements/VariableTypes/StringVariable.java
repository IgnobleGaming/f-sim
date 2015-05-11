package elements.VariableTypes;

import elements.Variable;
import elements.Variable.Flag;

public class StringVariable extends Variable {
	
	protected String Current;
	protected String Default;
	protected String Latched;
	
	public StringVariable(String Name, String Desc, String DefaultValue, Flag DefaultFlag)
	{
		super(Name, Desc, DefaultFlag); // make sure the name is set
		Current = DefaultValue;
		Default = DefaultValue;
		Latched = DefaultValue;
	}
	
	public String Current()
	{
		return Current;
	}
	
	public void Current(String NewValue)
	{
		if (CurrentFlag == Flag.Modifiable)
			Current = NewValue;
		else if (CurrentFlag == Flag.CheatProtected && TheGame.CheatsEnabled)
			Current = NewValue;
		else if (CurrentFlag == Flag.Latched)
			Latched = NewValue;
		// log attempt to change protected variable
	}
	
	public String Default()
	{
		return Default;
	}
	
	public String Latched()
	{
		return Latched;
	}
}