/*
 * Created on May 6, 2004
 * */
package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.descriptor.SkipInFiling;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOThesaurusDescriptor;
import org.folio.cataloging.shared.CorrelationValues;

import java.io.Serializable;

/**
 * Hibernate class for table THS_HDG
 *
 * @author paulm
 * @version $Revision: 1.17 $, $Date: 2006/09/27 08:24:29 $
 * @since 1.0
 */
public class THS_HDG extends Descriptor implements Serializable, SkipInFiling {
  private static final long serialVersionUID = 1L;
  private int skipInFiling;
  private int typeCode;
  private int indexingLanguage;

  public THS_HDG() {
    super();
    //setAccessPointLanguage(Defaults.getShort("thesaurus.accessPointLanguage"));
    setScriptingLanguage(Defaults.getString("thesaurus.scriptingLanguage"));
    //setIndexingLanguage(Defaults.getShort("thesaurus.indexingLanguage"));
    //setSkipInFiling(Defaults.getShort("title.skipInFiling"));
    setTypeCode(Defaults.getShort("thesaurus.typeCode"));
    setVerificationLevel(Defaults.getChar("thesaurus.verificationLevel"));

  }

  /**
   * Getter for indexingLanguage
   *
   * @return indexingLanguage
   */
  public int getIndexingLanguage() {
    return indexingLanguage;
  }

  /**
   * Setter for indexingLanguage
   *
   * @param s indexingLanguage
   */
  public void setIndexingLanguage(short s) {
    indexingLanguage = s;
  }

  /**
   * Getter for typeCode
   *
   * @return typeCode
   */
  public int getTypeCode() {
    return typeCode;
  }

  /**
   * Setter for typeCode
   *
   * @param s typeCode
   */
  public void setTypeCode(short s) {
    typeCode = s;
  }

  /* (non-Javadoc)
   * @see com.libricore.librisuite.business.rdms.Descriptor#getReferenceClass()
   */
  public Class getReferenceClass(Class targetClazz) {
    if (targetClazz == this.getClass()) {
      return THS_REF.class;
    } else {
      return null;
    }
  }

  /* (non-Javadoc)
   * @see librisuite.hibernate.Descriptor#getDefaultBrowseKey()
   */
  public String getDefaultBrowseKey() {
    return "354P0";
  }

  public String getNextNumberKeyFieldCode() {
    return "TS";
  }

  /* (non-Javadoc)
   * @see librisuite.hibernate.Descriptor#getDAO()
   */
  public AbstractDAO getDAO() {
    return new DAOThesaurusDescriptor();
  }

  /* (non-Javadoc)
   * @see librisuite.hibernate.Descriptor#getAccessPointClass()
   */
  public Class getAccessPointClass() {
    return SubjectAccessPoint.class;
  }


  /**
   *
   */
  public int getSkipInFiling() {
    return skipInFiling;
  }


  /**
   *
   */
  public void setSkipInFiling(short s) {
    skipInFiling = s;
  }


  /* (non-Javadoc)
   * @see librisuite.hibernate.Descriptor#getCategory()
   */
  public int getCategory() {
    return 23;
  }

  /* (non-Javadoc)
   * @see librisuite.hibernate.Descriptor#getCorrelationValues()
   */
  public CorrelationValues getCorrelationValues() {
    return new CorrelationValues(typeCode, CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED);
  }

  /* (non-Javadoc)
   * @see librisuite.hibernate.Descriptor#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    typeCode = v.getValue(1);

  }

  /* (non-Javadoc)
   * @see librisuite.hibernate.Descriptor#getSortFormParameters()
   */
  public SortFormParameters getSortFormParameters() {
    return new SortFormParameters(100, 103, getTypeCode(), 0, getSkipInFiling());
  }

  /* (non-Javadoc)
   * @see librisuite.hibernate.Descriptor#getHeadingNumberSearchIndex()
   */
  public String getHeadingNumberSearchIndexKey() {
    return "229P";
  }

  public String getLockingEntityType() {
    return "TS";
  }

}
