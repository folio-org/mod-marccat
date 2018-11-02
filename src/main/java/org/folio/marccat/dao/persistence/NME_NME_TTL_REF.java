/*
 * (c) LibriCore
 *
 * Created on Dec 29, 2005
 *
 * NME_NME_TTL_REF.java
 */
package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.dao.*;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/01/05 13:25:59 $
 * @since 1.0
 */
public class NME_NME_TTL_REF extends REF {
  private static final DAONameNameTitleReferences theDAO =
    new DAONameNameTitleReferences();
  private int nameTitleHeadingNumber;
  private int nameHeadingNumber;
  private String sourceHeadingType;

  public boolean isSourceName() {
    return getSourceHeadingType().equals("NH");
  }

  /**
   * @since 1.0
   */
  public int getNameHeadingNumber() {
    return nameHeadingNumber;
  }

  /**
   * @since 1.0
   */
  public void setNameHeadingNumber(int i) {
    nameHeadingNumber = i;
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
    if (isSourceName()) {
      return new NameTitleDescriptorDAO();
    } else {
      return new NameDescriptorDAO();
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
    if (source instanceof NME_HDG) {
      setNameHeadingNumber(source.getKey().getHeadingNumber());
      setNameTitleHeadingNumber(target.getKey().getHeadingNumber());
      setSourceHeadingType("NH");
    } else {
      setNameTitleHeadingNumber(source.getKey().getHeadingNumber());
      setNameHeadingNumber(target.getKey().getHeadingNumber());
      setSourceHeadingType("MH");
    }
  }

  /* (non-Javadoc)
   * @see REF#getSource()
   */
  public int getSource() {
    if (isSourceName()) {
      return getNameHeadingNumber();
    } else {
      return getNameTitleHeadingNumber();
    }
  }

  /* (non-Javadoc)
   * @see REF#setSource(int)
   */
  public void setSource(int i) {
    if (isSourceName()) {
      setNameHeadingNumber(i);
    } else {
      setNameTitleHeadingNumber(i);
    }
  }

  /* (non-Javadoc)
   * @see REF#getTarget()
   */
  public int getTarget() {
    if (isSourceName()) {
      return getNameTitleHeadingNumber();
    } else {
      return getNameHeadingNumber();
    }
  }

  /* (non-Javadoc)
   * @see REF#setTarget(int)
   */
  public void setTarget(int i) {
    if (isSourceName()) {
      setNameTitleHeadingNumber(i);
    } else {
      setNameHeadingNumber(i);
    }
  }

  /* (non-Javadoc)
   * @see REF#createReciprocal()
   */
  public REF createReciprocal() {
    NME_NME_TTL_REF result = (NME_NME_TTL_REF) this.clone();
    if (isSourceName()) {
      result.setSourceHeadingType("MH");
    } else {
      result.setSourceHeadingType("NH");
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
    if (obj instanceof NME_NME_TTL_REF) {
      NME_NME_TTL_REF ref = (NME_NME_TTL_REF) obj;
      return this.getNameHeadingNumber() == ref.getNameHeadingNumber()
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
    return getNameHeadingNumber() + 3 * getNameTitleHeadingNumber();
  }

}
