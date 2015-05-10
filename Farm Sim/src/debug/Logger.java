package debug;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss");
	private Date date = new Date();
	private PrintWriter logger;
	private boolean debug;

	public Logger(boolean debug) {

		this.debug = debug;

		try {
			logger = new PrintWriter(dateFormat.format(date) + ".txt");
		} catch (FileNotFoundException e) {
			System.out.println("Fuck, we can't even start a logger.");
			e.printStackTrace();
		}
	}

	public void log(LogType type, String der_class, String str) {
		if (debug) {
			if (str != null)
				switch (type) {
				case CRITICAL:
					logger.println("Critical: " + der_class + ": " + str);
					System.out.println("Critical error, check log");
					break;
				case ERROR:
					logger.println("Error: " + der_class + ": " + str);
					break;
				case WARNING:
					logger.println("Warning: " + der_class + ": " + str);
					break;
				case INFO:
					logger.println("INFO: " + der_class + ": " + str);
					break;
				}
			else
				logger.println(der_class
						+ ": null string passed into log.  Log-Type = " + type);
		}
	}
}
