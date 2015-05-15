package start;

import interfaces.*;
import interfaces.file.Logging;

import java.util.*;

public class Main
{
	private State gameState;
	public static Game GameObject;

	public void init()
	{
		gameState = State.INTRO;
		GameObject = Game.GetInstance();
		GameObject.Testing();
	}

	public void run()
	{		
		long passed = 0;
		while (GameObject.IsRunning)
		{
			GameObject.UpdateGameTime(); // update our game time
			GameObject.UpdateWorld();
			if (GameObject.GameTime() - passed >= 1000)
			{
				GameObject.Log().Write(Logging.Type.INFO, "1 Second has Passed %d", passed);
				passed = GameObject.GameTime();
			}
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
			GameObject.Output().updateFPS();
		}
	}
	
	public static void main(String[] argv)
	{
		Main main = new Main();
		main.init();
		main.run();
	}
}
