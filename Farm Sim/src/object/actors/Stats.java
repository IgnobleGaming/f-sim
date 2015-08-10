package object.actors;

public class Stats
{
	private Stat Health;
	private Stat Fatigue;
	private Stat Hunger;
	
	public Stats()
	{
		Health = new Stat(1, 1, 100, Stat.Type.HEALTH);
		Fatigue = new Stat(0, 20, 100, Stat.Type.FATIGUE);
		Hunger = new Stat(0, 100, 100, Stat.Type.HUNGER);
	}
	
	public Stat Health()
	{
		return Health;
	}
	
	public Stat Fatigue()
	{
		return Fatigue;
	}
	
	public Stat Hunger()
	{
		return Hunger;
	}
}
