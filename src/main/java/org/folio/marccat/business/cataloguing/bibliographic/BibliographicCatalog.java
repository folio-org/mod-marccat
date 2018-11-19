package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.business.cataloguing.common.*;
import org.folio.marccat.business.common.AbstractMapBackedFactory;
import org.folio.marccat.business.common.MapBackedFactory;
import org.folio.marccat.business.common.PropertyBasedFactoryBuilder;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.*;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.NewTagException;
import org.folio.marccat.config.GlobalStorage;
import org.folio.marccat.shared.CorrelationValues;

import java.util.*;

/**
 * Bibliographic implementation of {@link Catalog} interface.
 *
 * @author paulm
 * @author janick
 * @author cchiama
 */
public class BibliographicCatalog extends Catalog {

  private static final ModelDAO MODEL_DAO = new BibliographicModelDAO();
  private static final BibliographicCatalogDAO CATALOG_DAO = new BibliographicCatalogDAO();

  private static AbstractMapBackedFactory TAG_FACTORY;
  private static AbstractMapBackedFactory FIXED_FIELDS_FACTORY;

  static {
    TAG_FACTORY = new MapBackedFactory();
    FIXED_FIELDS_FACTORY = new MapBackedFactory();
    final PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
    builder.load(
      "/org/folio/cataloging/business/cataloguing/bibliographic/tagFactory.properties",
      TAG_FACTORY);
    builder.load(
      "/org/folio/cataloging/business/cataloguing/bibliographic/fixedFieldFactory.properties",
      FIXED_FIELDS_FACTORY);
  }

  public static void main(final String args[]) {
    BibliographicCatalog catalog = new BibliographicCatalog();
    Tag tag = (Tag) catalog.getTagFactory().create(1);
  }

  @Override
  public CatalogDAO getCatalogDao() {
    return CATALOG_DAO;
  }

  @Override
  public List getTagCategories(final Locale locale) throws DataAccessException {
    return DAO_CODE_TABLE.getOptionListOrderAlphab(T_BIB_TAG_CAT.class, locale);
  }

  /**
   * Ensures that after creating a new BibItem (usually from a model) that the
   * item has at least the required mandatory tags
   *
   * @since 1.0
   */
  public void addRequiredTags(final CatalogItem item) throws NewTagException {
    final BibliographicLeader leader = createRequiredLeaderTag(item);
    if (!item.getTags().contains(leader)) {
      item.addTag(leader);
    }

    final ControlNumberTag controlnumber = createRequiredControlNumberTag(item);
    if (!item.getTags().contains(controlnumber)) {
      item.addTag(controlnumber);
    }

    final DateOfLastTransactionTag dateTag = createRequiredDateOfLastTransactionTag(item);
    if (!item.getTags().contains(dateTag)) {
      item.addTag(dateTag);
    }

    final MaterialDescription mdTag = createRequiredMaterialDescriptionTag(item);
    if (!item.getTags().contains(mdTag)) {
      item.addTag(mdTag);
    }

    final CataloguingSourceTag source = createRequiredCataloguingSourceTag(item);
    if (!item.getTags().contains(source)) {
      item.addTag(source);
    }

    item.sortTags();
  }

  public DateOfLastTransactionTag createRequiredDateOfLastTransactionTag(CatalogItem item) throws NewTagException {
    final DateOfLastTransactionTag dateTag =
      (DateOfLastTransactionTag) getNewTag(item,
        GlobalStorage.HEADER_CATEGORY,
        new CorrelationValues(
          new BibliographicDateOfLastTransactionTag().getHeaderType(),
          CorrelationValues.UNDEFINED,
          CorrelationValues.UNDEFINED));
    return dateTag;
  }


  public void addRequiredTagsForModel(final CatalogItem item) throws NewTagException {
    BibliographicLeader leader = createRequiredLeaderTag(item);
    if (!item.getTags().contains(leader)) {
      item.addTag(leader);
    }

    ControlNumberTag controlnumber = createRequiredControlNumberTag(item);
    if (!item.getTags().contains(controlnumber)) {
      item.addTag(controlnumber);
    }

    MaterialDescription mdTag = createRequiredMaterialDescriptionTag(item);
    if (!item.getTags().contains(mdTag)) {
      item.addTag(mdTag);

    }

    CataloguingSourceTag source = createRequiredCataloguingSourceTag(item);
    if (!item.getTags().contains(source)) {
      item.addTag(source);
    }
  }

  public CataloguingSourceTag createRequiredCataloguingSourceTag(final CatalogItem item) throws NewTagException {
    CataloguingSourceTag source =
      (CataloguingSourceTag) getNewTag(item,
        GlobalStorage.HEADER_CATEGORY,
        new CorrelationValues(
          new BibliographicCataloguingSourceTag().getHeaderType(),
          CorrelationValues.UNDEFINED,
          CorrelationValues.UNDEFINED));
    return source;
  }

  public MaterialDescription createRequiredMaterialDescriptionTag(CatalogItem item) throws NewTagException {
    MaterialDescription mdTag =
      (MaterialDescription) getNewTag(item,
        GlobalStorage.HEADER_CATEGORY,
        new CorrelationValues(
          GlobalStorage.MATERIAL_DESCRIPTION_HEADER_TYPE,
          CorrelationValues.UNDEFINED,
          CorrelationValues.UNDEFINED));
    return mdTag;
  }

  public ControlNumberTag createRequiredControlNumberTag(CatalogItem item) throws NewTagException {
    ControlNumberTag controlnumber =
      (ControlNumberTag) getNewTag(item,
        GlobalStorage.HEADER_CATEGORY,
        new CorrelationValues(
          new BibliographicControlNumberTag().getHeaderType(),
          CorrelationValues.UNDEFINED,
          CorrelationValues.UNDEFINED));
    return controlnumber;
  }

  public BibliographicLeader createRequiredLeaderTag(CatalogItem item) throws NewTagException {
    BibliographicLeader leader =
      (BibliographicLeader) getNewTag(item,
        GlobalStorage.HEADER_CATEGORY,
        new CorrelationValues(
          new BibliographicLeader().getHeaderType(),
          CorrelationValues.UNDEFINED,
          CorrelationValues.UNDEFINED));
    return leader;
  }

  public AbstractMapBackedFactory getFixedFieldFactory() {
    return FIXED_FIELDS_FACTORY;
  }

  public AbstractMapBackedFactory getTagFactory() {
    return TAG_FACTORY;
  }

  @Override
  public Tag getNewHeaderTag(final CatalogItem item, final int header) throws NewTagException {
    return (Tag) setItemIfNecessary(item, getFixedFieldFactory().create(header));
  }

  @Override
  public Tag getNewTag(final CatalogItem item, final int category, final CorrelationValues correlationValues) throws NewTagException {
    Tag tag = (Tag) getTagFactory().create(category);
    tag = (Tag) setItemIfNecessary(item, tag);

    if (correlationValues != null) {
      if (tag.correlationChangeAffectsKey(correlationValues)) {
        if (tag instanceof HeaderField) {
          tag = (Tag) getFixedFieldFactory().create(correlationValues.getValue(1));
          tag = (Tag) setItemIfNecessary(item, tag);
          tag.setCorrelation(1, correlationValues.getValue(1));
        } else if (tag instanceof BibliographicNoteTag) {
          if (item.getAmicusNumber() == null) {
            tag = new PublisherManager();
            tag.setCorrelationValues(correlationValues);
          } else {
            tag = new PublisherManager(item.getAmicusNumber().intValue(), item.getUserView());
            tag.setCorrelationValues(correlationValues);
          }
          tag = (Tag) setItemIfNecessary(item, tag);
        } else {
          tag.setCorrelationValues(correlationValues);
        }
      } else {
        tag.setCorrelationValues(correlationValues);
      }
    }
    tag.markNew();
    tag.setTagImpl(item.getTagImpl());
    return tag;
  }

  @Override
  protected CatalogItem getNewItem() {
    return new BibliographicItem();
  }

  @Override
  protected ItemEntity getNewItemEntity() {
    return new BIB_ITM();
  }

  @Override
  public CatalogItem applyKeyToItem(final CatalogItem item, final Object[] key) {
    BibliographicItem bibliographicItem = (BibliographicItem) item;

    int cataloguingView = (Integer) key[0];
    if (key.length > 1) {
      int amicusNumber = (Integer) key[1];
      bibliographicItem.getBibItmData().setAmicusNumber(amicusNumber);
    }

    bibliographicItem.getBibItmData().setUserViewString(View.makeSingleViewString(cataloguingView));
    bibliographicItem.setUserView(cataloguingView);
    return bibliographicItem;
  }

  @Override
  public ModelDAO getModelDAO() {
    return MODEL_DAO;
  }

  @Override
  public Model newModel(final CatalogItem item) {
    //return new BibliographicModel(item);
    return null;
  }

  @Override
  public void addDefaultTags(final CatalogItem item) {
    try {
      item.addTag(getNewTag(item, (short) 1, new ControlNumberAccessPoint().getCorrelationValues()));
      item.addTag(getNewTag(item, (short) 1, new ClassificationAccessPoint().getCorrelationValues()));
    } catch (NewTagException e) {
      throw new RuntimeException("error creating bibliographic leader");
    }
  }

  @Override
  public void addDefaultTag(final CatalogItem item) {
    try {
      item.addTag(
        getNewTag(
          item,
          (short) 1,
          new BibliographicLeader().getCorrelationValues()));
      item.addTag(getNewTag(item, (short) 1, new BibliographicLeader().getCorrelationValues()));
    } catch (NewTagException e) {
      throw new RuntimeException("error creating bibliographic leader");
    }

  }

  @Override
  public String getMarcTypeCode() {
    return "B";
  }

  @Override
  public void changeDescriptorType(final CatalogItem item, final int index, final int descriptorType) {
    // do nothing (not applicable to bib)
  }

  @Override
  public List getValidHeadingTypeList(final Tag tag, final Locale locale) {
    // not applicable to bibliographic tags
    return null;
  }


  @Override
  public String getLockingEntityType() {
    return "BI";
  }

}
