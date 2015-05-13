package object;

public class Variable<T> extends object.Object {
	
	public enum Flag { ReadOnly, CheatProtected, Latched, External, Configuration, Modifiable }
	
	protected final String Name;
	protected final String Description;
	protected Flag CurrentFlag;
	
	protected T Current;
	protected T Default;
	protected T Latched;
	protected T Min;
	protected T Max;
	
	public Variable(String Name, String Desc, T DefaultValue, T DefaultMin, T DefaultMax, Flag DefaultFlag) // for number types
	{
		this.Name = Name;
		Description = Desc;
		Current = DefaultValue;
		Default = DefaultValue;
		Latched = DefaultValue;
		Min = DefaultMin;
		Max = DefaultMax;
	}
	
	public Variable(String Name, String Desc, T DefaultValue, Flag DefaultFlag) // for string types
	{
		this.Name = Name;
		Description = Desc;
		Current = DefaultValue;
		Default = DefaultValue;
		Latched = DefaultValue;
	}

	public T Current()
	{
		return Current;
	}
	
	public void Current(T NewValue)
	{
		
		if (Min != null && Max != null) // only the case when working with number types
		{
			float val = (float)NewValue; // shitty work around < \/
			
			if (val > (float)Max || val < (float)Min)
				return;
		}
		
		if (CurrentFlag == Flag.Modifiable)
			Current = NewValue;
		else if (CurrentFlag == Flag.CheatProtected && (int)TheGame.Variables.get("sv_cheats").Current != 1)
			Current = NewValue;
		else if (CurrentFlag == Flag.Latched)
			Latched = NewValue;
		// log attempt to change protected variable
	}
	
	public T Default()
	{
		return Default;
	}
	
	public T Latched()
	{
		return Latched;
	}
	
	public T Min()
	{
		return Min;
	}
	
	public T Max()
	{
		return Max;
	}
}
