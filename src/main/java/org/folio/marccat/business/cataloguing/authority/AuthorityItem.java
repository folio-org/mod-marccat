package org.folio.marccat.business.cataloguing.authority;

import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.cataloguing.common.TagImpl;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.MandatoryTagException;
import java.util.Collections;


public class AuthorityItem extends CatalogItem {

  private static final Log logger = LogFactory.getLog(AuthorityItem.class);
  private AUT autItmData;


  public AuthorityItem() {
    super();
  }

  /**
   * @since 1.0
   */
  public AUT getAutItmData() {
    return autItmData;
  }

  /**
   * @since 1.0
   */
  public void setAutItmData(AUT aut) {
    autItmData = aut;
  }

  /* (non-Javadoc)
   * @see CatalogItem#getItemEntity()
   */
  public ItemEntity getItemEntity() {
    return getAutItmData();
  }

  /* (non-Javadoc)
   * @see CatalogItem#setItemEntity(ItemEntity)
   */
  public void setItemEntity(ItemEntity item) {
    setAutItmData((AUT) item);
  }

  /**
   * @since 1.0
   */
  @Override
  public ModelItem getModelItem() {
    return modelItem;
  }

  /**
   * @since 1.0
   */
  @Override
  public void setModelItem(Model model) {
    this.modelItem = new AuthorityModelItem();
    this.modelItem.markNew();
    this.modelItem.setItem(this.getAmicusNumber().longValue());
    this.modelItem.setModel(model);
    this.modelItem.setRecordFields(
      model.getRecordFields());
    this.session = this.modelItem.getDAO().currentSession();

  }

  /**
   * @since 1.0
   */
  public void setModelItem(ModelItem modelItem) {
    this.modelItem = modelItem;
  }

  /* (non-Javadoc)
   * @see CatalogItem#getTagImpl()
   */
  public TagImpl getTagImpl() {
    return new AuthorityTagImpl();
  }

  /* (non-Javadoc)
   * @see CatalogItem#getUserView()
   */
  public int getUserView() {
    return AuthorityCatalog.CATALOGUING_VIEW;
  }

  /* (non-Javadoc)
   * @see CatalogItem#checkForMandatoryTags()
   */
  public void checkForMandatoryTags(Session session) {
    final String[] tags = new String[]{"000", "008", "040", "1"};
    for (int i = 0; i < tags.length; i++) {
      if (findFirstTagByNumber(tags[i], this.session) == null) {
        if ("1".equals(tags[i])) {
          throw new MandatoryTagException("1XX");
        } else {
          throw new MandatoryTagException(tags[i]);
        }
      }
    }
  }

  @Override
  public void sortTags() {
    final Session session = this.session;

    Collections.sort(getTags(),  (o1, o2) -> {
      Tag t1 = o1;
      Tag t2 = o2;
      try {
        return t1.getMarcEncoding(session).getMarcTag().
          compareTo(t2.getMarcEncoding().getMarcTag());
      } catch (Exception e) {
        logger.warn(e);
        return 0;
      }
    });
  }
}
