package game;

import java.util.*;

public class Controller extends interfaces.Game
{

	private enum States
	{
		INTRO, MAIN_MENU, GAME, INVENTORY, SETTINGS
	};
	
	private enum InputType
	{
		UP, DOWN, LEFT, RIGHT, ESCAPE, MENU
	};

	private States State;
	private Queue<InputType> InputQueue;

	public Controller()
	{
		this.State = States.INTRO;
		InputQueue = new PriorityQueue<InputType>();
	}

	public void update()
	{
		switch (State)
		{
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

	public boolean AddInput(InputType Command)
	{
		InputType NewInput = Command;
		if (InputQueue.size() < (int)Variables.get("c_maxinputqueue").Current())
		{
			InputQueue.add(NewInput);
			return true;
		}
		
		else
		{
			//log that there's too many commands in queue
			return false;
		}		
	}
	
	public void updateState(States NewState)
	{
		this.State = NewState;
	}

}
