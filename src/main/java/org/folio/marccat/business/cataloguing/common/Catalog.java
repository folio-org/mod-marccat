package org.folio.marccat.business.cataloguing.common;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.dao.CatalogDAO;
import org.folio.marccat.dao.CodeTableDAO;
import org.folio.marccat.dao.ModelDAO;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.dao.persistence.Model;
import org.folio.marccat.exception.NewTagException;
import org.folio.marccat.shared.CorrelationValues;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Locale;

/**
 * Supertype layer of all Catalogs impl.
 */

public abstract class Catalog {

  protected static final CodeTableDAO CODE_TABLE_DAO = new CodeTableDAO();
  private final Log logger = LogFactory.getLog(getClass());

  protected static Object setItemIfNecessary(final CatalogItem item, final Object o) {
    if (o instanceof PersistsViaItem) {
      ((PersistsViaItem) o).setItemEntity(item.getItemEntity());
    }
    return o;
  }

  public abstract Model newModel(CatalogItem item);

  /**
   * Returns the {@link ModelDAO} associated with this instance.
   *
   * @return the {@link ModelDAO} associated with this instance.
   */
  public abstract ModelDAO getModelDAO();

  /**
   * Returns the {@link CatalogDAO} associated with this instance.
   *
   * @return the {@link CatalogDAO} associated with this instance.
   */
  public abstract CatalogDAO getCatalogDao();

  /**
   * gets a list of heading types that are applicable for the current
   * authority tag's heading type
   *
   * @since 1.0
   */
  public abstract List getValidHeadingTypeList(Tag t, Locale locale);

  /**
   * change the heading type of an Authority Heading or Reference tag
   *
   * @since 1.0
   */
  public abstract void changeDescriptorType(CatalogItem item, int index, int descriptorType);

  /**
   * A unique String representing the subclass type for use in jsp logic tags
   *
   * @since 1.0
   */
  public abstract String getMarcTypeCode();

  /**
   * Called when a new Catalogue item requires at least one tag (new models)
   *
   * @since 1.0
   */
  public abstract void addDefaultTag(CatalogItem item);

  public abstract void addDefaultTags(CatalogItem item);

  public abstract Tag getNewTag(CatalogItem item, int category,
                                CorrelationValues correlationValues);

  public CatalogItem getCatalogItem(final Session session, final int... key) {

    final CatalogItem b = getCatalogDao().getCatalogItemByKey(session, key);

    for (int i = 0; i < b.getNumberOfTags(); i++) {
      try {
        b.getTag(i).getMarcEncoding(session);
      } catch (Exception e) {
        continue;
      }
    }
    b.sortTags();
    return b;
  }

  public abstract Tag getNewHeaderTag(CatalogItem item, int header);

  public abstract List getTagCategories(Locale l);

  public final Tag getNewTag(CatalogItem item, int category) {
    return getNewTag(item, category, null);
  }

  public final Tag getNewTag(
    final CatalogItem item,
    final int category,
    final int correlationValue1,
    final int correlationValue2,
    final int correlationValue3) {
    return getNewTag(
      item,
      category,
      new CorrelationValues(correlationValue1, correlationValue2, correlationValue3));
  }

  /**
   * Ensures that after creating a new Item (usually from a model) that the
   * item has at least the required mandatory tags.
   *
   * @since 1.0
   */
  public abstract void addRequiredTags(CatalogItem item);

  public abstract void addRequiredTagsForModel(CatalogItem item);

  public void deleteCatalogItem(final CatalogItem item, final Session session) throws HibernateException {
    getCatalogDao().deleteCatalogItem(item, session);
  }

  /**
   * @param item
   * @deprecated
   */
  @Deprecated
  public void saveCatalogItem(CatalogItem item) {
  }

  /**
   * This method is used to generate tags from a model
   *
   * @since 1.0
   */
  public Tag parseModelXmlElementAddToItem(final Element xmlElement, final CatalogItem item) {
    final Tag tag = parseModelXmlElement(xmlElement, item);
    if (tag != null) {
      item.addTag(tag);
      setItemIfNecessary(item, tag);
    }
    return tag;
  }

  /**
   * This method is used to generate tags from a model
   *
   * @since 1.0
   */
  public Tag parseModelXmlElement(Element xmlElement, CatalogItem item) {
    Tag tag = null;
    try {
      tag = getNewTag(
        item,
        Short.parseShort(xmlElement.getAttribute("categoryCode")),
        Short.parseShort(xmlElement.getAttribute("firstCorrelationValue")),
        Short.parseShort(xmlElement.getAttribute("secondCorrelationValue")),
        Short.parseShort(xmlElement.getAttribute("thirdCorrelationValue")));
      tag.parseModelXmlElementContent(xmlElement);
    } catch (final NewTagException newTagException) {
      logger.error("", newTagException);
    }
    return tag;
  }

  protected abstract CatalogItem getNewItem();

  protected abstract ItemEntity getNewItemEntity();

  public CatalogItem newCatalogItemWithoutAmicusNumber() {
    CatalogItem item = getNewItem();
    ItemEntity itemEntity = getNewItemEntity();
    itemEntity.markNew();
    item.setItemEntity(itemEntity);
    return item;
  }

  public CatalogItem newCatalogItem(final Object[] key) {
    CatalogItem result = newCatalogItemWithoutAmicusNumber();
    return applyKeyToItem(result, key);
  }

  public abstract CatalogItem applyKeyToItem(CatalogItem item, Object[] key);

  public CatalogItem newCatalogItem(final Model model, final Object[] key) {
    CatalogItem item = newCatalogItem(key);
    item.setModelItem(model);
    return item;
  }

  public void lock(final int itemNumber, final String userName, final String uuid, final Session session) {
    getCatalogDao().lock(itemNumber, getLockingEntityType(), userName, uuid, session);
  }

  public void unlock(final int itemNumber, final String username, final Session session) {
    getCatalogDao().unlock(itemNumber, getLockingEntityType(), username, session);
  }

  public abstract String getLockingEntityType();
}
