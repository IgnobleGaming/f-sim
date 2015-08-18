package interfaces;

import interfaces.file.FileManager;

public class ResourceLoader
{
	public enum Components
	{
		ACTOR, WORLD, 
	}
	
	
	private static ResourceLoader Instance;
	
	private FileManager FM;
	
	public ResourceLoader()
	{
		FM = FileManager.getInstance();
	}
	
	public void Load()
	{
		
	}
	
}
