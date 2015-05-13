package game;


public class Controller {

	private enum States {INTRO, MAIN_MENU, GAME, INVENTORY, SETTINGS};
	
	private States State;
	
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
