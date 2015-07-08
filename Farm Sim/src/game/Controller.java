package game;

import interfaces.Camera;
import interfaces.Game;
import interfaces.Objects;
import interfaces.file.Logging;
import object.Entity.State;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import renderable.Console;
import renderable.Renderable;

import java.awt.TrayIcon.MessageType;
import java.util.ArrayList;

import jdk.nashorn.internal.runtime.regexp.joni.constants.Arguments;

public class Controller
{
	private int NewestKey;
	private ArrayList<Character> InputChars;
	private ArrayList<String> Args = new ArrayList<String>();
	private String InputStr = "";

	private enum InputType
	{
		UP, DOWN, LEFT, RIGHT, ESCAPE, ZOOM, CONSOLE, RELEASE, ENTER, NONE, BACK
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

	/**
	 * Interpret the read input depending on current game state
	 * @param input ( type of input )
	 * @param chars ( optional characters if in console or menu state )
	 */
	private void ProcessInput(InputType input, char... chars)
	{
		switch (Game.GetInstance().State())
		{
			case LOADING:
				break;
			case MENU: // in
				for (char c : chars)
				{
					if (utilities.Text.isAlphaNumeric(c) && InputChars.size() < 64)
					{
						InputChars.add(c);
						InputStr += c;
					}
				}
				switch (input)
				{
					case CONSOLE:
						Console.GetInstance().ToggleVisibility();
						break;
					case ENTER:
						if (InputStr.split("\\s").length > 0)
						{
							int count = 0;
							for (String S : InputStr.split(" "))
							{
								if (count > 0)
									Args.add(S);
								count++;
							}
							InputStr = InputStr.split("\\s")[0];
						}
						if (InputStr.length() > 0)
						{
							interfaces.GameCommands.GetInstance().Execute(InputStr, Args);
						}
						InputChars.removeAll(InputChars);
						Args.removeAll(Args);
						InputStr = "";
						break;
					case BACK:
						if (InputChars.size() > 0 && InputStr.length() > 0)
						{
							InputChars.remove(InputChars.size() - 1);
							InputStr = InputStr.substring(0,InputStr.length() - 1);
						}
						break;
				}
				Console.GetInstance().Input().InputText.Text("> " + InputStr + "_");
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

	/**
	 * Polled reading of each input event
	 */
	private void ReadInput()
	{
		// this is polled
		while(Mouse.next())
		{
			int EventButton = Mouse.getEventButton();
			int MouseWheelDelta = Mouse.getEventDWheel();
			
			switch (EventButton)
			{
				default:
					break;
			}
			
			if (Math.abs(MouseWheelDelta) > 100)
			{
				double CurDist = Camera.getInstance().Distance();
				double ZoomDelta = CurDist;
						
				if (MouseWheelDelta > 0)
				{
					if (CurDist > .2)
						ZoomDelta = CurDist * .95;
				}
				else
				{
					if (CurDist < 5)
						ZoomDelta = CurDist * 1.05;
				}
					
				Camera.getInstance().SetDistance(ZoomDelta);
			}
		}
		
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
						In = InputType.DOWN;
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
					case Keyboard.KEY_RETURN:
						In = InputType.ENTER;
						break;
					case Keyboard.KEY_BACK:
						In = InputType.BACK;
						break;
					default:
						ProcessInput(InputType.NONE, Keyboard.getEventCharacter());
				}

				ProcessInput(In);
			}

			else			// key is being released
			{
				if (EventKey == NewestKey)
				{
					ProcessInput(InputType.RELEASE);
					NewestKey = 0;
				}
			}
		}
	}
}
