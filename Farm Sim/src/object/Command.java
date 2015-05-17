package object;

public class Command
{
	private String Name;
	private String Description;	
	private interfaces.GameCommands.CommandFunction Function;
	
	public Command(String N, String D, interfaces.GameCommands.CommandFunction F)
	{
		Name = N;
		Description = D;
		Function = F;
	}
	
	public String Name()
	{
		return Name;
	}
	
	public String Description()
	{
		return Description;
	}
	
	public interfaces.GameCommands.CommandFunction Function()
	{
		return Function;
	}
}
