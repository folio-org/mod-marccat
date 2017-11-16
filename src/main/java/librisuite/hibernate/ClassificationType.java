/*
 * (c) LibriCore
 * 
 * Created on Jan 14, 2005
 * 
 * ClassificationType.java
 */
package librisuite.hibernate;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/01/18 15:49:24 $
 * @since 1.0
 */
public class ClassificationType extends T_SINGLE {

	public static boolean isDewey(short s) {
		return s == 12;
	}
}
