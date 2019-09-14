package cshu271.datastore;

public class UnavailableException extends Exception
{

	/**
	 * Creates a new instance of <code>UnavailableException</code> without
	 * detail message.
	 */
	public UnavailableException()
	{
	}

	/**
	 * Constructs an instance of <code>UnavailableException</code> with the
	 * specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public UnavailableException(String msg)
	{
		super(msg);
	}
}
