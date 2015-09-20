package interfaces;

import interfaces.file.FileManager;
import interfaces.file.Logging;
import interfaces.file.types.*;
import interfaces.file.types.MaterialFile.Orientation;
import specifier.*;
import renderable.*;
import renderable.Renderable.Position;
import renderable.Renderable.PositionType;
import interfaces.Camera;
import game.Map;

import java.util.*;

import org.lwjgl.input.Keyboard;

import object.actors.Actor;
import game.Controller;
import game.Mapbuilder;
import game.Tile;

@SuppressWarnings({ "rawtypes", "unchecked" })
/**
 * Main container for all game functions
 * @author Michael
 *
 */
public class Game
{
	private Logging Log;
	private Render Output;
	private Variables GameVariables;
	private FileManager Files;
	private Controller Input;
	private renderable.Console Con;
	private Calendar Date;
	private long GameTime, LastFrame = 0;
	private int Delta = 0;
	private State CurrentState;
	private Objects GameObjects;
	public boolean IsRunning = true;
	private Actor PlayerEnt;
	private Map Map;
	private Camera GameCamera;

	
	public int SelectedEnt = 0;
	private static Game Instance;
	
	public enum State
	{
		LOADING, MENU, INGAME
	};

	/**
	 * Initialize the crucial variables and log
	 */
	private Game()
	{
		Date = Calendar.getInstance();
		GameTime = Date.getTimeInMillis();		
		Log = Logging.getInstance("sim_console.log", Logging.Level.DEBUG);	
	}
	
	/**
	 * Start up the game instance and initialize all the singleton components for the first time
	 */
	private void Init()
	{		
		CurrentState = State.LOADING;
		Log.Write(Logging.Type.INFO, "GAME Initialization ====== %d", GameTime);
		
		GameVariables = Variables.GetInstance();
		InitializeVariables();
		
		GameObjects = Objects.GetInstance();
		
		Output = Render.GetInstance();	
		Files = FileManager.getInstance();
		Input = new game.Controller();
		
		Map = game.Map.GetInstance();
		
		Mapbuilder.MapGeneration(Map);

		//GenerateStats();
		
		GameObjects.Add(Map);
		
		GameCamera = Camera.getInstance(Output.Width(), Output.Height(), 1);
		
		//MaterialFile TextMat = new MaterialFile("resources\\hud\\fonts\\consolas_huge.png", MaterialFile.Type.PNG);
		//TextMat.Open();
		//Files.Add(TextMat);
		
		
		Con = renderable.Console.GetInstance();
	}
	
	private void GenerateStats()
	{
		class Stats
		{
			public int max = 0;
			public int min = 0;
			public int avg = 0;
			
			public int [] numbers = new int [100];
			
			public Stats()
			{
				max = 0;
				min = 0;
				avg = 0;
			}
			
			public void AVG()
			{
				for (int a : numbers)
				{
					avg += a;
				}
				
				avg = avg / numbers.length;
			}
		}
		
		Stats[] Stats = new Stats[7];
		
		for (int i = 0; i < 7; i++)
		{
			Stats[i] = new Stats();
		}
		
		for (int i = 0; i < 100; i++)
		{
			System.out.println(i);
			
			Mapbuilder.MapGeneration(Map);
			
			int [] Counts = Mapbuilder.VerifyQuality();
			
			for (int j = 0; j < 7; j++)
			{
				if (i == 0)
				{
					Stats[j].max = Counts[j];
					Stats[j].min = Counts[j];
				}
				
				if (Counts[j] > Stats[j].max)
					Stats[j].max = Counts[j];
				if (Counts[j] < Stats[j].min)
					Stats[j].min = Counts[j];
				
				Stats[j].numbers[i] = Counts[j];
			}
			
			//MiniMap.writeMMImage(i + " - ", Map);;
		}
		
		int count = 0;
		
		for (int i = 0; i < 7; i++)
		{
			Stats[i].AVG();
			
			if (Stats[i].avg != 0)
			{
				System.out.println("--------   " + count + "   ---------");
				System.out.println("Min - " + Stats[i].min + " Avg - " + Stats[i].avg + " Max - " + Stats[i].max);
			}
			
			count++;
		}
		
	}
	
	public static Game GetInstance()
	{
		if (Instance == null)
		{
			Instance = new Game();
			Instance.Init();
		}
		return Instance;
	}
	
	/**
	 * Testing method to try out new features
	 */
	public void Testing()
	{	
		TextFile test = new TextFile("textfile.txt", false, false);
		test.Open();
		Files.Add(test);
		
		/********** MENU STUFF ************/
		
		MaterialFile default_button = new MaterialFile("resources\\ui\\buttons\\button_default.png", MaterialFile.Type.PNG);
		default_button.Open();
		Files.Add(default_button);
		
		MaterialFile default_menu = new MaterialFile("resources\\ui\\menus\\menu_default.png", MaterialFile.Type.PNG);
		default_menu.Open();
		Files.Add(default_menu);
		
		/******* END ********/
				
		if (Files.Retrieve(test.getHash()) instanceof TextFile)
		{
			TextFile test2 = (TextFile)Files.Retrieve(test.getHash());
			Log.Write(Logging.Type.INFO, "Sample Text File Contains: %s", test2.getText());
		}
		
		HUD.GetInstance().Init();
		HUD.GetInstance().ZIndex(1000);
		GameObjects.Add(renderable.HUD.GetInstance());
	
		MaterialFile cpusprite = new MaterialFile("resources\\cpu.png", MaterialFile.Type.PNG);
		cpusprite.Open();
		object.Entity cpu = new object.Entity("AltPlayer", "CPU Player", Renderable.GetPosFromLocation(Position.CENTERCENTER, PositionType.ABSOLUTE, 16, 16, 100, 100, null), new specifier.Vector(), 32, 32, object.Entity.Flag.VISIBLE);
		cpu.SetSprite(cpusprite);
		
		cpu.Move(specifier.Direction.Relative.UP);
		
		
		/****************************************************************************************************
		 ****************************************Player Image Resources**************************************
		 ***************************************************************************************************/
		MaterialFile playersprite = new MaterialFile("resources\\player.png", MaterialFile.Type.PNG);
		playersprite.Open();
		Actor player = new Actor("Player", "Main Character", new specifier.Vector2D(1024, 1024), new specifier.Vector(), 32, 32, object.Entity.Flag.VISIBLE);		
		
		player.SetSprite(playersprite);
		
		/*~~~~~~~~~~ Stationary animation ~~~~~~~~~~*/
		MaterialFile playeranim_0 = new MaterialFile("resources\\player_stationary_0.png", MaterialFile.Type.PNG);
		playeranim_0.Open();
		
		MaterialFile playeranim_1 = new MaterialFile("resources\\player_stationary_1.png", MaterialFile.Type.PNG);
		playeranim_1.Open();
		
		MaterialFile playeranim_2 = new MaterialFile("resources\\player_stationary_2.png", MaterialFile.Type.PNG);
		playeranim_2.Open();
		
		player.AddAnimation(object.Entity.State.STATIONARY, 550, playeranim_0, playeranim_1, playeranim_2);
		
		/*~~~~~~~~~~ Walking left Animation ~~~~~~~~~~*/
		MaterialFile playerwalkleft_0 = new MaterialFile("resources\\WL0.png", MaterialFile.Type.PNG);
		playerwalkleft_0.Open();
		
		MaterialFile playerwalkleft_1 = new MaterialFile("resources\\WL1.png", MaterialFile.Type.PNG);
		playerwalkleft_1.Open();
		
		MaterialFile playerwalkleft_2 = new MaterialFile("resources\\WL2.png", MaterialFile.Type.PNG);
		playerwalkleft_2.Open();
		
		MaterialFile playerwalkleft_3 = new MaterialFile("resources\\WL3.png", MaterialFile.Type.PNG);
		playerwalkleft_3.Open();
		
		MaterialFile playerwalkleft_4 = new MaterialFile("resources\\WL4.png", MaterialFile.Type.PNG);
		playerwalkleft_4.Open();
		
		MaterialFile playerwalkleft_5 = new MaterialFile("resources\\WL5.png", MaterialFile.Type.PNG);
		playerwalkleft_5.Open();
		
		player.AddAnimation(object.Entity.State.MOVINGLEFT, 550, playerwalkleft_0.SetOrientation(Orientation.DOWN), playerwalkleft_1.SetOrientation(Orientation.DOWN),
				playerwalkleft_2.SetOrientation(Orientation.DOWN), playerwalkleft_3.SetOrientation(Orientation.DOWN), playerwalkleft_4.SetOrientation(Orientation.DOWN), playerwalkleft_5.SetOrientation(Orientation.DOWN));

		/*~~~~~~~~~~ Walking right Animation ~~~~~~~~~~*/
		MaterialFile playerwalkright_0 = new MaterialFile("resources\\WR0.png", MaterialFile.Type.PNG);
		playerwalkright_0.Open();
		
		MaterialFile playerwalkright_1 = new MaterialFile("resources\\WR1.png", MaterialFile.Type.PNG);
		playerwalkright_1.Open();
		
		MaterialFile playerwalkright_2 = new MaterialFile("resources\\WR2.png", MaterialFile.Type.PNG);
		playerwalkright_2.Open();
		
		MaterialFile playerwalkright_3 = new MaterialFile("resources\\WR3.png", MaterialFile.Type.PNG);
		playerwalkright_3.Open();
		
		MaterialFile playerwalkright_4 = new MaterialFile("resources\\WR4.png", MaterialFile.Type.PNG);
		playerwalkright_4.Open();
		
		MaterialFile playerwalkright_5 = new MaterialFile("resources\\WR5.png", MaterialFile.Type.PNG);
		playerwalkright_5.Open();
		
		player.AddAnimation(object.Entity.State.MOVINGRIGHT, 450, playerwalkright_0.SetOrientation(Orientation.DOWN), playerwalkright_1.SetOrientation(Orientation.DOWN),
				playerwalkright_2.SetOrientation(Orientation.DOWN), playerwalkright_3.SetOrientation(Orientation.DOWN), playerwalkright_4.SetOrientation(Orientation.DOWN), playerwalkright_5.SetOrientation(Orientation.DOWN));

		/*~~~~~~~~~~ Walking up Animation ~~~~~~~~~~*/
		MaterialFile playerwalkup_0 = new MaterialFile("resources\\player_walkup_0.png", MaterialFile.Type.PNG);
		playerwalkup_0.Open();
		
		MaterialFile playerwalkup_1 = new MaterialFile("resources\\player_walkup_1.png", MaterialFile.Type.PNG);
		playerwalkup_1.Open();
		
		MaterialFile playerwalkup_2 = new MaterialFile("resources\\player_walkup_2.png", MaterialFile.Type.PNG);
		playerwalkup_2.Open();
		
		player.AddAnimation(object.Entity.State.MOVINGUP, 450, playerwalkup_0.SetOrientation(Orientation.DOWN), playerwalkup_1.SetOrientation(Orientation.DOWN), playerwalkup_2.SetOrientation(Orientation.DOWN), playerwalkup_1.SetOrientation(Orientation.DOWN));

		/*~~~~~~~~~~ Walking down Animation ~~~~~~~~~~*/
		MaterialFile playerwalkdown_0 = new MaterialFile("resources\\player_walkdown_0.png", MaterialFile.Type.PNG);
		playerwalkdown_0.Open();
		
		MaterialFile playerwalkdown_1 = new MaterialFile("resources\\player_walkdown_1.png", MaterialFile.Type.PNG);
		playerwalkdown_1.Open();
		
		MaterialFile playerwalkdown_2 = new MaterialFile("resources\\player_walkdown_2.png", MaterialFile.Type.PNG);
		playerwalkdown_2.Open();
		
		player.AddAnimation(object.Entity.State.MOVINGDOWN, 250, playerwalkdown_0.SetOrientation(Orientation.DOWN), playerwalkdown_1.SetOrientation(Orientation.DOWN));

		
		/******************************************************************************************************/
		
		
		
		PlayerEnt = player;
		PlayerEnt.MovementSpeed(1);
		GameObjects.Add(PlayerEnt);
		//GameObjects.Add(cpu);
		CurrentState = State.INGAME;
	}
	

	/**
	 * Load up all the in-game variables -- may need to be moved
	 */
	private void InitializeVariables()
	{
		GameVariables.Set(new object.Variable("c_maxinputqueue", "how many inputs can be queue before rejecting", 10, 1, 100, object.Variable.Flag.Modifiable));
		
		GameVariables.Set(new object.Variable("g_cheats", "cheats enabled", false, object.Variable.Flag.Developer));
		GameVariables.Set(new object.Variable("g_developer", "developer mode enabled", true, object.Variable.Flag.Developer));
		GameVariables.Set(new object.Variable("g_debuginfo", "draw debug information on screen", true, object.Variable.Flag.Developer));
		
		GameVariables.Set(new object.Variable("fs_cwd", "base path for the file system", System.getProperty("user.dir"), object.Variable.Flag.ReadOnly));
		GameVariables.Set(new object.Variable("fs_logfile", "name of the log file", "console.log", object.Variable.Flag.Latched));
		
		GameVariables.Set(new object.Variable("vid_width", "horizontal screen resolution", 1280, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("vid_height", "vertical screen resolution", 720, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("vid_vsync", "vertical sync enabled", false, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("vid_maxfps", "max user frame rate", 60, 1, 1000, object.Variable.Flag.Configuration));
		
		GameVariables.Set(new object.Variable("ctrl_UP", "Up", Keyboard.KEY_UP, Keyboard.CHAR_NONE, Keyboard.KEY_DELETE, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("ctrl_DOWN", "Down", Keyboard.KEY_DOWN, Keyboard.CHAR_NONE, Keyboard.KEY_DELETE, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("ctrl_LEFT", "Left", Keyboard.KEY_LEFT, Keyboard.CHAR_NONE, Keyboard.KEY_DELETE, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("ctrl_RIGHT", "Right", Keyboard.KEY_RIGHT, Keyboard.CHAR_NONE, Keyboard.KEY_DELETE, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("ctrl_INTERACT", "Interact", Keyboard.KEY_E, Keyboard.CHAR_NONE, Keyboard.KEY_DELETE, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("ctrl_CONSOLE", "Console", Keyboard.KEY_GRAVE, Keyboard.CHAR_NONE, Keyboard.KEY_DELETE, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("ctrl_MAP", "Map", Keyboard.KEY_M, Keyboard.CHAR_NONE, Keyboard.KEY_DELETE, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("ctrl_RETURN", "Line Return", Keyboard.KEY_RETURN, Keyboard.CHAR_NONE, Keyboard.KEY_DELETE, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("ctrl_BACK", "BACK", Keyboard.KEY_BACK, Keyboard.CHAR_NONE, Keyboard.KEY_DELETE, object.Variable.Flag.Configuration));
		
		GameVariables.Set(new object.Variable("m_width", "maximum width (in tiles ) of map", 512, 1, 1000, object.Variable.Flag.Latched));
		GameVariables.Set(new object.Variable("m_height", "maximum height (in tiles ) of map", 512, 1, 1000, object.Variable.Flag.Latched));	
		GameVariables.Set(new object.Variable("m_tilesize", "the size ( in pixels ) of each tile", 32, 16, 128, object.Variable.Flag.Latched));
		
		GameCommands.GetInstance().Add(new object.Command("quit", "quit the game", GameCommands.CommandFunction.quit));
		GameCommands.GetInstance().Add(new object.Command("help", "list all commands", GameCommands.CommandFunction.help));
	}
	
	public long GameTime()
	{
		return GameTime;
	}
	
	public int Delta()
	{
		return Delta;
	}
	
	public void UpdateGameTime()
	{
		long NewTime = System.nanoTime() / 1000000;
		Delta = (int)(NewTime - GameTime);
		GameTime = NewTime;
	}
	
	public Logging Log()
	{
		return Log;
	}
	
	public Render Output()
	{
		return Output;
	}
	
	public Controller Input()
	{
		return Input;
	}

	
	public Console Con()
	{
		return Con;
	}
	
	public object.Entity Controllable()
	{
		return PlayerEnt;
	}
	
	/**
	 * Iterates through the renderable objects in the word and updates their attributes
	 */
	public void UpdateWorld()
	{
		for (renderable.Renderable R : GameObjects.Objs())
		{
			if (R instanceof object.Entity || R instanceof game.Map)
				R.Update();
		}
		//GameCamera.Update();
	}
	
	public void updateMenus()
	{
		
	}
	
	public State State()
	{
		return CurrentState;
	}
	
	public void State(State S)
	{
		CurrentState = S;
	}
	
	public object.Entity Player()
	{
		return PlayerEnt;
	}
}

