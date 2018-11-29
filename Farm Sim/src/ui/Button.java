package ui;

import specifier.Vector2D;
import interfaces.Game;
import interfaces.file.*;
import interfaces.file.types.*;

public class Button extends UIElement
{
	protected String default_sprite;
	protected String hover_sprite;
	
	public Button(String Label, UIElement Parent, Vector2D parentOffset, int Width, int Height, String default_sprite, String hover_sprite, boolean isActive)
	{
		super("Button", "Button UI Element", Label, Width, Height, new specifier.Vector2D(parentOffset.x, parentOffset.y), Parent, isActive);
		this.default_sprite = default_sprite;
		this.hover_sprite = hover_sprite;
		
		temp(); //to be removed.
		
		setSprite();
	}
	
	private void temp() {
		default_sprite = "resources\\ui\\buttons\\button_default.png";
		hover_sprite   = "resources\\ui\\buttons\\button_default_selected.png";
	}
	
	public String toString()
	{
		return String.format("Button: \"%s\" -- %s", this.Name, this.Description);
	}
	
	public void onHover()
	{
		onHover(hover_sprite);
	}
	
	private void onHover(String filePath)
	{
		this.SetSprite((MaterialFile)FileManager.getInstance().Retrieve(filePath));
	}
	
	public void onLeave()
	{
		//"resources\\ui\\buttons\\button_default.png"
		onLeave(default_sprite);
	}
	
	private void onLeave(String s) {
		if (isActive)
			this.SetSprite((MaterialFile)FileManager.getInstance().Retrieve(s));
	}
	
	public void Action()
	{
		Game.GetInstance().State(Game.State.INGAME);
	}
	
	public void setSprite() {
		setSprite((isActive) ? default_sprite : hover_sprite);
	}
	
	private void setSprite(String filePath) {
		this.SetSprite((MaterialFile)FileManager.getInstance().Retrieve(filePath));
	}
	
	protected void activeOn() {
		isActive = true;
	}
	
	public boolean isActive(){
		return isActive;
	}
	
	protected void activeOff() {
		isActive = false;
	}
}
