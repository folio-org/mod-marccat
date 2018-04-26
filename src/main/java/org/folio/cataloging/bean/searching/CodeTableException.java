package org.folio.cataloging.bean.searching;

import org.folio.cataloging.exception.ModCatalogingException;

public class CodeTableException extends ModCatalogingException {
	public static final String NO_CODE 				= "errors.CodeTableException.no.code";
	public static final String CODE_ALREADY_PRESENT = "errors.CodeTableException.code.already.present";
	public static final String NO_LONG 				= "errors.CodeTableException.no.long";
	public static final String NO_SHORT 			= "errors.CodeTableException.no.short";
	public static final String WRONG_CODE_TYPE 		= "errors.CodeTableException.wrong.code.type";
	public static final String MAP_NOT_FOUND 		= "errors.CodeTableException.map.not.found";
	public static final String PROPERTY_NOT_FOUND 	= "errors.CodeTableException.property.not.found";
	public static final String DUPLICATE_CODE 	    = "errors.CodeTableException.code.duplicate";
	public static final String USE_CODE 	        = "errors.CodeTableException.code.used";
	public static final String UNDERSCORE_PRESENT   = "errors.CodeTableException.underscore.present";
	public static final String STRING_ALREADY_PRESENT = "errors.CodeTableException.string.already.present";
	
	Class clazz = null;
	private boolean isMsgKey;
	private Object arg0;
	

	public CodeTableException() {
		super();
	}

	public CodeTableException(String message, Throwable cause) {
		super(message, cause);
	}

	public CodeTableException(String message) {
		super(message);
	}
	
	public CodeTableException(String key, boolean isKey, Object arg0) {
		this(key);
		this.isMsgKey = isKey;
		this.arg0 = arg0;
	}

	public CodeTableException(Throwable cause) {
		super(cause);
	}

	public CodeTableException(Class clazz, Throwable th) {
		this(th);
		this.clazz = clazz;
	}

	public boolean isMsgKey() {
		return isMsgKey;
	}

	public void setMsgKey(boolean isMsgKey) {
		this.isMsgKey = isMsgKey;
	}
	
	public Object getArg0() {
		return arg0;
	}

	public void setArg0(Object arg0) {
		this.arg0 = arg0;
	}

}
