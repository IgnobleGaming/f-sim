package interfaces;

import interfaces.file.FileManager;
import interfaces.file.Logging;
import interfaces.file.types.*;
import specifier.*;
import renderable.*;
import renderable.Renderable.Position;
import renderable.Renderable.PositionType;

import java.util.*;

import game.Controller;

@SuppressWarnings({ "rawtypes", "unchecked" })

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

	
	public int SelectedEnt = 0;
	private static Game Instance;
	
	public enum State
	{
		LOADING, MENU, INGAME
	};

	private Game()
	{
		Date = Calendar.getInstance();
		GameTime = Date.getTimeInMillis();		
		Log = Logging.getInstance("sim_console.log", Logging.Level.DEBUG);	
	}
	
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
		
		MaterialFile TileSprite = new MaterialFile("resources\\ingame\\tiles\\grass_flower.png", MaterialFile.Type.PNG);
		TileSprite.Open();
		Files.Add(TileSprite);
		
		MaterialFile TileSprite2 = new MaterialFile("resources\\ingame\\tiles\\grass_dirt.png", MaterialFile.Type.PNG);
		TileSprite2.Open();
		Files.Add(TileSprite2);
		
		MaterialFile TileSprite3 = new MaterialFile("resources\\ingame\\tiles\\grass_stone.png", MaterialFile.Type.PNG);
		TileSprite3.Open();
		Files.Add(TileSprite3);
		
		MaterialFile TileSprite4 = new MaterialFile("resources\\ingame\\tiles\\grass_water.png", MaterialFile.Type.PNG);
		TileSprite4.Open();
		Files.Add(TileSprite4);
		
		game.Map GameMap = new game.Map();
		GameMap.Load();
		
		GameObjects.Add(GameMap);
		
		HUD.GetInstance().Init();
		HUD.GetInstance().ZIndex(1000);
		GameObjects.Add(renderable.HUD.GetInstance());
		
		MaterialFile playersprite = new MaterialFile("resources\\player.png", MaterialFile.Type.PNG);
		playersprite.Open();
		object.Entity player = new object.Entity("Player", "Main Character", Renderable.GetPosFromLocation(Position.CENTERCENTER, PositionType.ABSOLUTE, 32, 32, 0, 0, null), new specifier.Vector(), 32, 32, object.Entity.Flag.VISIBLE);
		
		player.AddSprites(playersprite);		

		PlayerEnt = player;	
		PlayerEnt.MovementSpeed(5);
		GameObjects.Add(PlayerEnt);
		
		CurrentState = State.INGAME;
	}
	

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
		
		GameVariables.Set(new object.Variable("m_maxwidth", "maximum width (in tiles ) of map", 40, 1, 1000, object.Variable.Flag.Latched));
		GameVariables.Set(new object.Variable("m_maxheight", "maximum height (in tiles ) of map", 40, 1, 1000, object.Variable.Flag.Latched));
		
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
	
	public void UpdateWorld()
	{
		for (renderable.Renderable R : GameObjects.Objs())
			if (R instanceof object.Entity)
				((object.Entity) R).Update();
	}
	
	public State State()
	{
		return CurrentState;
	}
	
	public void State(State S)
	{
		CurrentState = S;
	}
}
