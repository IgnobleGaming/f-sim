package interfaces.file;

import interfaces.Game;

import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.io.Console;
import java.util.concurrent.ArrayBlockingQueue;
public class Logging
{
	public enum Type
	{
		DEBUG, INFO, WARNING, ERROR, OUT
	};
	
	public enum Level
	{
		DEBUG, ALL, PRODUCTION
	};
	
	private PrintWriter LogHandle;
	private String LogPath;
	private Level LogLevel;
	private ArrayBlockingQueue<String> LogQueue;
	
	private static Logging Log;

	private Logging(String LogPath, Level LogLevel)
	{	
		this.LogPath = LogPath;
		this.LogLevel = LogLevel;
				
		try
		{
			LogHandle = new PrintWriter(LogPath);
		}
		
		catch (FileNotFoundException E)
		{
			System.out.println("Couldn't create log file!");
		}		
	}
	
	
	public void Write (Type MessageType, String Message, Object... args)
	{
		if (LogHandle != null && start.Main.GameObject != null) // if it hasn't been initialized!
		{
			DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			String dateFormatted = formatter.format(start.Main.GameObject.GameTime());
			String ConMessage = String.format(" [%s]     %s", MessageType.toString(), Message);
			Message = String.format("%s [%s] - %s", dateFormatted, MessageType.toString(), Message);
			Game.GetInstance().Con().WriteLine(String.format(ConMessage, args));
			LogHandle.println(String.format(Message, args));
			LogHandle.flush();
		}
	}
	
	public static Logging getInstance(String LogPath, Level LogLevel)
	{
		if (Log == null)
			Log = new Logging(LogPath, LogLevel);
		return Log;
	}
	
	public static Logging getInstance()
	{
		if (Log == null)
			Log = new Logging("console.log", Level.DEBUG);
		return Log;
	}
}
