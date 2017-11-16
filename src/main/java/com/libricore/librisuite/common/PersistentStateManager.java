package com.libricore.librisuite.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import librisuite.business.common.PersistenceState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PersistentStateManager {
	private static final Log logger =
		LogFactory.getLog(PersistentStateManager.class);
	
	/**
	 * Using a list keep can track multiple instances of the same state but only the
	 * last is committed (because the PersistentState do not track the 
	 * discarded/intermediate changes)
	 * use Hashtable for a fast index search and to keep only a instance of state
	 * 
	 */
	private List/*<PersistenceState>*/ states = null;
	boolean acceptRegistrations = false;
	
	public PersistentStateManager() {
		super();
		logger.debug("PersistentStateManager creation for this thread");
		states = new ArrayList/*<PersistenceState>*/();
	}

	public void commit() {
		Iterator it = states.iterator();
		while (it.hasNext()) {
			PersistenceState element = (PersistenceState) it.next();
			element.confirmChanges();
		}
		logger.debug("PersistentStateManager "+states.size()+" items committed");
		reset();
	}
	
	public void rollback() {
		logger.warn("PersistentStateManager rollback");
		// do nothing
	}

	public void reset() {
		logger.debug("PersistentStateManager reset: unregister "+states.size()+" items");
		unregisterAll();
	}

	/**
	 * Register the time shifted persistent state of an object
	 * @param newPersistenceState
	 */
	public void register(PersistenceState newPersistenceState) {
		if(acceptRegistrations) {
			logger.debug("change persistent status: "+newPersistenceState.toString());
			states.add(newPersistenceState);
		} else newPersistenceState.confirmChanges();
	}

	/**
	 * Remove all registered time shifted persistent states
	 */
	private void unregisterAll() {
		states.clear();
	}

	public void begin() {
		reset();
		acceptRegistrations = true;
	}

	public void end() {
		acceptRegistrations = false;
	}
	

}
