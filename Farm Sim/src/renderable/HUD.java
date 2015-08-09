package renderable;

import interfaces.Game;
import interfaces.Render;

import org.newdawn.slick.Color;

public class HUD extends Renderable
{
	private GUIFont FPSCounter;
	private MiniMap MiniMap;
	private static HUD Instance;
	
	public HUD(int width, int height)
	{
		super(width, height);
	}
	
	public void Init()
	{
		FPSCounter = new GUIFont(GUIFont.FontFamily.Consolas, "", GUIFont.Size.MEDIUM, Color.white, 0,0);
		MiniMap = renderable.MiniMap.GetInstance();
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
		
		if (MiniMap.Visible)
			MiniMap.Draw();
		
		if (FPSCounter != null)
		{
			FPSCounter.Text(Game.GetInstance().Output().FPS() + " FPS");
			specifier.Vector2D Pos = GetPosFromLocation(Position.TOPRIGHT, PositionType.ABSOLUTE, FPSCounter.Width(), FPSCounter.Height(), -100, 10, null);
			FPSCounter.Position(Pos);
			FPSCounter.Draw();
		}
	}
}
