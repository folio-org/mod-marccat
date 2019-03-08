package org.folio.marccat.business.cataloguing.common;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.dao.CatalogDAO;
import org.folio.marccat.dao.DAOCodeTable;
import org.folio.marccat.dao.ModelDAO;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.dao.persistence.Model;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.NewTagException;
import org.folio.marccat.exception.RecordInUseException;
import org.folio.marccat.shared.CorrelationValues;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Locale;

/**
 * Supertype layer of all Catalogs impl.
 */

public abstract class Catalog {

  protected final static DAOCodeTable DAO_CODE_TABLE = new DAOCodeTable();
  private final Log logger = LogFactory.getLog(getClass());

  protected static Object setItemIfNecessary(final CatalogItem item, final Object o) {
    if (o instanceof PersistsViaItem) {
      ((PersistsViaItem) o).setItemEntity(item.getItemEntity());
    }
    return o;
  }

  abstract public Model newModel(CatalogItem item);

  /**
   * Returns the {@link ModelDAO} associated with this instance.
   *
   * @return the {@link ModelDAO} associated with this instance.
   */
  abstract public ModelDAO getModelDAO();

  /**
   * Returns the {@link CatalogDAO} associated with this instance.
   *
   * @return the {@link CatalogDAO} associated with this instance.
   */
  abstract public CatalogDAO getCatalogDao();

  /**
   * gets a list of heading types that are applicable for the current
   * authority tag's heading type
   *
   * @since 1.0
   */
  abstract public List getValidHeadingTypeList(Tag t, Locale locale) throws DataAccessException;

  /**
   * change the heading type of an Authority Heading or Reference tag
   *
   * @since 1.0
   */
  abstract public void changeDescriptorType(CatalogItem item, int index, int descriptorType);

  /**
   * A unique String representing the subclass type for use in jsp logic tags
   *
   * @since 1.0
   */
  abstract public String getMarcTypeCode();

  /**
   * Called when a new Catalogue item requires at least one tag (new models)
   *
   * @since 1.0
   */
  abstract public void addDefaultTag(CatalogItem item);

  abstract public void addDefaultTags(CatalogItem item);

  abstract public Tag getNewTag(CatalogItem item, int category,
                                CorrelationValues correlationValues) throws NewTagException;

  public CatalogItem getCatalogItem(final Session session, final int... key) throws DataAccessException {
    // FIXME: In case this method is needed the following line should be moved on the persistence layer.
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

  abstract public Tag getNewHeaderTag(CatalogItem item, int header) throws NewTagException;

  abstract public List getTagCategories(Locale l) throws DataAccessException;

  public final Tag getNewTag(CatalogItem item, int category) throws NewTagException {
    return getNewTag(item, category, null);
  }

  public final Tag getNewTag(
    final CatalogItem item,
    final int category,
    final int correlationValue1,
    final int correlationValue2,
    final int correlationValue3) throws NewTagException {
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
  abstract public void addRequiredTags(CatalogItem item) throws NewTagException;

  abstract public void addRequiredTagsForModel(CatalogItem item) throws NewTagException;

  public void deleteCatalogItem(final CatalogItem item, final Session session) throws DataAccessException, HibernateException {
    getCatalogDao().deleteCatalogItem(item, session);
  }

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

  abstract protected CatalogItem getNewItem();

  abstract protected ItemEntity getNewItemEntity();

  public CatalogItem newCatalogItemWithoutAmicusNumber() {
    CatalogItem item = getNewItem();
    ItemEntity itemEntity = getNewItemEntity();
    itemEntity.markNew();
    item.setItemEntity(itemEntity);
    return item;
  }

  public CatalogItem newCatalogItem(final Object[] key) throws DataAccessException {
    CatalogItem result = newCatalogItemWithoutAmicusNumber();
    return applyKeyToItem(result, key);
  }

  abstract public CatalogItem applyKeyToItem(CatalogItem item, Object[] key);

  public CatalogItem newCatalogItem(final Model model, final Object[] key) throws DataAccessException {
    CatalogItem item = newCatalogItem(key);
    item.setModelItem(model);
    return item;
  }

  public void lock(final int itemNumber, final String userName, final String uuid, final Session session) throws DataAccessException, RecordInUseException {
    getCatalogDao().lock(itemNumber, getLockingEntityType(), userName, uuid, session);
  }

  public void unlock(final int itemNumber, final String username, final Session session) throws DataAccessException {
    getCatalogDao().unlock(itemNumber, getLockingEntityType(), username, session);
  }

  abstract public String getLockingEntityType();
}
