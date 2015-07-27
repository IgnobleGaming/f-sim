package game;

import interfaces.Camera;
import interfaces.Game;
import object.Entity;
import object.Entity.State;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import renderable.Console;
import java.util.ArrayList;

public class Controller
{
	private int NewestKey;
	private ArrayList<Character> InputChars;
	private ArrayList<String> Args = new ArrayList<String>();
	private String InputStr = "";
	private int CT = 0;
	private int TT = 300;

	private enum InputType
	{
		UP, DOWN, LEFT, RIGHT, INTERACT, ESCAPE, ZOOM, CONSOLE, RELEASE, ENTER, NONE, BACK
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
	 * 
	 * @param input
	 *            ( type of input )
	 * @param chars
	 *            ( optional characters if in console or menu state )
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
							InputStr = InputStr.substring(0, InputStr.length() - 1);
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
					case INTERACT:
						Game.GetInstance().Controllable().Interact();
						break;
					case NONE:
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
		if (Game.GetInstance().Controllable() != null && !Game.GetInstance().Controllable().CheckFlag(Entity.Flag.LOCKED))
		{
			while (Mouse.next())
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
						if (CurDist > .30)
							ZoomDelta = CurDist - .05f;
					} else
					{
						if (CurDist < 2)
							ZoomDelta = CurDist + .05f;
					}

					Camera.getInstance().SetDistance(ZoomDelta);
				}
			}

			ReadMovementInput();
			ReadActionInput();

		}
	}

	/**
	 * Read Movement Input - Detects if any movement key is pressed and moves the controllable in that direction otherwise set player anim to stationary
	 */
	private void ReadMovementInput()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_D)
				|| Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP))
				ProcessInput(InputType.UP);
			if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT))
				ProcessInput(InputType.LEFT);
			if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN))
				ProcessInput(InputType.DOWN);
			if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
				ProcessInput(InputType.RIGHT);
		} else
			ProcessInput(InputType.RELEASE);
	}

	private void ReadActionInput()
	{
		InputType In = InputType.NONE;  // Input to Process
		
		CT += Game.GetInstance().Delta(); // Current Time

		// Checks to see if another action can be processed //
		if (CT > TT)
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_E))
			{
				ProcessInput(InputType.INTERACT);
				CT = 0;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_GRAVE))
			{
				In = InputType.CONSOLE;
				CT = 0;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_RETURN))
			{
				In = InputType.ENTER;
				CT = 0;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_BACK))
			{
				In = InputType.BACK;
				CT = 0;
			}
			
			ProcessInput(In);
		}
	}
}
