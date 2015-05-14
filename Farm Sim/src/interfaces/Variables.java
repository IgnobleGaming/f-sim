package interfaces;

import java.util.HashMap;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Variables
{
	private static Variables Instance;
	private HashMap<String, object.Variable> GameVars;
	
	private Variables()
	{
		GameVars = new HashMap<String, object.Variable>();
	}
	
	public static Variables GetInstance()
	{
		if (Instance == null)
			Instance = new Variables();
		return Instance;
	}
	
	public void Set(object.Variable Var)
	{
		GameVars.put(Var.Name(), Var);
	}
	
	public object.Variable Get(String Key)
	{
		return GameVars.get(Key);
	}
}
