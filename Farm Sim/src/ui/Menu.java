package ui;

import renderable.Renderable;
import ui.UIElement;
import java.util.ArrayList;
import interfaces.file.types.*;
import interfaces.Game;
import interfaces.Render;
import interfaces.file.FileManager;

public class Menu extends Renderable
{
	private UIElement Background;
	private ArrayList<UIElement> Members;
	
	public Menu(int Width, int Height)
	{
		super(Width, Height);
		int Wid = Render.GetInstance().Width();
		int Hei = Render.GetInstance().Height();
		Members = new ArrayList<UIElement>();
		Background = new UIElement("Menu Element", "Parent Menu", "", Wid, Hei, new specifier.Vector2D(Wid / 2, Height /2));
		Background.SetSprite((MaterialFile)FileManager.getInstance().Retrieve("resources\\ui\\menus\\menu_default.png"));
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
