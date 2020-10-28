package org.folio.marccat.business.cataloguing.authority;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
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
import org.folio.marccat.business.common.View;
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
import org.folio.marccat.dao.persistence.AuthorityNote;
import org.folio.marccat.dao.persistence.AuthorityReferenceTag;
import org.folio.marccat.dao.persistence.AuthoritySubjectHeadingTag;
import org.folio.marccat.dao.persistence.AuthorityTitleHeadingTag;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.EquivalenceReference;
import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.dao.persistence.Model;
import org.folio.marccat.dao.persistence.REF;
import org.folio.marccat.dao.persistence.ReferenceType;
import org.folio.marccat.dao.persistence.SBJCT_HDG;
import org.folio.marccat.dao.persistence.SeeAlsoReferenceTag;
import org.folio.marccat.dao.persistence.SeeReferenceTag;
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
    AUT_TYPE_BY_DESCRIPTOR_TYPE.put(Global.AUT_NAME_CATEGORY, Global.NAME_TYPE_HDG);
    AUT_TYPE_BY_DESCRIPTOR_TYPE.put(Global.AUT_TITLE_CATEGORY, Global.TITLE_TYPE_HDG);
    AUT_TYPE_BY_DESCRIPTOR_TYPE.put(Global.AUT_SUBJECT_CATEGORY, Global.SUBJECT_TYPE_HDG);
  }

  private static final Map<Object, Object> AUT_TYPE_BY_DESCRIPTOR_VALIDATION_TYPE = new HashMap<>();
  static {
    AUT_TYPE_BY_DESCRIPTOR_VALIDATION_TYPE.put(Global.AUT_NAME_CATEGORY, Global.NAME_TYPE_HDG);
    AUT_TYPE_BY_DESCRIPTOR_VALIDATION_TYPE.put(Global.AUT_TITLE_CATEGORY, Global.TITLE_TYPE_HDG);
    AUT_TYPE_BY_DESCRIPTOR_VALIDATION_TYPE.put(Global.AUT_SUBJECT_CATEGORY, Global.SUBJECT_TYPE_HDG);
  }

  protected static AbstractMapBackedFactory fixedFieldFactory;

  private static final Map<Object, Object> HEADING_TAG_BY_AUT_TYPE = new HashMap<>();
  static {
    HEADING_TAG_BY_AUT_TYPE.put(Global.NAME_TYPE_HDG, AuthorityNameHeadingTag.class);
    HEADING_TAG_BY_AUT_TYPE.put(Global.TITLE_TYPE_HDG, AuthorityTitleHeadingTag.class);
    HEADING_TAG_BY_AUT_TYPE.put(Global.SUBJECT_TYPE_HDG, AuthoritySubjectHeadingTag.class);
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

    if (correlationValues != null && tag.correlationChangeAffectsKey(correlationValues)) {
      if (tag instanceof HeaderField) {
        tag = (Tag) getFixedFieldFactory().create(correlationValues.getValue(1));
      } else if (tag instanceof AuthorityHeadingTag || tag instanceof AuthorityReferenceTag) {
        Integer refTypePosition = correlationValues.getLastUsedPosition();
        int refType;

        if (refTypePosition != null) {
          refType = correlationValues.getValue(refTypePosition.intValue());
        } else {
          throw new ModMarccatException("attempt to create reference tag with no reference type specified");
        }
        if (ReferenceType.isSeenFrom(refType)) {
          tag = new SeeReferenceTag();
        } else if (ReferenceType.isSeeAlsoFrom(refType)) {
          tag = new SeeAlsoReferenceTag();
        } else if (ReferenceType.isEquivalence(refType)) {
          tag = new EquivalenceReference();
        } else {
          throw new ModMarccatException("invalid reference type for authority reference tag");
        }
      }
    }
    if (tag instanceof AuthorityReferenceTag) {
      // set the target descriptor and reference to be compatible with the
      // given category
      String headingType = ((AUT) item.getItemEntity()).getHeadingType();
      Class<?> sourceHeadingClazz = getDaoByType(headingType).getPersistentClass();

      /*
       * if we were called with category == 16 (a generic Reference) then set the
       * reference to be an interfile type based on the heading type, otherwise the
       * stringValue of category will correspond to the desired target descriptor type
       * and we should create an appropriate reference class for this heading-> target
       * combination (e.g. NME_NME_TTL_REF)
       */
      Class<?> targetHeadingClazz;
      if (getAutTypeByDescriptorType(category) != null) {
        targetHeadingClazz = getDaoByType(getAutTypeByDescriptorType(category)).getPersistentClass();
      } else {
        targetHeadingClazz = sourceHeadingClazz;
      }

      try {
        Descriptor sourceDescriptor = (Descriptor) sourceHeadingClazz.newInstance();
        Descriptor targetDescriptor = (Descriptor) targetHeadingClazz.newInstance();
        REF r = REF.newInstance(sourceDescriptor, targetDescriptor, ReferenceType.SEEN_FROM,
            View.DEFAULT_BIBLIOGRAPHIC_VIEW);
        ((AuthorityReferenceTag) tag).setReference(r);
        ((AuthorityReferenceTag) tag).setTargetDescriptor(targetDescriptor);
      } catch (InstantiationException | IllegalAccessException e) {
        throw new ModMarccatException(modMarccatExMessage);
      }
    } else if (tag instanceof AuthorityHeadingTag) {
      // change the heading type to correspond with the heading
      AUT autItm = (AUT) item.getItemEntity();
      autItm.setHeadingType(getAutTypeByDescriptorType(category));
    }
    if (correlationValues != null) {
      tag.setCorrelationValues(correlationValues);
    }

    if (tag instanceof AuthorityReferenceTag) {
      Descriptor descriptor = ((AuthorityReferenceTag) tag).getDescriptor();
      if (descriptor instanceof SBJCT_HDG) {
        SBJCT_HDG subject = (SBJCT_HDG) descriptor;
        subject.setSourceCode(Global.SUBJECT_SOURCE_CODE_OTHERS);
      }
    }

    tag = (Tag) setItemIfNecessary(item, tag);
    tag.markNew();
    tag.setTagImpl(item.getTagImpl());
    return tag;
  }

  public static String getAutTypeByDescriptorType(int descriptorType) {
    return AUT_TYPE_BY_DESCRIPTOR_TYPE.get(descriptorType);
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
    // TODO It is an abstract class that should be implemented. At the moment this
    // function is not used.
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

  public static DescriptorDAO getDaoByType(String type) {
    DescriptorDAO result = null;
    try {
      result = (DescriptorDAO) ((Class) DAO_BY_AUT_TYPE.get(type)).newInstance();
    } catch (InstantiationException | IllegalAccessException ex) {
      throw new ModMarccatException(modMarccatExMessage);
    }
    return result;
  }

  public void toAuthorityLeader(final String leaderValue, final AuthorityLeader authorityLeader) {
    authorityLeader.setRecordStatusCode(leaderValue.charAt(5));
    authorityLeader.setEncodingLevel(leaderValue.charAt(17));
  }

  public void toAuthority008Tag(final String tag008Value, final Authority008Tag authority008) {
    Date date = new SimpleDateFormat("yyMMdd").parse(tag008Value.substring(0, 6), new ParsePosition(0));
    authority008.setEnteredOnFileDate(date);
    authority008.setSubjectDescriptor(tag008Value.charAt(6));
    authority008.setRomanizationScheme(tag008Value.charAt(7));
    authority008.setBilingualUsage(tag008Value.charAt(8));
    authority008.setRecordType(tag008Value.charAt(9));
    authority008.setCataloguingRules(tag008Value.charAt(10));
    authority008.setSubjectSystem(tag008Value.charAt(11));
    authority008.setSeriesType(tag008Value.charAt(12));
    authority008.setSeriesNumbering(tag008Value.charAt(13));
    authority008.setMainAddedEntryIndicator(tag008Value.charAt(14));
    authority008.setSubjectEntryIndicator(tag008Value.charAt(15));
    authority008.setSeriesEntryIndicator(tag008Value.charAt(16));
    authority008.setSubDivisionType(tag008Value.charAt(17));
    authority008.setGovernmentAgency(tag008Value.charAt(28));
    authority008.setReferenceStatus(tag008Value.charAt(29));
    authority008.setRecordRevision(tag008Value.charAt(31));
    authority008.setNonUniqueName(tag008Value.charAt(32));
    authority008.setHeadingStatus(tag008Value.charAt(33));
    authority008.setRecordModification(tag008Value.charAt(38));
    authority008.setCataloguingSourceCode(tag008Value.charAt(39));
  }

  public AuthorityNote createAuthorityNote(final CatalogItem item, final CorrelationValues correlationValues) {
    return (AuthorityNote) getNewTag(item, Global.AUT_NOTE_CATEGORY, correlationValues);
  }

}
