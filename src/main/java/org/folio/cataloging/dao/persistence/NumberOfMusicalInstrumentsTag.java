package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.common.PersistentObjectWithView;
import org.folio.cataloging.business.common.UserViewHelper;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.SystemNextNumberDAO;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.util.StringText;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2006/07/11 08:01:05 $
 * @since 1.0
 */
public class NumberOfMusicalInstrumentsTag extends VariableHeaderUsingItemEntity implements PersistentObjectWithView {

  private int musicalInstrumentKeyNumber;
  private String stringTextString;
  private StringText stringText;
  private UserViewHelper userViewHelper = new UserViewHelper ( );

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public NumberOfMusicalInstrumentsTag() {
    super ( );
    setHeaderType ((short) 14);
    setPersistenceState (new PersistenceState ( ));
    setStringText (new StringText (Subfield.SUBFIELD_DELIMITER + "a"));
  }

  /**
   * @since 1.0
   */
  public int getMusicalInstrumentKeyNumber() {
    return musicalInstrumentKeyNumber;
  }

  /**
   * @since 1.0
   */
  public void setMusicalInstrumentKeyNumber(int i) {
    musicalInstrumentKeyNumber = i;
  }

  /**
   * @since 1.0
   */
  public StringText getStringText() {
    return stringText;
  }

  /**
   * @since 1.0
   */
  public void setStringText(StringText text) {
    stringText = text;
  }

  /**
   * @since 1.0
   */
  public String getStringTextString() {
    return getStringText ( ).toString ( );
  }

  /**
   * @since 1.0
   */
  public void setStringTextString(String string) {
    setStringText (new StringText (string));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
   */
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO ( );
    setMusicalInstrumentKeyNumber (dao.getNextNumber ("X5", session));
  }

  private void setBibItm(BIB_ITM bib_itm) {
    super.setItemEntity (bib_itm);
    int sourceCode = getStringTextString ( ).indexOf (Subfield.SUBFIELD_DELIMITER + "2");
    if (sourceCode != -1) {
      setHeaderType ((short) 54);
    } else {
      setHeaderType ((short) 14);
    }
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PersistsViaItem#setItemEntity(librisuite.business.cataloguing.common.ItemEntity)
   */
  public void setItemEntity(ItemEntity item) {
    setBibItm ((BIB_ITM) item);
  }


  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    if (obj instanceof NumberOfMusicalInstrumentsTag) {
      NumberOfMusicalInstrumentsTag aTag = (NumberOfMusicalInstrumentsTag) obj;
      return super.equals (obj)
        && this.getMusicalInstrumentKeyNumber ( ) == aTag.getMusicalInstrumentKeyNumber ( );
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getItemNumber ( ) + getMusicalInstrumentKeyNumber ( );
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#isAbleToBeDeleted()
   */
  public boolean isAbleToBeDeleted() {
    return true;
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#getDAO()
   */
  public AbstractDAO getDAO() {
    return getPersistenceState ( ).getDAO ( );
  }

  /**
   * @since 1.0
   */
  public String getUserViewString() {
    return userViewHelper.getUserViewString ( );
  }

  /**
   * @since 1.0
   */
  public void setUserViewString(String string) {
    userViewHelper.setUserViewString (string);
  }

  /**
   * @since 1.0
   */
  public int getBibItemNumber() {
    return getItemNumber ( );
  }

  /**
   * @since 1.0
   */
  public void setBibItemNumber(int i) {
    setItemNumber (i);
  }

}
