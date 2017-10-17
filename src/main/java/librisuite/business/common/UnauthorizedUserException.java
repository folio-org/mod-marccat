package librisuite.business.common;

public class UnauthorizedUserException extends DataAccessException 
{
	private static final long serialVersionUID = -8207968816842862351L;

	public UnauthorizedUserException() {
		super();
	}

	public UnauthorizedUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnauthorizedUserException(String message) {
		super(message);
	}

	public UnauthorizedUserException(Throwable cause) {
		super(cause);
	}
}
