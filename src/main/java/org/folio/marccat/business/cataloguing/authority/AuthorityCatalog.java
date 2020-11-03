package org.folio.marccat.business.cataloguing.authority;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.folio.marccat.business.cataloguing.common.Catalog;
import org.folio.marccat.business.cataloguing.common.CataloguingSourceTag;
import org.folio.marccat.business.cataloguing.common.ControlNumberTag;
import org.folio.marccat.business.cataloguing.common.DateOfLastTransactionTag;
import org.folio.marccat.business.cataloguing.common.HeaderField;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.AbstractMapBackedFactory;
import org.folio.marccat.business.common.MapBackedFactory;
import org.folio.marccat.business.common.PropertyBasedFactoryBuilder;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.dao.AuthorityCatalogDAO;
import org.folio.marccat.dao.CatalogDAO;
import org.folio.marccat.dao.DescriptorDAO;
import org.folio.marccat.dao.ModelDAO;
import org.folio.marccat.dao.NameDescriptorDAO;
import org.folio.marccat.dao.NameTitleDescriptorDAO;
import org.folio.marccat.dao.SubjectDescriptorDAO;
import org.folio.marccat.dao.TitleDescriptorDAO;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.Authority008Tag;
import org.folio.marccat.dao.persistence.AuthorityCataloguingSourceTag;
import org.folio.marccat.dao.persistence.AuthorityControlNumberTag;
import org.folio.marccat.dao.persistence.AuthorityDateOfLastTransactionTag;
import org.folio.marccat.dao.persistence.AuthorityHeadingTag;
import org.folio.marccat.dao.persistence.AuthorityLeader;
import org.folio.marccat.dao.persistence.AuthorityNameHeadingTag;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.dao.persistence.Model;
import org.folio.marccat.dao.persistence.T_AUT_TAG_CAT;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.shared.CorrelationValues;

/**
 * @author elena
 *
 */
public class AuthorityCatalog extends Catalog {

  private static String modMarccatExMessage = "Could not create object";
  private static final AuthorityCatalogDAO daoCatalog = new AuthorityCatalogDAO();

  private static final Map<String, Class<?>> DAO_BY_AUT_TYPE = new HashMap<>();
  static {
    DAO_BY_AUT_TYPE.put(Global.NAME_TYPE_HDG, NameDescriptorDAO.class);
    DAO_BY_AUT_TYPE.put(Global.TITLE_TYPE_HDG, TitleDescriptorDAO.class);
    DAO_BY_AUT_TYPE.put(Global.SUBJECT_TYPE_HDG, SubjectDescriptorDAO.class);
    DAO_BY_AUT_TYPE.put(Global.NAME_TITLE_TYPE_HDG, NameTitleDescriptorDAO.class);
  }

  private static final Map<Integer, String> AUT_TYPE_BY_DESCRIPTOR_TYPE = new HashMap<>();
  static {
    AUT_TYPE_BY_DESCRIPTOR_TYPE.put(Global.NAME_CATEGORY, Global.NAME_TYPE_HDG);
    AUT_TYPE_BY_DESCRIPTOR_TYPE.put(Global.TITLE_CATEGORY, Global.TITLE_TYPE_HDG);
    AUT_TYPE_BY_DESCRIPTOR_TYPE.put(Global.SUBJECT_CATEGORY, Global.SUBJECT_TYPE_HDG);
    AUT_TYPE_BY_DESCRIPTOR_TYPE.put(Global.NAME_TITLE_CATEGORY, Global.NAME_TITLE_TYPE_HDG);
  }

  private static final Map<Object, Object> AUT_TYPE_BY_DESCRIPTOR_VALIDATION_TYPE = new HashMap<>();
  static {
    AUT_TYPE_BY_DESCRIPTOR_VALIDATION_TYPE.put(Global.AUT_NAME_CATEGORY, Global.NAME_TYPE_HDG);
    AUT_TYPE_BY_DESCRIPTOR_VALIDATION_TYPE.put(Global.AUT_TITLE_CATEGORY, Global.TITLE_TYPE_HDG);
    AUT_TYPE_BY_DESCRIPTOR_VALIDATION_TYPE.put(Global.AUT_SUBJECT_CATEGORY, Global.SUBJECT_TYPE_HDG);
  }

  protected static AbstractMapBackedFactory fixedFieldFactory;

  private static final Map<String, Class<?>> HEADING_TAG_BY_AUT_TYPE = new HashMap<>();
  static {
    HEADING_TAG_BY_AUT_TYPE.put(Global.NAME_TYPE_HDG, AuthorityNameHeadingTag.class);
  }

  protected static AbstractMapBackedFactory tagFactory;
  static {
    tagFactory = new MapBackedFactory();
    fixedFieldFactory = new MapBackedFactory();
    PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
    builder.load("/org/folio/marccat/business/cataloguing/authority/tagFactory.properties", tagFactory);
    builder.load("/org/folio/marccat/business/cataloguing/authority/fixedFieldFactory.properties", fixedFieldFactory);
  }

  public AbstractMapBackedFactory getFixedFieldFactory() {
    return fixedFieldFactory;
  }

  public List<?> getTagCategories(Locale l) {
    return DAO_CODE_TABLE.getOptionList(T_AUT_TAG_CAT.class, l);
  }

  public AbstractMapBackedFactory getTagFactory() {
    return tagFactory;
  }

  public Tag getNewHeaderTag(CatalogItem item, short header) {
    return (Tag) setItemIfNecessary(item, getFixedFieldFactory().create(header));
  }

  protected CatalogItem getNewItem() {
    return new AuthorityItem();
  }

  protected ItemEntity getNewItemEntity() {
    return new AUT();
  }

  public CatalogItem applyKeyToItem(CatalogItem item, Object[] key) {
    // no key elements to apply for Authorities
    return item;
  }

  public String getMarcTypeCode() {
    return "A";
  }

  protected Correlation getCorrelation() {
    return null;
  }

  protected CorrelationKey getCorrelationKeyByControlfield() {
    return null;
  }

  @Override
  public Tag getNewTag(CatalogItem item, int category, CorrelationValues correlationValues) {
    Tag tag = (Tag) getTagFactory().create(category);
    tag = (Tag) setItemIfNecessary(item, tag);

    if (correlationValues != null) {
      if (tag.correlationChangeAffectsKey(correlationValues)) {
        if (tag instanceof HeaderField) {
          tag = (Tag) getFixedFieldFactory().create(correlationValues.getValue(1));
          tag = (Tag) setItemIfNecessary(item, tag);
          tag.setCorrelation(1, correlationValues.getValue(1));
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
  public Tag getNewHeaderTag(CatalogItem item, int header) {
    return (Tag) setItemIfNecessary(item, getFixedFieldFactory().create(header));
  }

  @Override
  public String getLockingEntityType() {
    return "AA";
  }

  @Override
  public List<?> getValidHeadingTypeList(Tag t, Locale locale) {
    return Collections.emptyList();
  }

  public AuthorityLeader createRequiredLeaderTag(CatalogItem item) {
    return (AuthorityLeader) getNewTag(item, Global.HEADER_CATEGORY, new CorrelationValues(
        new AuthorityLeader().getHeaderType(), CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED));
  }

  public ControlNumberTag createRequiredControlNumberTag(CatalogItem item) {
    return (ControlNumberTag) getNewTag(item, Global.HEADER_CATEGORY, new CorrelationValues(
        new AuthorityControlNumberTag().getHeaderType(), CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED));
  }

  public DateOfLastTransactionTag createRequiredDateOfLastTransactionTag(CatalogItem item) {
    return (DateOfLastTransactionTag) getNewTag(item, Global.HEADER_CATEGORY,
        new CorrelationValues(new AuthorityDateOfLastTransactionTag().getHeaderType(), CorrelationValues.UNDEFINED,
            CorrelationValues.UNDEFINED));
  }

  public Authority008Tag createRequired008Tag(CatalogItem item) {
    return (Authority008Tag) getNewTag(item, Global.HEADER_CATEGORY, new CorrelationValues(
        new Authority008Tag().getHeaderType(), CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED));
  }

  public CataloguingSourceTag createRequiredCataloguingSourceTag(final CatalogItem item) {
    return (CataloguingSourceTag) getNewTag(item, Global.HEADER_CATEGORY, new CorrelationValues(
        new AuthorityCataloguingSourceTag().getHeaderType(), CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED));
  }

  @Override
  public Model newModel(CatalogItem item) {
    // TODO It is an abstract class that should be implemented. At the moment this
    // function is not used.
    return null;
  }

  @Override
  public CatalogDAO getCatalogDao() {
    return daoCatalog;
  }

  @Override
  public void changeDescriptorType(CatalogItem item, int index, int descriptorType) {
    // TODO It is an abstract class that should be implemented. At the moment this
    // function is not used.

  }

  @Override
  public ModelDAO getModelDAO() {
    // TODO It is an abstract class that should be implemented. At the moment this
    // function is not used.
    return null;
  }

  public static AuthorityHeadingTag createHeadingTagByType(String type) {
    AuthorityHeadingTag result = null;
    try {
      result = (AuthorityHeadingTag) ((Class<?>) HEADING_TAG_BY_AUT_TYPE.get(type)).newInstance();
    } catch (InstantiationException | IllegalAccessException ex) {
      throw new ModMarccatException(modMarccatExMessage);
    }
    return result;
  }

  public static DescriptorDAO getDaoByType(String type) {
    DescriptorDAO result = null;
    try {
      result = (DescriptorDAO) ((Class<?>) DAO_BY_AUT_TYPE.get(type)).newInstance();
    } catch (InstantiationException | IllegalAccessException ex) {
      throw new ModMarccatException(modMarccatExMessage);
    }
    return result;
  }

}
