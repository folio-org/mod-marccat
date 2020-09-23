package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.OrderedTag;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

/**
 * @author elena
 *
 */
public class AuthorityNote extends VariableField implements PersistentObjectWithView, OrderedTag {
	private static final short NOTE_CATEGORY = 7;
	private String content = null;
	private int noteType;
	private int noteNumber = -1;
	private Integer sequenceNumber;

	public AuthorityNote() {
		super();
		setPersistenceState(new PersistenceState());
	}

	/**
	 *
	 */
	public AuthorityNote(final int itemNbr) {
		super(itemNbr);
	}

	@Override
	public boolean isBrowsable() {
		return false;
	}

	/**
	 *
	 */
	public int getNoteNumber() {
		return noteNumber;
	}

	/**
	 * @param i
	 */
	public void setNoteNumber(final int i) {
		noteNumber = i;
	}

	/**
	 *
	 */
	public String getStringTextString() {
		return content;
	}

	/**
	 * This value is used by Hibernate It must preserve the $a value
	 */
	public void setStringTextString(final String st) {
		this.content = st;
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
			return aNote.getAutItemNumber() == this.getAutItemNumber() && aNote.getNoteNumber() == this.getNoteNumber();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getNoteNumber();
	}

	public StringText getStringText() {
		return new StringText(content);
	}

	/**
	 * @param text should not ends with Subfield separator
	 */
	public void setStringText(final StringText text) {
		content = text.toString();
	}

	public int getCategory() {
		return NOTE_CATEGORY;
	}

	@Override
	public CorrelationValues getCorrelationValues() {
		return null;
	}

	public void setCorrelationValues(final CorrelationValues v) {
		setNoteType(v.getValue(1));
	}

	/**
	 *
	 */
	public int getNoteType() {
		return noteType;
	}

	/**
	 *
	 */
	public void setNoteType(final int s) {
		noteType = s;
	}

	@Override
	public AbstractDAO getDAO() {
		return null;
	}

	@Override
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		if (v.getValue(1) == Global.PUBLISHER_DEFAULT_NOTE_TYPE) {
			return true;
		} else if (v.getValue(1) == 381) {
			return true;
		} else
			return v.getValue(1) == 382;
	}

	public int getAutItemNumber() {
		return getItemNumber();
	}

	public void setAutItemNumber(int i) {
		setItemNumber(i);
	}

	public void setContent(String text) {
		this.content = text;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer integer) {
		sequenceNumber = integer;
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
