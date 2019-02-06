package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.AccessPoint;
import org.folio.marccat.dao.persistence.ItemEntity;

public abstract class AuthorityAccessPoint extends AccessPoint implements PersistsViaItem {

  private AUT autItm;


  public AuthorityAccessPoint() {
    super();
  }

  /**
   * Class constructor
   *
   * @param itemNumber
   * @since 1.0
   */
  public AuthorityAccessPoint(int itemNumber) {
    super(itemNumber);
    // TODO Auto-generated constructor stub
  }

  /**
   * @since 1.0
   */
  private AUT getAutItm() {
    return autItm;
  }

  /**
   * @since 1.0
   */
  private void setAutItm(AUT aut) {
    autItm = aut;
  }

  /* (non-Javadoc)
   * @see PersistsViaItem#getItemEntity()
   */
  public ItemEntity getItemEntity() {
    return getAutItm();
  }

  /* (non-Javadoc)
   * @see PersistsViaItem#setItemEntity(ItemEntity)
   */
  public void setItemEntity(ItemEntity item) {
    setAutItm((AUT) item);
  }

}
