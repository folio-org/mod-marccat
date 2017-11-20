package org.folio.cataloging.business.digital;

import org.folio.cataloging.exception.LibrisuiteException;

public class DirectoryCreationException extends LibrisuiteException 
{
	private static final long serialVersionUID = -3435860527808312569L;

	public DirectoryCreationException() {
		super();
	}
	
	public DirectoryCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public DirectoryCreationException(String message) {
		super(message);
	}

	public DirectoryCreationException(Throwable cause) {
		super(cause);
	}

}
