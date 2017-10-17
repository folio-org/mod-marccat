package librisuite.business.descriptor;

import librisuite.business.exception.LibrisuiteException;
/**
 * Raised when a new heading is created whose sortform is identical
 * to another heading in a different view.  The user would normally
 * create a version of the matched heading in their own view rather
 * than create an entirely different heading.
 * @author paul
 *
 */
public class MatchedHeadingInAnotherViewException extends LibrisuiteException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
