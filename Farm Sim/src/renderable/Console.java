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
		super(700, 700);
		Lines = new LinkedList<GUIFont>();
		Texture = new interfaces.file.types.MaterialFile("resources\\console.png", Type.PNG);
		Texture.Open();
		AddSprites(Texture);
		Position(GetPosFromLocation(Renderable.Position.TOPCENTER, Renderable.PositionType.ABSOLUTE, this.Width(), this.Height(), 0, 0, null));
		for (int i = 0; i < 36; i++)
		{

			GUIFont Adding = new GUIFont(GUIFont.FontFamily.Consolas, "", GUIFont.Size.SMALL, Color.white, 0, 0);
			specifier.Vector2D FontPos = GetPosFromLocation(Renderable.Position.TOPLEFT, Renderable.PositionType.RELATIVE, Adding.Width(), Adding.Height(), 50, i * (Adding.Height() + 8), this);
			Adding.Position(FontPos);
			Lines.add(Adding);
		}

		Lines.get(30).Colour = Color.red;
		Lines.get(31).Colour = Color.yellow;
		Lines.get(29).Colour = Color.orange;
		Visible = false;
		In = new InputField(600, 20);
		In.Position(GetPosFromLocation(Renderable.Position.BOTTOMLEFT, Renderable.PositionType.RELATIVE, In.Width(), In.Height(), 50, -66, this));

		specifier.Vector2D Pos = GetPosFromLocation(Renderable.Position.BOTTOMLEFT, Renderable.PositionType.RELATIVE, In.InputText.Width(), In.InputText.Width(), 0, -12, In);
		In.InputText.Position(Pos);
		In.InputText.ZIndex(123123123);
	}

	public static Console GetInstance()
	{
		if (Instance == null)
			Instance = new Console();

		return Instance;
	}

	public void WriteLine(Logging.Type Type, String Message)
	{
		Color LineColor = null;
		switch (Type)
		{
			case ERROR:
				LineColor = Color.red;
				break;
			case WARNING:
				LineColor = Color.yellow;
				break;
			default:
				LineColor = Color.white;
		}

		GUIFont Cur = Lines.getFirst();
		for (int i = 1; i < Lines.size(); i++)
		{
			if (Cur != null)
			{
				Cur.Text(Lines.get(i).Text());
				Cur.Colour = Lines.get(i).Colour;
				Lines.get(i).Text(Message);
				Lines.get(i).Colour = LineColor;
			}
			Cur = Lines.get(i);
		}
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
		} else
		{
			Visible = true;
			Logging.getInstance().Write(Logging.Type.INFO, "Console is now visible");
			Game.GetInstance().State(Game.State.MENU);
		}
	}

	public InputField Input()
	{
		return In;
	}
}
