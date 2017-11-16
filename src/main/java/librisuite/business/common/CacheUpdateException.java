/*
 * (c) LibriCore
 * 
 * Created on Aug 25, 2004
 * 
 * CacheUpdateException.java
 */
package librisuite.business.common;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/08/25 14:03:58 $
 * @since 1.0
 * MIKE: updated to manage strings
 */
public class CacheUpdateException extends DataAccessException {

	public CacheUpdateException() {
		super();
	}

	public CacheUpdateException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheUpdateException(String message) {
		super(message);
	}

	public CacheUpdateException(Throwable cause) {
		super(cause);
	}

}
