package specifier;

import object.Variable;

public class Stat
{
	public enum Type
	{
		HEALTH, FATIGUE, HUNGER
	}
	
	private Variable Value;
	private Type Type;
	private int DecModifier;
	private int IncModifier;
	private String Name;
	private String Desc;
	
	/**
	 * Stat Constructor
	 * @param L - Current Level
	 * @param M - Max Level for Stat
	 * @param T - Type of Stat
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Stat(int Min, int L, int Max, Type T)
	{
		Type = T;
		
		Name = Stat.GetName(T);
		Desc = Stat.GetDesc(T);
		
		Value = new Variable("g_" + Name, Desc, L, Min, L, Variable.Flag.ReadOnly);
	}
	
	/**
	 * Get Current Level
	 * @return - Current Level
	 */
	public int Current()
	{
		return (int)Value.Current();
	}
	
	/**
	 * Max value of stat
	 * @return
	 */
	public int Cap()
	{
		return (int)Value.Max();
	}
	
	/**
	 * Increase Current Level
	 * @return Current stat level
	 */
	public int Increment()
	{
		if ((int)Value.Current() + IncModifier <= (int)Value.Max())	
			Value.Current((int)Value.Current() + IncModifier);
		
		return (int)Value.Current();
	}
	
	/**
	 * Decrease Current Level
	 * @return Current stat level
	 */
	public int Decrement()
	{
		if ((int)Value.Current() - DecModifier >= (int)Value.Min())	
			Value.Current((int)Value.Current() - DecModifier);
		
		return (int)Value.Current();
	}
	
	public void SetIncrementalModifier(int I)
	{
		IncModifier = I;
	}
	
	public void SetDecrementalModifier(int I)
	{
		DecModifier = I;
	}
	
	/**
	 * Get Name of a Type
	 * 
	 * @return - String name of Stat
	 */
	public static String GetName(Type T)
	{
		String Name = "";
		
		switch(T)
		{
			case HEALTH:
				Name = "Health";
			case FATIGUE:
				Name = "Fatigue";
			case HUNGER:
				Name = "Hunger";
		}
		
		return Name;
	}

	public static String GetDesc(Type T)
	{
		String Desc = "";
		
		switch(T)
		{
			case HEALTH:
				Desc = "Health of Player";
			case FATIGUE:
				Desc = "Fatigue of Player";
			case HUNGER:
				Desc = "Hunger of Player";
		}
		
		return Desc;
	}
}
