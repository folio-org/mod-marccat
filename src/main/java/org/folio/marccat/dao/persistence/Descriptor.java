package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.folio.marccat.business.cataloguing.common.SortFormObject;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.SortFormException;
import org.folio.marccat.business.descriptor.MatchedHeadingInAnotherViewException;
import org.folio.marccat.business.descriptor.SortFormParameters;
import org.folio.marccat.business.descriptor.SortformUtils;
import org.folio.marccat.dao.DescriptorDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.*;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;
import static org.folio.marccat.config.constants.Global.EMPTY_STRING;


/**
 * The base class for each descriptor.
 */
public abstract class Descriptor implements PersistentObjectWithView, SortFormObject {

  /**
   * The copy subject indicator.
   */
  private char copyToSubjectIndicator;

  /**
   * The access point language.
   */
  private int accessPointLanguage;

  /**
   * The authority count.
   */
  private int authorityCount = 0;

  /**
   * The authority source code.
   */
  private int authoritySourceCode = T_AUT_HDG_SRC.SOURCE_NOT_SPECIFIED;

  /**
   * The authority source text.
   */
  private String authoritySourceText;

  /**
   * The key.
   */
  private DescriptorKey key;

  /**
   * The po.
   */
  private PersistenceState po = new PersistenceState();

  /**
   * The scripting language.
   */
  private String scriptingLanguage;

  /**
   * The sort form.
   */
  private String sortForm;

  /**
   * The string text.
   */
  private String stringText;

  /**
   * The skip in filing.
   */
  private int skipInFiling;

  /**
   * The verification level.
   */
  private char verificationLevel;

  /**
   * The indexing language.
   */
  private int indexingLanguage = 0;

  /**
   * Instantiates a new descriptor.
   */
  public Descriptor() {
    setKey(new DescriptorKey());
    StringText s = new StringText();
    s.addSubfield(new Subfield("a", EMPTY_STRING));
    setStringText(s.toString());
  }

  /**
   * @throws SortFormException
   */
  @Override
  public void calculateAndSetSortForm() {
    setSortForm(SortformUtils.get().defaultSortform(getStringText()));
  }

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
   * Builds the browse term.
   *
   * @return the string
   */
  public String buildBrowseTerm() {
    return getDisplayText();
  }

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
   * Setter for accesPointLanguage.
   *
   * @param s accesPointLanguage
   */
  public void setAccessPointLanguage(int s) {
    accessPointLanguage = s;
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
   * Sets the authority count.
   *
   * @param s the new authority count
   */
  public void setAuthorityCount(int s) {
    authorityCount = s;
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
   * Getter for key.
   *
   * @return key
   */
  public DescriptorKey getKey() {
    return key;
  }

  /**
   * Setter for key.
   *
   * @param hdg_key key
   */
  public void setKey(DescriptorKey hdg_key) {
    key = hdg_key;
  }

  /**
   * Helper method to format stringText for display.
   *
   * @return the display text
   */
  public String getDisplayText() {
    return new StringText(getStringText()).toDisplayString();
  }

  /**
   * Getter for scriptingLanguage.
   *
   * @return scriptingLanguage
   */
  public String getScriptingLanguage() {
    return scriptingLanguage;
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
   * Getter for sortForm.
   *
   * @return sortForm
   */
  public String getSortForm() {
    return sortForm;
  }

  /**
   * Setter for sortForm.
   *
   * @param string sortForm
   */
  public void setSortForm(String string) {
    this.sortForm = string;
  }

  /**
   * Getter for stringText.
   *
   * @return stringText
   */
  public String getStringText() {
    return stringText;
  }

  /**
   * Setter for stringText.
   *
   * @param string stringText
   */
  public void setStringText(String string) {
    stringText = string;
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
   * Sets the update status.
   *
   * @param i the new update status
   */
  public void setUpdateStatus(int i) {
    po.setUpdateStatus(i);
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
   * Sets the user view string.
   *
   * @param s the new user view string
   */
  public void setUserViewString(String s) {
    getKey().setUserViewString(s);
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
   * Sets the verification level.
   *
   * @param c the new verification level
   */
  public void setVerificationLevel(char c) {
    verificationLevel = c;
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
      return other.key == null;
    } else return key.equals(other.key);
  }

  /**
   * Entity type code used in S_LCK_TBL.
   *
   * @return entity type code
   */
  public abstract String getLockingEntityType();

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
    return n >> 16;
  }

  /**
   * So, the high order 2 bytes gives the type and the low order 2 bytes gives
   * the function.
   *
   * @param n the n
   * @return the function
   */
  public int getFunction(int n) {
    return n & 65535;
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
   * Generate new key.
   *
   * @throws DataAccessException the data access exception
   */
  public void generateNewKey(final Session session) throws HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    getKey().setHeadingNumber(dao.getNextNumber(getNextNumberKeyFieldCode(), session));
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
  public String toString() {
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

  public void setConfigValues(final Map<String, String> configuration) {
    setVerificationLevel(configuration.get("title.verificationLevel").charAt(0));
    setAccessPointLanguage(Integer.parseInt(configuration.get("title.accessPointLanguage")));
    setIndexingLanguage(Integer.parseInt(configuration.get("title.indexingLanguage")));
    setScriptingLanguage(configuration.get("subject.scriptingLanguage"));
    setCopyToSubjectIndicator(configuration.get("name.copyToSubjectIndicator").charAt(0));
  }

  /**
   * Gets the category.
   *
   * @return the category
   */
  public abstract int getCategory();

  /**
   * Gets the access point class.
   *
   * @return the access point class
   */
  public abstract Class getAccessPointClass();

  /**
   * Gets the correlation values.
   *
   * @return the correlation values
   */
  public abstract CorrelationValues getCorrelationValues();

  /**
   * Sets the correlation values.
   *
   * @param v the new correlation values
   */
  public abstract void setCorrelationValues(CorrelationValues v);

  /**
   * Gets the default browse key.
   *
   * @return the language independent (key) index value to be used when
   * browsing for entries of this type of Descriptor (e.g. Names ==
   * "2P0"). The value returned should correspond to the value of
   * IDX_LIST.IDX_LIST_KEY_NBR + IDX_LIST_TYPE_CDE
   */
  public abstract String getDefaultBrowseKey();


  /**
   * Gets the heading number search index key.
   *
   * @return the language independent index key value to be used when
   * searching by headingNumber for this type of Descriptor (e.g.
   * Names == "227P" (NK index)).
   */
  public abstract String getHeadingNumberSearchIndexKey();


  /**
   * Gets the next number key field code.
   *
   * @return the next number key field code
   */
  public abstract String getNextNumberKeyFieldCode();

  /**
   * Gets the reference class.
   *
   * @param targetClazz the target clazz
   * @return the persistent class of the cross-reference table associated with
   * this descriptor type (e.g. Names == NME_REF) to the referenced
   * target class
   */
  public abstract Class getReferenceClass(Class targetClazz);

  /**
   * Gets the sort form parameters.
   *
   * @return the sort form parameters
   */
  public abstract SortFormParameters getSortFormParameters();
}
