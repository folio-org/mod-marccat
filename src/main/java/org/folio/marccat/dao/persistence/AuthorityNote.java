package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.common.View;

/**
 * @author elena
 *
 */
public class AuthorityNote extends RecordNote {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5041286137447344231L;

	public AuthorityNote() {
		super();
	}

	/**
	 *
	 */
	public AuthorityNote(final int itemNbr) {
		super(itemNbr);
	}
	
	@Override
	public int hashCode() {
		return getNoteNbr();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof AuthorityNote)) {
			return false;
		} else {
			AuthorityNote aNote = (AuthorityNote) obj;
			return aNote.getAutItemNumber() == this.getAutItemNumber() && aNote.getNoteNbr() == this.getNoteNbr();
		}
	}

	public int getAutItemNumber() {
		return getItemNumber();
	}

	public void setAutItemNumber(int i) {
		setItemNumber(i);
	}

	@Override
	public String getUserViewString() {
		return View.DEFAULT_AUTHORITY_VIEW_AS_STRING;
	}

	@Override
	public void setUserViewString(String s) {
		 // Do nothing because the view is always  View.AUTHORITY
	}
}
