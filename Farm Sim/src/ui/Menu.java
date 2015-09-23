package ui;

import ui.UIElement;

import java.util.ArrayList;

import game.Controller;
import interfaces.file.types.*;
import interfaces.file.FileManager;
import specifier.*;

public class Menu extends UIElement
{
	private UIElement Background;
	private ArrayList<UIElement> Members;
	protected UIElement selectedElem;
	
	public Menu(int Width, int Height)
	{
		super("MENU", "MENU ELEMENT", "TEST", Width, Height, new Vector2D(Width / 2, Height / 2), null);
		Members = new ArrayList<UIElement>();
		Background = new UIElement("Menu Element", "Parent Menu", "", Width, Width, new Vector2D(Width / 2, Height / 2), this);
		Background.SetSprite((MaterialFile)FileManager.getInstance().Retrieve("resources\\ui\\menus\\menu_default.png"));
		selectedElem = this;
	}
	
	public void Draw()
	{
		Background.Draw();
		
		for (UIElement E : Members)
		{
			if (E.Visible)
				E.Draw();
		}
	}
	
	public void Update()
	{
		Background.Update();
		
		for (UIElement E : Members)
		{
			E.Update();
			if (E.inSelectionRange())
			{
				E.onHover();
				if (Controller.primaryDown())
					E.Action();
			}
			else // needs to be changed because not always leaving
				E.onLeave();
		}		
	}
	
	public void addChild(UIElement Child)
	{
		if (!Members.contains(Child))
			Members.add(Child);
	}
	
	public void removeChild(UIElement Child)
	{
		if (Members.contains(Child))
			Members.remove(Child);
	}
}
