package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.DuplicateDescriptorException;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.common.PersistentObjectWithView;
import org.folio.cataloging.business.descriptor.MatchedHeadingInAnotherViewException;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.DAODescriptor;
import org.folio.cataloging.dao.SystemNextNumberDAO;
import org.folio.cataloging.exception.DescriptorHasEmptySubfieldsException;
import org.folio.cataloging.exception.DescriptorHasNoSubfieldsException;
import org.folio.cataloging.exception.InvalidDescriptorException;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;


/**
 * The base class for each descriptor.
 */
public abstract class Descriptor implements PersistentObjectWithView
{

  /** The copy subject indicator. */
  private char copyToSubjectIndicator;

  /** The access point language. */
	private int accessPointLanguage;

	/** The authority count. */
	private int authorityCount = 0;

	/** The authority source code. */
	private int authoritySourceCode = T_AUT_HDG_SRC.SOURCE_NOT_SPECIFIED;

	/** The authority source text. */
	private String authoritySourceText;

	/** The key. */
	private DescriptorKey key;

	/** The po. */
	private PersistenceState po = new PersistenceState();

	/** The scripting language. */
	private String scriptingLanguage;

	/** The sort form. */
	private String sortForm;

	/** The string text. */
	private String stringText;

	/** The skip in filing. */
	private int skipInFiling;

	/** The verification level. */
	private char verificationLevel;

	/** The indexing language. */
	private int indexingLanguage = 0;

	/**
	 * Gets the indexing language.
	 *
	 * @return the indexing language
	 */
	public int getIndexingLanguage() {
		return indexingLanguage;
	}

	/**
	 * Sets the indexing language.
	 *
	 * @param indexingLanguage the new indexing language
	 */
	public void setIndexingLanguage(int indexingLanguage) {
		this.indexingLanguage = indexingLanguage;
	}

	/**
	 * Instantiates a new descriptor.
	 */
	public Descriptor() {
		setKey(new DescriptorKey());
		StringText s = new StringText();
		s.addSubfield(new Subfield("a", ""));
		setStringText(s.toString());
	}

	/**
	 * Evict.
	 *
	 * @throws DataAccessException the data access exception
	 */
	public void evict() throws DataAccessException {
		po.evict(this);
	}

	/**
	 * Builds the browse term.
	 *
	 * @return the string
	 */
	public String buildBrowseTerm() {
		return getDisplayText();
	}

	/**
	 * Gets the access point class.
	 *
	 * @return the access point class
	 */
	abstract public Class getAccessPointClass();

	/**
	 * Gets the authority access point class.
	 *
	 * @return the authority access point class
	 */
	public Class getAuthorityAccessPointClass() {
		return null;
	}

	/**
	 * Gets the access point language.
	 *
	 * @return the access point language
	 */
	public int getAccessPointLanguage() {
		return accessPointLanguage;
	}

	/**
	 * A persistent member for subclasses that support Authorities. Otherwise,
	 * the default value of 0 is returned.
	 *
	 * @return the authority count
	 */
	public int getAuthorityCount() {
		return authorityCount;
	}

	/**
	 * Overridable method to be more specific in determining appropriate browse
	 * key.
	 *
	 * @return the browse key
	 * @since 1.0
	 */
	public String getBrowseKey() {
		return getDefaultBrowseKey();
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public abstract int getCategory();

	/**
	 * Gets the correlation values.
	 *
	 * @return the correlation values
	 */
	abstract public CorrelationValues getCorrelationValues();

	/**
	 * Gets the default browse key.
	 *
	 * @return the language independent (key) index value to be used when
	 * browsing for entries of this type of Descriptor (e.g. Names ==
	 * "2P0"). The value returned should correspond to the value of
	 * IDX_LIST.IDX_LIST_KEY_NBR + IDX_LIST_TYPE_CDE
	 */
	abstract public String getDefaultBrowseKey();

	/**
	 * Helper method to format stringText for display.
	 *
	 * @return the display text
	 */
	public String getDisplayText() {
		return new StringText(getStringText()).toDisplayString();
	}


	/**
	 * Gets the heading number search index key.
	 *
	 * @return the language independent index key value to be used when
	 * searching by headingNumber for this type of Descriptor (e.g.
	 * Names == "227P" (NK index)).
	 */
	abstract public String getHeadingNumberSearchIndexKey();


	/**
	 * Getter for key.
	 *
	 * @return key
	 */
	public DescriptorKey getKey() {
		return key;
	}

	/**
	 * Gets the next number key field code.
	 *
	 * @return the next number key field code
	 */
	abstract public String getNextNumberKeyFieldCode();

	/**
	 * Gets the reference class.
	 *
	 * @param targetClazz the target clazz
	 * @return the persistent class of the cross-reference table associated with
	 * this descriptor type (e.g. Names == NME_REF) to the referenced
	 * target class
	 */
	abstract public Class getReferenceClass(Class targetClazz);

	/**
	 * Getter for scriptingLanguage.
	 *
	 * @return scriptingLanguage
	 */
	public String getScriptingLanguage() {
		return scriptingLanguage;
	}

	/**
	 * Getter for sortForm.
	 *
	 * @return sortForm
	 */
	public String getSortForm() {
		return sortForm;
	}

	/**
	 * Gets the sort form parameters.
	 *
	 * @return the sort form parameters
	 */
	abstract public SortFormParameters getSortFormParameters();

	/**
	 * Getter for stringText.
	 *
	 * @return stringText
	 */
	public String getStringText() {
		return stringText;
	}

	/**
	 * Gets the update status.
	 *
	 * @return the update status
	 */
	public int getUpdateStatus() {
		return po.getUpdateStatus();
	}


	/**
	 * Gets the user view string.
	 *
	 * @return the user view string
	 */
	public String getUserViewString() {
		return getKey().getUserViewString();
	}

	/**
	 * Getter for verificationLevel.
	 *
	 * @return verificationLevel
	 */
	public char getVerificationLevel() {
		return verificationLevel;
	}

	/**
	 * Checks if is changed.
	 *
	 * @return true, if is changed
	 */
	public boolean isChanged() {
		return po.isChanged();
	}

	/**
	 * Checks if is deleted.
	 *
	 * @return true, if is deleted
	 */
	public boolean isDeleted() {
		return po.isDeleted();
	}

	/**
	 * Checks if is new.
	 *
	 * @return true, if is new
	 */
	public boolean isNew() {
		return po.isNew();
	}

	/**
	 * Mark changed.
	 */
	public void markChanged() {
		po.markChanged();
	}

	/**
	 * Mark deleted.
	 */
	public void markDeleted() {
		po.markDeleted();
	}

	/**
	 * Mark new.
	 */
	public void markNew() {
		po.markNew();
	}

	/**
	 * Mark unchanged.
	 */
	public void markUnchanged() {
		po.markUnchanged();
	}

	/**
	 * On delete.
	 *
	 * @param arg0 the arg 0
	 * @return true, if successful
	 * @throws CallbackException the callback exception
	 */
	public boolean onDelete(Session arg0) throws CallbackException {
		return po.onDelete(arg0);
	}

	/**
	 * On load.
	 *
	 * @param arg0 the arg 0
	 * @param arg1 the arg 1
	 */
	public void onLoad(Session arg0, Serializable arg1) {
		po.onLoad(arg0, arg1);
	}

	/**
	 * On save.
	 *
	 * @param arg0 the arg 0
	 * @return true, if successful
	 * @throws CallbackException the callback exception
	 */
	public boolean onSave(Session arg0) throws CallbackException {
		return po.onSave(arg0);
	}

	/**
	 * On update.
	 *
	 * @param arg0 the arg 0
	 * @return true, if successful
	 * @throws CallbackException the callback exception
	 */
	public boolean onUpdate(Session arg0) throws CallbackException {
		return po.onUpdate(arg0);
	}

	/**
	 * Setter for accesPointLanguage.
	 *
	 * @param s accesPointLanguage
	 */
	public void setAccessPointLanguage(int s) {
		accessPointLanguage = s;
	}

	/**
	 * Sets the authority count.
	 *
	 * @param s the new authority count
	 */
	public void setAuthorityCount(int s) {
		authorityCount = s;
	}

	/**
	 * Sets the correlation values.
	 *
	 * @param v the new correlation values
	 */
	abstract public void setCorrelationValues(CorrelationValues v);

	/**
	 * Setter for key.
	 *
	 * @param hdg_key  key
	 */
	public void setKey(DescriptorKey hdg_key) {
		key = hdg_key;
	}

	/**
	 * Setter for scriptingLanguage.
	 *
	 * @param string scriptingLanguage
	 */
	public void setScriptingLanguage(String string) {
		scriptingLanguage = string;
	}

	/**
	 * Setter for sortForm.
	 *
	 * @param string sortForm
	 */
	public void setSortForm(String string) {
		sortForm = string;
	}

	/**
	 * Setter for stringText.
	 *
	 * @param string  stringText
	 */
	public void setStringText(String string) {
		stringText = string;
	}

	/**
	 * Sets the update status.
	 *
	 * @param i the new update status
	 */
	public void setUpdateStatus(int i) {
		po.setUpdateStatus(i);
	}

	/**
	 * Sets the user view string.
	 *
	 * @param s the new user view string
	 */
	public void setUserViewString(String s) {
		getKey().setUserViewString(s);
	}

	/**
	 * Sets the verification level.
	 *
	 * @param c the new verification level
	 */
	public void setVerificationLevel(char c) {
		verificationLevel = c;
	}


	/**
	 * Does this Descriptor subclass support the Transfer Items function Default
	 * is true. Override if not supported (e.g. shelf lists)
	 *
	 * @return true, if is can transfer
	 */
	public boolean isCanTransfer() {
		return true;
	}

	/**
	 * Change affects cache table.
	 *
	 * @return true, if successful
	 */
	public boolean changeAffectsCacheTable() {
		return false;
	}

	/**
	 * Gets the skip in filing.
	 *
	 * @return the skip in filing
	 */
	public int getSkipInFiling() {
		return skipInFiling;
	}

	/**
	 * Sets the skip in filing.
	 *
	 * @param skipInFiling the new skip in filing
	 */
	public void setSkipInFiling(int skipInFiling) {
		this.skipInFiling = skipInFiling;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/**
	 * Entity type code used in S_LCK_TBL.
	 *
	 * @return entity type code
	 */
	abstract public String getLockingEntityType();

	/**
	 * Gets the heading number.
	 *
	 * @return the heading number
	 */
	public int getHeadingNumber() {
		return key.getHeadingNumber();
	}

	/**
	 * Sets the heading number.
	 *
	 * @param i the new heading number
	 */
	public void setHeadingNumber(int i) {
		key.setHeadingNumber(i);
	}


	/**
	 * The t_xxx_type_fnctn values are made from the combination of the type and
	 * function (which gives values 1, 2 in the correlation tables) For example
	 * 589832 = hex(00090008) = Type 9 function 8. So, the high order 2 bytes
	 * gives the type and the low order 2 bytes gives the function.
	 *
	 * @param n the n
	 * @return the type
	 */
	public int getType(int n) {
		int type = n >> 16;
		return type;
	}

	/**
	 * So, the high order 2 bytes gives the type and the low order 2 bytes gives
	 * the function.
	 *
	 * @param n the n
	 * @return the function
	 */
	public int getFunction(int n) {
		int function = n & 65535;
		return function;
	}

	/**
	 * Gets the authority source code.
	 *
	 * @return the authority source code
	 */
	public int getAuthoritySourceCode() {
		return authoritySourceCode;
	}

	/**
	 * Sets the authority source code.
	 *
	 * @param authoritySourceCode the new authority source code
	 */
	public void setAuthoritySourceCode(int authoritySourceCode) {
		this.authoritySourceCode = authoritySourceCode;
		if (authoritySourceCode != T_AUT_HDG_SRC.SOURCE_IN_SUBFIELD_2) {
			setAuthoritySourceText(null);
		}
	}


	/**
	 * Check descriptor.
	 *
	 * @param allowPotentialDup the allow potential dup
	 * @param session the session
	 * @throws DuplicateDescriptorException the duplicate descriptor exception
	 * @throws MatchedHeadingInAnotherViewException the matched heading in another view exception
	 * @throws HibernateException the hibernate exception
	 * @throws SQLException the SQL exception
	 */
	@Deprecated
	//TODO: move this method in API
	public void checkDescriptor(boolean allowPotentialDup, final Session session)
			throws DuplicateDescriptorException,
			MatchedHeadingInAnotherViewException, HibernateException, SQLException {

		if (isDeleted()) { // no check needed
			return;
		}
		if (isMatchingAnotherHeading(session)) {
			throw new DuplicateDescriptorException(this);
		}
		if (!allowPotentialDup && hasMatchingSortformInAnotherView(session)) {
			throw new MatchedHeadingInAnotherViewException();
		}
	}

	/**
	 * Checks if is matching another heading.
	 *
	 * @param session the session
	 * @return true, if is matching another heading
	 * @throws HibernateException the hibernate exception
	 * @throws SQLException the SQL exception
	 */
	@Deprecated
	//TODO: move this method in API
	public boolean isMatchingAnotherHeading(final Session session) throws HibernateException, SQLException {
		return ((DAODescriptor) getDAO()).isMatchingAnotherHeading(this, session);
	}

	/**
	 * Checks for matching sortform in another view.
	 *
	 * @param session the session
	 * @return true, if successful
	 * @throws HibernateException the hibernate exception
	 * @throws SQLException the SQL exception
	 */
	@Deprecated
	//TODO: move this method in API
	public boolean hasMatchingSortformInAnotherView(final Session session) throws HibernateException, SQLException {
		return ((DAODescriptor) getDAO())
				.hasMatchingSortformInAnotherView(this, session);
	}

	/**
	 * Generate new key.
	 *
	 * @throws DataAccessException the data access exception
	 */
	public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
		SystemNextNumberDAO dao = new SystemNextNumberDAO();
		getKey().setHeadingNumber(dao.getNextNumber(getNextNumberKeyFieldCode(), session));
	}

	/**
	 * performs validations on the descriptor (prior to saving).
	 *
	 * @throws InvalidDescriptorException the invalid descriptor exception
	 * @since 1.0
	 */
	@Deprecated
	//TODO: move this method in API
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
	 * Gets the authority source text.
	 *
	 * @return the authority source text
	 */
	public String getAuthoritySourceText() {
		return authoritySourceText;
	}

	/**
	 * Sets the authority source text.
	 *
	 * @param authoritySourceText the new authority source text
	 */
	public void setAuthoritySourceText(String authoritySourceText) {
		this.authoritySourceText = authoritySourceText;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}

  /**
   * Getter for copyToSubjectIndicator
   *
   * @return copyToSubjectIndicator
   */
  public char getCopyToSubjectIndicator() {
    return copyToSubjectIndicator;
  }
  /**
   * Setter for copySubjectIndicator
   *
   * @param c copySubjectIndicator
   */
  public void setCopyToSubjectIndicator(char c) {
    copyToSubjectIndicator = c;
  }

	public void setConfigValues(final Map<String, String> configuration){
	  setVerificationLevel(configuration.get("title.verificationLevel").charAt(0));
	  setAccessPointLanguage(configuration.get("title.accessPointLanguage").charAt(0));
	  setIndexingLanguage(Integer.parseInt(configuration.get("title.indexingLanguage")));
	  setScriptingLanguage(configuration.get("subject.scriptingLanguage"));
	  setCopyToSubjectIndicator(configuration.get("name.copyToSubjectIndicator").charAt(0));
  }
}
