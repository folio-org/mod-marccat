package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.descriptor.SkipInFiling;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.TitleDescriptorDAO;
import org.folio.cataloging.shared.CorrelationValues;

import java.io.Serializable;

/**
 * Hibernate class for table TTL_HDG.
 *
 * @author paulm
 * @author carment
 */
public class TTL_HDG extends Descriptor implements SkipInFiling, Serializable {

  /**
   * The copy to subject indicator.
   */
  private char copyToSubjectIndicator;

  /**
   * The indexing language.
   */
  private int indexingLanguage;

  /**
   * The skip in filing.
   */
  private int skipInFiling;

  /**
   * Instantiates a new ttl hdg.
   */
  public TTL_HDG() {
    super ( );
  }


  /**
   * Gets the copy to subject indicator.
   *
   * @return the copy to subject indicator
   */
  public char getCopyToSubjectIndicator() {
    return copyToSubjectIndicator;
  }

  /**
   * Sets the copy to subject indicator.
   *
   * @param c the new copy to subject indicator
   */
  public void setCopyToSubjectIndicator(char c) {
    copyToSubjectIndicator = c;
  }

  /* (non-Javadoc)
   * @see Descriptor#getIndexingLanguage()
   */
  public int getIndexingLanguage() {
    return indexingLanguage;
  }

  /* (non-Javadoc)
   * @see Descriptor#setIndexingLanguage(short)
   */
  public void setIndexingLanguage(int s) {
    indexingLanguage = s;
  }


  /* (non-Javadoc)
   * @see Descriptor#getReferenceClass(java.lang.Class)
   */
  public Class getReferenceClass(Class targetClazz) {
    if (targetClazz == TTL_HDG.class) {
      return TTL_REF.class;
    } else if (targetClazz == NME_TTL_HDG.class) {
      return TTL_NME_TTL_REF.class;
    } else if (targetClazz == NME_HDG.class) {
      return NME_TO_TTL_REF.class;
    } else {
      return null;
    }
  }

  /* (non-Javadoc)
   * @see Descriptor#getSkipInFiling()
   */
  public int getSkipInFiling() {
    return skipInFiling;
  }

  /**
   * Sets the skip in filing.
   *
   * @param s the new skip in filing
   */
  public void setSkipInFiling(int s) {
    skipInFiling = s;
  }


  /* (non-Javadoc)
   * @see Descriptor#getDefaultBrowseKey()
   */
  public String getDefaultBrowseKey() {
    return "7P0";
  }


  /**
   * Gets the dao.
   *
   * @return the dao
   */
  public AbstractDAO getDAO() {
    return new TitleDescriptorDAO ( );
  }


  /* (non-Javadoc)
   * @see Descriptor#getAccessPointClass()
   */
  public Class getAccessPointClass() {
    return TitleAccessPoint.class;
  }


  /* (non-Javadoc)
   * @see Descriptor#getNextNumberKeyFieldCode()
   */
  public String getNextNumberKeyFieldCode() {
    return "TH";
  }


  /* (non-Javadoc)
   * @see Descriptor#getCategory()
   */
  public int getCategory() {
    return 22;
  }


  /* (non-Javadoc)
   * @see Descriptor#getCorrelationValues()
   */
  public CorrelationValues getCorrelationValues() {
    return new CorrelationValues ( );
  }

  /* (non-Javadoc)
   * @see Descriptor#setCorrelationValues(CorrelationValues)
   */
  @Override
  public void setCorrelationValues(CorrelationValues v) {

  }

  /* (non-Javadoc)
   * @see Descriptor#getSortFormParameters()
   */
  public SortFormParameters getSortFormParameters() {
    return new SortFormParameters (100, 102, 0, 0, getSkipInFiling ( ));
  }

  /* (non-Javadoc)
   * @see Descriptor#getHeadingNumberSearchIndexKey()
   */
  public String getHeadingNumberSearchIndexKey() {
    return "228P";
  }

  /* (non-Javadoc)
   * @see Descriptor#changeAffectsCacheTable()
   */
  public boolean changeAffectsCacheTable() {
    return true;
  }

  /* (non-Javadoc)
   * @see Descriptor#getLockingEntityType()
   */
  public String getLockingEntityType() {
    return "TH";
  }
}
