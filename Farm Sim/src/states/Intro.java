package states;

import debug.LogType;
import debug.Logger;

public class Intro
{

	private Logger log;

	public void init(Logger logger)
	{
		this.log = logger;
	}

	public int main()
	{
		return 0;
	}

	public void logReport(LogType type, String class_str, String message)
	{
		log.log(type, class_str, message);
	}
}
