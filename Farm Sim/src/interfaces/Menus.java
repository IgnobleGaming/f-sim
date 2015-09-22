package interfaces;

import java.util.ArrayList;
import ui.*;

public class Menus
{
	private static Menus Instance;
	private ArrayList<Menu> availableMenus;
	private Menu currentMenu;
	
	private Menus()
	{
		availableMenus = new ArrayList<Menu>();
		currentMenu = new Menu(Render.GetInstance().Width(), Render.GetInstance().Height());
	}
	
	public static Menus getInstance()
	{
		if (Instance == null)
			Instance = new Menus();
		return Instance;
	}
	
	public Menu getCurrentMenu()
	{
		return currentMenu;
	}
}
