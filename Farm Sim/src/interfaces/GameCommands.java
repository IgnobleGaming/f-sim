package interfaces;

import interfaces.file.Logging.Type;

import java.util.HashMap;
import java.util.ArrayList;

import renderable.Console;

public class GameCommands
{
	private HashMap<String, object.Command> Commands;
	private static GameCommands Instance;
	
	public enum CommandFunction
	{
		quit, map
	}
	
	private GameCommands()
	{
		Commands = new HashMap<String, object.Command>();
	}
	
	public void Add(object.Command NewCommand)
	{
		Commands.put(NewCommand.Name(), NewCommand);
	}
		
	public static GameCommands GetInstance()
	{
		if (Instance == null)
			Instance = new GameCommands();
		
		return Instance;
	}
	
	public boolean Execute(String CMDName, ArrayList<String> Arguments)
	{
		// we need to test for the variable first
		if (Variables.GetInstance().Get(CMDName) != null)
		{
			object.Variable R = Variables.GetInstance().Get(CMDName);
			if (Arguments.size() > 0) // they want to set the variable
			{
				R.Current(Arguments.get(0));
				return true;
			}
			
			else
			{
				Console.GetInstance().WriteLine(Type.INFO, R.StringInfo());
				return true;
			}
		}
		
		if (Commands.get(CMDName) != null)
			return ExecuteCMD(Commands.get(CMDName).Function());
		
		Console.GetInstance().WriteLine(Type.WARNING, "Unknown Command " + "\"" + CMDName + "\"");
		return false;
	}
	
	private boolean ExecuteCMD(CommandFunction F)
	{
		switch (F)
		{
			case quit:
				System.exit(0);
				return true;
		}
		
		return false;
	}
}
