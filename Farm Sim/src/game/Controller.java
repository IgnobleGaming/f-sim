package game;

import java.util.Queue;


public class Controller {

	private enum States {INTRO, MAIN_MENU, GAME, INVENTORY, SETTINGS};
	private States State;
	
	//private Queue<Command> queue;     unsure how this should work.  if we want to push and pop the 
	//									commands, perhaps create a templated commands class.
	
	private boolean LBDown;
	private boolean RBDown;
	
	public Controller() {
		this.State = States.INTRO;
	}
	
	public void update() {
		switch (State) {
		case INTRO:
			break;
		case MAIN_MENU:
			break;
		case GAME:
			break;
		case INVENTORY:
			break;
		case SETTINGS:
			break;
		}
	}
	
	public void updateState(States NewState) {
		this.State = NewState;
	}

	
}
