package object.actors;

import object.Variable;

public class Stat
{
	public enum Type
	{
		HEALTH, FATIGUE, HUNGER
	}
	
	private Variable Value;
	private Type Type;
	private String Name;
	private String Desc;
	
	/**
	 * Stat Constructor
	 * @param L - Current Level
	 * @param M - Max Level for Stat
	 * @param T - Type of Stat
	 */
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
	 * Get Actual Level of Stat
	 * @return
	 */
	public int CurrentCap()
	{
		return (int)Value.Max();
	}
	
	/**
	 * Alter Current level
	 * @param M - Mod
	 */
	public void Modify(int M)
	{
		if ((int)Value.Current() + M >= (int)Value.Min() && (int)Value.Current() + M <= (int)Value.Max())
			Value.Current((int)Value.Current() + M);
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
