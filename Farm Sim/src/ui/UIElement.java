package ui;
import renderable.Renderable;
import specifier.Vector2D;

import org.newdawn.slick.Color;

import game.Controller;
import interfaces.Render;
import renderable.GUIFont;
import renderable.GUIFont.FontFamily;
import renderable.GUIFont.Size;

public class UIElement extends Renderable
{
	protected String Name;
	protected String Description;
	private GUIFont Label;
	protected UIElement Parent;
	protected boolean isActive;
	
	public UIElement(String Name, String Description, String Label, int Width, int Height, Vector2D initialPos, UIElement Parent, boolean active)
	{
		super(Width, Height);
		this.Name = Name;
		this.Description = Description;
		this.XPos = initialPos.x;
		this.YPos = initialPos.y;
		this.Parent = Parent;
		this.isActive = active;
		Vector2D labelPos = new Vector2D(initialPos.x, initialPos.y);
		this.Label = new GUIFont(FontFamily.Consolas, Label, Size.SMALL, Color.white, labelPos);
		
		if (Parent != null)
		{
			labelPos = GetPosFromLocation(Position.CENTERCENTER, PositionType.RELATIVE, this.Label.fontWidth(), this.Label.fontHeight(), initialPos.x, initialPos.y, this.Parent);
			this.Label.Position(labelPos);
			
			this.XPos = initialPos.x + Parent.XPos;
			this.YPos = initialPos.y + Parent.YPos;
		}
	}
	
	public void setLabel(String newLabel)
	{
		Label.Text(newLabel);
	}
	
	public void Draw()
	{
		Render.DrawImage(this.CurrentSprite, this.translatedRelativePos(), Width, Height);
		Label.Draw();
	}
	
	public boolean inSelectionRange()
	{
		Vector2D mousePosition = Controller.getMousePosition();
		return (mousePosition.x <= this.XPos + this.Width() / 2 && mousePosition.x >= this.XPos - this.Width() / 2 && mousePosition.y >= this.YPos - this.Height() / 2 && mousePosition.y <= this.YPos + this.Height() / 2 );
	}
	
	public void Update()
	{
		
	}
	
	public void onHover()
	{
		
	}
	
	public void onLeave()
	{
		
	}
	
	public void Action()
	{
		
	}
}
