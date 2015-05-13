package interfaces.file;

import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class Logging extends interfaces.Game
{
	public enum Type
	{
		DEBUG, INFO, WARNING, ERROR, OUT
	};
	
	private PrintWriter LogHandle;
	
	public Logging()
	{
		String LogPath = (String)super.Variables.get("fs_cwd").Current() + "\\" + (String)super.Variables.get("fs_logfile").Current();
		
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
		if (LogHandle != null) // if it hasn't been initialized!
		{
			DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			String dateFormatted = formatter.format(GameTime);
			Message = String.format("%s [%s] - %s", dateFormatted, MessageType.toString(), Message);
			LogHandle.println(String.format(Message, args));
		}
	}

}
