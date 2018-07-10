package org.folio.cataloging.bean.cataloguing.heading;

import net.sf.hibernate.HibernateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.Global;
import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.bean.searching.IsbnEditorBean;
import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.DuplicateDescriptorException;
import org.folio.cataloging.business.common.ErrorIsbnProcException;
import org.folio.cataloging.dao.persistence.Descriptor;
import org.folio.cataloging.business.descriptor.DescriptorFactory;
import org.folio.cataloging.business.descriptor.MatchedHeadingInAnotherViewException;
import org.folio.cataloging.dao.DAOBibliographicValidation;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.dao.DAODescriptor;
import org.folio.cataloging.dao.DAOGlobalVariable;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.shared.Validation;
import org.folio.cataloging.util.StringText;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public abstract class HeadingBean extends LibrisuiteBean {
	private static final Log logger = LogFactory.getLog(HeadingBean.class);
	
	Validation validation = null;
	private List languageOfAccessPointList = new ArrayList();
	private List verificationLevelList = new ArrayList();
	private List categoryList = new ArrayList();
	private List authoritySourceList = new ArrayList();

	StringText stringText = new StringText();
	Set remainingValidSubfields;
	List validSubfieldList;
	private String browseFieldName;
	private String sufieldTitle = "";
	private String subfieldIndex;
	private String sourceHeadingNum;
	private boolean newHeading;
	private String newShelfList;
	private Locale locale;

	private boolean isAbleUri = false;
	public abstract void setSkipInFiling(int s);

	public boolean isAbleUri() {
		return isAbleUri;
	}

	public void setAbleUri(boolean isAbleUri) {
		this.isAbleUri = isAbleUri;
	}

	public boolean isHeadingWithUri(String headingCategory)
	{
		Integer headingType = Global.HEADING_TYPE_MAP.get(headingCategory);
		if (headingType != null){
			
		}
		return (headingType != null);
	}
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}


	/**
	 * Whether the $2 text for authority source should be shown on the jsp page
	 * 
	 * @return
	 */
	public boolean isShowSourceText() {
        return T_AUT_HDG_SRC.SOURCE_IN_SUBFIELD_2 == getAuthoritySourceCode();
    }

	/**
	 * 
	 * @return
	 */
	public int getAuthoritySourceCode() {
		return getHeading().getAuthoritySourceCode();
	}

	public void setAuthoritySourceCode(int s) {
		getHeading().setAuthoritySourceCode(s);
	}

	public String getNewShelfList() {
		return newShelfList;
	}

	public void setNewShelfList(String newShelfList) {
		this.newShelfList = newShelfList;
	}

	public boolean isNewHeading() {
		return newHeading;
	}

	public void setNewHeading(boolean newHeading) {
		this.newHeading = newHeading;
	}

	public String getSourceHeadingNum() {
		return sourceHeadingNum;
	}

	public void setSourceHeadingNum(String sourceHeadingNum) {
		this.sourceHeadingNum = sourceHeadingNum;
	}

	public String getSubfieldIndex() {
		return subfieldIndex;
	}

	public void setSubfieldIndex(String subfieldIndex) {
		this.subfieldIndex = subfieldIndex;
	}

	public String getSufieldTitle() {
		if (sufieldTitle.equals("b"))
			sufieldTitle = "Suddivisione temporale";
		else if (sufieldTitle.equals("c"))
			sufieldTitle = "Suddivisione geografica";
		else if (sufieldTitle.equals("d"))
			sufieldTitle = "Suddivisione particolare";
		return sufieldTitle;
	}

	public void setSufieldTitle(String sufieldTitle) {
		this.sufieldTitle = sufieldTitle;
	}

	/**
	 * MIKE: This field store the previous HeadingBean i.e. to create a
	 * NameTitle we need first to create a name and a title. But without this
	 * field, the NameHB or the TitleHB replace the original NameTitleHB
	 */
	private HeadingBean previousHeadingBean = null;

	public HeadingBean() {
		super();
	}

	public List getLanguageOfAccessPointList() {
		return languageOfAccessPointList;
	}

	public List getVerificationLevelList() {
		return verificationLevelList;
	}

	public void setLanguageOfAccessPointList(List list) {
		languageOfAccessPointList = list;
	}

	public void setVerificationLevelList(List list) {
		verificationLevelList = list;
	}

	public int getLanguageOfAccessPoint() {
		return getHeading().getAccessPointLanguage();
	}

	public void setLanguageOfAccessPoint(int s) {
		getHeading().setAccessPointLanguage(s);
	}

	public char getVerificationLevel() {
		return getHeading().getVerificationLevel();
	}

	public void setVerificationLevel(char c) {
		getHeading().setVerificationLevel(c);
	}

	// TODO the category retrieved from the heading is not always found in the
	// list
	// probably easiest (best?) to drop category from the page

	public int getCategory() {
		return getHeading().getCategory();
	}

	public void setCategory(int s) {
	}

	public int getHeadingNumber() {
		return getHeading().getKey().getHeadingNumber();
	}

	public void setHeadingNumber(int i) {

	}

	public String getUserView() {
		return getHeading().getKey().getUserViewString();
	}

	public void setUserView(String view) {
		getHeading().setKey(new DescriptorKey(getHeadingNumber(), view));
	}

	public List<String> getRepeatableSubfields() {
		return validation.getRepeatableSubfieldCodes();
	}

	public StringText getStringText() {
		return stringText;
	}

	public List<String> getValidSubfields() {
		return validation.getValidSubfieldCodes();
	}

	public void setStringText(StringText text) {
		stringText = text;
		getHeading().setStringText(text.toString());
		populateSubfieldLists();
	}

	public abstract Descriptor getHeading();

	public final void setDescriptor(Descriptor descriptor) throws DataAccessException {
		
		setHeading(descriptor);
		//TODO paulm Add this setting of validation when a new descriptor is set (rather than setting it externally)
		// but -- need to think about AuthorityValidation -- possibly provide a seDescriptorFromTag(Tag t)
		// then we could determine the correct validation from the tagImpl
		setValidation(new DAOBibliographicValidation().load(descriptor.getCategory(), descriptor.getCorrelationValues()));
		stringText = new StringText(descriptor.getStringText());
		try {
			populateLists(new DAOCodeTable(), getLocale());
		} catch (DataAccessException e) {
			//TODO paulm eat this exception for now
			logger.warn("Exception thrown in HeadingBean constructor");
		}
		populateSubfieldLists();
	}

	protected abstract void setHeading(Descriptor descriptor);

	public List getValidSubfieldList() {
		return validSubfieldList;
	}

	protected void setValidSubfieldList(List list) {
		validSubfieldList = list;
	}

	public Set getValidSubfieldList(int i) {
		return (Set) validSubfieldList.get(i);
	}

	public void setValidSubfieldList(int i, Set set) {
		validSubfieldList.set(i, set);
	}

	public Set getRemainingValidSubfields() {
		return remainingValidSubfields;
	}

	public void setRemainingValidSubfields(Set set) {
		remainingValidSubfields = set;
	}

	public Validation getValidation() {
		return validation;
	}

	public void setValidation(Validation validation) {
		this.validation = validation;
		populateSubfieldLists();
	}

	//TODO: change it! it must call this methods from storageService
	void populateSubfieldLists() {
		//remainingValidSubfields = validation.computeRemainingValidSubfields(getStringText());
		//validSubfieldList = validation.computeValidSubfieldList(getStringText());
	}

	final public void populateLists(DAOCodeTable dao, Locale l)
			throws DataAccessException {
		setLanguageOfAccessPointList(dao.getOptionList(T_LANG_OF_ACS_PNT.class,
				l));
		setVerificationLevelList(dao.getOptionList(T_VRFTN_LVL.class, l));
		setCategoryList(dao.getOptionList(T_BIB_TAG_CAT.class, l));
		setAuthoritySourceList(dao.getOptionList(T_AUT_HDG_SRC.class, l));
		onPopulateLists(dao, l);
	}

	protected abstract void onPopulateLists(DAOCodeTable dao, Locale l)
			throws DataAccessException;

	public List getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List list) {
		categoryList = list;
	}

	public void setUsersMainLibrary(int mainLibraryNumber) {
		// do nothing as default implementation -- only shelf lists use
		// mainLibraryNumber
	}

	public void updateDescriptorFromBrowse(Descriptor d) {
		if (d instanceof NME_HDG) {
			((NME_TTL_HDG) getHeading()).setNameHeading((NME_HDG) d);
		} else if (d instanceof TTL_HDG) {
			((NME_TTL_HDG) getHeading()).setTitleHeading((TTL_HDG) d);
		}
	}

	/**
	 * Does this heading type support the Transfer Items menu option
	 * 
	 * @since 1.0
	 */
	public boolean isCanTransfer() {
		if (!getHeading().isNew()) {
			return getHeading().isCanTransfer();
		} else {
			return false;
		}
	}

	public String getBrowseFieldName() {
		return browseFieldName;
	}

	public void setBrowseFieldName(String browseFieldName) {
		this.browseFieldName = browseFieldName;
	}

	/*
	 * public boolean isNameTitleHeadingBean(){ return this instanceof
	 * NameTitleHeadingBean; }
	 */

	/**
	 * check if an HB is already presents
	 */
	public boolean hasPreviousHeadingBean() {
		return previousHeadingBean != null;
	}

	/**
	 * remove the previous HB
	 */
	public void removePreviousHeadingBean() {
		setPreviousHeadingBean(null);
	}

	/**
	 * @return Returns the previousHeadingBean.
	 */
	public HeadingBean getPreviousHeadingBean() {
		return previousHeadingBean;
	}

	/**
	 * @param previousHeadingBean
	 *            The previousHeadingBean to set.
	 */
	public void setPreviousHeadingBean(HeadingBean previousHeadingBean) {
		this.previousHeadingBean = previousHeadingBean;
	}

	public void setTypeValues(CorrelationValues correlationValues,
							  int category) {
		// TODO Override this method

	}

	public boolean isShowTable() {
        return getValidation().getMarcTagDefaultSubfieldCode() == 'b'
                || getValidation().getMarcTagDefaultSubfieldCode() == 'c'
                || getValidation().getMarcTagDefaultSubfieldCode() == 'd';
	}

	public int getLocalTypeCode() {
		int code = -1;
		if (this.getHeading() instanceof CLSTN) {
			if (((CLSTN) getHeading()).getTypeCode() == 29)
				code = 29;
		}
		return code;
	}

	public boolean isSubject() {
        return this.getHeading() instanceof SBJCT_HDG;
    }

	public boolean isSubjectBean() {
        return this instanceof SubjectHeadingBean;
	}

	public String getDisplayStringSubject() {
		String returnString = new String();
		Iterator iter = getStringText().getSubfieldList().iterator();
		while (iter.hasNext()) {
			Subfield aStringTextSubField = (Subfield) iter.next();
			String content = aStringTextSubField.getContent();
			String code = aStringTextSubField.getCode();
			if (code.equals("v") || code.equals("x") || code.equals("y")
					|| code.equals("z")) {
				returnString = returnString.trim();
				returnString = returnString.concat("--");
				returnString = returnString.concat(content);
			} else {
				returnString = returnString.concat(content);
				returnString = returnString.concat(" ");
			}
		}

		return returnString.trim();
	}

	/**
	 * @param descriptor
	 * @param isbnEditorBean
	 * @throws IOException
	 * @throws InterruptedException *
	 *             addISBNHyphens() aggiunge i trattini in automatico al codice
	 *             ISBN utilizzando una procedura php
	 * @throws DataAccessException
	 */
	public void addISBNHyphens(Descriptor descriptor,
			IsbnEditorBean isbnEditorBean) throws ErrorIsbnProcException,
			DataAccessException {
		DAOGlobalVariable dgv = new DAOGlobalVariable();
		char value_hyphen = dgv.getValueByName("isbn_hyphen").charAt(0);
		if (value_hyphen == '1') {
			if (descriptor instanceof CNTL_NBR) {
				if (((CNTL_NBR) descriptor).getTypeCode() == 9) {
					StringText st = new StringText(descriptor.getStringText());
					StringText stA = st.getSubfieldsWithCodes("a");
					String isbn = stA.getDisplayText();
					if (!st.getSubfieldsWithCodes("a").isEmpty()
							&& isbn.indexOf("-1") == -1) {
						// isbn = isbnEditorBean.formatIsbn(isbn);
						isbn = isbnEditorBean.formatIsbn(isbn.replaceAll("-",
								""));
						st.removeSubfield(0);
						st.addSubfield(0, new Subfield("a", isbn));
						descriptor.setStringText(st.toString());

					}
				}
			}
		}
	}

	public boolean isPublisherBean() {
        return this instanceof PublisherHeadingBean;
	}

	private boolean possibleDuplicate = false;

	/**
	 * pm 2011 get the current heading from the database (in catalogue view) or
	 * a copy if the same heading does not yet exist in the cataloguing view
	 * 
	 * @param cataloguingView
	 * @return
	 * @throws DataAccessException
	 */
	//TODO: The session is missing from the method
	public Descriptor getHeadingInCataloguingView(int cataloguingView)
			throws DataAccessException, HibernateException {
		Descriptor d = getHeading();
		return ((DAODescriptor) d.getDAO()).findOrCreateMyView(d
				.getHeadingNumber(), d.getUserViewString(), cataloguingView, null);
	}

	/*
	 * Moved from EditHeadingAction and body moved to Descriptor by pm
	 */
	/**
	 * a) An Heading is duplicated if it is new and the sortform already exists
	 * or b) if the heading is not new but the heading found by sortform is not
	 * itself (sortform unchanged or changed two times)
	 * 
	 * @param heading
	 * @param allowPotentialDup
	 * @throws DuplicateDescriptorException
	 * @throws MatchedHeadingInAnotherViewException
	 */
	//TODO: The session is missing from the method
	public void checkDescriptor(Descriptor heading, boolean allowPotentialDup)
			throws DuplicateDescriptorException,
			MatchedHeadingInAnotherViewException, HibernateException, SQLException {
		heading.checkDescriptor(allowPotentialDup , null);
	}

	public boolean isPossibleDuplicate() {
		return possibleDuplicate;
	}

	public void setPossibleDuplicate(boolean b) {
		possibleDuplicate = b;
	}

	public List getAuthoritySourceList() {
		return authoritySourceList;
	}

	public void setAuthoritySourceList(List authoritySourceList) {
		this.authoritySourceList = authoritySourceList;
	}

	public String getAuthoritySourceText() {
		return getHeading().getAuthoritySourceText();
	}

	public void setAuthoritySourceText(String authoritySourceText) {
		getHeading().setAuthoritySourceText(authoritySourceText);
	}

	public void initTextFromBrowse(String headingText, String browseIndex) {
		setStringText(new StringText(Subfield.SUBFIELD_DELIMITER
				+ getValidation().getMarcTagDefaultSubfieldCode() + headingText));
	}

	/**
	 * Creates a new HeadingBean of the appropriate type for the descriptor and
	 * initializes the bean to work with the given descriptor
	 * 
	 * @param d
	 * @param locale
	 * @return
	 * @throws MarcCorrelationException
	 * @throws DataAccessException
	 */
	public static HeadingBean createBeanFromDescriptor(Descriptor d,
			Locale locale) throws DataAccessException {
		HeadingBean result = DescriptorFactory.createBean(d.getCategory());
		result.setLocale(locale);
		result.setDescriptor(d);
		return result;
	}
	
	/**
	 * Update in the bean the qualifier
	 * @throws DataAccessException
	 */
	public void refreshQualifier()throws DataAccessException{
		
	}
	public void changeTypeCode(Tag currentTag){
		
	}
}