package org.folio.cataloging.business.cataloguing.bibliographic;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.PersistentObjectWithView;
import org.folio.cataloging.business.common.UserViewHelper;
import org.folio.cataloging.dao.persistence.AccessPoint;
import org.folio.cataloging.util.StringText;

import java.sql.SQLException;

public abstract class BibliographicAccessPoint extends AccessPoint implements PersistentObjectWithView {
  private String materialSpecified;
  private UserViewHelper userViewHelper = new UserViewHelper ( );

  private Integer sequenceNumber;

  public BibliographicAccessPoint() {
    super ( );
  }

  public BibliographicAccessPoint(int itemNumber) {
    super (itemNumber);
  }

  public StringText getStringText() {
    StringText result = super.getStringText ( );
    result.parse (materialSpecified);
    return result;
  }

  public void setStringText(StringText stringText) {
    materialSpecified = stringText.getSubfieldsWithCodes ("3").toString ( );
    super.setStringText (stringText);
  }

  public void generateNewKey(final Session session) throws HibernateException, SQLException {
    super.generateNewKey (session);
    if (getDescriptor ( ).isNew ( )) {
      getDescriptor ( ).getKey ( ).setUserViewString (getUserViewString ( ));
    }
    setUserViewString (getDescriptor ( ).getKey ( ).getUserViewString ( ));
  }

  public String getMaterialSpecified() {
    return materialSpecified;
  }

  public void setMaterialSpecified(String string) {
    materialSpecified = string;
  }

  public String getUserViewString() {
    return userViewHelper.getUserViewString ( );
  }

  public void setUserViewString(String s) {
    userViewHelper.setUserViewString (s);
  }

  public int getBibItemNumber() {
    return getItemNumber ( );
  }

  public void setBibItemNumber(int itemNumber) {
    setItemNumber (itemNumber);
  }

  public boolean equals(Object obj) {
    if (super.equals (obj)) {
      BibliographicAccessPoint anApf = (BibliographicAccessPoint) obj;
      return this.getUserViewString ( ).equals (anApf.getUserViewString ( ));
    } else {
      return false;
    }
  }

  public int hashCode() {
    return super.hashCode ( ) + getUserViewString ( ).hashCode ( );
  }

  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(Integer sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }
}
