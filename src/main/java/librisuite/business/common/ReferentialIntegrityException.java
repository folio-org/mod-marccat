/*
 * (c) LibriCore
 * 
 * Created on Dec 3, 2004
 * 
 * ReferentialIntegrityException.java
 */
package librisuite.business.common;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/12/07 10:01:49 $
 * @since 1.0
 */
public class ReferentialIntegrityException extends DataAccessException {

	private String fromTable;
	private String toTable;
	
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public ReferentialIntegrityException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 * @since 1.0
	 */
	public ReferentialIntegrityException(String message) {
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
	public ReferentialIntegrityException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param cause
	 * @since 1.0
	 */
	public ReferentialIntegrityException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param fromTable the type holding the reference
	 * @param toTable the type being deleted
	 * @since 1.0
	 */
	public ReferentialIntegrityException(String fromTable, String toTable) {
		super();
		setFromTable(fromTable);
		setToTable(toTable);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getFromTable() {
		return fromTable;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getToTable() {
		return toTable;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setFromTable(String string) {
		fromTable = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setToTable(String string) {
		toTable = string;
	}

}
