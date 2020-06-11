package org.folio.marccat.business.cataloguing.bibliographic;

import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.cataloguing.common.TagImpl;

import org.folio.marccat.dao.persistence.BIB_ITM;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.dao.persistence.ModelItem;
import org.folio.marccat.exception.MandatoryTagException;

import java.io.Serializable;
import java.util.Iterator;

public class BibliographicItem extends CatalogItem implements Serializable {
  private static final long serialVersionUID = 8676099561229020012L;
  private BIB_ITM bibItm;
  private int userView;

  public BibliographicItem() {
    super();
  }


  public BibliographicItem(Integer id) {
    super();
    this.bibItm.setAmicusNumber(id);
  }

  public BIB_ITM getBibItm() {
    return bibItm;
  }

  public void setBibItmData(BIB_ITM bibItm) {
    bibItm = bibItm;
    /*
     * Set all PersistsViaBibItem tags
     */
    Iterator iter = getTags().iterator();
    while (iter.hasNext()) {
      Tag aTag = (Tag) iter.next();
      if (aTag instanceof PersistsViaItem) {
        ((PersistsViaItem) aTag).setItemEntity(bibItm);
      }
    }
  }

  @Override
  public TagImpl getTagImpl() {
    return new BibliographicTagImpl();
  }

  public int getUserView() {
    return userView;
  }

  public void setUserView(int i) {
    userView = i;
  }

  public void setModelItem(ModelItem modelItem) {
    this.modelItem = modelItem;
  }



  /* (non-Javadoc)
   * @see CatalogItem#checkForMandatoryTags()
   */
  /*
   * Note that only editable mandatory tags are included -- generated tags
   * like 000, 001, 005 cannot be added by the user and will be generated
   * if not present
   */
  public void checkForMandatoryTags(Session session) {
    final String[] tags = new String[]{"000", "008", "040"};
    for (int i = 0; i < tags.length; i++) {
      if (findFirstTagByNumber(tags[i], session) == null) {
        throw new MandatoryTagException(tags[i]);
      }
    }
  }

  public ItemEntity getItemEntity() {
    return bibItm;
  }

  /* (non-Javadoc)
   * @see CatalogItem#setItemEntity(ItemEntity)
   */
  public void setItemEntity(ItemEntity item) {
    setBibItmData((BIB_ITM) item);
  }




}
