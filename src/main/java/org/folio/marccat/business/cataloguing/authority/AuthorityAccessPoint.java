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
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  private AUT getAutItm() {
    return autItm;
  }


  private void setAutItm(AUT aut) {
    autItm = aut;
  }


  public ItemEntity getItemEntity() {
    return getAutItm();
  }


  public void setItemEntity(ItemEntity item) {
    setAutItm((AUT) item);
  }

}
