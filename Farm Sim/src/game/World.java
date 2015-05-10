package game;

import debug.LogType;
import debug.Logger;

public class World {

	private Logger log;
	
	public World(Logger logger) {
		this.log = logger;
	}
	
	public void logReport(LogType type, String class_str, String message) {
		log.log(type, class_str, message);
	}
}
