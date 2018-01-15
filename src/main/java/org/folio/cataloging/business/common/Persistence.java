/*
 * (c) LibriCore
 * 
 * Created on Oct 12, 2004
 * 
 * Persistence.java
 */
package org.folio.cataloging.business.common;

import net.sf.hibernate.Lifecycle;
import org.folio.cataloging.dao.common.HibernateUtil;

import java.io.Serializable;

/**
 * Implementing this interface indicates that the implementing 
 * object is known to the persistency layer (Hibernate)
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2006/07/11 08:01:06 $
 * @since 1.0
 */
public interface Persistence extends Lifecycle, Serializable {

	
	/**
	 * Removes the object from the persistence session
	 * @since 1.0
	 */
    void evict() throws DataAccessException;
	
	/**
	 * 
	 */
    int getUpdateStatus();

	/**
		 * 
		 */
    void setUpdateStatus(int i);

	boolean isChanged();

	boolean isDeleted();

	boolean isNew();

	/**
	 * If object is now UNCHANGED make it CHANGED (otherwise leave it alone)
	 *
	 */
    void markChanged();

	void markNew();

	void markUnchanged();

	void markDeleted();

	/**
		 * causes the object to generate new key values
		 *
		 */
    void generateNewKey() throws DataAccessException;
	
	HibernateUtil getDAO();

}
