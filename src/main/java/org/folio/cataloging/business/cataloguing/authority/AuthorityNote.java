/*
 * (c) LibriCore
 * 
 * Created on Dec 5, 2005
 * 
 * AuthorityNote.java
 */
package org.folio.cataloging.business.cataloguing.authority;

import java.util.List;

import org.folio.cataloging.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.cataloguing.common.ItemEntity;
import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.dao.DAOSystemNextNumber;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.persistence.AUT;
import org.folio.cataloging.dao.persistence.T_AUT_NTE_TYP;

import org.folio.cataloging.util.StringText;
import org.folio.cataloging.model.Subfield;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/04/27 12:56:53 $
 * @since 1.0
 */
public class AuthorityNote extends VariableField implements Persistence, PersistsViaItem {
	private AUT autItm;
	private int noteNumber;
	private int itemNumber;
	private short noteType = Defaults.getShort("authorityNote.noteType");
	private String noteStringText;
	private PersistenceState persistenceState = new PersistenceState();
	
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AuthorityNote() {
		super();
		StringText s = new StringText();
		s.addSubfield(new Subfield("a", ""));
		setStringText(s);
		setPersistenceState(persistenceState);
	}

	/**
	 * Class constructor
	 *
	 * @param itemNumber
	 * @since 1.0
	 */
	public AuthorityNote(int itemNumber) {
		super(itemNumber);
	    setPersistenceState(persistenceState);
	}

	/* (non-Javadoc)
	 * @see TagInterface#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setNoteNumber(dao.getNextNumber("AN"));
	}

	/* (non-Javadoc)
	 * @see TagInterface#getCategory()
	 */
	public short getCategory() {
		return 7;
	}

	/* (non-Javadoc)
	 * @see TagInterface#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return new CorrelationValues().change(1, getNoteType());
	}

	/* (non-Javadoc)
	 * @see TagInterface#getFirstCorrelationList()
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return getDaoCodeTable().getList(T_AUT_NTE_TYP.class,true);
	}

	/* (non-Javadoc)
	 * @see VariableField#getStringText()
	 */
	public StringText getStringText() {
		return new StringText(getNoteStringText());
	}

	/* (non-Javadoc)
	 * @see TagInterface#isNote()
	 */
	public boolean isNote() {
		return true;
	}

	/* (non-Javadoc)
	 * @see TagInterface#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setNoteType(v.getValue(1));
	}

	/* (non-Javadoc)
	 * @see VariableField#setStringText(org.folio.cataloging.util.StringText)
	 */
	public void setStringText(StringText stringText) {
		setNoteStringText(stringText.toString());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getNoteNumber() {
		return noteNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setNoteNumber(int i) {
		noteNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public short getNoteType() {
		return noteType;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setNoteType(short s) {
		noteType = s;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getNoteStringText() {
		return noteStringText;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setNoteStringText(String string) {
		noteStringText = string;
	}

	/* (non-Javadoc)
	 * @see PersistsViaItem#getItemEntity()
	 */
	public ItemEntity getItemEntity() {
		return autItm;
	}

	/* (non-Javadoc)
	 * @see PersistsViaItem#setItemEntity(ItemEntity)
	 */
	public void setItemEntity(ItemEntity item) {
		this.autItm = (AUT)item;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		//result = prime * result + itemNumber;
		result = prime * result + noteNumber;
		//result = prime * result + noteType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AuthorityNote other = (AuthorityNote) obj;
		/*if (itemNumber != other.itemNumber)
			return false;*/
		if (noteNumber != other.noteNumber)
			return false;
		/*if(noteType != other.noteType)
			return false;*/
		return true;
	}

	
	
}
