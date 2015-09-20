package ui;
import renderable.Renderable;
import specifier.Vector2D;

import org.newdawn.slick.Color;

import interfaces.Render;
import renderable.GUIFont;
import renderable.GUIFont.FontFamily;
import renderable.GUIFont.Size;

public class UIElement extends Renderable
{
	private String Name;
	private String Description;
	private GUIFont Label;
	protected UIElement Parent;
	
	public UIElement(String Name, String Description, String Label, int Width, int Height, Vector2D initialPos)
	{
		super(Width, Height);
		this.Name = Name;
		this.Description = Description;
		this.XPos = initialPos.x;
		this.YPos = initialPos.y;
		this.Label = new GUIFont(FontFamily.Consolas, Label, Size.SMALL, Color.white, this.XPos, this.YPos);
		this.ZIndex(1000);
		this.Label.ZIndex(1000);
	}
	
	public void setLabel(String newLabel)
	{
		Label.Text(newLabel);
	}
	
	public void Draw()
	{
		Render.DrawImage(this.CurrentSprite, this.translatedRelativePos());
		Label.Draw();
	}
}
