package org.folio.cataloging.business.common;

public class UploadFileDigitalException extends DataAccessException 
{
	private static final long serialVersionUID = -8207968816842862351L;

	public UploadFileDigitalException() {
		super();
	}

	public UploadFileDigitalException(String message, Throwable cause) {
		super(message, cause);
	}

	public UploadFileDigitalException(String message) {
		super(message);
	}

	public UploadFileDigitalException(Throwable cause) {
		super(cause);
	}
}
