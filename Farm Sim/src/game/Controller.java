package game;

import interfaces.Game;
import interfaces.Objects;
import interfaces.file.Logging;
import object.Entity.State;

import org.lwjgl.input.Keyboard;

import renderable.Console;
import renderable.Renderable;

public class Controller
{
	private int LastKey;

	private enum InputType
	{
		UP, DOWN, LEFT, RIGHT, ESCAPE, CONSOLE, RELEASE
	};

	public Controller()
	{
		Keyboard.enableRepeatEvents(false);
	}

	public void Update()
	{
		ReadInput();
	}

	private void ProcessInput(InputType input)
	{
		switch (Game.GetInstance().CurrentState())
		{
			case LOADING:
				break;
			case MENU: //in
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
			if (Keyboard.getEventKeyState())
			{
				LastKey = Keyboard.getEventKey();
				InputType In = InputType.RELEASE;
				
				switch (LastKey)
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
				}
				
				ProcessInput(In);
			}
			
			else
			{
				if(Keyboard.getEventKey() == LastKey)
					ProcessInput(InputType.RELEASE);
			}
		}

		// event driven
		/*
		 * if (Keyboard.isKeyDown(Keyboard.KEY_UP)) ProcessInput(InputType.UP);
		 * 
		 * if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) ProcessInput(InputType.DOWN);
		 * 
		 * if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) ProcessInput(InputType.LEFT);
		 * 
		 * if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) ProcessInput(InputType.RIGHT);
		 * 
		 * if (Keyboard.isKeyDown(Keyboard.KEY_GRAVE)) ProcessInput(InputType.CONSOLE);
		 */
	}
}
