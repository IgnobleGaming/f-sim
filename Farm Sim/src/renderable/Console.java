package renderable;

import interfaces.Game;
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
	private InputField In;
	
	private Console()
	{
		Lines = new LinkedList<GUIFont>();
		In = new InputField(800);
		In.Position(GetPosFromLocation(Renderable.Position.CENTERCENTER));
		Texture = new interfaces.file.types.MaterialFile("resources\\console.png", Type.PNG);
		Texture.Open();
		AddSprites(Texture);
		Resize(1000, Texture.Height());
		Position(GetPosFromLocation(Renderable.Position.CENTERCENTER));
		for (int i = 0; i < 38; i++)
		{
			GUIFont Adding = new GUIFont("Consolas", "", GUIFont.Size.SMALL, Color.white, this.XPos - (Texture.Width() /2) + 57, i * 15);	
			Lines.add(Adding);
		}
		Visible = false;
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
		
		In.Draw();
		
		for (GUIFont G : Lines)
			G.Draw();
	}
	
	public void ToggleVisibility()
	{
		if (Visible)
		{
			Visible = false;
			Logging.getInstance().Write(Logging.Type.INFO, "Console is now hidden");
			Game.GetInstance().State(Game.State.INGAME);
		}
		else
		{
			Visible = true;
			Logging.getInstance().Write(Logging.Type.INFO, "Console is now visible");
			Game.GetInstance().State(Game.State.MENU);
		}
	}
}
