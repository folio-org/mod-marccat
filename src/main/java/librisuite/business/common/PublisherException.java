package librisuite.business.common;

public class PublisherException extends Exception
{
	private static final long serialVersionUID = -8207968816842862351L;

	public PublisherException() {
		super();
	}

	public PublisherException(String message, Throwable cause) {
		super(message, cause);
	}

	public PublisherException(String message) {
		super(message);
	}

	public PublisherException(Throwable cause) {
		super(cause);
	}
}
