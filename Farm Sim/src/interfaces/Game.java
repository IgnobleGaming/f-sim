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

import game.Controller;
import game.Mapbuilder;
import game.Tile.Flag;

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
	private object.Entity PlayerEnt;
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
		Mapbuilder MB = game.Mapbuilder.GetInstance();
		
		MB.Build();
		
		GameObjects.Add(Map);
		
		GameCamera = Camera.getInstance(Output.Width(), Output.Height(), 1);
		
		MaterialFile TextMat = new MaterialFile("resources\\hud\\fonts\\consolas_huge.png", MaterialFile.Type.PNG);
		TextMat.Open();
		Files.Add(TextMat);
		
		
		Con = renderable.Console.GetInstance();
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
		
		
		if (Files.Retrieve(test.getHash()) instanceof TextFile)
		{
			TextFile test2 = (TextFile)Files.Retrieve(test.getHash());
			Log.Write(Logging.Type.INFO, "Sample Text File Contains: %s", test2.getText());
		}
		
		/*************************************************************************************************
		 *****************************************Tile Image Resources************************************
		 ************************************************************************************************/
		
		
		
		/***************************************************************************************************/
		
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
		object.Entity player = new object.Entity("Player", "Main Character", Renderable.GetPosFromLocation(Position.CENTERCENTER, PositionType.ABSOLUTE, 16, 16, 0, 0, null), new specifier.Vector(), 32, 32, object.Entity.Flag.VISIBLE);		
		
		player.SetSprite(playersprite);		
		
		MaterialFile playeranim_0 = new MaterialFile("resources\\player_stationary_0.png", MaterialFile.Type.PNG);
		playeranim_0.Open();
		
		MaterialFile playeranim_1 = new MaterialFile("resources\\player_stationary_1.png", MaterialFile.Type.PNG);
		playeranim_1.Open();
		
		MaterialFile playeranim_2 = new MaterialFile("resources\\player_stationary_2.png", MaterialFile.Type.PNG);
		playeranim_2.Open();
		
		player.AddAnimation(object.Entity.State.STATIONARY, 550, playeranim_0, playeranim_1, playeranim_2);
		
		MaterialFile playerwalkside_0 = new MaterialFile("resources\\player_walkside_0.png", MaterialFile.Type.PNG);
		playerwalkside_0.Open();
		
		MaterialFile playerwalkside_1 = new MaterialFile("resources\\player_walkside_1.png", MaterialFile.Type.PNG);
		playerwalkside_1.Open();
		
		MaterialFile playerwalkside_2 = new MaterialFile("resources\\player_walkside_2.png", MaterialFile.Type.PNG);
		playerwalkside_2.Open();
		
		player.AddAnimation(object.Entity.State.MOVINGRIGHT, 450, playerwalkside_0.SetOrientation(Orientation.DOWN), playerwalkside_1.SetOrientation(Orientation.DOWN), playerwalkside_2.SetOrientation(Orientation.DOWN), playerwalkside_1.SetOrientation(Orientation.DOWN));

		/******************************************************************************************************/
		
		PlayerEnt = player;
		PlayerEnt.MovementSpeed(1);
		GameObjects.Add(PlayerEnt);
		GameObjects.Add(cpu);
		CurrentState = State.INGAME;
	}
	

	/**
	 * Load up all the in-game variables -- may need to be moved
	 */
	private void InitializeVariables()
	{
		GameVariables.Set(new object.Variable("c_maxinputqueue", "how many inputs can be queue before rejecting", 10, 1, 100, object.Variable.Flag.Modifiable));
		
		GameVariables.Set(new object.Variable("g_cheats", "cheats enabled", false, object.Variable.Flag.Developer));
		GameVariables.Set(new object.Variable("g_developer", "developer mode enabled", false, object.Variable.Flag.Developer));
		GameVariables.Set(new object.Variable("g_debuginfo", "draw debug information on screen", false, object.Variable.Flag.Developer));
		
		GameVariables.Set(new object.Variable("fs_cwd", "base path for the file system", System.getProperty("user.dir"), object.Variable.Flag.ReadOnly));
		GameVariables.Set(new object.Variable("fs_logfile", "name of the log file", "console.log", object.Variable.Flag.Latched));
		
		GameVariables.Set(new object.Variable("vid_width", "horizontal screen resolution", 1280, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("vid_height", "vertical screen resolution", 720, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("vid_vsync", "vertical sync enabled", false, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("vid_maxfps", "max user frame rate", 60, 1, 1000, object.Variable.Flag.Configuration));
		
		GameVariables.Set(new object.Variable("m_width", "maximum width (in tiles ) of map", 512, 1, 1000, object.Variable.Flag.Latched));
		GameVariables.Set(new object.Variable("m_height", "maximum height (in tiles ) of map", 512, 1, 1000, object.Variable.Flag.Latched));	
		GameVariables.Set(new object.Variable("m_tilesize", "the size ( in pixels ) of each tile", 32, 16, 128, object.Variable.Flag.Latched));
		
		GameCommands.GetInstance().Add(new object.Command("quit", "quit the game", GameCommands.CommandFunction.quit));
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
			if (R instanceof object.Entity)
			{
				((object.Entity) R).Update();
			}
		}
		GameCamera.Update();
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

