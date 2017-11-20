/*
 * (c) LibriCore
 * 
 * Created on 09-jul-2004
 * 
 * HLDG_NTE.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author elena
 * @version $Revision: 1.4 $, $Date: 2004/08/19 14:05:26 $
 * @since 1.0
 */
public class HLDG_NTE implements Serializable{

	private CopyNoteKey key;
	private short noteType;
	private short noteSequence;
	private String noteText;
	
	

	public short getNoteSequence() {
		return noteSequence;
	}

	public String getNoteText() {
		return noteText;
	}

	public short getNoteType() {
		return noteType;
	}

	public void setNoteSequence(short s) {
		noteSequence = s;
	}

	public void setNoteText(String string) {
		noteText = string;
	}

	public void setNoteType(short s) {
		noteType = s;
	}




	public CopyNoteKey getKey() {
		return key;
	}

	public void setKey(CopyNoteKey key) {
		this.key = key;
	}

}
