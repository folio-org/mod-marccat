package org.folio.marccat.business.cataloguing.bibliographic;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.UserViewHelper;
import org.folio.marccat.dao.persistence.AccessPoint;
import org.folio.marccat.util.StringText;

import java.sql.SQLException;

public abstract class BibliographicAccessPoint extends AccessPoint implements PersistentObjectWithView {
  private String materialSpecified;
  private UserViewHelper userViewHelper = new UserViewHelper();

  private Integer sequenceNumber;

  public BibliographicAccessPoint() {
    super();
  }

  public BibliographicAccessPoint(int itemNumber) {
    super(itemNumber);
  }

  @Override
  public StringText getStringText() {
    StringText result = super.getStringText();
    result.parse(materialSpecified);
    return result;
  }

  @Override
  public void setStringText(StringText stringText) {
    materialSpecified = stringText.getSubfieldsWithCodes("3").toString();
    super.setStringText(stringText);
  }

  @Override
  public void generateNewKey(final Session session) throws HibernateException, SQLException {
    super.generateNewKey(session);
    if (getDescriptor().isNew()) {
      getDescriptor().getKey().setUserViewString(getUserViewString());
    }
    setUserViewString(getDescriptor().getKey().getUserViewString());
  }

  public String getMaterialSpecified() {
    return materialSpecified;
  }

  public void setMaterialSpecified(String string) {
    materialSpecified = string;
  }

  public String getUserViewString() {
    return userViewHelper.getUserViewString();
  }

  public void setUserViewString(String s) {
    userViewHelper.setUserViewString(s);
  }

  public int getBibItemNumber() {
    return getItemNumber();
  }

  public void setBibItemNumber(int itemNumber) {
    setItemNumber(itemNumber);
  }

  @Override
  public boolean equals(Object obj) {
    if (super.equals(obj)) {
      BibliographicAccessPoint anApf = (BibliographicAccessPoint) obj;
      return this.getUserViewString().equals(anApf.getUserViewString());
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return super.hashCode() + getUserViewString().hashCode();
  }

  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(Integer sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }
} 

