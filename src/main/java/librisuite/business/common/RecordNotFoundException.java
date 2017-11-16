/*
 * (c) LibriCore
 * 
 * Created on Oct 13, 2004
 * 
 * RecordNotFoundException.java
 */
package librisuite.business.common;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class RecordNotFoundException extends DataAccessException {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public RecordNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 * @since 1.0
	 */
	public RecordNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 * @param cause
	 * @since 1.0
	 */
	public RecordNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param cause
	 * @since 1.0
	 */
	public RecordNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
