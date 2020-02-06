package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicItem;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicTagImpl;
import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.CacheUpdateException;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.exception.RecordNotFoundException;
import org.folio.marccat.util.XmlUtils;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.folio.marccat.util.F.isNotNullOrEmpty;


public class BibliographicCatalogDAO extends CatalogDAO {
  private Log logger = new Log(BibliographicCatalogDAO.class);
  private String queryPart = "where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1' ";

  public BibliographicCatalogDAO() {
    super();
  }

  /**
   * Gets bibliographic item corresponding to bibliographic record.
   *
   * @param amicusNumber -- the amicus number of item.
   * @param userView     -- user view associated.
   * @param session      -- the current session hibernate.
   * @return the bibliographic item by amicus number.
   * @throws HibernateException in case of hibernate exception.
   */
  public BibliographicItem getBibliographicItemByAmicusNumber(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    BibliographicItem item = getBibliographicItemWithoutRelationships(amicusNumber, userView, session);
    item.getTags().addAll(getBibliographicRelationships(amicusNumber, userView, session));
    item.getTags().forEach(tag -> {
      tag.setTagImpl(new BibliographicTagImpl());
      if (tag instanceof MaterialDescription) {
        tag.setCorrelation(1, Global.MATERIAL_DESCRIPTION_HEADER_TYPE);
      }
      tag.setCorrelationKey(tag.getTagImpl().getMarcEncoding(tag, session));
    });
    item.sortTags();
    return item;
  }


  @Override
  /**
   * Save or update record in full_cache.
   */
  public void updateFullRecordCacheTable(final Session session, final CatalogItem item) throws HibernateException {
    updateFullRecordCacheTable(session, item, true);
  }

  /**
   * @param session
   * @param item
   * @param updateRelatedRecs
   * @throws HibernateException
   */
  private void updateFullRecordCacheTable(final Session session, final CatalogItem item, final boolean updateRelatedRecs) throws HibernateException {
    FULL_CACHE cache;
    DAOFullCache dao = new DAOFullCache();
    try {
      cache = dao.load(session, item.getAmicusNumber(), item.getUserView());
    } catch (RecordNotFoundException e) {
      cache = new FULL_CACHE();
      cache.setItemNumber(item.getAmicusNumber());
      cache.setUserView(item.getUserView());
    }
    item.sortTags();
    cache.setRecordData(XmlUtils.documentToString(item.toExternalMarcSlim(session)));
    cache.markChanged();
    persistByStatus(cache, session);

    if (updateRelatedRecs) {
      for (Object o : item.getTags()) {
        if (o instanceof BibliographicRelationshipTag) {
          BibliographicRelationshipTag t = (BibliographicRelationshipTag) o;
          if(t.getTargetBibItemNumber() > 0) {
            CatalogItem relItem = getBibliographicItemByAmicusNumber(t.getTargetBibItemNumber(), item.getUserView(), session);
            updateFullRecordCacheTable(session, relItem, false);
          }
        }
      }
    }

  }

  /**
   * Loads all tags for bibliographic item.
   *
   * @param amicusNumber -- the amicus number of item.
   * @param userView     -- user view associated.
   * @param session      -- the current session hibernate.
   * @return the bibliographic item without relationships.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  public BibliographicItem getBibliographicItemWithoutRelationships(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    try {
      BibliographicItem item = getBibliographicItem(amicusNumber, userView, session);
      item.getTags().addAll(getHeaderFields(item, userView, session));

      try {
        final String language = item.getItemEntity().getLanguageOfCataloguing();
        item.getTags().addAll(getBibliographicNotes(amicusNumber, userView, language, session));
      } catch (Exception e) {
        logger.error("notes not loaded", e);
      }

      try {
        item.getTags().addAll((List<ClassificationAccessPoint>) getAccessPointTags(ClassificationAccessPoint.class, amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error("classification not loaded");
      }

      try {
        item.getTags().addAll((List<ControlNumberAccessPoint>) getAccessPointTags(ControlNumberAccessPoint.class, amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error("ControlNumberAccessPoint not loaded");
      }

      try {
        item.getTags().addAll(getNameAccessPointTags(amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error("NameAccessPoint not loaded");
      }

      try {
        item.getTags().addAll(getTitleAccessPointTags(amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error("TitleAccessPoint not loaded");
      }

      try {
        item.getTags().addAll(getNameTitleAccessPointTags(amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error("NameTitleAccessPoint not loaded");
      }

      try {
        item.getTags().addAll(getPublisherTags(amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error("PublisherTags not loaded", e);
      }

      try {
        item.getTags().addAll((List<SubjectAccessPoint>) getAccessPointTags(SubjectAccessPoint.class, amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error("SubjectAccessPoint not loaded");
      }

      item.setModelItem(new BibliographicModelItemDAO().load(amicusNumber, session));

      return item;

    } catch (HibernateException he) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, he);
      throw new HibernateException(he);
    }
  }

  /**
   * Gets all header tag fields.
   *
   * @param item     -- the bibliographic item.
   * @param userView -- user view associated.
   * @param session  -- the current session hibernate.
   * @return list of tag containing headers.
   * @throws HibernateException in case of hibernate exception.
   */
  private List<Tag> getHeaderFields(final BibliographicItem item, final int userView, final Session session) throws HibernateException {

    final BIB_ITM bibItemData = item.getBibItmData();
    final List<Tag> result = new ArrayList<>();
    result.add(new BibliographicLeader());
    result.add(new BibliographicControlNumberTag());
    result.add(new BibliographicDateOfLastTransactionTag());
    result.add(new BibliographicCataloguingSourceTag());

    if (isNotNullOrEmpty(bibItemData.getAuthenticationCenterStringText())) {
      result.add(new BibliographicAuthenticationCodeTag());
    }

    if (isNotNullOrEmpty(bibItemData.getCountryStringText())) {
      result.add(new CountryOfPublicationTag());
    }

    if (isNotNullOrEmpty(bibItemData.getFormOfMusicStringText())) {
      result.add(new FormOfMusicalCompositionTag());
    }

    if (isNotNullOrEmpty(bibItemData.getGeographicAreaStringText())) {
      result.add(new BibliographicGeographicAreaTag());
    }

    if (isNotNullOrEmpty(bibItemData.getLanguageStringText())) {
      result.add(new LanguageCodeTag());
    }

    if (isNotNullOrEmpty(bibItemData.getProjectedPublicationDateCode())) {
      result.add(new ProjectedPublicationDateTag());
    }

    if (isNotNullOrEmpty(bibItemData.getSpecialCodedDatesStringText())) {
      result.add(new SpecialCodedDatesTag());
    }

    if (isNotNullOrEmpty(bibItemData.getTimePeriodStringText())) {
      result.add(new TimePeriodOfContentTag());
    }

    final int amicusNumber = item.getAmicusNumber();
    result.addAll(getMaterialDescriptions(amicusNumber, userView, session));
    result.addAll(getPhysicalDescriptions(amicusNumber, userView, session));
    result.addAll(getMusicalInstruments(amicusNumber, userView, session));

    return result.stream().map(tag -> {
      if (tag instanceof PersistsViaItem)
        ((PersistsViaItem) tag).setItemEntity(bibItemData);

      return tag;
    }).collect(Collectors.toList());

  }

  /**
   * Gets material description tags.
   *
   * @param amicusNumber -- the amicus number of item.
   * @param userView     -- the user view
   * @param session      -- the current session hibernate.
   * @return list of Tag material description.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  private List<MaterialDescription> getMaterialDescriptions(final int amicusNumber, final int userView, final Session session) throws HibernateException {

    final List<MaterialDescription> multiView = session.find("from MaterialDescription t "
        + queryPart,
      new Object[]{amicusNumber, userView},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    return (List<MaterialDescription>) isolateViewForList(multiView, userView, session);
  }

  /**
   * Gets physical description tags.
   *
   * @param amicusNumber -- the amicus number of item.
   * @param userView     -- the user view
   * @param session      -- the current session hibernate.
   * @return list of Tag physical description.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  private List<PhysicalDescription> getPhysicalDescriptions(final int amicusNumber, final int userView, final Session session) throws HibernateException {

    List<PhysicalDescription> multiView = session.find(
      "from org.folio.marccat.dao.persistence.PhysicalDescription t "
        + queryPart,
      new Object[]{amicusNumber, userView},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    return (List<PhysicalDescription>) isolateViewForList(multiView, userView, session);
  }

  /**
   * Gets physical description tags.
   *
   * @param amicusNumber -- the amicus number of item.
   * @param userView     -- the user view
   * @param session      -- the current session hibernate.
   * @return list of Tag physical description.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  private List<NumberOfMusicalInstrumentsTag> getMusicalInstruments(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    List<NumberOfMusicalInstrumentsTag> multiView = session.find(
      "from NumberOfMusicalInstrumentsTag t "
        + queryPart,
      new Object[]{amicusNumber, userView},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    return (List<NumberOfMusicalInstrumentsTag>) isolateViewForList(multiView, userView, session);
  }

  /**
   * Gets bibliographic note tags.
   *
   * @param amicusNumber -- the amicus number of item.
   * @param userView     -- the user view
   * @param session      -- the current session hibernate.
   * @return list of Tag bibliographic note.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  private List<BibliographicNoteTag> getBibliographicNotes(final int amicusNumber, final int userView, final String language, final Session session) throws HibernateException {

    List<BibliographicNote> multiView = session.find("from BibliographicNote t "
        + "where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1'",
      new Object[]{amicusNumber, userView},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    List<? extends PersistentObjectWithView> singleView = isolateViewForList(multiView, userView, session);

    return singleView.stream().map(current -> {
      try {
        final BibliographicNote note = (BibliographicNote) current;
        final DAOBibliographicNotesOverflow daoOverflow = new DAOBibliographicNotesOverflow();
        note.setOverflowList(daoOverflow.getBibNotesOverflowList(note.getBibItemNumber(), userView, note.getNoteNbr(), session));
        final BibliographicNoteTag bibliographicNoteTag = new BibliographicNoteTag(note);
        final BibliographicStandardNoteDAO dao = new BibliographicStandardNoteDAO();
        bibliographicNoteTag.markUnchanged();
        bibliographicNoteTag.setOverflowList(note.getOverflowList());
        if (!language.equals("")) {
          StandardNoteAccessPoint noteAcs = dao.getBibNoteStardard(amicusNumber, userView, note.getNoteNbr(), session);
          bibliographicNoteTag.setNoteStandard(noteAcs);
          if (noteAcs != null) {
            bibliographicNoteTag.setValueElement(dao.getSTDDisplayString(noteAcs.getTypeCode(), language, session));
          }
          setBibliographicNoteContent(bibliographicNoteTag);
        }
        return bibliographicNoteTag;
      } catch (HibernateException e) {
        throw new ModMarccatException(e);
      }
    }).collect(Collectors.toList());

  }

  /**
   * Sets content for standard note type.
   *
   * @param bibliographicNoteTag -- the bibliographic note tag.
   */
  private void setBibliographicNoteContent(final BibliographicNoteTag bibliographicNoteTag) {
    if (!bibliographicNoteTag.isStandardNoteType()) {
      return;
    }
    String content = bibliographicNoteTag.getNote().getStringTextString();
    if (isNotNullOrEmpty(content) && !content.contains(Global.SUBFIELD_DELIMITER)) {
      content = Global.SUBFIELD_DELIMITER + "a" + content;
      bibliographicNoteTag.getNote().markUnchanged();
    }
    if (!isNotNullOrEmpty(content)) {
      content = Global.SUBFIELD_DELIMITER + "a" + "";
      bibliographicNoteTag.getNote().markUnchanged();
    }
    bibliographicNoteTag.getNote().setContent(content);
  }

  /**
   * Gets bibliographic relationship tags.
   *
   * @param amicusNumber -- the amicus number of item.
   * @param userView     -- the user view
   * @param session      -- the current session hibernate.
   * @return list of Tag bibliographic relationship.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  private List <BibliographicRelationshipTag> getBibliographicRelationships(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    List <BibliographicRelationship> multiView = session.find("from BibliographicRelationship t "
        + "where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1'",
      new Object[]{amicusNumber, userView},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    List <? extends PersistentObjectWithView> singleView = isolateViewForList(multiView, userView, session);
    return singleView.stream().map(current -> {
      BibliographicRelationshipTag bibliographicRelationshipTag = null;
      try {
        bibliographicRelationshipTag = new BibliographicRelationshipTag((BibliographicRelationship) current, userView, session);
      } catch (HibernateException e) {
        e.printStackTrace();
      }
      bibliographicRelationshipTag.markUnchanged();
      return bibliographicRelationshipTag;
    }).collect(Collectors.toList());
  }

  /**
   * Gets bibliographic name-title access point tags.
   *
   * @param amicusNumber -- the amicus number of item.
   * @param userView     -- the user view
   * @param session      -- the current session hibernate.
   * @return list of Tag name-title access point.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  private List<NameTitleAccessPoint> getNameTitleAccessPointTags(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    List<NameTitleAccessPoint> result = (List<NameTitleAccessPoint>) getAccessPointTags(NameTitleAccessPoint.class, amicusNumber, userView, session);
    return result.stream().map(tag -> {
      final NME_TTL_HDG hdg = (NME_TTL_HDG) tag.getDescriptor();
      try {
        hdg.setNameHeading((NME_HDG) new NameDescriptorDAO().load(hdg.getNameHeadingNumber(), userView, session));
        hdg.setTitleHeading((TTL_HDG) new TitleDescriptorDAO().load(hdg.getTitleHeadingNumber(), userView, session));
      } catch (HibernateException e) {
        throw new DataAccessException(String.format(Message.MOD_MARCCAT_00016_NO_HEADING_FOUND, tag.getHeadingNumber()));
      }
      return tag;
    }).collect(Collectors.toList());
  }

  /**
   * Gets bibliographic name access point tags (not if part of NameTitle tag).
   *
   * @param amicusNumber -- the amicus number of item.
   * @param userView     -- the user view
   * @param session      -- the current session hibernate.
   * @return list of Tag name access point.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  private List<NameAccessPoint> getNameAccessPointTags(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    List<NameAccessPoint> result = (List<NameAccessPoint>) getAccessPointTags(NameAccessPoint.class, amicusNumber, userView, session);
    return result.stream().filter(nameTag -> !nameTag.isPartOfNameTitle()).collect(Collectors.toList());
  }

  /**
   * Gets bibliographic title access point tags (not if part of NameTitle tag).
   *
   * @param amicusNumber -- the amicus number of item.
   * @param userView     -- the user view
   * @param session      -- the current session hibernate.
   * @return list of Tag title access point.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  private List<TitleAccessPoint> getTitleAccessPointTags(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    List<TitleAccessPoint> result = (List<TitleAccessPoint>) getAccessPointTags(TitleAccessPoint.class, amicusNumber, userView, session);
    return result.stream().filter(titleTag -> !titleTag.isPartOfNameTitle()).collect(Collectors.toList());
  }

  /**
   * Gets list of access point tags for a requested type (clazz).
   *
   * @param clazz        -- the class type specified for tag.
   * @param amicusNumber -- the amicus number of item.
   * @param userView     -- the user view
   * @param session      -- the current session hibernate.
   * @return list of specified access point tag.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  private List<? extends PersistentObjectWithView> getAccessPointTags(final Class clazz, final int amicusNumber, final int userView, final Session session) throws HibernateException {

    List<? extends PersistentObjectWithView> multiView = session.find("from " + clazz.getName() + " as t "
        + " where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1'",
      new Object[]{amicusNumber, userView},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    List<? extends PersistentObjectWithView> singleView = isolateViewForList(multiView, userView, session);
    loadHeadings(singleView, userView, session);

    return singleView;
  }


  /**
   * Loads the BIB_ITM data into a new BibliographicItem.
   *
   * @param amicusNumber -- the item id to load.
   * @param userView     -- user view associated.
   * @param session      -- the current session hibernate.
   * @return the bibliographic item.
   * @throws HibernateException  in case of hibernate exception.
   * @throws DataAccessException in case of data access exception.
   */
  private BibliographicItem getBibliographicItem(final int amicusNumber, final int userView, final Session session) throws HibernateException, RecordNotFoundException {

    final BibliographicItem item = new BibliographicItem();

    final BIB_ITM bibItm = new DAOBibItem().load(amicusNumber, userView, session);
    item.setBibItmData(bibItm);
    item.setUserView(userView);

    return item;
  }

  /**
   * Updates s_cas_cache_bib_itm_dsply table.
   *
   * @param bibItemNumber        -- the bibliographic item number.
   * @param cataloguingView      -- marccat view associated.
   * @param uniformTitleSortForm -- the uniforme title sort form.
   * @param titleSortForm        -- the title sort form.
   * @param session              -- the current session hibernate.
   * @throws HibernateException in case of hibernate exception.
   */
  public void updateItemDisplayCacheTable(final int bibItemNumber, final int cataloguingView, final String uniformTitleSortForm,
                                          final String titleSortForm, final Session session) throws HibernateException {
    final Transaction transaction = getTransaction(session);

    CallableStatement proc = null;
    try {
      String result;
      Connection connection = session.connection();
      proc = connection.prepareCall("{call AMICUS.CFN_PR_CACHE_UPDATE(?, cast(? as smallint), ?, ?, ?, ?) }");
      proc.setInt(1, bibItemNumber);
      proc.setInt(2, cataloguingView);
      proc.setInt(3, -1);
      proc.setString(4, uniformTitleSortForm);
      proc.setString(5, titleSortForm);
      proc.registerOutParameter(6, Types.VARCHAR);
      proc.execute();
      result = proc.getString(6);
      if (!result.equals("0")) {
        cleanUp(transaction);
        throw new CacheUpdateException();
      }
      transaction.commit();
    } catch (Exception e) {
      cleanUp(transaction);

      throw new HibernateException(e);
    } finally {
      try {
        if (proc != null) {
          proc.close();
        }
      } catch (SQLException ex) {
        logger.error(ex.getMessage(), ex);
      }
    }
  }

  /**
   * Save or insert record in S_CACHE_BIB_ITM_DSPLY.
   *
   * @param item    -- the bibliographic record to save.
   * @param session -- the hibernate session associated to request.
   * @throws DataAccessException
   */
  protected void updateItemDisplayCacheTable(final CatalogItem item, final Session session)
    throws HibernateException {
    final Tag tag130 = item.findFirstTagByNumber("130", session);
    final Tag tag245 = item.findFirstTagByNumber("245", session);
    String uniformTitleSortForm = "";
    String titleSortForm = "";
    if (tag130 != null) {
      uniformTitleSortForm = getTitleSortForm((TitleAccessPoint) tag130);
    }
    if (tag245 != null) {
      titleSortForm = getTitleSortForm((TitleAccessPoint) tag245);
    }
    updateItemDisplayCacheTable(item.getAmicusNumber(), item.getUserView(), uniformTitleSortForm, titleSortForm, session);
    updateFullRecordCacheTable(session, item);
  }

  /**
   * Return the sort form for the title access point
   *
   * @param tag
   * @return the sort form for the title access point
   */
  private String getTitleSortForm(AccessPoint tag) {
    String uniformTitleSortForm;
    String accessPoint = tag.getAccessPointStringText().toDisplayString();
    TTL_HDG title = new TTL_HDG();
    title.setStringText("\u001fc" + accessPoint);
    title.calculateAndSetSortForm();
    uniformTitleSortForm = title.getSortForm();
    return uniformTitleSortForm;
  }

  @Override
  public CatalogItem getCatalogItemByKey(final Session session, final int... key) {
    int id = key[0];
    int cataloguingView = key[1];

    try {
      return getBibliographicItemByAmicusNumber(id, cataloguingView, session);
    } catch (final HibernateException exception) {
      throw new DataAccessException(exception);
    }
  }


  @SuppressWarnings("unchecked")
  private List<PublisherManager> getPublisherTags(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    List<PublisherAccessPoint> publisherAccessPoints = (List<PublisherAccessPoint>) getAccessPointTags(PublisherAccessPoint.class, amicusNumber, userView, session);
    return publisherAccessPoints.stream().map(PublisherManager::new).collect(Collectors.toList());
  }



  /**
   * @param bibItemNumber
   * @param cataloguingView
   * @deprecated
   */
  @Deprecated
  public void updateCacheTable(
    final int bibItemNumber,
    final int cataloguingView) {
  }




}
