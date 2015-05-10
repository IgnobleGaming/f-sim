package start;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.util.ResourceLoader;

public class Main {

	private final String resources = "/res";
	
	ResourceLoader res_loader;
	
	State gameState = State.INTRO;
	
	public void init() throws IOException {
		res_loader = new ResourceLoader();
	}

	public void run() {
		while (true) {
			switch(gameState) {
			case INTRO:
				System.out.println(gameState);
				break;
			case MAIN_MENU:
				break;
			case GAME:
				break;
			}
		}
	}
	
	public static void main(String[] argv) {
		Main main = new Main();
		main.run();
	}
}
