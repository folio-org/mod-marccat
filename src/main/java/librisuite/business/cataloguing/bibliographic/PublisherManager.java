/*
 * (c) LibriCore
 * 
 * Created on Dec 21, 2004
 * 
 * PublisherTag.java
 */
package librisuite.business.cataloguing.bibliographic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.LibrisuiteUtils;
import librisuite.business.common.PersistenceState;
import librisuite.business.common.PersistentObjectWithView;
import librisuite.business.common.UserViewHelper;
import librisuite.business.common.View;
import librisuite.business.descriptor.DAODescriptor;
import librisuite.business.descriptor.DAOPublisherDescriptor;
import librisuite.business.descriptor.Descriptor;
import librisuite.business.descriptor.PublisherTagDescriptor;
import librisuite.hibernate.BibliographicNoteType;
import librisuite.hibernate.PUBL_HDG;
import librisuite.hibernate.PUBL_TAG;
import librisuite.hibernate.REF;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.casalini.approval.IGlobalConst;
import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.StringText;
import com.libricore.librisuite.common.Subfield;

/**
 * Publishers differ from other access points in that multiple publisher headings
 * contribute to a single tag, controlled through the intermediate table PUBL_TAG.  This class then 
 * implements the Tag/VariableField behaviour
 * and manages the persistence of the constituent PUBL_TAG and PublisherAccessPoint objects
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2009/12/14 22:24:41 $
 * @since 1.0
 */
public class PublisherManager extends VariableField implements PersistentObjectWithView , Equivalent
{
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(PublisherManager.class);
	private static final DAOPublisherManager daoPublisherTag = new DAOPublisherManager();
	private PersistenceState persistenceState = new PersistenceState();
	private List/*<PUBL_TAG>*/ publisherTagUnits = new ArrayList/*<PUBL_TAG>*/();
	private List/*<PUBL_TAG>*/ deletedUnits = new ArrayList/*<PUBL_TAG>*/();
	private PublisherAccessPoint apf = new PublisherAccessPoint();
	private List/*<String>*/ dates = new ArrayList/*<String>*/();
	private int tagUnitIndex;
	private String manufacturerPlace = "";
	private String manufacturer = "";
	private String manufacturerDate = "";
	private UserViewHelper userViewHelper = new UserViewHelper();
	/* Bug 4155 inizio */
	private String stringTextForFastDigitPublisher;
	
	public String getStringTextForFastDigitPublisher() {
		return stringTextForFastDigitPublisher;
	}

	public void setStringTextForFastDigitPublisher(
			String stringTextForFastDigitPublisher) {
		this.stringTextForFastDigitPublisher = stringTextForFastDigitPublisher;
	} 
	
	/*
	 * noteType is used only for the edit page correlation to allow for the possibility
	 * that a publisher tag can be changed to another note type.
	 */
	private short noteType = 24;

	public short getNoteType() {
		return noteType;
	}

	public void setNoteType(short noteType) {
		this.noteType = noteType;
	}

	public PublisherManager() 
	{
		super();
		setPersistenceState(persistenceState);
	}

	public PublisherManager(int bib_itm, int view) 
	{
		super();
		setApf(new PublisherAccessPoint(bib_itm));
		/*TODO
		 * Changes to publisher with 3.5.4.2 introduced a new
		 * table PUBL_TAG to manage the link between publisher
		 * headings and publisher tags.
		 * This implementation is 'quick and dirty'.  There is confusion
		 * about whether the new PublisherManager (formerly PublisherTag)
		 * should be the access point or something higher.
		 */
		getApf().setUserViewString(View.makeSingleViewString(view));
		setPersistenceState(persistenceState);
		setBibItemNumber(bib_itm);
		setUserViewString(View.makeSingleViewString(view));	
	}

	public PublisherManager(PublisherAccessPoint apf) 
	{
		super();
		setPersistenceState(persistenceState);
		setItemNumber(apf.getItemNumber());
		setUserViewString(apf.getUserViewString());
		setUpdateStatus(apf.getUpdateStatus());
		setApf(apf);
		getDates().add("");
		setPublisherTagUnits(((PublisherTagDescriptor)getApf().getDescriptor()).getPublisherTagUnits());
		Collections.sort(getPublisherTagUnits(), new PublisherTagComparator());
	}

	/*
	 * Copy dates from access points into Publisher Tag while editing
	 */
	private void copyDates() {
		Iterator ite = getPublisherTagUnits().iterator();
		while(ite.hasNext()){
			PUBL_TAG unit = (PUBL_TAG)ite.next();
			getDates().add(unit.getDate());
	   }
	}

	/*
	 * Extracts subfields e,f,g from the "last" access point and stores them in separate
	 * member variables
	 */
	private void extractManufacturerData() 
	{
		if (getPublisherTagUnits().size() > 0) {
			PUBL_TAG lastPublTag =(PUBL_TAG)
			publisherTagUnits.get(publisherTagUnits.size() - 1);
			StringText s = new StringText(lastPublTag.getOtherSubfields());
			lastPublTag.setOtherSubfields(s.getSubfieldsWithoutCodes("368efg").toString());
			String otherFieldsText = s.getSubfieldsWithCodes("368efg").toString(); 
			if (otherFieldsText != null && otherFieldsText.indexOf(Subfield.SUBFIELD_DELIMITER)>-1){
				setStringTextForFastDigitPublisher(otherFieldsText.replaceAll(IGlobalConst.SUBFIELD_DELIMITER, IGlobalConst.SUBFIELD_DELIMITER_FOR_VIEW));
			}else {
				setStringTextForFastDigitPublisher(otherFieldsText);
			}
			/* Bug 4155 fine */
		}
	}

	
	/**
	 * pm 2011 updates the publisher tag with the recently browsed publisher
	 * heading
	 * 
	 * @throws DataAccessException
	 */
	public void updatePublisherFromBrowse(PUBL_HDG p)
			throws DataAccessException {
		PUBL_HDG myP = (PUBL_HDG) ((DAODescriptor) p.getDAO())
				.findOrCreateMyView(p.getHeadingNumber(),
						p.getUserViewString(), View
								.toIntView(getUserViewString()));
		PUBL_TAG tagUnit = (PUBL_TAG) getPublisherTagUnits().get(
				getTagUnitIndex());
		tagUnit.setDescriptor(myP);
	}


	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#getStringText()
	 */
	public StringText getStringText() 
	{
		logger.debug("getting StringText");
		StringText result = new StringText();
		Iterator ite = getPublisherTagUnits().iterator();
		while(ite.hasNext()){
			PUBL_TAG aTagUnit = (PUBL_TAG)ite.next();
			result.add(new StringText(aTagUnit.getStringText()));
			logger.debug("added: " + aTagUnit.getStringText());
		}
		return result;
	}

	/**
	 * @return the MARC display string for the publisher tag
	 */
	public String getDisplayString() {
		/*
		 * This is used in the worksheet jsp for displaying tags that
		 * cannot be directly edited on the page (as with publisher)
		 */
		return getStringText().getMarcDisplayString();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#setStringText(com.StringText)
	 */
	public void setStringText(StringText stringText) {
		// should only be called from model parsing
		/*
		 * clear the current apf's
		 */
		setPublisherTagUnits(new ArrayList/*<PUBL_TAG>*/());
		/*
		 * break up the string text into constituent PublisherTagUnits
		 */
		logger.debug("Setting string text to " + stringText);
		StringText newText = new StringText();
		String lastSubfield = "a";
		List/*<Subfield>*/ theList = stringText.getSubfieldList();
		Iterator ite = theList.iterator();
		while(ite.hasNext()){
			Subfield aSubfield = (Subfield)ite.next();
			logger.debug("subfield is " + aSubfield);
			if (aSubfield.getCode().compareTo(lastSubfield) < 0
				|| theList.lastIndexOf(aSubfield) == theList.size() - 1) {
				logger.debug("first test passed");
				if (theList.lastIndexOf(aSubfield) == theList.size() - 1) {
					// add the last subfield to newText
					logger.debug("is the last so add to newText");
					newText.addSubfield(aSubfield);
				}
				/*
				 * we have reached the end of a tag Unit  of subfields
				 * so create a new PUBL_TAG and set the text
				 */
				if (newText.getNumberOfSubfields() > 0) {
					logger.debug("adding new publisher tag row");
					addNewTagUnit();
					PUBL_TAG p = (PUBL_TAG)
						getPublisherTagUnits().get(
							getPublisherTagUnits().size() - 1);
					p.setStringText(newText.toString());
					newText = new StringText();
				}
			}
			newText.addSubfield(aSubfield);
			lastSubfield = aSubfield.getCode();
		}
		logger.debug("StringText is \"" + newText + "\"");
		parseForEditing();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCategory()
	 */
	public short getCategory() {
		return 7;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return getApf().getCorrelationValues();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List/*<PUBL_TAG>*/ getPublisherTagUnits() {
		return publisherTagUnits;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setPublisherTagUnits(List/*<PUBL_TAG>*/ list) {
		publisherTagUnits = list;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getFirstCorrelationList()
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return getDaoCodeTable().getList(BibliographicNoteType.class);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof PublisherManager) {
			PublisherManager p = (PublisherManager) obj;
			return p.getApf().equals(this.getApf());
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#getDAO()
	 */
	public HibernateUtil getDAO() {
		return daoPublisherTag;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isWorksheetEditable()
	 */
	public boolean isWorksheetEditable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isPublisher()
	 */
	public boolean isPublisher() {
		return true;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getManufacturerDate() {
		return manufacturerDate;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getManufacturerPlace() {
		return manufacturerPlace;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setManufacturer(String string) {
		manufacturer = string;
	}

	public void setManufacturerDate(String string) {
		manufacturerDate = string;
	}

	public void setManufacturerPlace(String string) {
		manufacturerPlace = string;
	}

	public List/*<PUBL_TAG>*/ getDeletedUnits() {
		return deletedUnits;
	}

	public void setDeletedUnits(List/*<PUBL_TAG>*/ list) {
		deletedUnits = list;
	}

	public int getTagUnitIndex() {
		return tagUnitIndex;
	}

	public void setApfIndex(int i) {
		tagUnitIndex = i;
	}

	public List/*<String>*/ getDates() {
		return dates;
	}

	public List/*<String>*/ getDates(int i) {
		logger.debug("getting date[" + i + "] = " + dates.get(i));
		return dates;
	}

	public void setDates(List/*<String>*/ list) {
		logger.debug("setting all dates");
		dates = list;
		for (int i = 0; i < dates.size(); i++) {
			logger.debug("dates[" + i + "]='" + dates.get(i) + "'");
		}
	}

	public void addNewTagUnit() {
		PUBL_TAG tagUnit = new PUBL_TAG();
		tagUnit.setUserViewString(getUserViewString());
		getPublisherTagUnits().add(tagUnit);
		getDates().add("");
	}

	/**
	 * Extract dates and manufacturer data for editing as individual fields
	 * 
	 * @since 1.0
	 */
	public void parseForEditing() {
		extractManufacturerData();
		copyDates();
	}

	/**
	 * Incorporate edit changes (dates, manufacturer data, sequences, etc.) into
	 * the tagUnits ready for saving to the database
	 * 
	 * @since 1.0
	 */
	public void saveEdits() {
		int i = 0;
		Iterator ite = getPublisherTagUnits().iterator();
		while(ite.hasNext()){
			PUBL_TAG p = (PUBL_TAG)ite.next();
			PUBL_HDG pu = ((PUBL_HDG)p.getDescriptor());
			  
			if(pu!=null && pu.getKey().getHeadingNumber()==-1){
 			    pu.setNameStringText("");
	    	    pu.setPlaceStringText("");
			  }
	
			p.setSequenceNumber(i + 1);
			if (!("".equals(getDates().get(i)))) {
				p.setOtherSubfields(Subfield.SUBFIELD_DELIMITER + "c" + getDates().get(i));
			}
			else
				p.setOtherSubfields("");
			i = i + 1;
		}
	     setDates(new ArrayList/*<String>*/());
		
		// check if any of subfields e,f,g are present
		StringText s = new StringText();
		
		
		s.add(new StringText(getStringTextForFastDigitPublisher()));
			
		if (s.getNumberOfSubfields() > 0) {
			if (getPublisherTagUnits().size() == 0) {
				// create a new unit to hold the manufacturer data
				getPublisherTagUnits().add(new PUBL_TAG());
			}
			// add the manufacturer data to the last unit
			PUBL_TAG p =(PUBL_TAG)getPublisherTagUnits().get(getPublisherTagUnits().size() - 1);
			StringText st = new StringText(p.getOtherSubfields());
			st.add(s);
			p.setOtherSubfields(st.toString());
		}
		logger.debug("saveEdits");
		((PublisherTagDescriptor)getApf().getDescriptor()).setPublisherTagUnits(getPublisherTagUnits());
	}

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content =
				getStringText().generateModelXmlElementContent(xmlDocument);
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		setStringText(StringText.parseModelXmlElementContent(xmlElement));
	}

	public String getUserViewString() {
		return userViewHelper.getUserViewString();
	}

	public void setUserViewString(String string) {
		userViewHelper.setUserViewString(string);
	}

	public int getBibItemNumber() {
		return getItemNumber();
	}

	public void setBibItemNumber(int i) {
		setItemNumber(i);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#evict()
	 */
	public void evict() throws DataAccessException {
		PublisherAccessPoint apf = getApf();
		apf.evict();
		Iterator ite = getPublisherTagUnits().iterator();
		while(ite.hasNext()){
			PUBL_TAG p = (PUBL_TAG)ite.next();
			p.evict();
		}
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		getApf().setCorrelationValues(v);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#markChanged()
	 */
	public void markChanged() {
		Iterator ite = getPublisherTagUnits().iterator();
		while(ite.hasNext()){
		  PUBL_TAG p = (PUBL_TAG)ite.next();
		  p.markChanged();
		}
		super.markChanged();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#markDeleted()
	 */
	public void markDeleted() {
		Iterator ite = getPublisherTagUnits().iterator();
		while(ite.hasNext()){
			PUBL_TAG p = (PUBL_TAG)ite.next();
			p.markDeleted();
		}
		super.markDeleted();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#markNew()
	 */
	public void markNew() {
		Iterator ite = getPublisherTagUnits().iterator();
		while(ite.hasNext()){
			PUBL_TAG p = (PUBL_TAG)ite.next();
			p.markNew();
		}
		super.markNew();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#markUnchanged()
	 */
	public void markUnchanged() {
		Iterator ite = getPublisherTagUnits().iterator();
		while(ite.hasNext()){
			PUBL_TAG p = (PUBL_TAG)ite.next();
			p.markUnchanged();
		}
		super.markUnchanged();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#setUpdateStatus(int)
	 */
	public void setUpdateStatus(int i) {
		Iterator ite = getPublisherTagUnits().iterator();
		while(ite.hasNext()){
			PUBL_TAG p = (PUBL_TAG)ite.next();
			p.setUpdateStatus(i);
		}
		super.setUpdateStatus(i);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
	 */
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return !BibliographicNoteType.isPublisherType(v.getValue(1));
	}

	public PublisherAccessPoint getApf() {
		return apf;
	}

	public void setApf(PublisherAccessPoint apf) {
		this.apf = apf;
	}

	//@Override
	public int hashCode() {
		return getApf().hashCode();
	}

	public void removeDescriptor(int i) 
	{
		PUBL_TAG pap =(PUBL_TAG) getPublisherTagUnits().get(i);
		detachDescriptor(pap);
	}
	 
	private void detachDescriptor(PUBL_TAG pap) {
		PUBL_HDG publ_hdg = ((PUBL_HDG)pap.getDescriptor());
		if(pap.getPublisherHeadingNumber()==null){
		   publ_hdg.setNameStringText("");
		   publ_hdg.setPlaceStringText("");
	
	    }else{ 
		   pap.setDescriptor(null);
		   pap.setPublisherHeadingNumber(null);
		}
	}
		//TODO da aggiornare tag 260
		
	public List replaceEquivalentDescriptor(short indexingLanguage,	int cataloguingView) throws DataAccessException 
	{
		DAODescriptor dao = new DAOPublisherDescriptor();
		DAOPublisherManager daoPu = new DAOPublisherManager();
		List newTags = new ArrayList();
		PUBL_TAG pu = null;
		PublisherManager aTag = (PublisherManager) (LibrisuiteUtils.deepCopy(this));
		PublisherAccessPoint apf = aTag.getApf();
		List/*<PUBL_TAG>*/ publisherTagApp = new ArrayList/*<PUBL_TAG>*/();
		
		for (int i = 0; i < getPublisherTagUnits().size(); i++) {
			pu = (PUBL_TAG) getPublisherTagUnits().get(i);
			Descriptor d = pu.getDescriptor();
			REF ref = dao.getCrossReferencesWithLanguage(d, cataloguingView,
					indexingLanguage);
			if (ref != null) {
				aTag.markNew();
				int tagNumber = daoPu.getNextPublisherTagNumber();
				pu.setPublisherTagNumber(tagNumber);
				pu.setDescriptor((PUBL_HDG)dao.load(ref.getTarget(), cataloguingView));
				pu.setPublisherHeadingNumber(new Integer(pu.getDescriptor()
						.getKey().getHeadingNumber()));
				publisherTagApp.add(pu);
				apf.markNew();
				apf.setHeadingNumber(new Integer(tagNumber));
				

			}
			else{
				aTag.markNew();
				int tagNumber = daoPu.getNextPublisherTagNumber();
				publisherTagApp.add(pu);
				apf.markNew();
				apf.setHeadingNumber(new Integer(tagNumber));
			}
		}
		if(aTag!=null){
		  aTag.setPublisherTagUnits(publisherTagApp);
		  newTags.add(aTag);
		}
		return newTags;
	}
}