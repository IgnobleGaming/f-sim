package interfaces;

import interfaces.file.FileManager;
import interfaces.file.Logging;

import java.util.*;

import game.Controller;

@SuppressWarnings({ "rawtypes", "unchecked" })

public class Game
{
	public Logging Log;
	public Map<String, object.Variable> Variables;
	public List<Object> Objects;
	public FileManager Files;
	public Controller Input;
	public Calendar Date;
	public long GameTime;

	public void Init()
	{
		Date = Calendar.getInstance();
		GameTime = Date.getTimeInMillis();
		Variables = new HashMap<String, object.Variable>();
		Log = new Logging();
		Objects = new ArrayList<Object>();
		Files = new FileManager();
		Input = new game.Controller();
		
		InitializeVariables();
	}
	

	private void InitializeVariables()
	{
		Variables.put("c_maxinputqueue", new object.Variable("c_maxinputqueue", "how many inputs can be queue before rejecting", 10, 1, 100, object.Variable.Flag.Modifiable));
		Variables.put("g_cheats", new object.Variable("g_cheats", "cheats enabled", false, object.Variable.Flag.Developer));
		Variables.put("g_developer", new object.Variable("g_developer", "developer mode enabled", false, object.Variable.Flag.Developer));
		Variables.put("fs_cwd", new object.Variable("fs_cwd", "base path for the file system", System.getProperty("user.dir"), object.Variable.Flag.ReadOnly));
		Variables.put("fs_logfile", new object.Variable("fs_logfile", "name of the log file", "console.log", object.Variable.Flag.Latched));
	}
}
