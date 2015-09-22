package ui;

import specifier.Vector2D;
import interfaces.file.*;
import interfaces.file.types.*;

public class Button extends UIElement
{
	public Button(String Label, UIElement Parent, Vector2D parentOffset, int Width, int Height)
	{
		super("Button", "Button UI Element", Label, Width, Height, new specifier.Vector2D(Parent.Position().x + parentOffset.x, Parent.Position().y + parentOffset.y), Parent);
		this.SetSprite((MaterialFile)FileManager.getInstance().Retrieve("resources\\ui\\buttons\\button_default.png"));
	}
	
	public String toString()
	{
		return String.format("Button: \"%s\" -- %s", this.Name, this.Description);
	}
}
