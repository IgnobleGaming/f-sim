package game;

import interfaces.Camera;
import interfaces.Game;
import interfaces.Variables;
import object.Entity;
import object.Entity.State;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import renderable.Console;
import renderable.MiniMap;
import utilities.KeyCommands;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.PriorityBlockingQueue;

public class Controller
{
	private int NewestKey;
	private ArrayList<Character> InputChars;
	private ArrayList<String> Args = new ArrayList<String>();
	private String InputStr = "";
	private int CT = 0;
	private int TT = 300;
	private PriorityBlockingQueue<InputOrder> keyBuffer;
	private KeyCommands Commands;

	private class InputOrder implements Comparable<InputOrder>
	{
		InputType Key;
		long timePressed;

		public InputOrder()
		{
		}

		public int compareTo(InputOrder other)
		{
			return (int) (other.timePressed - this.timePressed);
		}
	}

	private enum InputType
	{
		UP, DOWN, LEFT, RIGHT, INTERACT, MAP, ESCAPE, ZOOM, CONSOLE, RELEASE, ENTER, NONE, BACK
	};

	public Controller()
	{
		InputChars = new ArrayList<Character>();
		Keyboard.enableRepeatEvents(true);
		keyBuffer = new PriorityBlockingQueue<InputOrder>();
		Commands = new KeyCommands();
	}

	public void Update()
	{
		ReadInput();
		if (keyBuffer.size() > 0 && keyBuffer.peek().Key != null)
			ProcessInput(keyBuffer.peek().Key);
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
					case ESCAPE:
						Game.GetInstance().State(Game.State.INGAME);
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
					case ESCAPE:
						Game.GetInstance().State(Game.State.MENU);
						break;
					case INTERACT:
						Game.GetInstance().Controllable().SetState(State.INTERACTING);
						break;
					case MAP:
						if (MiniMap.GetInstance().Visible)
							MiniMap.GetInstance().Visible = false;
						else
							MiniMap.GetInstance().Visible = true;
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
			if ((boolean) Variables.GetInstance().Get("g_developer").Current())
			{
				int MouseWheelDir = Mouse.getDWheel();

				if (MouseWheelDir != 0)
				{
					double CurDist = Camera.getInstance().Distance();
					double ZoomDelta = CurDist;

					if (MouseWheelDir > 0)
					{
						if (CurDist > .1)
							ZoomDelta = CurDist - .05f;
					} else
					{
						if (CurDist < 20)
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
		Keyboard.enableRepeatEvents(false);

		while (Keyboard.next())
		{
			int pressedKey = Keyboard.getEventKey();
			InputOrder keyOrd = new InputOrder();
			keyOrd.timePressed = Game.GetInstance().GameTime();
			
			
			if (Game.GetInstance().State() == Game.State.MENU)
			{
					ProcessInput(InputType.NONE, Keyboard.getEventCharacter()); 
					return;
			}

			/*
			switch (pressedKey)
			{
				case Commands.UP:
					keyOrd.Key = InputType.UP;
					break;
				case Keyboard.KEY_S:
				case Keyboard.KEY_DOWN:
					keyOrd.Key = InputType.DOWN;
					break;
				case Keyboard.KEY_A:
				case Keyboard.KEY_LEFT:
					keyOrd.Key = InputType.LEFT;
					break;
				case Keyboard.KEY_D:
				case Keyboard.KEY_RIGHT:
					keyOrd.Key = InputType.RIGHT;
					break;
				default:    
					return;

			}
			*/
			
			if (pressedKey == Commands.UP)
				keyOrd.Key = InputType.UP;
			else if (pressedKey == Commands.DOWN)
				keyOrd.Key = InputType.DOWN;
			else if (pressedKey == Commands.LEFT)
				keyOrd.Key = InputType.LEFT;
			else if (pressedKey == Commands.RIGHT)
				keyOrd.Key = InputType.RIGHT;
			else
				break;

			if (Keyboard.getEventKeyState())
			{
				keyBuffer.add(keyOrd);
				// System.out.println("ADDED NEW INPUT " + keyOrd.Key);
			}

			else
			{
				if (!keyBuffer.isEmpty())
				{
					for (InputOrder P : keyBuffer)
					{
						if (P.Key == keyOrd.Key)
						{
							keyBuffer.remove(P);
							ProcessInput(InputType.RELEASE);
							// System.out.println("REMOVED NEW INPUT " + P.Key);
						}
					}

				}
			}
		}
	}

	private void ReadActionInput()
	{
		InputType In = InputType.RELEASE; // Input to Process

		CT += Game.GetInstance().Delta(); // Current Time

		// Checks to see if another action can be processed //
		if (CT > TT)
		{
			if (Keyboard.isKeyDown(Commands.INTERACT))
			{
				In = InputType.INTERACT;
				CT = 0;
			} else if (Keyboard.isKeyDown(Commands.MAP))
			{
				ProcessInput(InputType.MAP);
				CT = 0;
			} else if (Keyboard.isKeyDown(Commands.CONSOLE))
			{
				In = InputType.CONSOLE;
				CT = 0;
			} else if (Keyboard.isKeyDown(Commands.RETURN))
			{
				In = InputType.ENTER;
				CT = 0;
			} else if (Keyboard.isKeyDown(Commands.BACK))
			{
				In = InputType.BACK;
				CT = 0;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			{
				In = InputType.ESCAPE;
				CT = 0;
			}
			
			ProcessInput(In);
		}
	}
	
	public static specifier.Vector2D getMousePosition()
	{
		return new specifier.Vector2D(Mouse.getX(), Mouse.getY());
	}
}
