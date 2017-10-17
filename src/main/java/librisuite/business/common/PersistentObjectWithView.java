/*
 * (c) LibriCore
 * 
 * Created on Aug 25, 2004
 * 
 * PersistentObjectWithView.java
 */
package librisuite.business.common;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/12 12:54:37 $
 * @since 1.0
 */
public interface PersistentObjectWithView extends Persistence {

	abstract public String getUserViewString();
	
	abstract public void setUserViewString(String s);
	
}
