package elements.VariableTypes;

import elements.Variable;
import elements.Variable.Flag;

public class IntVariable extends Variable {
	
	protected int Current;
	protected int Default;
	protected int Latched;
	protected int Min;
	protected int Max;
	
	public IntVariable(String Name, String Desc, int DefaultValue, int DefaultMin, int DefaultMax, Flag DefaultFlag)
	{
		super(Name, Desc, DefaultFlag); // make sure the name is set
		Current = DefaultValue;
		Default = DefaultValue;
		Latched = DefaultValue;
		Min = DefaultMin;
		Max = DefaultMax;
	}
	
	public int Current()
	{
		return Current;
	}
	
	public void Current(int NewValue)
	{
		if (CurrentFlag == Flag.Modifiable)
			Current = NewValue;
		else if (CurrentFlag == Flag.CheatProtected && TheGame.CheatsEnabled)
			Current = NewValue;
		else if (CurrentFlag == Flag.Latched)
			Latched = NewValue;
		// log attempt to change protected variable
	}
	
	public int Default()
	{
		return Default;
	}
	
	public int Latched()
	{
		return Latched;
	}
	
	public int Min()
	{
		return Min;
	}
	
	public int Max()
	{
		return Max;
	}
}
