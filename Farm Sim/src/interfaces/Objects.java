package interfaces;

import java.util.ArrayList;

import renderable.Renderable;
/**
 * Container for renderable game objects ( used by game )
 * @author Michael
 *
 */
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
	
	public boolean Remove(Renderable Obj)
	{
		return Objs.remove(Obj);
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
	
	public ArrayList<object.Entity> Entities()
	{
		ArrayList<object.Entity> entityList = new ArrayList<object.Entity>();
		for (Renderable R : Objs)
		{
			if (R.getClass().isInstance(object.Entity.class))
				entityList.add((object.Entity)R);
		}
		return entityList;
	}
}
