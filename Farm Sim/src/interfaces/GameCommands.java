package interfaces;

import java.util.HashMap;

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
	
	public boolean Execute(String CMDName, String... Arguments)
	{
		// we need to test for the variable first
		if (Variables.GetInstance().Get(CMDName) != null)
		{
			object.Variable R = Variables.GetInstance().Get(CMDName);
			if (Arguments.length > 0) // they want to set the variable
			{
				R.Current(Arguments[0]);
				return true;
			}
			
			else
			{
				Console.GetInstance().WriteLine(R.StringInfo());
				return true;
			}
		}
		
		if (Commands.get(CMDName) != null)
			return ExecuteCMD(Commands.get(CMDName).Function());
		
		Console.GetInstance().WriteLine("Unknown Command");
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
