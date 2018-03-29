/*
 * (c) LibriCore
 * 
 * Created on Aug 25, 2004
 * 
 * PersistentObjectWithView.java
 */
package org.folio.cataloging.business.common;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/12 12:54:37 $
 * @since 1.0
 */
public interface PersistentObjectWithView extends Persistence {

	String getUserViewString();
	
	void setUserViewString(String s);
	
}
