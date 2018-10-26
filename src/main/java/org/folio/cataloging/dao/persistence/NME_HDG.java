package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.NameDescriptorDAO;
import org.folio.cataloging.shared.CorrelationValues;

import java.io.Serializable;

/**
 * Hibernate class for table NME_HDG.
 *
 * @author paulm
 * @author carment
 */
public class NME_HDG extends Descriptor implements Serializable {
  /** The copy subject indicator. */
  //private char copyToSubjectIndicator;

  /**
   * The indexing language.
   */
  private int indexingLanguage;

  /**
   * The sub type code.
   */
  private int subTypeCode;

  /**
   * The type code.
   */
  private int typeCode;

  /**
   * Instantiates a new nme hdg.
   */
  public NME_HDG() {
    super();
  }

  /* (non-Javadoc)
   * @see Descriptor#getAccessPointClass()
   */
  public Class getAccessPointClass() {
    return NameAccessPoint.class;
  }

  /* (non-Javadoc)
   * @see Descriptor#getCategory()
   */
  public int getCategory() {
    return 17;
  }


  /* (non-Javadoc)
   * @see Descriptor#getCorrelationValues()
   */
  public CorrelationValues getCorrelationValues() {
    return new CorrelationValues(
      typeCode,
      subTypeCode,
      CorrelationValues.UNDEFINED);
  }

  /* (non-Javadoc)
   * @see Descriptor#setCorrelationValues(CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    typeCode = v.getValue(1);
    subTypeCode = v.getValue(2);
  }

  /**
   * Gets the dao.
   *
   * @return the dao
   */
  public AbstractDAO getDAO() {
    return new NameDescriptorDAO();
  }

  /* (non-Javadoc)
   * @see Descriptor#getDefaultBrowseKey()
   */
  public String getDefaultBrowseKey() {
    return "2P0";
  }

  /* (non-Javadoc)
   * @see Descriptor#getHeadingNumberSearchIndexKey()
   */
  public String getHeadingNumberSearchIndexKey() {
    return "227P";
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
   * @see Descriptor#getNextNumberKeyFieldCode()
   */
  public String getNextNumberKeyFieldCode() {
    return "NH";
  }

  /* (non-Javadoc)
   * @see Descriptor#getReferenceClass(java.lang.Class)
   */
  public Class getReferenceClass(Class targetClazz) {
    if (targetClazz == NME_HDG.class) {
      return NME_REF.class;
    } else if (targetClazz == NME_TTL_HDG.class) {
      return NME_NME_TTL_REF.class;
    } else if (targetClazz == TTL_HDG.class) {
      return NME_TO_TTL_REF.class;
    } else {
      return null;
    }
  }

  /* (non-Javadoc)
   * @see Descriptor#getSortFormParameters()
   */
  public SortFormParameters getSortFormParameters() {
    return new SortFormParameters(
      100,
      101,
      getTypeCode(),
      getSubTypeCode(),
      0);
  }

  /**
   * Gets the sub type code.
   *
   * @return the sub type code
   */
  public int getSubTypeCode() {
    return subTypeCode;
  }

  /**
   * Sets the sub type code.
   *
   * @param s the new sub type code
   */
  public void setSubTypeCode(int s) {
    subTypeCode = s;
  }

  /**
   * Gets the type code.
   *
   * @return the type code
   */
  public int getTypeCode() {
    return typeCode;
  }

  /**
   * Sets the type code.
   *
   * @param s the new type code
   */
  public void setTypeCode(int s) {
    typeCode = s;
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
    return "NH";
  }


}
