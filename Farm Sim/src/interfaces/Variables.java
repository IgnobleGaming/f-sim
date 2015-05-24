package interfaces;

import java.util.HashMap;

@SuppressWarnings({ "rawtypes", "unchecked" })
/**
 * Container used to handle all ingame variables
 * @author Michael
 *
 */
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
	
	/**
	 * Set/Add a given variable value
	 * @param Var ( templated variable object )
	 */
	public void Set(object.Variable Var)
	{
		GameVars.put(Var.Name(), Var);
	}
	
	/**
	 * Retrieve an variable object
	 * @param Key ( name of variable )
	 * @return if variable exists, returns the whole object
	 */
	public object.Variable Get(String Key)
	{
		return GameVars.get(Key);
	}
}
