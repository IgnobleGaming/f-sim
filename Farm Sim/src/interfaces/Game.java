package interfaces;

import interfaces.file.FileManager;
import interfaces.file.Logging;
import interfaces.file.types.TextFile;

import java.util.*;

import renderable.*;
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
		INTRO, MAIN_MENU, GAME, INVENTORY, SETTINGS
	};

	private Game()
	{
		Date = Calendar.getInstance();
		GameTime = Date.getTimeInMillis();		
		Log = Logging.getInstance("sim_console.log", Logging.Level.DEBUG);	
	}
	
	private void Init()
	{
		Log.Write(Logging.Type.INFO, "GAME Initialization ====== %d", GameTime);
		
		GameVariables = Variables.GetInstance();
		InitializeVariables();
		Con = renderable.Console.GetInstance();
		
		Output = new Render();	
		GameObjects = Objects.GetInstance();
		Files = FileManager.getInstance();
		Input = new game.Controller();

		
		CurrentState = State.INTRO;
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
		
		renderable.HUD.GetInstance().Init();
		GameObjects.Add(renderable.HUD.GetInstance());
		
		interfaces.file.types.MaterialFile playersprite = new interfaces.file.types.MaterialFile("resources\\player.png", interfaces.file.types.MaterialFile.Type.PNG);
		playersprite.Open();
		playersprite.Scale((float).1);
		object.Entity player = new object.Entity("Player", "Main Character", new specifier.Vector2D(0,0), new specifier.Vector(), object.Entity.Flag.VISIBLE);
		player.AddSprites(playersprite);		
		
		//renderable.GUIFont testFont = new renderable.GUIFont("Segoe UI", "This is test", GUIFont.Size.HUGE, org.newdawn.slick.Color.blue, 250, 50);
		//GameObjects.Add(testFont);
		
		//renderable.GUIFont testFont2 = new renderable.GUIFont("Segoe UI", "This is test2", GUIFont.Size.HUGE, org.newdawn.slick.Color.red, 300, 300);
		//GameObjects.Add(testFont2);
		
		//renderable.GUIFont testFont3 = new renderable.GUIFont("Segoe UI", "This is test3", GUIFont.Size.HUGE, org.newdawn.slick.Color.green, 588, 500);
		//GameObjects.Add(testFont3);

		PlayerEnt = player;	
		GameObjects.Add(PlayerEnt);

	}
	

	private void InitializeVariables()
	{
		GameVariables.Set(new object.Variable("c_maxinputqueue", "how many inputs can be queue before rejecting", 10, 1, 100, object.Variable.Flag.Modifiable));
		GameVariables.Set(new object.Variable("g_cheats", "cheats enabled", false, object.Variable.Flag.Developer));
		GameVariables.Set(new object.Variable("g_developer", "developer mode enabled", false, object.Variable.Flag.Developer));
		GameVariables.Set(new object.Variable("fs_cwd", "base path for the file system", System.getProperty("user.dir"), object.Variable.Flag.ReadOnly));
		GameVariables.Set(new object.Variable("fs_logfile", "name of the log file", "console.log", object.Variable.Flag.Latched));
		
		GameVariables.Set(new object.Variable("vid_width", "horizontal screen resolution", 1280, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("vid_height", "vertical screen resolution", 720, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("vid_vsync", "vertical sync enabled", true, object.Variable.Flag.Configuration));
		GameVariables.Set(new object.Variable("vid_maxfps", "max user frame rate", 60, object.Variable.Flag.Configuration));
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
	
	public State CurrentState()
	{
		return CurrentState;
	}
	
	public Console Con()
	{
		return Con;
	}
	
	public object.Entity Controllable()
	{
		return PlayerEnt;
	}
}
