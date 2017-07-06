package bn.blaszczyk.rose.creators;

public class CreateException extends Exception {

	private static final long serialVersionUID = 1L;

	public CreateException(final String message) 
	{
		super(message);
	}

	public CreateException(final Throwable cause)
	{
		super(cause);
	}

	public CreateException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

}
