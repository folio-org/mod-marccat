package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.common.SortFormException;
import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.business.descriptor.SortFormParameters;
import org.folio.marccat.business.descriptor.SortformUtils;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.SubjectDescriptorDAO;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Hibernate class for table SBJCT_HDG.
 *
 * @author paulm
 * @author carment
 */
public class SBJCT_HDG extends Descriptor implements Serializable, SkipInFiling {

  /**
   * The copy from heading type.
   */
  private String copyFromHeadingType;

  /**
   * The copy from heading number.
   */
  private Integer copyFromHeadingNumber;

  /**
   * The skip in filing.
   */
  private int skipInFiling;

  /**
   * The type code.
   */
  private int typeCode;

  /**
   * The source code.
   */
  private int sourceCode;

  /**
   * The secondary source code.
   */
  private String secondarySourceCode;

  /**
   * Instantiates a new sbjct hdg.
   */
  public SBJCT_HDG() {
    super();
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
   * @see Descriptor#getAuthoritySourceCode()
   */
  @Override
  public int getAuthoritySourceCode() {

    return getSourceCode();
  }

  /* (non-Javadoc)
   * @see Descriptor#setAuthoritySourceCode(int)
   */
  @Override
  public void setAuthoritySourceCode(int authoritySourceCode) {
    setSourceCode(authoritySourceCode);
  }

  /* (non-Javadoc)
   * @see Descriptor#getReferenceClass(java.lang.Class)
   */
  public Class getReferenceClass(Class targetClazz) {
    if (targetClazz == this.getClass()) {
      return SBJCT_REF.class;
    } else {
      return null;
    }
  }

  /* (non-Javadoc)
   * @see Descriptor#getDefaultBrowseKey()
   */
  public String getDefaultBrowseKey() {
    return "9P0";
  }

  /* (non-Javadoc)
   * @see Descriptor#getNextNumberKeyFieldCode()
   */
  public String getNextNumberKeyFieldCode() {
    return "SH";
  }

  /**
   * Gets the dao.
   *
   * @return the dao
   */
  public AbstractDAO getDAO() {
    return new SubjectDescriptorDAO();
  }

  /* (non-Javadoc)
   * @see Descriptor#getAccessPointClass()
   */
  public Class getAccessPointClass() {
    return SubjectAccessPoint.class;
  }

  /**
   * Gets the copy from heading number.
   *
   * @return the copy from heading number
   */
  public Integer getCopyFromHeadingNumber() {
    return copyFromHeadingNumber;
  }

  /**
   * Sets the copy from heading number.
   *
   * @param integer the new copy from heading number
   */
  public void setCopyFromHeadingNumber(Integer integer) {
    copyFromHeadingNumber = integer;
  }

  /**
   * Gets the copy from heading type.
   *
   * @return the copy from heading type
   */
  public String getCopyFromHeadingType() {
    return copyFromHeadingType;
  }

  /**
   * Sets the copy from heading type.
   *
   * @param string the new copy from heading type
   */
  public void setCopyFromHeadingType(String string) {
    copyFromHeadingType = string;
  }

  /**
   * Gets the secondary source code.
   *
   * @return the secondary source code
   */
  public String getSecondarySourceCode() {
    return secondarySourceCode;
  }

  /**
   * Sets the secondary source code.
   *
   * @param string the new secondary source code
   */
  public void setSecondarySourceCode(String string) {
    if (SubjectSource.isOtherSource(getSourceCode())) {
      secondarySourceCode = string;
    } else {
      secondarySourceCode = null;
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
  public void setSkipInFiling(short s) {
    skipInFiling = s;
  }

  /**
   * Gets the source code.
   *
   * @return the source code
   */
  public int getSourceCode() {
    return sourceCode;
  }

  /**
   * Sets the source code.
   *
   * @param s the new source code
   */
  public void setSourceCode(int s) {
    sourceCode = s;
    if (!SubjectSource.isOtherSource(s)) {
      setSecondarySourceCode(null);
    }
  }

  /* (non-Javadoc)
   * @see Descriptor#getCategory()
   */
  public int getCategory() {
    return 18;
  }


  /* (non-Javadoc)
   * @see Descriptor#getCorrelationValues()
   */
  public CorrelationValues getCorrelationValues() {
    return new CorrelationValues(typeCode, CorrelationValues.UNDEFINED,
      sourceCode);
  }


  /* (non-Javadoc)
   * @see Descriptor#setCorrelationValues(CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    typeCode = v.getValue(1);
    sourceCode = v.getValue(3);
  }


  /* (non-Javadoc)
   * @see Descriptor#getSortFormParameters()
   */
  public SortFormParameters getSortFormParameters() {
    return new SortFormParameters(100, 103, getTypeCode(), 0,
      getSkipInFiling());
  }

  /* (non-Javadoc)
   * @see Descriptor#getHeadingNumberSearchIndexKey()
   */
  public String getHeadingNumberSearchIndexKey() {
    return "229P";
  }

  /* (non-Javadoc)
   * @see Descriptor#getLockingEntityType()
   */
  public String getLockingEntityType() {
    return "SH";
  }


  /* (non-Javadoc)
   * @see Descriptor#buildBrowseTerm()
   */
  @Deprecated
  @Override
  public String buildBrowseTerm() {
    String returnString = "";
    StringText text = new StringText(getStringText());
    Iterator iter = text.getSubfieldList().iterator();
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

  @Override
  public void calculateAndSetSortForm() throws SortFormException {
    StringText st = SortformUtils.get().stripSkipInFiling(getStringText(), (short) getSkipInFiling());
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < st.getNumberOfSubfields(); i++) {
      Subfield sf = st.getSubfield(i);
      if (i > 0) {
        result.append(" ");
        if ("vxyz".indexOf(sf.getCode()) > 0) {
          result.append(" ");
        }
      }
      result.append(SortformUtils.get().defaultSortform(sf.toString()));
    }
    setSortForm(result.toString());
  }
}
