package ui;

import specifier.Vector2D;

public class Button extends UIElement
{
	public Button(String Label, UIElement Parent, Vector2D parentOffset, int Width, int Height)
	{
		super("Button", "Button UI Element", Label, Width, Height, new specifier.Vector2D(Parent.Position().x + parentOffset.x, Parent.Position().y + parentOffset.y));
	}
}
