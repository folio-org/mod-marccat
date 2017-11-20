/*
 * (c) LibriCore
 * 
 * Created on Aug 13, 2004
 * 
 * Command.java
 */
package org.folio.cataloging.business;

import org.folio.cataloging.business.common.DataAccessException;

/**
 * Interface for implementing undo/redo
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2004/10/28 14:39:52 $
 * @since 1.0
 */
public interface Command {

	abstract public void execute() throws DataAccessException;
	
	abstract public void reExecute() throws DataAccessException;
	
	abstract public void unExecute() throws DataAccessException;
}
