package librisuite.business.common;

public class IsbnDigitException extends Exception
{
	private static final long serialVersionUID = -8207968816842862351L;

	public IsbnDigitException() {
		super();
	}

	public IsbnDigitException(String message, Throwable cause) {
		super(message, cause);
	}

	public IsbnDigitException(String message) {
		super(message);
	}

	public IsbnDigitException(Throwable cause) {
		super(cause);
	}
}
