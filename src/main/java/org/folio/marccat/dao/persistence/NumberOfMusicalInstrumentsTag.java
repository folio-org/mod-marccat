package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.bibliographic.VariableHeaderUsingItemEntity;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.UserViewHelper;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.util.StringText;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2006/07/11 08:01:05 $
 * @since 1.0
 */
public class NumberOfMusicalInstrumentsTag extends VariableHeaderUsingItemEntity implements PersistentObjectWithView {

  private int musicalInstrumentKeyNumber;
  private StringText stringText;
  private UserViewHelper userViewHelper = new UserViewHelper();


  public NumberOfMusicalInstrumentsTag() {
    super();
    setHeaderType((short) 14);
    setPersistenceState(new PersistenceState());
    setStringText(new StringText(Subfield.SUBFIELD_DELIMITER + "a"));
  }


  public int getMusicalInstrumentKeyNumber() {
    return musicalInstrumentKeyNumber;
  }


  public void setMusicalInstrumentKeyNumber(int i) {
    musicalInstrumentKeyNumber = i;
  }


  @Override
  public StringText getStringText() {
    return stringText;
  }


  @Override
  public void setStringText(StringText text) {
    stringText = text;
  }


  public String getStringTextString() {
    return getStringText().toString();
  }


  public void setStringTextString(String string) {
    setStringText(new StringText(string));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
   */
  @Override
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setMusicalInstrumentKeyNumber(dao.getNextNumber("X5", session));
  }

  private void setBibItm(BIB_ITM bib_itm) {
    super.setItemEntity(bib_itm);
    int sourceCode = getStringTextString().indexOf(Subfield.SUBFIELD_DELIMITER + "2");
    if (sourceCode != -1) {
      setHeaderType((short) 54);
    } else {
      setHeaderType((short) 14);
    }
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PersistsViaItem#setItemEntity(librisuite.business.cataloguing.common.ItemEntity)
   */
  @Override
  public void setItemEntity(ItemEntity item) {
    setBibItm((BIB_ITM) item);
  }


  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof NumberOfMusicalInstrumentsTag) {
      NumberOfMusicalInstrumentsTag aTag = (NumberOfMusicalInstrumentsTag) obj;
      return super.equals(obj)
        && this.getMusicalInstrumentKeyNumber() == aTag.getMusicalInstrumentKeyNumber();
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return getItemNumber() + getMusicalInstrumentKeyNumber();
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#isAbleToBeDeleted()
   */
  @Override
  public boolean isAbleToBeDeleted() {
    return true;
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#getDAO()
   */
  public AbstractDAO getDAO() {
    return getPersistenceState().getDAO();
  }


  public String getUserViewString() {
    return userViewHelper.getUserViewString();
  }


  public void setUserViewString(String string) {
    userViewHelper.setUserViewString(string);
  }


  public int getBibItemNumber() {
    return getItemNumber();
  }


  public void setBibItemNumber(int i) {
    setItemNumber(i);
  }

}
