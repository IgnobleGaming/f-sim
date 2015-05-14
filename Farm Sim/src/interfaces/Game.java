package interfaces;

import interfaces.file.FileManager;
import interfaces.file.Logging;
import interfaces.file.types.TextFile;

import java.util.*;

import renderable.*;
import debug.LogType;
import game.Controller;
import game.Render;

@SuppressWarnings({ "rawtypes", "unchecked" })

public class Game
{
	private Logging Log;
	private Render Output;
	private Map<String, object.Variable> Variables;
	private ArrayList<Renderable> Objects;
	private FileManager Files;
	private Controller Input;
	private Calendar Date;
	private long GameTime;
	private State CurrentState;
	public boolean IsRunning = true;
	
	public int SelectedEnt = 0;
	
	public enum State
	{
		INTRO, MAIN_MENU, GAME, INVENTORY, SETTINGS
	};

	public Game()
	{
		Date = Calendar.getInstance();
		GameTime = Date.getTimeInMillis();		
		Log = Logging.getInstance("sim_console.log", Logging.Level.DEBUG);	
	}
	
	public void Init()
	{
		Log.Write(Logging.Type.INFO, "GAME Initialization ====== %d", GameTime);
		
		Variables = new HashMap<String, object.Variable>();
		InitializeVariables();
		
		Output = new Render();	
		Objects = new ArrayList<Renderable>();
		Files = FileManager.getInstance();
		Input = new game.Controller();
		
		CurrentState = State.INTRO;
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
		
		renderable.GUIFont testFont = new renderable.GUIFont("Segoe UI", "This is test", GUIFont.Size.HUGE, org.newdawn.slick.Color.blue, 250, 50);
		
		Objects.add(testFont);
	}
	

	private void InitializeVariables()
	{
		Variables.put("c_maxinputqueue", new object.Variable("c_maxinputqueue", "how many inputs can be queue before rejecting", 10, 1, 100, object.Variable.Flag.Modifiable));
		Variables.put("g_cheats", new object.Variable("g_cheats", "cheats enabled", false, object.Variable.Flag.Developer));
		Variables.put("g_developer", new object.Variable("g_developer", "developer mode enabled", false, object.Variable.Flag.Developer));
		Variables.put("fs_cwd", new object.Variable("fs_cwd", "base path for the file system", System.getProperty("user.dir"), object.Variable.Flag.ReadOnly));
		Variables.put("fs_logfile", new object.Variable("fs_logfile", "name of the log file", "console.log", object.Variable.Flag.Latched));
		
		Variables.put("vid_width", new object.Variable("vid_width", "horizontal screen resolution", 1280, object.Variable.Flag.Configuration));
		Variables.put("vid_height", new object.Variable("vid_height", "vertical screen resolution", 720, object.Variable.Flag.Configuration));
		Variables.put("vid_vsync", new object.Variable("vid_vsync", "vertical sync enabled", true, object.Variable.Flag.Configuration));
		Variables.put("vid_maxfps", new object.Variable("vid_vsync", "vertical sync enabled", 60, object.Variable.Flag.Configuration));
	}
	
	public long GameTime()
	{
		return GameTime;
	}
	
	public void GameTime(long newTime)
	{
		if (newTime > GameTime)
			GameTime = newTime;
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
	
	public Map<String, object.Variable> Variables()
	{
		return Variables;
	}
	
	public State CurrentState()
	{
		return CurrentState;
	}
	
	public ArrayList<Renderable> Objects()
	{
		return Objects;
	}
}
