package ui;

import renderable.Renderable;
import ui.UIElement;
import java.util.ArrayList;
import interfaces.file.types.*;
import interfaces.Game;
import interfaces.Render;
import interfaces.file.FileManager;

public class Menu extends UIElement
{
	private UIElement Background;
	private ArrayList<UIElement> Members;
	
	public Menu(int Width, int Height)
	{
		super("MENU", "MENU ELEMENT", "TEST", Width, Height, new specifier.Vector2D(Width / 2, Height / 2), null);
		Members = new ArrayList<UIElement>();
		MaterialFile Mat = (MaterialFile)FileManager.getInstance().Retrieve("resources\\ui\\menus\\menu_default.png");
		Background = new UIElement("Menu Element", "Parent Menu", "", Width, Width, new specifier.Vector2D(Width / 2, Height / 2), this);
		Background.SetSprite(Mat);
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
