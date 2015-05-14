package interfaces;

import java.util.ArrayList;

import renderable.Renderable;

public class Objects
{
	private ArrayList<Renderable> Objs;
	
	private static Objects Instance;
	
	private Objects()
	{
		Objs = new ArrayList<Renderable>();
	}
	
	public static Objects GetInstance()
	{
		if (Instance == null)
			Instance = new Objects();
		
		return Instance;
	}
	
	public Renderable Get(int index)
	{
		return Objs.get(index);
	}
	
	public boolean Add(Renderable Obj)
	{
		Objs.add(Obj);
		return true;
	}
	
	public ArrayList<Renderable> Objs()
	{
		return Objs;
	}
}