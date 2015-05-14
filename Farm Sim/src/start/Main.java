package start;

import interfaces.*;
import java.util.*;

public class Main
{
	private State gameState;
	public static Game GameObject;

	public void init()
	{
		gameState = State.INTRO;
		GameObject = new Game();
		GameObject.Init();
		GameObject.Testing();
	}

	public void run()
	{		
		while (GameObject.IsRunning)
		{
			GameObject.GameTime(Calendar.getInstance().getTimeInMillis()); // update our game time
			GameObject.Input().Update();
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
