package interfaces;

import interfaces.file.FileManager;
import interfaces.file.Logging;
import interfaces.file.types.TextFile;

import java.util.*;

import debug.LogType;
import game.Controller;

@SuppressWarnings({ "rawtypes", "unchecked" })

public class Game
{
	private Logging Log;
	private Map<String, object.Variable> Variables;
	private List<Object> Objects;
	private FileManager Files;
	private Controller Input;
	private Calendar Date;
	private long GameTime;

	public Game()
	{
		Date = Calendar.getInstance();
		GameTime = Date.getTimeInMillis();		
		Log = Logging.getInstance("sim_console.log", Logging.Level.DEBUG);	
	}
	
	public void Init()
	{
		Log.Write(Logging.Type.INFO, "GAME Initialization ====== %d", GameTime);
		
		Objects = new ArrayList<Object>();
		Files = FileManager.getInstance();
		Input = new game.Controller();
		
		Variables = new HashMap<String, object.Variable>();
		InitializeVariables();
		
		TextFile test = new TextFile("textfile.txt", false, false);
		test.Open();
		Files.Add(test);
		
		
		if (Files.Retrieve(test.getHash()) instanceof TextFile)
		{
			TextFile test2 = (TextFile)Files.Retrieve(test.getHash());
			Log.Write(Logging.Type.INFO, "Sample Text File Contains: %s", test2.getText());
		}
	}
	

	private void InitializeVariables()
	{
		Variables.put("c_maxinputqueue", new object.Variable("c_maxinputqueue", "how many inputs can be queue before rejecting", 10, 1, 100, object.Variable.Flag.Modifiable));
		Variables.put("g_cheats", new object.Variable("g_cheats", "cheats enabled", false, object.Variable.Flag.Developer));
		Variables.put("g_developer", new object.Variable("g_developer", "developer mode enabled", false, object.Variable.Flag.Developer));
		Variables.put("fs_cwd", new object.Variable("fs_cwd", "base path for the file system", System.getProperty("user.dir"), object.Variable.Flag.ReadOnly));
		Variables.put("fs_logfile", new object.Variable("fs_logfile", "name of the log file", "console.log", object.Variable.Flag.Latched));
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
	
	public Map<String, object.Variable> Variables()
	{
		return Variables;
	}
}
