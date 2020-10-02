package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.OrderedTag;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

public class RecordNote extends VariableField implements PersistentObjectWithView, OrderedTag{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 480330480928121688L;
	
	private static final short NOTE_CATEGORY = 7;
	private String content = null;
	private int noteType;
	private int noteNumber = -1;
	private Integer sequenceNumber;
	
	public RecordNote() {
		super();
		setPersistenceState(new PersistenceState());
	}
	
	public RecordNote(final int itemNbr) {
		super(itemNbr);
	}
	
	@Override
	public boolean isBrowsable() {
		return false;
	}
	
	@Override
	public int hashCode() {
		return getNoteNbr();
	}
	
	@Override
	  public boolean equals(final Object obj) {
	    if (!(obj instanceof RecordNote)) {
	      return false;
	    } else {
	    	RecordNote aNote = (RecordNote) obj;
	      return aNote.getUserViewString().equals(this.getUserViewString()) &&
	        aNote.getNoteNbr() == this.getNoteNbr();
	    }
	  }
	
	/**
	 *
	 */
	public int getNoteNbr() {
		return noteNumber;
	}

	/**
	 * @param i
	 */
	public void setNoteNbr(final int i) {
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
		return View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING;
	}

	@Override
	public void setUserViewString(String s) {
		// Do nothing
		
	}
	
}
