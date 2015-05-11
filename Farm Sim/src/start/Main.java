package start;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.util.ResourceLoader;

import debug.LogType;
import debug.Logger;
import interfaces.*;
import interfaces.FileTypes.TextFile;

public class Main {

	private final String resources = "/res";
	
	private ResourceLoader res_loader;
	private Logger log;
	private State gameState;
	
	public void init() {
		
		gameState = State.INTRO;
		res_loader = new ResourceLoader();
		
		log = new Logger(true);
		
		TextFile test = new TextFile("textfile.txt", false, false);
		test.Open();
		log.log(LogType.INFO, "INIT", test.getText());
		
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.create();
		} catch (LWJGLException e) {
			log.log(LogType.ERROR, "Main", "Problem creating display");
			e.printStackTrace();
		}
	}

	public void run() {
		while (!Display.isCloseRequested()) { // needs to poll if close request but i aint created no damn dsplay yet,.
			switch(gameState) {
			case INTRO:
				Display.update();
				break;
			case MAIN_MENU:
				break;
			case GAME:
				break;
			}
		}

		System.out.println("test");
	}

	public static void main(String[] argv) {
		Main main = new Main();
		main.init();
		main.run();
	}
}
