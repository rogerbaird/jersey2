package cshu271.datastore;

public class AccessDeniedException extends Exception
{

	/**
	 * Creates a new instance of <code>AccessDeniedException</code> without
	 * detail message.
	 */
	public AccessDeniedException()
	{
	}

	/**
	 * Constructs an instance of <code>AccessDeniedException</code> with the
	 * specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public AccessDeniedException(String msg)
	{
		super(msg);
	}
}
