/*
 * (c) LibriCore
 * 
 * Created on 10-ene-2005
 * 
 * BRWR_NTE.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class BRWR_NTE implements Serializable{

	private BorrowerNoteKey key;
	private String noteText;
	
	
    /**
     * @return Returns the key.
     * @exception
     * @since 1.0
     */
    public BorrowerNoteKey getKey() {
        return key;
    }
    /**
     * @param key The key to set.
     * @exception
     * @since 1.0
     */
    public void setKey(BorrowerNoteKey key) {
        this.key = key;
    }
    /**
     * @return Returns the noteText.
     * @exception
     * @since 1.0
     */
    public String getNoteText() {
        return noteText;
    }
    /**
     * @param noteText The noteText to set.
     * @exception
     * @since 1.0
     */
    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
