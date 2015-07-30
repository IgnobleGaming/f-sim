package interfaces;

import interfaces.file.Logging.Type;

import java.util.HashMap;
import java.util.ArrayList;

import com.sun.javafx.binding.Logging;

import renderable.Console;

/**
 * Manages valid game commands and user command attempts 
 * @author Michael
 *
 */
public class GameCommands
{
	private HashMap<String, object.Command> Commands;
	private static GameCommands Instance;
	
	public enum CommandFunction
	{
		quit, map, help
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
	
	/**
	 * Attempt to execute user entered command or variable
	 * @param CMDName ( command name or variable provided by user )
	 * @param Arguments ( optional arguments provided by command or variable )
	 * @return true if command execution succeeded, false otherwise
	 */
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
	
	/**
	 * Executes a command function
	 * @param F ( function to execute )
	 * @return true if succeeded false if unhandled 
	 */
	private boolean ExecuteCMD(CommandFunction F)
	{
		switch (F)
		{
			case quit:
				System.exit(0);
				return true;
			case help:
				interfaces.file.Logging.getInstance().Write(interfaces.file.Logging.Type.OUT, "NAME------------------DESCRIPTION-------------");
			
				for(object.Command C : Commands.values())
				{
					System.out.println(C.Description());
					interfaces.file.Logging.getInstance().Write(interfaces.file.Logging.Type.OUT, "%s                  %s", C.Name(), C.Description());
				}
				interfaces.file.Logging.getInstance().Write(interfaces.file.Logging.Type.OUT, "----------------------------------------------");
				return true;
		}
		
		return false;
	}
}
