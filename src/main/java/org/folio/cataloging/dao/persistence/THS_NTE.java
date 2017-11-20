/*
 * (c) LibriCore
 * 
 * Created on 09-jul-2004
 * 
 * HLDG_NTE.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

import org.folio.cataloging.business.common.Defaults;

/**
 * @author elena
 * @version $Revision: 1.4 $, $Date: 2004/08/19 14:05:26 $
 * @since 1.0
 */
public class THS_NTE implements Serializable{

	private ThesaurusNoteKey key;
	private short noteType = Defaults.getShort("thesaurusNote.noteType");
	private String noteText;
	private String language;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ThesaurusNoteKey getKey() {
		return key;
	}

	public void setKey(ThesaurusNoteKey key) {
		this.key = key;
	}


	public String getNoteText() {
		return noteText;
	}


	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}


	

	
	public short getNoteType() {
		return noteType;
	}

	

	public void setNoteType(short s) {
		noteType = s;
	}




	

}
