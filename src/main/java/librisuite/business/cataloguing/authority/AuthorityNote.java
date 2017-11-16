/*
 * (c) LibriCore
 * 
 * Created on Dec 5, 2005
 * 
 * AuthorityNote.java
 */
package librisuite.business.cataloguing.authority;

import java.util.List;

import librisuite.business.cataloguing.bibliographic.PersistsViaItem;
import librisuite.business.cataloguing.bibliographic.VariableField;
import librisuite.business.cataloguing.common.ItemEntity;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DAOSystemNextNumber;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
import librisuite.hibernate.AUT;
import librisuite.hibernate.T_AUT_NTE_TYP;

import com.libricore.librisuite.common.StringText;
import com.libricore.librisuite.common.Subfield;

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
	 * @see librisuite.business.cataloguing.common.TagInterface#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setNoteNumber(dao.getNextNumber("AN"));
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#getCategory()
	 */
	public short getCategory() {
		return 7;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return new CorrelationValues().change(1, getNoteType());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#getFirstCorrelationList()
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return getDaoCodeTable().getList(T_AUT_NTE_TYP.class,true);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#getStringText()
	 */
	public StringText getStringText() {
		return new StringText(getNoteStringText());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#isNote()
	 */
	public boolean isNote() {
		return true;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setNoteType(v.getValue(1));
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#setStringText(com.libricore.librisuite.common.StringText)
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
	 * @see librisuite.business.cataloguing.bibliographic.PersistsViaItem#getItemEntity()
	 */
	public ItemEntity getItemEntity() {
		return autItm;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.PersistsViaItem#setItemEntity(librisuite.business.cataloguing.common.ItemEntity)
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
