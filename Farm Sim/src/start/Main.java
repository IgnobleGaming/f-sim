package start;

import interfaces.*;

public class Main
{
	public static Game GameObject;

	public void Init()
	{
		GameObject = Game.GetInstance();
	}

	public void Run()
	{		
		while (GameObject.IsRunning)
		{
			GameObject.UpdateGameTime(); // update our game time
			GameObject.Input().Update(); // register all inputs and process state changes
			switch (GameObject.State())
			{
				case LOADING:
					GameObject.Testing();
					break;
				case MENU:
					break;
				case INGAME:
					GameObject.UpdateWorld(); // process all the changes that need to be made (locations/states etc)
					break;
			}
			GameObject.Output().Update(); // render all renderables
			GameObject.Output().updateFPS(); // how long did this frame take?
		}
	}
	
	public static void main(String[] argv)
	{
		Main main = new Main();
		main.Init();
		main.Run();
	}
}
