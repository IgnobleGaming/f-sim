package elements.VariableTypes;

import elements.Variable;
import elements.Variable.Flag;

public class FloatVariable extends Variable {
	
	protected float Current;
	protected float Default;
	protected float Latched;
	protected float Min;
	protected float Max;
	
	public FloatVariable(String Name, String Desc, float DefaultValue, float DefaultMin, float DefaultMax, Flag DefaultFlag)
	{
		super(Name, Desc, DefaultFlag); // make sure the name is set
		Current = DefaultValue;
		Default = DefaultValue;
		Latched = DefaultValue;
		Min = DefaultMin;
		Max = DefaultMax;
	}
	
	public float Current()
	{
		return Current;
	}
	
	public void Current(float NewValue)
	{
		if (CurrentFlag == Flag.Modifiable)
			Current = NewValue;
		else if (CurrentFlag == Flag.CheatProtected && TheGame.CheatsEnabled)
			Current = NewValue;
		else if (CurrentFlag == Flag.Latched)
			Latched = NewValue;
		// log attempt to change protected variable
	}
	
	public float Default()
	{
		return Default;
	}
	
	public float Latched()
	{
		return Latched;
	}
	
	public float Min()
	{
		return Min;
	}
	
	public float Max()
	{
		return Max;
	}
}