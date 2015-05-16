package renderable;

import interfaces.Objects;
import interfaces.Render;
import interfaces.Variables;
import interfaces.file.Logging;
import interfaces.file.types.MaterialFile.Type;

import java.util.LinkedList;

import org.newdawn.slick.Color;

public class Console extends HUD
{
	private LinkedList<GUIFont> Lines;
	private static Console Instance;
	private interfaces.file.types.MaterialFile Texture;
	
	private Console()
	{
		Lines = new LinkedList<GUIFont>();
		Texture = new interfaces.file.types.MaterialFile("resources/console.png", Type.PNG);
		Texture.Open();
		int width = (int)Variables.GetInstance().Get("vid_width").Current();
		int height = (int)Variables.GetInstance().Get("vid_height").Current();
		Texture.Scale((float).45);
		Position(width/2, 0);
		for (int i = 0; i < 20; i++)
		{
			GUIFont Adding = new GUIFont("Consolas", "", GUIFont.Size.SMALL, Color.white, 5, i * 15);	
			Lines.add(Adding);
		}
	}
	
	public static Console GetInstance()
	{
		if (Instance == null)
			Instance = new Console();
		
		return Instance;
	}
	
	public void WriteLine(String Message)
	{
		String LastLine = Lines.getLast().Text();
		for (int i = Lines.size()-1; i > -1; i--)
		{
			String NewLastLine = Lines.get(i).Text();
			Lines.get(i).Text(LastLine);
			LastLine = NewLastLine;
		}
		Lines.getLast().Text(Message);
	
	}
	
	public void Draw()
	{
		Render.DrawImage(Texture, Position());
		Logging.getInstance().Write(Logging.Type.INFO, "Console created @ %d %d", Position().x, Position().y);
		
		for (GUIFont G : Lines)
			G.Draw();
	}
	
	public void ToggleVisibility()
	{
		if (Visible)
		{
			Visible = false;
			Logging.getInstance().Write(Logging.Type.INFO, "Console is now hidden");
		}
		else
		{
			Visible = true;
			Logging.getInstance().Write(Logging.Type.INFO, "Console is now visible");
		}
	}
}
