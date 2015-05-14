package game;

import interfaces.Game;
import interfaces.Objects;
import interfaces.file.Logging;

import org.lwjgl.input.Keyboard;

import renderable.Renderable;

public class Controller
{
	private enum InputType
	{
		UP, DOWN, LEFT, RIGHT, ESCAPE, MENU
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
		//Logging.getInstance().Write(Logging.Type.INFO, "%s key was pressed!", input.toString());
		
		switch (input)
		{
			case UP:
				Objects.GetInstance().Get(0).Move(specifier.Direction.Relative.UP);
				break;
			case DOWN:
				Objects.GetInstance().Get(0).Move(specifier.Direction.Relative.DOWN);
				break;
			case LEFT:
				Objects.GetInstance().Get(0).Move(specifier.Direction.Relative.LEFT);
				break;
			case RIGHT:
				Objects.GetInstance().Get(0).Move(specifier.Direction.Relative.RIGHT);
				break;
		}

	}

	private void ReadInput()
	{
		// this is polled
		/*while (Keyboard.next())
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
			}
		}
		
		 event driven */
		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
			ProcessInput(InputType.UP);

		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			ProcessInput(InputType.DOWN);
	
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			ProcessInput(InputType.LEFT);

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			ProcessInput(InputType.RIGHT);
	}
}
