package start;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.util.ResourceLoader;

import debug.LogType;
import debug.Logger;
import interfaces.*;
import interfaces.file.types.TextFile;

import java.util.Calendar;

public class Main
{

	private final String resources = "/res";

	private ResourceLoader res_loader;
	private Logger log;
	private State gameState;
	private Game GameObject;

	public void init()
	{

		gameState = State.INTRO;
		res_loader = new ResourceLoader();

		log = new Logger(true);
		
		GameObject = new Game();
		GameObject.Init();

		TextFile test = new TextFile("textfile.txt", false, false);
		test.Open();
		log.log(LogType.INFO, "INIT", test.getText());

		try
		{
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e)
		{
			log.log(LogType.ERROR, "Main", "Problem creating display");
			e.printStackTrace();
		}
	}

	public void run()
	{
		while (!Display.isCloseRequested())
		{
			GameObject.GameTime = Calendar.getInstance().getTimeInMillis(); // update our game time
			switch (gameState)
			{
				case INTRO:
					Display.update();
					break;
				case MAIN_MENU:
					break;
				case GAME:
					break;
			}
		}
	}

	public static void main(String[] argv)
	{
		Main main = new Main();
		main.init();
		main.run();
	}
}
