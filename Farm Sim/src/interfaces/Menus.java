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
		currentMenu = new Menu(1920, 1080);
	}
	
	public static Menus getInstance()
	{
		if (Instance == null)
			return new Menus();
		return Instance;
	}
	
	public Menu getCurrentMenu()
	{
		return currentMenu;
	}
}
