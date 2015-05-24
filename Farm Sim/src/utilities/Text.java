package utilities;

/**
 * Class used for basic string attributes
 * @author Michael
 *
 */
public class Text
{
	/**
	 * See if character is upper/lowercase or number
	 * @param c
	 * @return
	 */
	public static boolean isAlphaNumeric(char c)
	{
		if((c > 64 && c < 90) || (c > 96 && c < 122) || ( c > 47 && c < 58) || c == 95 || c == 32)
			return true;
		return false;
	}
}
