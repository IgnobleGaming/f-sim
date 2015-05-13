package interfaces;

import interfaces.file.FileManager;
import interfaces.file.Logging;

import java.util.*;

import object.Object;

public class Game
{
	public Logging Log;
	public Map<String, object.Variable> Variables;
	protected List<Object> Objects;
	public FileManager Files;
	public boolean CheatsEnabled = false;

	public Game()
	{
		Log = new Logging();
		Variables = new HashMap<String, object.Variable>();
		Objects = new ArrayList<Object>();
		Files = new FileManager();
	}
}
