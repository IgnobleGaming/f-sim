package renderable;

import interfaces.Game;
import interfaces.Render;

import org.newdawn.slick.Color;

public class HUD extends Renderable
{
	private GUIFont FPSCounter;
	private static HUD Instance;
	
	public HUD(int width, int height)
	{
		super(width, height);
	}
	
	public void Init()
	{
		FPSCounter = new GUIFont("Consolas", "", GUIFont.Size.MEDIUM, Color.white, 1200, 10);
	}
	
	public static HUD GetInstance()
	{
		if (Instance == null)
			Instance = new HUD(Render.GetInstance().Width(),Render.GetInstance().Height());
		return Instance;
	}
	
	public void Draw()
	{
		if (Console.GetInstance().Visible)
			Console.GetInstance().Draw();
		
		if (FPSCounter != null)
		{
			FPSCounter.Text(Game.GetInstance().Output().FPS() + " FPS");
			FPSCounter.Draw();
		}
	}
}
