/*
 * (c) LibriCore
 * 
 * Created on Oct 12, 2004
 * 
 * Persistence.java
 */
package librisuite.business.common;

import java.io.Serializable;

import net.sf.hibernate.Lifecycle;

import com.libricore.librisuite.common.HibernateUtil;

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
	public void evict() throws DataAccessException;
	
	/**
	 * 
	 */
	public int getUpdateStatus();

	/**
		 * 
		 */
	public void setUpdateStatus(int i);

	public boolean isChanged();

	public boolean isDeleted();

	public boolean isNew();

	/**
	 * If object is now UNCHANGED make it CHANGED (otherwise leave it alone)
	 *
	 */
	public void markChanged();

	public void markNew();

	public void markUnchanged();

	public void markDeleted();

	/**
		 * causes the object to generate new key values
		 *
		 */
	public abstract void generateNewKey() throws DataAccessException;
	
	public abstract HibernateUtil getDAO();

}
