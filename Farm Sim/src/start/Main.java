package start;

import debug.LogType;
import debug.Logger;
import interfaces.*;
import interfaces.file.types.TextFile;

import java.util.Calendar;

import renderable.GUIFont;

public class Main
{
	private State gameState;
	public static Game GameObject;

	public void init()
	{
		gameState = State.INTRO;
		GameObject = new Game();
		GameObject.Init();
	}

	public void run()
	{		
		renderable.GUIFont testFont = new renderable.GUIFont("Times New Roman", "This is test", GUIFont.Size.HUGE);
		while (GameObject.IsRunning)
		{
			GameObject.GameTime(Calendar.getInstance().getTimeInMillis()); // update our game time
			GameObject.Output().AddRenderElement(testFont);
			switch (gameState)
			{
				case INTRO:
					break;
				case MAIN_MENU:
					break;
				case GAME:
					break;
			}
			GameObject.Output().Update();		
		}
	}
	
	public static void main(String[] argv)
	{
		Main main = new Main();
		main.init();
		main.run();
	}
}
