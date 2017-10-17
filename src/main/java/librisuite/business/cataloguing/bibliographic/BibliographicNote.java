/*
 * Created on 20-jul-2004
 *
 */
package librisuite.business.cataloguing.bibliographic;

import java.util.ArrayList;
import java.util.List;

import librisuite.business.cataloguing.common.OrderedTag;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DAOSystemNextNumber;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.business.common.PersistenceState;
import librisuite.business.common.PersistentObjectWithView;
import librisuite.business.common.UserViewHelper;
import librisuite.hibernate.BibliographicNoteType;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.StringText;
import com.libricore.librisuite.common.Subfield;

/**
 * Class comment
 * @author janick
 */
public class BibliographicNote extends VariableField implements PersistentObjectWithView ,OrderedTag{

	private static final short bibliographicNoteCategory = 7;
	/**
	 * The content is all or a part of the stringText.toString() result.
	 */
	private String content = null;
	private short noteType;
	private int noteNbr = -1;
	private char overflowIndicator = '0';
	public List overflowList = new ArrayList();
	private UserViewHelper userViewHelper = new UserViewHelper();
	public static final short PUBLISHER_TYPE = 24;
	private Integer sequenceNumber;

	public BibliographicNote() {
		super();
		StringText s = new StringText();
		s.addSubfield(new Subfield("a", ""));
		setStringText(s);
		setNoteType(Defaults.getShort("bibliographicNote.noteType"));
		setPersistenceState(new PersistenceState());
	}

	/**
	 * 
	 */
	public BibliographicNote(int itemNbr) {
		super(itemNbr);
	}

	public boolean isBrowsable() {
		return false;
	}

	/**
	 * 
	 */
	public int getNoteNbr() {
		return noteNbr;
	}

	/**
	 * 
	 */
	public String getStringTextString() {		
		return content;		
	}
	
	/**
	 * @param i
	 */
	public void setNoteNbr(int i) {
		noteNbr = i;
	}

	/**
	 * This value is used by Hibernate
	 * It must preserve the $a value
	 */
	public void setStringTextString(String string) {
		this.content = string;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof BibliographicNote)) {
			return false;
		}
		else {
			BibliographicNote aNote = (BibliographicNote)obj;
			return aNote.getBibItemNumber() == this.getBibItemNumber() &&
			aNote.getUserViewString().equals(this.getUserViewString()) &&
			aNote.getNoteNbr() == this.getNoteNbr();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getNoteNbr(); //TODO don't know if this is right
	}

	/**
	 * @deprecated use MadesNoteTag.getStringText()
	 * TODO _MIKE: if the content ends with the Subfield.SEPARATOR the result
	 * is impredictable
	 */
	public StringText getStringText() {
		// return new StringText(stringText.toString() +
		// getOverFlowStringText(getOverflowList()).toString());
		return new StringText(content);
	}

	/**
	 * @param text should not ends with Subfield separator
	 */
	public void setStringText(StringText text) {
		content = text.toString();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCategory()
	 */
	public short getCategory() {
		return bibliographicNoteCategory;
	}

	/**
	 * @deprecated
	 */
	public List getFirstCorrelationList()
		throws DataAccessException {
		return getDaoCodeTable().getList(BibliographicNoteType.class,true);
	}

	/**
	 * 
	 */
	public short getNoteType() {
		return noteType;
	}

	/**
	 * 
	 */
	public void setNoteType(short s) {
		noteType = s;
	}

	/**
	 * @deprecated
	 */
	public List getSecondCorrelationList(short value1)
		throws DataAccessException {
		return null;
	}

	/**
	 * @deprecated
	 */
	public List getThirdCorrelationList(short value1, short value2)
		throws DataAccessException {
		return null;
	}

	/**
	 * 
	 */
	public char getOverflowIndicator() {
		return overflowIndicator;
	}

	/**
	 * 
	 */
	public void setOverflowIndicator(char c) {
		overflowIndicator = c;
	}

	/**
	 * @deprecated
	 */
	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setNoteNbr(dao.getNextNumber("BN"));
	}

	/**
	 * @deprecated
	 */	public String getRequiredEditPermission() {
		return "editNote";
	}

	/**
	 * @deprecated
	 */
	public CorrelationValues getCorrelationValues() {
		return (new CorrelationValues()).change(1, getNoteType());
	}

	/**
	 * @deprecated
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setNoteType(v.getValue(1));		
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#getDAO()
	 */
	public HibernateUtil getDAO() {
		return new DAOBibliographicNote();
	}


	/**
	 * @return
	 */
	public List getOverflowList() {
		return overflowList;
	}


	/**
	 * 
	 * 
	 * @param list
	 */
	public void setOverflowList(List list) {
		overflowList = list;
	}

	/**
	 * @deprecated
	 */
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		if (v.getValue(1) == BibliographicNote.PUBLISHER_TYPE){
			return true;
		 }else if (v.getValue(1) == 381){
			return true;
	    }else if (v.getValue(1) == 382){
		   return true;
	   }
		return false;
	}

	/**
	 * @since 1.0
	 */
	public String getUserViewString() {
		return userViewHelper.getUserViewString();
	}

	/**
	 * @since 1.0
	 */
	public void setUserViewString(String string) {
		userViewHelper.setUserViewString(string);
	}

	/**
	 * @since 1.0
	 */
	public int getBibItemNumber() {
		return getItemNumber();
	}

	/**
	 * @since 1.0
	 */
	public void setBibItemNumber(int i) {
		setItemNumber(i);
	}

	
	public String getContent(){
		return content;
	}
	
	public void setContent(String text){
		this.content = text;
	}
	/*public Integer getSequenceNumber() {
		return new Integer(sequenceNumber);
	}
	
	
	public void setSequenceNumber(Integer integer) {
		sequenceNumber = 0;
		if (integer != null) {
			sequenceNumber = integer.intValue();
		}
	}*/
	/**
	 * 
	 */
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}


/**
	 * @param integer
	 */
	public void setSequenceNumber(Integer integer) {
		sequenceNumber = integer;
	}


}
