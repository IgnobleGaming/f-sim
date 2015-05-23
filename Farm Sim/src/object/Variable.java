package object;

import java.awt.TrayIcon.MessageType;

import renderable.Console;
import interfaces.Variables;
import interfaces.file.Logging;
import interfaces.file.Logging.Type;
import java.lang.NumberFormatException;

/**
 * Variable class used by ingame variables
 * @author Michael
 *
 * @param <T> ( templated object type either number based or string )
 */
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
	protected T Latched = null;
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

	/**
	 * Update variable's current value if modifiable
	 * @param NewValue ( variable's desired new value )
	 */
	public void Current(Object NewValue)
	{
		if (Current.getClass() == Integer.class)
		{
			try{ NewValue = Integer.parseInt(NewValue.toString()); }
			catch (NumberFormatException E) { return; }
		}
		
		else if (Current.getClass() == float.class)
		{
			try{ NewValue = Float.parseFloat(NewValue.toString()); }
			catch (NumberFormatException E) { return; }
		}
		
		else if (Current.getClass() == boolean.class)
		{
			try{ NewValue = Boolean.parseBoolean(NewValue.toString()); }
			catch (NumberFormatException E) { return; }
		}
		
		else if (Current.getClass() == String.class)
		{
			NewValue = (String)NewValue;
		}
		
		else
		{
			Logging.getInstance().Write(Type.WARNING, "\"%s\" is an invalid value for \"%s\"", NewValue.toString(), this.Name);
		}
		
		if (Current.getClass() != String.class) // only the case when working with number types
		{
			if (Current.getClass() == Integer.class)
			{
				if ((int)NewValue > (int)Max || (int)NewValue  < (int)Min)
				{
					Logging.getInstance().Write(Type.WARNING, "\"%s\" is an out of range value for \"%s\"", NewValue.toString(), this.Name);
					return;
				}
			}
			
			else if (Current.getClass() == float.class)
			{
				if ((float)NewValue > (float)Max || (float)NewValue  < (float)Min)
				{
					Logging.getInstance().Write(Type.WARNING, "\"%s\" is an out of range value for \"%s\"", NewValue.toString(), this.Name);
					return;
				}
			}			
		}
		if (CurrentFlag == Flag.Modifiable || CurrentFlag == Flag.Configuration)
			Current = (T)NewValue;
		else if (CurrentFlag == Flag.CheatProtected && (boolean)Variables.GetInstance().Get("g_cheats").Current)
			Current = (T)NewValue;
		else if (CurrentFlag == Flag.Latched)
			Latched = (T)NewValue;
		else if (CurrentFlag == Flag.Developer && (boolean)Variables.GetInstance().Get("g_developer").Current)
			Current = (T)NewValue;
		else
			Logging.getInstance().Write(Logging.Type.WARNING, "\"%s\" is not modifiable", this.Name);
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
	
	/**
	 * Get the information of a variable ( used by commands )
	 * @return formatted variable information
	 */
	public String StringInfo()
	{
		String Info;
		if (Min != null && Max != null)
			Info = String.format("%s [current=\"%s\"] [default=\"%s\"] | [min=\"%s\"] [max=\"%s\"]", this.Name, this.Current.toString(), this.Default.toString(), this.Min.toString(), this.Max.toString());
		else
			Info = String.format("%s [current=\"%s\"]  [default=\"%s\"]", this.Name, this.Current.toString(), this.Default.toString());
		return Info;
	}
}
