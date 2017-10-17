package librisuite.business.common;

public class EditorExistException extends Exception
{
	private static final long serialVersionUID = -8207968816842862351L;

	public EditorExistException() {
		super();
	}

	public EditorExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public EditorExistException(String message) {
		super(message);
	}

	public EditorExistException(Throwable cause) {
		super(cause);
	}
}
