package org.folio.cataloging.business.descriptor;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.common.Browsable;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.dao.DAODescriptor;
import org.folio.cataloging.dao.DAOIndexList;
import org.folio.cataloging.dao.DAOSystemNextNumber;
import org.folio.cataloging.dao.persistence.DescriptorKey;
import org.folio.cataloging.dao.persistence.T_AUT_HDG_SRC;
import org.folio.cataloging.exception.DescriptorHasEmptySubfieldsException;
import org.folio.cataloging.exception.DescriptorHasNoSubfieldsException;
import org.folio.cataloging.exception.InvalidDescriptorException;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.util.HtmlUtils;
import org.folio.cataloging.util.StringText;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Locale;

public abstract class Descriptor implements PersistentObjectWithView 
{
	private static Log logger = LogFactory.getLog(Descriptor.class);
	
	private static final int CROP_LENGTH = Defaults.getInteger("tooltip.max.length");
	private short accessPointLanguage;
	private short authorityCount = 0;
	private short authoritySourceCode = T_AUT_HDG_SRC.SOURCE_NOT_SPECIFIED;
	private String authoritySourceText;
	private DescriptorKey key;
	private PersistenceState po = new PersistenceState();
	private String scriptingLanguage;
	private String sortForm;
	private String stringText;
	private short skipInFiling;
	private char verificationLevel;
	private short indexingLanguage = 0;
	private ConfigHandler configHandler = ConfigHandler.getInstance();
	
	public short getIndexingLanguage() {
		return indexingLanguage;
	}

	public void setIndexingLanguage(short indexingLanguage) {
		this.indexingLanguage = indexingLanguage;
	}

	public Descriptor() {
		setKey(new DescriptorKey());
		StringText s = new StringText();
		s.addSubfield(new Subfield("a", ""));
		setStringText(s.toString());
		setDefaultAccessPointAndIndexLang();
	}

	public void evict() throws DataAccessException {
		po.evict(this);
	}

	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		getKey().setHeadingNumber(
				dao.getNextNumber(getNextNumberKeyFieldCode()));
	}

	public String buildBrowseTerm() {
		return getDisplayText();
	}

	abstract public Class getAccessPointClass();

	public Class getAuthorityAccessPointClass() {
		return null; // default implementation
	}

	public short getAccessPointLanguage() {
		return accessPointLanguage;
	}

	final public String getAuthoritiesQueryString(Locale locale)throws DataAccessException 
	{
		return new DAOIndexList().getLocalizedIndexByKey(getHeadingNumberSearchIndexKey(), locale)
				+ " = \"" + getKey().getHeadingNumber() + "\"";
	}

	/**
	 * A persistent member for subclasses that support Authorities. Otherwise,
	 * the default stringValue of 0 is returned.
	 * 
	 * @return
	 */
	public short getAuthorityCount() {
		return authorityCount;
	}

	/**
	 * Overridable method to be more specific in determining appropriate browse
	 * key
	 * 
	 * @since 1.0
	 */
	public String getBrowseKey() {
		return getDefaultBrowseKey();
	}

	public abstract short getCategory();

	abstract public CorrelationValues getCorrelationValues();

	/**
	 * @return the language independent (key) index stringValue to be used when
	 *         browsing for entries of this type of Descriptor (e.g. Names ==
	 *         "2P0"). The stringValue returned should correspond to the stringValue of
	 *         IDX_LIST.IDX_LIST_KEY_NBR + IDX_LIST_TYPE_CDE
	 * 
	 */
	abstract public String getDefaultBrowseKey();

	/**
	 * Helper method to format stringText for display
	 * 
	 * 
	 */
	public String getDisplayText() {
		return new StringText(getStringText()).toDisplayString();
	}

	public String getSafeHtmlText() {
		return HtmlUtils.filter(getDisplayText());
	}

	public String getCroppedHtmlText() {
		String s = getSafeHtmlText();
		if (s.length() > CROP_LENGTH) {
			s = s.substring(0, CROP_LENGTH) + "...";
		}
		return s;
	}

	/**
	 * @return the language independent index key stringValue to be used when
	 *         searching by headingNumber for this type of Descriptor (e.g.
	 *         Names == "227P" (NK index)).
	 */
	abstract public String getHeadingNumberSearchIndexKey();

	/**
	 * @return a string representing a query to be used for searching the
	 *         documents attached to this heading. If the descriptor supports
	 *         headingNumber searches then the query is built using this index,
	 *         otherwise the sortform is used for searching
	 * 
	 * @since 1.0
	 */
	public String getHeadingQueryString(Locale locale)
			throws DataAccessException {
		int i = getKey().getHeadingNumber();

		if (getKey().getHeadingNumber() > 0) {
			String index = new DAOIndexList().getLocalizedIndexByKey(
					getHeadingNumberSearchIndexKey(), locale);
			if (index != null) {
				return index + " = " + getKey().getHeadingNumber();
			}
		}
		String index = new DAOIndexList().getLocalizedIndexByKey(
				getBrowseKey(), locale);
		return index + " = \"" + getSortForm() + "\"";
	}

	/**
	 * Getter for key
	 * 
	 * @return key
	 */
	public DescriptorKey getKey() {
		return key;
	}

	abstract public String getNextNumberKeyFieldCode();

	/**
	 * @return the persistent class of the cross-reference table associated with
	 *         this descriptor type (e.g. Names == NME_REF) to the referenced
	 *         target class
	 * 
	 */
	abstract public Class getReferenceClass(Class targetClazz);

	/**
	 * Getter for scriptingLanguage
	 * 
	 * @return scriptingLanguage
	 */
	public String getScriptingLanguage() {
		return scriptingLanguage;
	}

	/**
	 * Getter for sortForm
	 * 
	 * @return sortForm
	 */
	public String getSortForm() {
		return sortForm;
	}

	abstract public SortFormParameters getSortFormParameters();

	/**
	 * Getter for stringText
	 * 
	 * @return stringText
	 */
	public String getStringText() {
		return stringText;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getUpdateStatus() {
		return po.getUpdateStatus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.common.PersistentObjectWithView#getUserViewString()
	 */
	public String getUserViewString() {
		return getKey().getUserViewString();
	}

	/**
	 * Getter for verificationLevel
	 * 
	 * @return verificationLevel
	 */
	public char getVerificationLevel() {
		return verificationLevel;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isChanged() {
		return po.isChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isDeleted() {
		return po.isDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isNew() {
		return po.isNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markChanged() {
		po.markChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markDeleted() {
		po.markDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markNew() {
		po.markNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markUnchanged() {
		po.markUnchanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onDelete(Session arg0) throws CallbackException {
		return po.onDelete(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void onLoad(Session arg0, Serializable arg1) {
		po.onLoad(arg0, arg1);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onSave(Session arg0) throws CallbackException {
		return po.onSave(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onUpdate(Session arg0) throws CallbackException {
		return po.onUpdate(arg0);
	}

	/**
	 * Setter for accesPointLanguage
	 * 
	 * @param s
	 *            accesPointLanguage
	 */
	public void setAccessPointLanguage(short s) {
		accessPointLanguage = s;
	}

	/**
	 * @param s
	 */
	public void setAuthorityCount(short s) {
		authorityCount = s;
	}

	abstract public void setCorrelationValues(CorrelationValues v);

	/**
	 * Setter for key
	 * 
	 * @param hdg_key
	 *            key
	 */
	public void setKey(DescriptorKey hdg_key) {
		key = hdg_key;
	}

	/**
	 * Setter for scriptingLanguage
	 * 
	 * @param string
	 *            scriptingLanguage
	 */
	public void setScriptingLanguage(String string) {
		scriptingLanguage = string;
	}

	/**
	 * Setter for sortForm
	 * 
	 * @param string
	 *            sortForm
	 */
	public void setSortForm(String string) {
		sortForm = string;
	}

	/**
	 * Setter for stringText
	 * 
	 * @param string
	 *            stringText
	 */
	public void setStringText(String string) {
		stringText = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setUpdateStatus(int i) {
		po.setUpdateStatus(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.common.PersistentObjectWithView#setUserViewString(java.lang.String)
	 */
	public void setUserViewString(String s) {
		getKey().setUserViewString(s);
	}

	public void setVerificationLevel(char c) {
		verificationLevel = c;
	}

	/**
	 * performs validations on the descriptor (prior to saving)
	 * 
	 * @since 1.0
	 */
	public void validate() throws InvalidDescriptorException {
		StringText st = new StringText(getStringText());
		if (st.getSubfieldList().size() == 0) {
			throw new DescriptorHasNoSubfieldsException();
		}
		Iterator iter = st.getSubfieldList().iterator();
		while (iter.hasNext()) {
			Subfield s = (Subfield) iter.next();
			if (s.getContent() == null || "".equals(s.getContent())) {
				throw new DescriptorHasEmptySubfieldsException();
			}
		}
	}

	/**
	 * Does this Descriptor subclass support the Transfer Items function Default
	 * is true. Override if not supported (e.g. shelf lists)
	 * 
	 * @since 1.0
	 */
	public boolean isCanTransfer() {
		return true;
	}

	public boolean changeAffectsCacheTable() {
		return false;
	}

	public short getSkipInFiling() {
		return skipInFiling;
	}

	public void setSkipInFiling(short skipInFiling) {
		this.skipInFiling = skipInFiling;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Descriptor other = (Descriptor) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	public boolean isMatchingAnotherHeading() {
		return ((DAODescriptor) getDAO()).isMatchingAnotherHeading(this);
	}

	/*
	 * public short getIndexingLanguage(){ return 0; }
	 */
	/**
	 * Entity type code used in S_LCK_TBL
	 * 
	 * @return entity type code
	 */
	abstract public String getLockingEntityType();

	public int getHeadingNumber() {
		return key.getHeadingNumber();
	}

	public void setHeadingNumber(int i) {
		key.setHeadingNumber(i);
	}

	// TODO aggiungere due voci nel default e togliere il resto
	public void setDefaultAccessPointAndIndexLang() {
		short indexLang = 0;
		short accessPointLang = 0;
		accessPointLang = new Short(configHandler.findValue(
				"t_lang_of_acs_pnt", "accessPointLanguage"));
		indexLang = new Short(configHandler.findValue("t_lang_of_idxg",
				"accessPointLanguage"));
		this.setIndexingLanguage(indexLang);
		this.setAccessPointLanguage(accessPointLang);
	}

	/**
	 * The t_xxx_type_fnctn values are made from the combination of the type and
	 * function (which gives values 1, 2 in the correlation tables) For example
	 * 589832 = hex(00090008) = Type 9 function 8. So, the high order 2 bytes
	 * gives the type and the low order 2 bytes gives the function.
	 * 
	 * @param n
	 * @return
	 */
	public int getType(int n) {
		int type = n >> 16;
		return type;
	}

	/**
	 * So, the high order 2 bytes gives the type and the low order 2 bytes gives
	 * the function.
	 * 
	 * @param n
	 * @return
	 */
	public int getFunction(int n) {
		int function = n & 65535;
		return function;
	}

	public void checkDescriptor(boolean allowPotentialDup)
			throws DuplicateDescriptorException,
			MatchedHeadingInAnotherViewException {
		logger.debug("checkDescriptor(" + allowPotentialDup + ")");
		if (isDeleted()) { // no check needed
			return;
		}
		if (isMatchingAnotherHeading()) {
			throw new DuplicateDescriptorException(this);
		}
		if (!allowPotentialDup && hasMatchingSortformInAnotherView()) {
			throw new MatchedHeadingInAnotherViewException();
		}
	}

	public boolean hasMatchingSortformInAnotherView() {
		return ((DAODescriptor) getDAO())
				.hasMatchingSortformInAnotherView(this);
	}

	public short getAuthoritySourceCode() {
		return authoritySourceCode;
	}

	public void setAuthoritySourceCode(short authoritySourceCode) {
		this.authoritySourceCode = authoritySourceCode;
		if (authoritySourceCode != T_AUT_HDG_SRC.SOURCE_IN_SUBFIELD_2) {
			setAuthoritySourceText(null);
		}
	}

	public String getAuthoritySourceText() {
		return authoritySourceText;
	}

	public void setAuthoritySourceText(String authoritySourceText) {
		this.authoritySourceText = authoritySourceText;
	}

	/**
	 * Modify the state of this Descriptor based on the contents of the
	 * currentTag. Used within the worksheet when the user specifies a
	 * particular tag/indicator/values and then after a browse decides to create
	 * a new heading. The user wants the default values for the new heading to
	 * reflect the settings in the worksheet, rather than the global default
	 * settings for new headings.
	 * 
	 * @param currentTag
	 */
	public void setDefaultsFromWorksheet(Tag currentTag) {
		// TODO paulm do we need to do something different for name/title tags
		// where we add a new name?
		if (currentTag instanceof Browsable) {
			Descriptor d = ((Browsable) currentTag).getDescriptor();
			if (d.getClass().equals(this.getClass())) {
				setCorrelationValues(d.getCorrelationValues());
				setAuthoritySourceCode(d.getAuthoritySourceCode());
				setSkipInFiling(d.getSkipInFiling());
			}
		}
	}

	public String toString() 
	{
	   return ToStringBuilder.reflectionToString(this);
	}
}