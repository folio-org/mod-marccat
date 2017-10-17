package librisuite.business.common;

public class OperationFailedException extends Exception
{
	private static final long serialVersionUID = -8207968816842862351L;

	public OperationFailedException() {
		super();
	}

	public OperationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public OperationFailedException(String message) {
		super(message);
	}

	public OperationFailedException(Throwable cause) {
		super(cause);
	}
}
