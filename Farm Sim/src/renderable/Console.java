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
		super(700,700);
		Lines = new LinkedList<GUIFont>();
		Texture = new interfaces.file.types.MaterialFile("resources\\console.png", Type.PNG);
		Texture.Open();
		AddSprites(Texture);
		Position(GetPosFromLocation(Renderable.Position.TOPCENTER, Renderable.PositionType.ABSOLUTE, this.Width(), this.Height(), 0, 0, null));
		for (int i = 0; i < 1; i++)
		{
			GUIFont Adding = new GUIFont("Consolas", "", GUIFont.Size.SMALL, Color.white, this.XPos - (Texture.Width() /2) + 57, i * 15);	
			Lines.add(Adding);
		}
		Visible = false;
		In = new InputField(600, 20);
		In.Position(GetPosFromLocation(Renderable.Position.BOTTOMLEFT, Renderable.PositionType.RELATIVE, In.Width(), In.Height(), 50, -66, this));
		
		specifier.Vector2D Pos = GetPosFromLocation(Renderable.Position.BOTTOMLEFT, Renderable.PositionType.RELATIVE, In.InputText.Width(), In.InputText.Height(), 0, -27, In);
		In.InputText.Position(Pos);
		In.InputText.ZIndex(123123123);
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
			Logging.getInstance().Write(Logging.Type.DEBUG, "Console Center is ( %d %d )", XPos, YPos);
			Logging.getInstance().Write(Logging.Type.DEBUG, "Console input is ( %d %d )", In.XPos, In.YPos);
		}
	}
	
	public InputField Input()
	{
		return In;
	}
}
