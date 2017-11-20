/*
 * (c) LibriCore
 * 
 * Created on Aug 5, 2004
 * 
 * BibliographicNoteType.java
 */
package org.folio.cataloging.dao.persistence;

/**
 * @author carmen
 * @version %I%, %G%
 */
public class ReferenceThsTyp extends T_SINGLE {
 public static final short SEEN_FROM = 2;
	
	static public boolean isSeeAlso(short type) {
		return type == 3;
	}

}
