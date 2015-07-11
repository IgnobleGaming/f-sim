package utilities;

import java.util.Comparator;

/**
 * Class to compare z indexes when rendering
 * 
 * @author Michael
 *
 */
public class RenderPriorityCompare implements Comparator<renderable.Renderable>
{
	@Override
	public int compare(renderable.Renderable R1, renderable.Renderable R2)
	{
		return (R1.ZIndex() - R2.ZIndex());
	}
}
