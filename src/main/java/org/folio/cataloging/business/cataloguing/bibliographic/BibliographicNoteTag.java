package org.folio.cataloging.business.cataloguing.bibliographic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.folio.cataloging.business.cataloguing.common.OrderedTag;
import org.folio.cataloging.business.codetable.ValueLabelElement;
import org.folio.cataloging.business.common.ConfigHandler;
import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.dao.DAOSystemNextNumber;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.common.PersistentObjectWithView;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.marchelper.MarcHelperTag;
import org.folio.cataloging.dao.persistence.BibliographicNoteType;
import org.folio.cataloging.dao.persistence.StandardNoteAccessPoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.common.LocalKeyGenerator;
import org.folio.cataloging.util.StringText;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.dao.DAOBibliographicNoteTag;
import org.folio.cataloging.dao.DAOBibliographicNotesOverflow;
import org.folio.cataloging.dao.DAOBibliographicStandardNote;

/**
 * TODO: Javadoc
 *
 * @author paulm
 * @author agazzarini
 */
public class BibliographicNoteTag extends VariableField implements PersistentObjectWithView ,MarcHelperTag,OrderedTag {
	private static final long serialVersionUID = 5008624075912779670L;
	private static final Log logger = LogFactory.getLog(BibliographicNoteTag.class);
	private static final short bibliographicNoteCategory = 7;
	private static final DAOBibliographicNoteTag daoNoteTag =	new DAOBibliographicNoteTag();
	private StringText stringText = null;
	private short noteType;
	private int noteNbr = LocalKeyGenerator.getInstance().generateNextKey(this.getClass());
	public BibliographicNote note = new BibliographicNote();
	public List deletedOverflowList = new ArrayList();
	private Integer sequenceNumber;
	private ValueLabelElement valueElement = null;
	private StandardNoteAccessPoint noteStandard= null;
	private ConfigHandler configHandler =ConfigHandler.getInstance();
	
	private static final int STANDARD_NOTE_MAX_LENGHT = 1024;
	private static final int OVERFLOW_NOTE_MAX_LENGHT = 1000;
	
	public ValueLabelElement loadValueLabelElement(int userView, String lingua) 
	{	
		try {
			DAOBibliographicStandardNote b = new DAOBibliographicStandardNote();
			if(isStandardNoteType()){
			  valueElement = b.getSTDDisplayString(noteStandard.getTypeCode(), lingua);
			}
			return valueElement;
		} catch (DataAccessException ex) {
			return null;
		}
	}
	
	public StandardNoteAccessPoint loadNoteStandard(int userView, String lingua) 
	{
		/*da creare DAOBibliographicStandardNote*/
		DAOBibliographicStandardNote b = new DAOBibliographicStandardNote();
		try {
			noteStandard= b.getBibNoteStardard(note.getBibItemNumber(), userView, note.getNoteNbr());
			/*passare il campo lingua della nota*/
			if(isStandardNoteType()){
			  valueElement = b.getSTDDisplayString(noteStandard.getTypeCode(), lingua);
			}
			return noteStandard;
		} catch (DataAccessException ex) {
			// non c'e' la StandardNote
			return null;
		}
	}
	
	public final boolean isStandardNoteType()
	{
		if(noteStandard!=null) 
			return true;
		else 
			return false;
	}
	
	public StandardNoteAccessPoint getNoteStandard() {
		return noteStandard;
	}
	
	public void setNoteStandard(StandardNoteAccessPoint noteStandard) {
		this.noteStandard = noteStandard;
	}
	
	public ValueLabelElement getValueElement() {
		return valueElement;
	}
	
	public void setValueElement(ValueLabelElement valueElement) {
		this.valueElement = valueElement;
	}
	
	public void setSequenceNumber(Integer integer) 
	{
		   sequenceNumber=integer;
			this.getNote().setSequenceNumber(integer);
			this.getNote().markChanged();
	}
	
	public Integer getSequenceNumber() {
		return this.getNote().getSequenceNumber();
	}
	
	public BibliographicNoteTag() 
	{
		super();
		StringText s = new StringText();
		s.addSubfield(new Subfield("a", ""));
		setStringText(s);
		setNote(new BibliographicNote());
		setPersistenceState(new PersistenceState());
		setDefaultNoteType();
	}
	
	public BibliographicNoteTag(BibliographicNote note) 
	{
		super();
		setNote(note);
		setPersistenceState(new PersistenceState());
	}
	
	public BibliographicNoteTag(int itemNbr) {
		super(itemNbr);
	}

	public boolean isBrowsable() {
		return false;
	}

	public int getNoteNbr() {
		return note.getNoteNbr();
	}

	/**
	 * Utilizzare per ricreare la stringa della nota intera ($a....$b...)
	 * Non accede a informazioni della nota e riguarda solo gli overflow
	 * @param list
	 * @return
	 */
	public String getOverFlowString(List list) 
	{
		String text = "";
		DAOBibliographicNotesOverflow b = new DAOBibliographicNotesOverflow();

		try{
			text = b.getBibNotesOverflow(list);
		} catch(DataAccessException ex) {
			return (stringText==null?"":stringText.toString());
		}
		return text;
	}

	private void setNoteNbr(int i) {
		note.setNoteNbr(i);
	}

	public StringText getStringText() 
	{
		List ovfList = getOverflowList();
		return new StringText(note.getContent() + getOverFlowString(ovfList));
	}
	
	/**
	 * Utilizzare per il display della nota intera standard
	 * Controllo presenza @1 testo nel modello nota standard
	 * Costruzione StringText con replace del contenuto della nota con @1
	 * @return StringText costruito
	 */
	public StringText getStandardNoteStringText() 
	{
		String value ="";
		if(valueElement!=null)
			value = valueElement.getLabel();
		
		if (value.indexOf("@1") != -1) {
			//In creazione della nota, salva tag, note.getContent() contiene \u001f
			if(note.getContent().indexOf("\u001f")!=-1)
				value = value.replaceAll("@1", note.getContent().substring(2));
			//In Load dal DB non è presente \u001f
			else
				value = value.replaceAll("@1", note.getContent());
			return new StringText(value);
		} else
			
	    return new StringText(value);
	}

	public void setStringText(StringText text) 
	{
		stringText = text;
		breakNotesStringText();
	}

	/**
	 * Used to display the entire note as marc string
	 * @return
	 */
	public String getStringTextString(){
		return getStringText().toString();
	}
	
	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCategory()
	 */
	public short getCategory() {
		return bibliographicNoteCategory;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCorrelationList(int)
	 */
	public List getFirstCorrelationList()
		throws DataAccessException {
		return getDaoCodeTable().getCorrelatedList(BibliographicNoteType.class,true," and bc.key.marcSecondIndicator <> '@' and bc.databaseFirstValue = ct.code ");
	}

	public short getNoteType() {
		return note.getNoteType();
	}

	public void setNoteType(short s) {
		note.setNoteType(s);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getSecondCorrelationList(short, java.util.Locale)
	 */
	public List getSecondCorrelationList(short value1) throws DataAccessException {
		return null;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getThirdCorrelationList(short, short, java.util.Locale)
	 */
	public List getThirdCorrelationList(short value1, short value2) throws DataAccessException {
		return null;
	}

	public char getOverflowIndicator() {
		return note.getOverflowIndicator();
	}

	public void setOverflowIndicator(char c) {
		note.setOverflowIndicator(c);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException 
	{
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setNoteNbr(dao.getNextNumber("BN"));
	}
	
	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getRequiredEditPermission()
	 */
	public String getRequiredEditPermission() {
		return "editNote";
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return (new CorrelationValues()).change(1, getNoteType());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setNoteType(v.getValue(1));		
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#getDAO()
	 */
	public HibernateUtil getDAO() {
		return daoNoteTag;
	}

	public void breakNotesStringText()
	{
		/* bib_nte table can only contain up to 1024 characters; the overflow should be inserted in the bib_nte_ovrfw table in rows of max 1000 characters; */	
		deletedOverflowList.addAll(getOverflowList());

		// MIKE: overflowList contains only the objects already moved in deletedOverflowList
		// Only the changed/unchanged/removed objects must be marked for deletion
		Iterator iter = deletedOverflowList.iterator();
		while (iter.hasNext()) {
			BibliographicNoteOverflow noteOverflow = (BibliographicNoteOverflow) iter.next();
			if(!noteOverflow.isNew()){
				noteOverflow.markDeleted();
			}
		}
		
		setOverflowList(new ArrayList());
		
		/* We work about the complete string "$anote+ovrflowa$btext$cother_text_etc" */
		String content = stringText.toString();
		
		/* Standard Note */
		String standardNote = wrapStandardNote(content, STANDARD_NOTE_MAX_LENGHT);
		logger.debug("Nota standard --> " + standardNote);
		note.setContent(standardNote);
		note.markChanged();
		
		/* Overflow Note */
		if (standardNote.length()+1<content.length()){
			wrapNoteOverflow(content.substring(standardNote.length()+1), OVERFLOW_NOTE_MAX_LENGHT, getOverflowList());
		}
		
		for (Iterator iterator = getOverflowList().iterator(); iterator.hasNext();) {
			BibliographicNoteOverflow item = (BibliographicNoteOverflow) iterator.next();
			logger.debug("Nota overflow        --> " + item.getStringText());
			logger.debug("Nota overflow length --> " + item.getStringText().length());
		}
	}
	
	/**
	 * Metodo per spezzare la nota standard a 1024 caratteri senza troncare le parole
	 * @param string
	 * @param charWrap
	 * @return
	 */
	public static String wrapStandardNote(String string, int charWrap) 
	{
	    int lastBreak = 0;
	    int nextBreak = charWrap;
	    if (string.length() > charWrap) {
	        String setString = "";
            while (string.charAt(nextBreak) != ' ' && nextBreak > lastBreak) {
                nextBreak--;
            }
            if (nextBreak == lastBreak) {
                nextBreak = lastBreak + charWrap;
            }
            setString = string.substring(lastBreak, nextBreak).trim();
            return setString;
	    } else {
	        return string;
	    }
	}
	
	/**
	 * Metodo per spezzare la nota overflow a 1000 caratteri senza troncare le parole
	 * @param string
	 * @param charWrap
	 * @param overflowList
	 */
	public static void wrapNoteOverflow(String string, int charWrap, List overflowList) 
	{
	    int lastBreak = 0;
	    int nextBreak = charWrap;
	    BibliographicNoteOverflow overflow = null;
	    if (string.length() > charWrap) {
	        String setString = "";
	        do {
	            while (string.charAt(nextBreak) != ' ' && nextBreak > lastBreak) {
	                nextBreak--;
	            }
	            if (nextBreak == lastBreak) {
	                nextBreak = lastBreak + charWrap;
	            }
	            setString += string.substring(lastBreak, nextBreak);
	            
	            overflow = new BibliographicNoteOverflow();
	            overflow.setStringText(setString);
	            overflow.markNew();
	            overflowList.add(overflow);

	            setString = "";
	            
	            lastBreak = nextBreak;
	            nextBreak += charWrap;
	        } 
	        while (nextBreak < string.length());
	        setString += string.substring(lastBreak);
	        
	        if (setString.length()>0){
	            overflow = new BibliographicNoteOverflow();
	            overflow.setStringText(setString);
	            overflow.markNew();
	            overflowList.add(overflow);
	        }
	    } else {
	    	if (string.length()>0){
	    		overflow = new BibliographicNoteOverflow();
	    		overflow.setStringText(string);
	    		overflow.markNew();
	    		overflowList.add(overflow);
	    	}
        }
	}

	public List getOverflowList() {
		return note.overflowList;
	}
	
	public List getOverflowList( int userView)
	{
		DAOBibliographicNotesOverflow b = new DAOBibliographicNotesOverflow();
		try{
			return b.getBibNotesOverflowList(note.getBibItemNumber(),userView,note.getNoteNbr());
		} catch(DataAccessException ex) {return null;}
	}

	public void setOverflowList(List list) {
		note.setOverflowList(list);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
	 */
	public boolean correlationChangeAffectsKey(CorrelationValues v) 
	{
		if (v.getValue(1) == BibliographicNote.PUBLISHER_TYPE){
			setNoteType(v.getValue(1));
			return true;
		 }else if (v.getValue(1) == 381){
			 setNoteType(v.getValue(1));
			return true;
	    }else if (v.getValue(1) == 382){
	    	setNoteType(v.getValue(1));
		   return true;
	   
	   }else if (v.getValue(1) >= 410 && v.getValue(1)<= 424 ){
	    	setNoteType(v.getValue(1));
		   return true;
	   }
		return false;
	}

	public BibliographicNote getNote() {
		return note;
	}

	public void setNote(BibliographicNote note) {
		this.note = note;
	}
	
	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#setBibItemNumber(int)
	 */
	public void setBibItemNumber(int i) {
		note.setItemNumber(i);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#setUserViewString(java.lang.String)
	 */
	public void setUserViewString(String string) {
		note.setUserViewString(string);
	}

	public List getDeletedOverflowList() {
		return deletedOverflowList;
	}

	public void setDeletedOverflowList(List list) {
		deletedOverflowList = list;
	}
	
	public boolean isNote() {
		return true;
	}

	public String getUserViewString() {
		return note.getUserViewString();
	}

	/* (non-Javadoc)
	 * @see TagInterface#setItemNumber(int)
	 */
	public void setItemNumber(int itemNumber){
		super.setItemNumber(itemNumber);
		setBibItemNumber(itemNumber);  // also sets the notes
	}

	public String toString() {
		return super.toString() +" n. "+ noteNbr+" type: "+noteType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + noteNbr;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BibliographicNoteTag other = (BibliographicNoteTag) obj;
		if (noteNbr != other.noteNbr)
			return false;
		return true;
	}

	public Descriptor getDescriptor() {
		return null;
	}

	public String getVariantCodes() {
		return null;
	}

	public String getKey() throws DataAccessException, MarcCorrelationException {
		return getMarcEncoding().getMarcTag();
	}
	public void setDefaultNoteType()
	{
		short noteType=0;
		noteType= new Short(configHandler.findValue("t_bib_nte_typ","bibliographicNote.noteType"));
		setNoteType(noteType);
	}
}