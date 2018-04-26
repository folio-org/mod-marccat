package org.folio.cataloging.business.digital;

import org.folio.cataloging.exception.ModCatalogingException;

import java.util.ArrayList;
import java.util.List;

public class RequiredFieldsException extends ModCatalogingException {

	private static final long serialVersionUID = 250170570458564169L;

	private List errorMessages = new ArrayList();
	
	public List getErrorMessages(){
		return errorMessages;
	}
	
	public RequiredFieldsException() {
		super();
	}
	
	public RequiredFieldsException(List errorMessages) {
		super();
		this.errorMessages = errorMessages;
	}
	
	public RequiredFieldsException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequiredFieldsException(String message) {
		super(message);
	}

	public RequiredFieldsException(Throwable cause) {
		super(cause);
	}

}
