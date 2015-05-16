package game;

import interfaces.Game;
import interfaces.Objects;
import interfaces.file.Logging;
import object.Entity.State;

import org.lwjgl.input.Keyboard;

import renderable.Console;
import renderable.Renderable;

import java.util.ArrayList;

public class Controller
{
	private int NewestKey;
	private ArrayList<Character> InputChars;

	private enum InputType
	{
		UP, DOWN, LEFT, RIGHT, ESCAPE, CONSOLE, RELEASE, ENTER, NONE
	};

	public Controller()
	{
		InputChars = new ArrayList<Character>();
		Keyboard.enableRepeatEvents(true);
	}

	public void Update()
	{
		ReadInput();
	}

	private void ProcessInput(InputType input, char... chars)
	{
		switch (Game.GetInstance().State())
		{
			case LOADING:
				break;
			case MENU: //in
				for (char c : chars)
					InputChars.add(c);
				switch (input)
				{
					case CONSOLE:
						Console.GetInstance().ToggleVisibility();
						break;
					case ENTER:
						
						InputChars.removeAll(InputChars);
						break;
				}
				break;
			case INGAME: // we are in the game so moving controllable entity
				switch (input)
				{
					case UP:
						Game.GetInstance().Controllable().SetState(State.MOVINGUP);
						break;
					case DOWN:
						Game.GetInstance().Controllable().SetState(State.MOVINGDOWN);
						break;
					case LEFT:
						Game.GetInstance().Controllable().SetState(State.MOVINGLEFT);
						break;
					case RIGHT:
						Game.GetInstance().Controllable().SetState(State.MOVINGRIGHT);
						break;
					case RELEASE:
						Game.GetInstance().Controllable().SetState(State.STATIONARY);
						break;
					case CONSOLE:
						Console.GetInstance().ToggleVisibility();
						break;
				}
				break;
		}


		// Logging.getInstance().Write(Logging.Type.INFO, "BUTTON PRESSED, MOVING %s - New POS is (%f %f)", input, Game.GetInstance().Controllable().Position().x, Game.GetInstance().Controllable().Position().y);
	}
	
	private void ReadInput()
	{
		// this is polled
		while (Keyboard.next())
		{
			int EventKey = Keyboard.getEventKey();
			if (Keyboard.getEventKeyState()) // key is pressed
			{
				if (NewestKey != EventKey) // a new input detected so force state to this one
					NewestKey = EventKey;
				
				InputType In = InputType.RELEASE;
				
				switch (EventKey)
				{
					case Keyboard.KEY_UP:
						In = InputType.UP;
						break;
					case Keyboard.KEY_DOWN:
						In =InputType.DOWN;
						break;
					case Keyboard.KEY_LEFT:
						In = InputType.LEFT;
						break;
					case Keyboard.KEY_RIGHT:
						In = InputType.RIGHT;
						break;
					case Keyboard.KEY_GRAVE:
						if (Keyboard.getEventKeyState())
							In = InputType.CONSOLE;
						break;
						default:
							ProcessInput(InputType.NONE, Keyboard.getEventCharacter());
				}
				
				ProcessInput(In);
			}
			
			else // key is released
			{
				if(EventKey == NewestKey)
				{
					ProcessInput(InputType.RELEASE);
					NewestKey = 0;
				}
			}
		}
	}
}
