package game;

import interfaces.Game;
import interfaces.Objects;
import interfaces.file.Logging;

import org.lwjgl.input.Keyboard;

import renderable.Console;
import renderable.Renderable;

public class Controller
{
	private enum InputType
	{
		UP, DOWN, LEFT, RIGHT, ESCAPE, MENU, CONSOLE
	};

	public Controller()
	{
	}

	public void Update()
	{
		ReadInput();
		switch (Game.GetInstance().CurrentState())
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
	
	private void ProcessInput(InputType input)
	{	
		switch (input)
		{
			case UP:
				Game.GetInstance().Controllable().Move(specifier.Direction.Relative.UP);
				break;
			case DOWN:
				Game.GetInstance().Controllable().Move(specifier.Direction.Relative.DOWN);
				break;
			case LEFT:
				Game.GetInstance().Controllable().Move(specifier.Direction.Relative.LEFT);
				break;
			case RIGHT:
				Game.GetInstance().Controllable().Move(specifier.Direction.Relative.RIGHT);
				break;
			case CONSOLE:
				Console.GetInstance().ToggleVisibility();
				break;
		}
		
		//Logging.getInstance().Write(Logging.Type.INFO, "BUTTON PRESSED, MOVING %s - New POS is (%f %f)", input, Game.GetInstance().Controllable().Position().x, Game.GetInstance().Controllable().Position().y);
	}

	private void ReadInput()
	{
		// this is polled
		while (Keyboard.next())
		{
			switch(Keyboard.getEventKey())
			{
				case Keyboard.KEY_UP:
					ProcessInput(InputType.UP);
					break;
				case Keyboard.KEY_DOWN:
					ProcessInput(InputType.DOWN);
					break;
				case Keyboard.KEY_LEFT:
					ProcessInput(InputType.LEFT);
					break;
				case Keyboard.KEY_RIGHT:
					ProcessInput(InputType.RIGHT);
					break;
				case Keyboard.KEY_GRAVE:
					if(Keyboard.getEventKeyState())
							ProcessInput(InputType.CONSOLE);
					break;
			}
		}
		
		 //event driven
		/*if (Keyboard.isKeyDown(Keyboard.KEY_UP))
			ProcessInput(InputType.UP);

		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			ProcessInput(InputType.DOWN);
	
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			ProcessInput(InputType.LEFT);

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			ProcessInput(InputType.RIGHT);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_GRAVE))
			ProcessInput(InputType.CONSOLE);*/
	}
}
