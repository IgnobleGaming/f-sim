package utilities;

import interfaces.Variables;

import org.lwjgl.input.Keyboard;



public class KeyCommands
{
	public int UP = Keyboard.KEY_UP;
	public int DOWN;
	public int LEFT;
	public int RIGHT;
	public int INTERACT;
	public int CONSOLE;
	public int MAP;
	public int RETURN;
	public int BACK;
	
	public KeyCommands()
	{
		Update();
	}
	
	public void Update()
	{
		UP = (int) Variables.GetInstance().Get("ctrl_UP").Current();
		DOWN = (int) Variables.GetInstance().Get("ctrl_DOWN").Current();
		RIGHT = (int) Variables.GetInstance().Get("ctrl_RIGHT").Current();
		LEFT = (int) Variables.GetInstance().Get("ctrl_LEFT").Current();
		INTERACT = (int) Variables.GetInstance().Get("ctrl_INTERACT").Current();
		CONSOLE = (int) Variables.GetInstance().Get("ctrl_CONSOLE").Current();
		MAP = (int) Variables.GetInstance().Get("ctrl_MAP").Current();
		RETURN = (int) Variables.GetInstance().Get("ctrl_RETURN").Current();
		BACK = (int) Variables.GetInstance().Get("ctrl_BACK").Current();
	}
}