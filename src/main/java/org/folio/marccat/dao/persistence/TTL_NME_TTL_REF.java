/*
 * (c) LibriCore
 *
 * Created on Dec 30, 2005
 *
 * TTL_NME_TTL_REF.java
 */
package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.dao.*;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/01/05 13:25:59 $
 * @since 1.0
 */
public class TTL_NME_TTL_REF extends REF {
  private static final DAOTitleNameTitleReferences theDAO = new DAOTitleNameTitleReferences();
  private int nameTitleHeadingNumber;
  private int titleHeadingNumber;
  private String sourceHeadingType;

  public boolean isSourceTitle() {
    return getSourceHeadingType().equals("TH");
  }

  /**
   * @since 1.0
   */
  public int getNameTitleHeadingNumber() {
    return nameTitleHeadingNumber;
  }

  /**
   * @since 1.0
   */
  public void setNameTitleHeadingNumber(int i) {
    nameTitleHeadingNumber = i;
  }

  /**
   * @since 1.0
   */
  public String getSourceHeadingType() {
    return sourceHeadingType;
  }

  /**
   * @since 1.0
   */
  public void setSourceHeadingType(String string) {
    sourceHeadingType = string;
  }

  /* (non-Javadoc)
   * @see REF#getTargetDAO()
   */
  public DAODescriptor getTargetDAO() {
    if (isSourceTitle()) {
      return new NameTitleDescriptorDAO();
    } else {
      return new TitleDescriptorDAO();
    }

  }

  /* (non-Javadoc)
   * @see REF#init(Descriptor, Descriptor, short, int)
   */
  public void init(
    Descriptor source,
    Descriptor target,
    short referenceType,
    int cataloguingView) {

    super.init(source, target, referenceType, cataloguingView);
    if (source instanceof TTL_HDG) {
      setTitleHeadingNumber(source.getKey().getHeadingNumber());
      setNameTitleHeadingNumber(target.getKey().getHeadingNumber());
      setSourceHeadingType("TH");
    } else {
      setNameTitleHeadingNumber(source.getKey().getHeadingNumber());
      setTitleHeadingNumber(target.getKey().getHeadingNumber());
      setSourceHeadingType("MH");
    }
  }


  /**
   * @since 1.0
   */
  public int getTitleHeadingNumber() {
    return titleHeadingNumber;
  }

  /**
   * @since 1.0
   */
  public void setTitleHeadingNumber(int i) {
    titleHeadingNumber = i;
  }

  /* (non-Javadoc)
   * @see REF#getSource()
   */
  public int getSource() {
    if (isSourceTitle()) {
      return getTitleHeadingNumber();
    } else {
      return getNameTitleHeadingNumber();
    }
  }

  /* (non-Javadoc)
   * @see REF#setSource(int)
   */
  public void setSource(int i) {
    if (isSourceTitle()) {
      setTitleHeadingNumber(i);
    } else {
      setNameTitleHeadingNumber(i);
    }
  }

  /* (non-Javadoc)
   * @see REF#getTarget()
   */
  public int getTarget() {
    if (isSourceTitle()) {
      return getNameTitleHeadingNumber();
    } else {
      return getTitleHeadingNumber();
    }
  }

  /* (non-Javadoc)
   * @see REF#setTarget(int)
   */
  public void setTarget(int i) {
    if (isSourceTitle()) {
      setNameTitleHeadingNumber(i);
    } else {
      setTitleHeadingNumber(i);
    }
  }

  /* (non-Javadoc)
   * @see REF#createReciprocal()
   */
  public REF createReciprocal() {
    TTL_NME_TTL_REF result = (TTL_NME_TTL_REF) this.clone();
    if (isSourceTitle()) {
      result.setSourceHeadingType("MH");
    } else {
      result.setSourceHeadingType("TH");
    }
    result.setType(ReferenceType.getReciprocal(getType()));
    return result;
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.PersistenceState#getDAO()
   */
  public AbstractDAO getDAO() {
    return theDAO;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    if (obj instanceof TTL_NME_TTL_REF) {
      TTL_NME_TTL_REF ref = (TTL_NME_TTL_REF) obj;
      return this.getTitleHeadingNumber() == ref.getTitleHeadingNumber()
        && this.getNameTitleHeadingNumber()
        == ref.getNameTitleHeadingNumber()
        && this.getType() == ref.getType()
        && this.getSourceHeadingType().equals(ref.getSourceHeadingType())
        && this.getUserViewString().equals(ref.getUserViewString());
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getTitleHeadingNumber() + 3 * getNameTitleHeadingNumber();
  }

}
