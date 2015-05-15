package renderable;

import interfaces.Objects;

import java.util.concurrent.ArrayBlockingQueue;

import org.newdawn.slick.Color;

public class Console extends HUD
{
	private ArrayBlockingQueue<GUIFont> Lines;
	private static Console Instance;
	
	private Console()
	{
		Lines = new ArrayBlockingQueue<GUIFont>(8);
	}
	
	public static Console GetInstance()
	{
		if (Instance == null)
			Instance = new Console();
		
		return Instance;
	}
	
	public void WriteLine(String Message)
	{
		if (Lines.size() >=8);
		{
			//Lines.remove();
			for (GUIFont G : Lines)
				G.MoveUp();
		}
		GUIFont Adding = new GUIFont("Segoe UI", Message, GUIFont.Size.SMALL, Color.black, 5, Lines.size() * 15);	
		Lines.add(Adding);
	}
	
	public void Draw()
	{
		for (GUIFont G : Lines)
			G.Draw();
	}
}
