package start;

import interfaces.*;
import java.util.*;

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
		renderable.GUIFont testFont = new renderable.GUIFont("Times New Roman", "This is test", GUIFont.Size.HUGE, org.newdawn.slick.Color.blue, 250, 50);
		Random rand = new Random();
		while (GameObject.IsRunning)
		{
			GameObject.GameTime(Calendar.getInstance().getTimeInMillis()); // update our game time
			GameObject.Output().AddRenderElement(testFont);
			testFont.Position(rand.nextInt(800), rand.nextInt(600));
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
