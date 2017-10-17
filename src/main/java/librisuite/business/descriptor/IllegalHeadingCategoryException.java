/*
 * Created on Nov 17, 2004
 */
package librisuite.business.descriptor;

/**
 * @author janick
 */
public class IllegalHeadingCategoryException extends Exception {

	/**
	 * 
	 */
	public IllegalHeadingCategoryException() {
		super();
	}

	/**
	 * @param arg0
	 */
	public IllegalHeadingCategoryException(String arg0) {
		super(arg0);

	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public IllegalHeadingCategoryException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public IllegalHeadingCategoryException(Throwable arg0) {
		super(arg0);
	}

}
