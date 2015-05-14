package renderable;

public class HUD extends Renderable
{
	private Console Con;
	private static HUD Instance;
	
	public static HUD GetInstance()
	{
		if (Instance == null)
			Instance = new HUD();
		return Instance;
	}
	
	public void Draw()
	{
		Console.GetInstance().Draw();
	}
}
