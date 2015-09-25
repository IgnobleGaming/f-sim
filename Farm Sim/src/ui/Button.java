package ui;

import specifier.Vector2D;
import interfaces.Game;
import interfaces.file.*;
import interfaces.file.types.*;

public class Button extends UIElement
{
	public Button(String Label, UIElement Parent, Vector2D parentOffset, int Width, int Height)
	{
		super("Button", "Button UI Element", Label, Width, Height, new specifier.Vector2D(parentOffset.x, parentOffset.y), Parent);
		this.SetSprite((MaterialFile)FileManager.getInstance().Retrieve("resources\\ui\\buttons\\button_default.png"));
	}
	
	public String toString()
	{
		return String.format("Button: \"%s\" -- %s", this.Name, this.Description);
	}
	
	public void onHover()
	{
		this.SetSprite((MaterialFile)FileManager.getInstance().Retrieve("resources\\ui\\buttons\\button_default_selected.png"));
	}
	
	public void onLeave()
	{
		this.SetSprite((MaterialFile)FileManager.getInstance().Retrieve("resources\\ui\\buttons\\button_default.png"));
	}
	
	public void Action()
	{
		Game.GetInstance().State(Game.State.INGAME);
	}
	
}
