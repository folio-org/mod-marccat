/*
 * Created on May 5, 2004
 *
 */
package librisuite.business.exception;


/**
 * Exception with socket connections to server processes
 * 
 * @author paulm
 *
 */
public class ConnectException extends LibrisuiteException {

	/**
	 * Class constructor
	 *
	 * @param arg0
	 * @since 1.0
	 */
	public ConnectException(String arg0) {
		super(arg0);
	}
	
	/**
	 * Class constructor
	 *
	 * @since 1.0
	 */
	public ConnectException() {
		super();
	}


}