package object;

import interfaces.Variables;
import interfaces.file.Logging;

public class Variable<T>
{

	public enum Flag
	{
		ReadOnly, CheatProtected, Latched, External, Configuration, Developer, Modifiable
	}

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
		CurrentFlag = DefaultFlag;
	}

	public Variable(String Name, String Desc, T DefaultValue, Flag DefaultFlag) // for string types
	{
		this.Name = Name;
		Description = Desc;
		Current = DefaultValue;
		Default = DefaultValue;
		Latched = DefaultValue;
		CurrentFlag = DefaultFlag;
	}

	public T Current()
	{
		return Current;
	}

	public void Current(T NewValue)
	{

		if (Min != null && Max != null) // only the case when working with number types
		{
			float val = (float) NewValue; // shitty work around < \/
			if (val > (float) Max || val < (float) Min)
				return;
		}
		if (CurrentFlag == Flag.Modifiable)
			Current = NewValue;
		else if (CurrentFlag == Flag.CheatProtected && (boolean)Variables.GetInstance().Get("g_cheats").Current)
			Current = NewValue;
		else if (CurrentFlag == Flag.Latched)
			Latched = NewValue;
		else if (CurrentFlag == Flag.Developer && (boolean)Variables.GetInstance().Get("g_developer").Current)
			Current = NewValue;
		else
			Logging.getInstance().Write(Logging.Type.WARNING, "Attempt to modify protected variable \"%\" failed", this.Name);
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
	
	public String Name()
	{
		return Name;
	}
	
	public String Description()
	{
		return Description;
	}
}
