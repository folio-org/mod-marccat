package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicItem;
import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicTagImpl;
import org.folio.cataloging.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.integration.GlobalStorage;
import org.folio.cataloging.integration.log.MessageCatalogStorage;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.util.XmlUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.folio.cataloging.F.deepCopy;
import static org.folio.cataloging.F.isNotNullOrEmpty;

/**
 * Class for data access bibliographic item.
 *
 * @author natasciab
 * @since 1.0
 */

//TODO: some methods can't be modify now, because some implementation logic have to be moved into API and storage
public class BibliographicCatalogDAO extends CatalogDAO {
  private Log logger = new Log (BibliographicCatalogDAO.class);

  public BibliographicCatalogDAO() {
    super ( );
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
    BibliographicItem item = getBibliographicItemWithoutRelationships (amicusNumber, userView, session);
    item.getTags ( ).addAll (getBibliographicRelationships (amicusNumber, userView, session));
    item.getTags ( ).forEach (tag -> {
      tag.setTagImpl (new BibliographicTagImpl ( ));
      if (tag instanceof MaterialDescription) {
        tag.setCorrelation (1, GlobalStorage.MATERIAL_DESCRIPTION_HEADER_TYPE);
      }
      tag.setCorrelationKey (tag.getTagImpl ( ).getMarcEncoding (tag, session));
    });
    item.sortTags ( );
    return item;
  }


  @Override
  /**
   * Save or update record in full_cache.
   */
  public void updateFullRecordCacheTable(final Session session, final CatalogItem item) throws HibernateException {
    updateFullRecordCacheTable (session, item, true);
  }

  // 2018 Paul Search Engine Java
  private void updateFullRecordCacheTable(final Session session, final CatalogItem item, final boolean updateRelatedRecs) throws HibernateException {
    FULL_CACHE cache;
    DAOFullCache dao = new DAOFullCache ( );
    try {
      cache = dao.load (session, item.getAmicusNumber ( ), item.getUserView ( ));
    } catch (RecordNotFoundException e) {
      cache = new FULL_CACHE ( );
      cache.setItemNumber (item.getAmicusNumber ( ));
      cache.setUserView (item.getUserView ( ));
    }
    cache.setRecordData (XmlUtils.documentToString (item.toExternalMarcSlim ( )));
    cache.markChanged ( );
    persistByStatus (cache, session);
    session.evict (cache);

    if (updateRelatedRecs) {
      for ( Object o : item.getTags ( ) ) {
        if (o instanceof BibliographicRelationshipTag) {
          BibliographicRelationshipTag t = (BibliographicRelationshipTag) o;
          CatalogItem relItem = getBibliographicItemByAmicusNumber (t.getTargetBibItemNumber ( ), item.getUserView ( ), session);
          updateFullRecordCacheTable (session, relItem, false);
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
  private BibliographicItem getBibliographicItemWithoutRelationships(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    try {
      BibliographicItem item = getBibliographicItem (amicusNumber, userView, session);
      item.getTags ( ).addAll (getHeaderFields (item, userView, session));

      try {
        final String language = item.getItemEntity ( ).getLanguageOfCataloguing ( );
        item.getTags ( ).addAll (getBibliographicNotes (amicusNumber, userView, language, session));
      } catch (Exception e) {
        logger.error ("notes not loaded", e);
      }

      try {
        item.getTags ( ).addAll ((List <ClassificationAccessPoint>) getAccessPointTags (ClassificationAccessPoint.class, amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error ("classification not loaded");
      }

      try {
        item.getTags ( ).addAll ((List <ControlNumberAccessPoint>) getAccessPointTags (ControlNumberAccessPoint.class, amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error ("ControlNumberAccessPoint not loaded");
      }

      try {
        item.getTags ( ).addAll (getNameAccessPointTags (amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error ("NameAccessPoint not loaded");
      }

      try {
        item.getTags ( ).addAll (getTitleAccessPointTags (amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error ("TitleAccessPoint not loaded");
      }

      try {
        item.getTags ( ).addAll (getNameTitleAccessPointTags (amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error ("NameTitleAccessPoint not loaded");
      }

      try {
        item.getTags ( ).addAll (getPublisherTags (amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error ("PublisherTags not loaded", e);
      }

      try {
        item.getTags ( ).addAll ((List <SubjectAccessPoint>) getAccessPointTags (SubjectAccessPoint.class, amicusNumber, userView, session));
      } catch (Exception e) {
        logger.error ("SubjectAccessPoint not loaded");
      }

      item.setModelItem (new BibliographicModelItemDAO ( ).load (amicusNumber, session));

      return item;

    } catch (HibernateException he) {
      logger.error (MessageCatalogStorage._00010_DATA_ACCESS_FAILURE, he);
      throw new HibernateException (he);
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
  private List <Tag> getHeaderFields(final BibliographicItem item, final int userView, final Session session) throws HibernateException {

    final BIB_ITM bibItemData = item.getBibItmData ( );
    final List <Tag> result = new ArrayList <> ( );
    result.add (new BibliographicLeader ( ));
    result.add (new BibliographicControlNumberTag ( ));
    result.add (new BibliographicDateOfLastTransactionTag ( ));
    result.add (new BibliographicCataloguingSourceTag ( ));

    if (isNotNullOrEmpty (bibItemData.getAuthenticationCenterStringText ( ))) {
      result.add (new BibliographicAuthenticationCodeTag ( ));
    }

    if (isNotNullOrEmpty (bibItemData.getCountryStringText ( ))) {
      result.add (new CountryOfPublicationTag ( ));
    }

    if (isNotNullOrEmpty (bibItemData.getFormOfMusicStringText ( ))) {
      result.add (new FormOfMusicalCompositionTag ( ));
    }

    if (isNotNullOrEmpty (bibItemData.getGeographicAreaStringText ( ))) {
      result.add (new BibliographicGeographicAreaTag ( ));
    }

    if (isNotNullOrEmpty (bibItemData.getLanguageStringText ( ))) {
      result.add (new LanguageCodeTag ( ));
    }

    if (isNotNullOrEmpty (bibItemData.getProjectedPublicationDateCode ( ))) { //todo
      result.add (new ProjectedPublicationDateTag ( ));
    }

    if (isNotNullOrEmpty (bibItemData.getSpecialCodedDatesStringText ( ))) {
      result.add (new SpecialCodedDatesTag ( ));
    }

    if (isNotNullOrEmpty (bibItemData.getTimePeriodStringText ( ))) {
      result.add (new TimePeriodOfContentTag ( ));
    }

    final int amicusNumber = item.getAmicusNumber ( );
    result.addAll (getMaterialDescriptions (amicusNumber, userView, session));
    result.addAll (getPhysicalDescriptions (amicusNumber, userView, session)); //Fixme
    result.addAll (getMusicalInstruments (amicusNumber, userView, session));

    return result.stream ( ).map (tag -> {
      if (tag instanceof PersistsViaItem)
        ((PersistsViaItem) tag).setItemEntity (bibItemData);

      return tag;
    }).collect (Collectors.toList ( ));

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
  private List <MaterialDescription> getMaterialDescriptions(final int amicusNumber, final int userView, final Session session) throws HibernateException {

    List <MaterialDescription> multiView = session.find ("from MaterialDescription t "
        + "where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1' ",
      new Object[]{amicusNumber, userView},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    return (List <MaterialDescription>) isolateViewForList (multiView, userView, session);
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
  private List <PhysicalDescription> getPhysicalDescriptions(final int amicusNumber, final int userView, final Session session) throws HibernateException {

    List <PhysicalDescription> multiView = session.find (
      "from org.folio.cataloging.dao.persistence.PhysicalDescription t "
        + "where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1' ",
      new Object[]{amicusNumber, userView},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    return (List <PhysicalDescription>) isolateViewForList (multiView, userView, session);
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
  private List <NumberOfMusicalInstrumentsTag> getMusicalInstruments(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    List <NumberOfMusicalInstrumentsTag> multiView = session.find (
      "from NumberOfMusicalInstrumentsTag t "
        + "where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1' ",
      new Object[]{amicusNumber, userView},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    return (List <NumberOfMusicalInstrumentsTag>) isolateViewForList (multiView, userView, session);
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
  private List <BibliographicNoteTag> getBibliographicNotes(final int amicusNumber, final int userView, final String language, final Session session) throws HibernateException {

    List <BibliographicNote> multiView = session.find ("from BibliographicNote t "
        + "where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1'",
      new Object[]{amicusNumber, userView},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    List <? extends PersistentObjectWithView> singleView = isolateViewForList (multiView, userView, session);

    return singleView.stream ( ).map (current -> {
      try {
        final BibliographicNote note = (BibliographicNote) current;
        final DAOBibliographicNotesOverflow daoOverflow = new DAOBibliographicNotesOverflow ( );
        note.setOverflowList (daoOverflow.getBibNotesOverflowList (note.getBibItemNumber ( ), userView, note.getNoteNbr ( ), session));
        final BibliographicNoteTag bibliographicNoteTag = new BibliographicNoteTag (note);
        final BibliographicStandardNoteDAO dao = new BibliographicStandardNoteDAO ( );
        bibliographicNoteTag.markUnchanged ( );
        bibliographicNoteTag.setOverflowList (note.getOverflowList ( ));
        if (!language.equals ("")) {
          StandardNoteAccessPoint noteAcs = dao.getBibNoteStardard (amicusNumber, userView, note.getNoteNbr ( ), session);
          bibliographicNoteTag.setNoteStandard (noteAcs);
          if (noteAcs != null) {
            bibliographicNoteTag.setValueElement (dao.getSTDDisplayString (noteAcs.getTypeCode ( ), language, session));
          }
          setBibliographicNoteContent (bibliographicNoteTag);
        }
        return bibliographicNoteTag;
      } catch (HibernateException e) {
        throw new RuntimeException (e);
      }
    }).collect (Collectors.toList ( ));

  }

  /**
   * Sets content for standard note type.
   *
   * @param bibliographicNoteTag -- the bibliographic note tag.
   */
  private void setBibliographicNoteContent(final BibliographicNoteTag bibliographicNoteTag) {
    if (!bibliographicNoteTag.isStandardNoteType ( )) {
      return;
    }
    String content = bibliographicNoteTag.getNote ( ).getContent ( );
    if (isNotNullOrEmpty (content) && !content.contains (GlobalStorage.DOLLAR)) {
      content = GlobalStorage.DOLLAR + "a" + content;
      bibliographicNoteTag.getNote ( ).markUnchanged ( );
    }
    if (!isNotNullOrEmpty (content)) {
      content = GlobalStorage.DOLLAR + "a" + "";
      bibliographicNoteTag.getNote ( ).markUnchanged ( );
    }
    bibliographicNoteTag.getNote ( ).setContent (content);
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
  private List <BibliographicRelationshipTag> getBibliographicRelationships(final int amicusNumber, final int userView, final Session session) throws HibernateException, DataAccessException {
    List <BibliographicRelationship> multiView = session.find ("from BibliographicRelationship t "
        + "where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1'",
      new Object[]{amicusNumber, userView},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    List <? extends PersistentObjectWithView> singleView = isolateViewForList (multiView, userView, session);
    return singleView.stream ( ).map (current -> {
      final BibliographicRelationshipTag bibliographicRelationshipTag = new BibliographicRelationshipTag ((BibliographicRelationship) current, userView);
      bibliographicRelationshipTag.markUnchanged ( );
      return bibliographicRelationshipTag;
    }).collect (Collectors.toList ( ));
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
  private List <NameTitleAccessPoint> getNameTitleAccessPointTags(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    List <NameTitleAccessPoint> result = (List <NameTitleAccessPoint>) getAccessPointTags (NameTitleAccessPoint.class, amicusNumber, userView, session);
    return result.stream ( ).map (tag -> {
      final NME_TTL_HDG hdg = (NME_TTL_HDG) tag.getDescriptor ( );
      //Done by Carmen in branch 73 //TODO after merge remove comments
      //TODO hdg.setNameHeading((NME_HDG) new DAONameDescriptor().load(hdg.getNameHeadingNumber(), userView, session)); //TODO: session missing
      //TODO hdg.setTitleHeading((TTL_HDG) new DAOTitleDescriptor().load(hdg.getTitleHeadingNumber(), userView, session)); //TODO: session missing
      return tag;
    }).collect (Collectors.toList ( ));
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
  private List <NameAccessPoint> getNameAccessPointTags(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    List <NameAccessPoint> result = (List <NameAccessPoint>) getAccessPointTags (NameAccessPoint.class, amicusNumber, userView, session);
    return result.stream ( ).filter (nameTag -> !nameTag.isPartOfNameTitle ( )).collect (Collectors.toList ( ));
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
  private List <TitleAccessPoint> getTitleAccessPointTags(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    List <TitleAccessPoint> result = (List <TitleAccessPoint>) getAccessPointTags (TitleAccessPoint.class, amicusNumber, userView, session);
    return result.stream ( ).filter (titleTag -> !titleTag.isPartOfNameTitle ( )).collect (Collectors.toList ( ));
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
  private List <? extends PersistentObjectWithView> getAccessPointTags(final Class clazz, final int amicusNumber, final int userView, final Session session) throws HibernateException {

    List <? extends PersistentObjectWithView> multiView = session.find ("from " + clazz.getName ( ) + " as t "
        + " where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1'",
      new Object[]{amicusNumber, userView},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    List <? extends PersistentObjectWithView> singleView = isolateViewForList (multiView, userView, session);
    loadHeadings (singleView, userView, session);

    return singleView;
  }

  @Deprecated
  public List loadAccessPointTags(Class apfClass, int id, int userView) throws DataAccessException {
    // use getAccessPointTags
		/*try {
			return getAccessPointTags(apfClass, id, userView);
		} catch (HibernateException e) {
			logAndWrap(e);
		}*/
    return null;
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
  private BibliographicItem getBibliographicItem(final int amicusNumber, final int userView, final Session session) throws HibernateException, DataAccessException {

    final BibliographicItem item = new BibliographicItem ( );

    final BIB_ITM bibItm = new DAOBibItem ( ).load (amicusNumber, userView, session);
    item.setBibItmData (bibItm);
    item.setUserView (userView);

    return item;
  }

  //TODO: can be removed?
  protected void insertDeleteTable(final CatalogItem item, final UserProfile user) throws DataAccessException {
    insertDeleteTable (item.getAmicusNumber ( ), item.getUserView ( ), user);
  }

  /**
   * Updates s_cas_cache_bib_itm_dsply table.
   *
   * @param bibItemNumber   -- the bibliographic item number.
   * @param cataloguingView -- cataloging view associated.
   * @param session         -- the current session hibernate.
   * @throws HibernateException in case of hibernate exception.
   */
  public void updateItemDisplayCacheTable(final int bibItemNumber, final int cataloguingView, final Session session) throws HibernateException {
    final Transaction transaction = getTransaction (session);

    CallableStatement proc = null;
    try {
      String result = null;
      Connection connection = session.connection ( );
      proc = connection.prepareCall ("{call AMICUS.CFN_PR_CACHE_UPDATE(?, cast(? as smallint), ?, ?) }");
      proc.setInt (1, bibItemNumber);
      proc.setInt (2, cataloguingView);
      proc.setInt (3, -1);
      proc.registerOutParameter (4, Types.VARCHAR);
      proc.execute ( );
      result = proc.getString (4);
      if (!result.equals ("0")) {
        cleanUp (transaction);
        logger.error (MessageCatalogStorage._00011_CACHE_UPDATE_FAILURE, result);
        throw new CacheUpdateException ( );
      }
      transaction.commit ( );
    } catch (Exception e) {
      cleanUp (transaction);
      logger.error (MessageCatalogStorage._00010_DATA_ACCESS_FAILURE, e);
      throw new HibernateException (e);
    } finally {
      try {
        if (proc != null) proc.close ( );
      } catch (SQLException ex) {
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
    throws DataAccessException {
    try {
      updateItemDisplayCacheTable (item.getAmicusNumber ( ).intValue ( ), item.getUserView ( ), session);
      updateFullRecordCacheTable (session, item);
    } catch (Exception e) {
    }
  }

  //TODO: maybe can be removed
  private void insertDeleteTable(
    final int bibItemNumber,
    final int cataloguingView,
    final UserProfile user)
    throws DataAccessException {
			/*new TransactionalHibernateOperation() {
				public void doInHibernateTransaction(Session s) throws SQLException, HibernateException, CacheUpdateException
					{
					int result;
					CallableStatement proc = null;
					try {
						Connection connection = s.connection();
						proc =
							connection.prepareCall(
								"{call AMICUS.Cfn_Pr_Bib_Deleted(?, ?, ?, ?) }");
						proc.setInt(1, bibItemNumber);
						proc.setInt(2, cataloguingView);
						//cambiare con tipo varchar anche sulla tabella
						proc.setString(3, user.getName());
						proc.registerOutParameter(4, Types.INTEGER);
						proc.execute();
						result = proc.getInt(4);
						if (result != 0) {
							logger.error("Errore nella proc AMICUS.Cfn_Pr_Bib_Deleted, code : " + result);
							throw new CacheUpdateException();
						}
					} finally {
						try {
							if(proc!=null) proc.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
			.execute();*/
  }


  @Override
  public CatalogItem getCatalogItemByKey(final Session session, final int... key) throws DataAccessException {
    int id = key[0];
    int cataloguingView = key[1];

    try {
      return getBibliographicItemByAmicusNumber (id, cataloguingView, session);
    } catch (final HibernateException exception) {
      throw new DataAccessException (exception);
    }
  }

  /**
   * Transfers the bibliographic items from one heading to another.
   *
   * @param source  -- the source heading descriptor.
   * @param target  -- the target heading descriptor.
   * @param session -- the current hibernate session.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  public void transferItems(final Descriptor source, final Descriptor target, final Session session) throws HibernateException {
    transferChecks (source, target);

    if (source instanceof PUBL_HDG) {
      transferPublisherItems (source, target, session);
    } else {
      final Transaction transaction = getTransaction (session);
      try {
        int cataloguingView = View.toIntView (source.getUserViewString ( ));

        List <? extends PersistentObjectWithView> raw = session.find ("from " + source.getAccessPointClass ( ).getName ( ) + " as apf "
            + " where apf.headingNumber = ? "
            + " and substr(apf.userViewString, ?, 1) = '1'",
          new Object[]{source.getKey ( ).getHeadingNumber ( ), cataloguingView},
          new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

        List <? extends PersistentObjectWithView> singleViewList = isolateViewForList (raw, cataloguingView, session);
        singleViewList.forEach (persistentObject -> {
          try {
            AccessPoint anAPF = (AccessPoint) persistentObject;
            anAPF.markDeleted ( );
            persistByStatus (anAPF, session);

            anAPF = (AccessPoint) anAPF.clone ( );
            anAPF.markNew ( );
            anAPF.setHeadingNumber (target.getKey ( ).getHeadingNumber ( ));
            persistByStatus (anAPF, session);

          } catch (HibernateException e) {
            throw new RuntimeException (e);
          }
        });
        transaction.commit ( );
      } catch (Exception e) {
        cleanUp (transaction);
        logger.error (MessageCatalogStorage._00010_DATA_ACCESS_FAILURE, e);
        throw new HibernateException (e);
      }

      if (target.changeAffectsCacheTable ( )) {
        ((DAODescriptor) target.getDAO ( )).updateCacheTable (target, session);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void transferPublisherItems(final Descriptor source, final Descriptor target, final Session session) throws HibernateException {
    final Transaction transaction = getTransaction (session);
    try {
      int cataloguingView = View.toIntView (source.getUserViewString ( ));

      List <PUBL_TAG> raw = session.find ("from  PUBL_TAG as tag "
          + " where  "
          + " tag.publisherHeadingNumber=?"
          + " and substr(tag.userViewString, ?, 1) = '1'",
        new Object[]{source.getKey ( ).getHeadingNumber ( ), cataloguingView},
        new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

      List <PUBL_TAG> singleViewList = (List <PUBL_TAG>) isolateViewForList (raw, cataloguingView, session);
      singleViewList.forEach (anAPF -> {
        try {
          anAPF.markChanged ( );
          anAPF.setPublisherHeadingNumber (target.getKey ( ).getHeadingNumber ( ));
          persistByStatus (anAPF, session);
        } catch (HibernateException e) {
          throw new RuntimeException (e);
        }
      });
      transaction.commit ( );
    } catch (Exception e) {
      cleanUp (transaction);
      logger.error (MessageCatalogStorage._00010_DATA_ACCESS_FAILURE, e);
      throw new HibernateException (e);
    }

    if (target.changeAffectsCacheTable ( )) {
      ((DAODescriptor) target.getDAO ( )).updateCacheTable (target, session);
    }
  }

  /**
   * Finds the items subjects that have equivalent subject headings (other languages) and adds these equivalent headings to the item.
   *
   * @param item    -- current catalog item corresponding to record.
   * @param session -- the current hibernate session.
   * @return a collection of equivalent subject access point.
   * @throws HibernateException in case of hibernate exception.
   */
  public Collection <SubjectAccessPoint> getEquivalentSubjects(final CatalogItem item, final Session session) throws HibernateException {
    final DAODescriptor dao = new SubjectDescriptorDAO ( );
    Collection <SubjectAccessPoint> newTags = new HashSet <> ( );

    List <Tag> allTags = item.getTags ( );
    allTags.stream ( ).filter (tag -> tag instanceof SubjectAccessPoint).forEach (subjectAccessPoint -> {
      try {
        List <REF> refs = dao.getCrossReferences (((SubjectAccessPoint) subjectAccessPoint).getDescriptor ( ), item.getUserView ( ), session);
        refs.stream ( ).filter (aRef -> ReferenceType.isEquivalence (aRef.getType ( ))).forEach (aRef -> {
          final SubjectAccessPoint aSubj = (SubjectAccessPoint) deepCopy (subjectAccessPoint);
          aSubj.markNew ( );
          aSubj.setDescriptor (dao.load (aRef.getTarget ( ), item.getUserView ( )));
          aSubj.setHeadingNumber (aSubj.getDescriptor ( ).getKey ( ).getHeadingNumber ( ));

          if (!allTags.contains (aSubj)) {
            newTags.add (aSubj);
          }
        });
      } catch (HibernateException e) {
        throw new RuntimeException (e);
      }
    });

    return newTags;
  }

  @Deprecated
  public Collection <SubjectAccessPoint> getEquivalentSubjects(final CatalogItem item) throws DataAccessException {
    return null;
  }

  @SuppressWarnings("unchecked")
  private List <PublisherManager> getPublisherTags(final int amicusNumber, final int userView, final Session session) throws HibernateException {
    List <PublisherAccessPoint> publisherAccessPoints = (List <PublisherAccessPoint>) getAccessPointTags (PublisherAccessPoint.class, amicusNumber, userView, session);
    return publisherAccessPoints.stream ( ).map (accessPoint -> new PublisherManager (accessPoint)).collect (Collectors.toList ( ));
  }

  /**
   * Check function for transferring from descriptor to another.
   *
   * @param source -- the source descriptor.
   * @param target -- the target descriptor.
   */
  private void transferChecks(final Descriptor source, final Descriptor target) {
    if (target == null) {
      logger.error (MessageCatalogStorage._00012_TARGET_DESCRIPTOR_NULL);
      throw new IllegalArgumentException ( );
    }

    if (source.getClass ( ) != target.getClass ( )) {
      logger.error (MessageCatalogStorage._00013_DIFFERENT_TARGET_SOURCE);
      throw new IllegalArgumentException ( );
    }
  }

  /**
   * Transfers headings from classification descriptor to subject descriptor.
   *
   * @param source       -- the source descriptor.
   * @param target       -- the target descriptor.
   * @param functionCode -- the function code to apply.
   * @param session      -- the current hibernate session.
   * @throws HibernateException in case of hibernate exception.
   */
  public void transferItemsForSuggest(final Descriptor source, final Descriptor target, final int functionCode, final Session session) throws HibernateException {

    transferChecks (source, target);
    final Transaction transaction = getTransaction (session);
    try {
      List <ClassificationAccessPoint> raw = session.find ("from ClassificationAccessPoint " +
          " as apf where apf.headingNumber = ?",
        new Object[]{source.getKey ( ).getHeadingNumber ( )}, new Type[]{Hibernate.INTEGER});

      raw.forEach (anAPF -> {
        try {

          int bibItem = anAPF.getItemNumber ( );
          String userViewString = anAPF.getUserViewString ( );
          anAPF.markDeleted ( );
          persistByStatus (anAPF, session);

          final SubjectAccessPoint sbjAccessPoint = new SubjectAccessPoint ( );
          sbjAccessPoint.setItemNumber (bibItem);
          sbjAccessPoint.setUserViewString (userViewString);
          sbjAccessPoint.setFunctionCode (functionCode);
          sbjAccessPoint.markNew ( );
          sbjAccessPoint.setHeadingNumber (target.getKey ( ).getHeadingNumber ( ));
          persistByStatus (sbjAccessPoint, session);

        } catch (HibernateException e) {
          throw new RuntimeException (e);
        }
      });
      transaction.commit ( );
    } catch (Exception e) {
      cleanUp (transaction);
      logger.error (MessageCatalogStorage._00010_DATA_ACCESS_FAILURE, e);
      throw new HibernateException (e);
    }

    if (target.changeAffectsCacheTable ( )) {
      ((DAODescriptor) target.getDAO ( )).updateCacheTable (target, session);
    }
  }

  /**
   * Updates the third correlation only for subjects (Function code).
   *
   * @param descriptor -- the tag descriptor.
   * @param session    -- the current hibernate session.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  public void updateSubjectFunctionCode(final Descriptor descriptor, final Session session) throws HibernateException {
    final Transaction transaction = getTransaction (session);
    try {
      final BibliographicCorrelationDAO bibliographicCorrelationDAO = new BibliographicCorrelationDAO ( );
      final SBJCT_HDG sbj = (SBJCT_HDG) descriptor;
      final int functionCode = bibliographicCorrelationDAO.getFirstValueFromSecondCorrelations (
        GlobalStorage.SUBJECT_CATEGORY, sbj.getTypeCode ( ), sbj.getSourceCode ( ), session);

      List <Integer> secondCorrelationValues = bibliographicCorrelationDAO.getSecondCorrelationListByOtherCorrelations (
        GlobalStorage.SUBJECT_CATEGORY, sbj.getTypeCode ( ), sbj.getSourceCode ( ), session)
        .stream ( ).map (Correlation::getDatabaseSecondValue).collect (Collectors.toList ( ));

      final int cataloguingView = View.toIntView (descriptor.getUserViewString ( ));

      List <? extends PersistentObjectWithView> raw = session.find ("from " + descriptor.getAccessPointClass ( ).getName ( ) + " as apf "
          + " where apf.headingNumber = ? and substr(apf.userViewString, ?, 1) = '1'",
        new Object[]{descriptor.getKey ( ).getHeadingNumber ( ), cataloguingView},
        new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

      List <? extends PersistentObjectWithView> singleViewList = isolateViewForList (raw, cataloguingView, session);
      singleViewList.forEach (item -> {
        AccessPoint anAPF = (AccessPoint) item;
        if (!secondCorrelationValues.contains (anAPF.getFunctionCode ( ))) {
          try {
            int amicusNumber = anAPF.getItemNumber ( );
            String userViewString = ((SubjectAccessPoint) anAPF).getUserViewString ( );
            anAPF.markDeleted ( );
            persistByStatus (anAPF, session);

            anAPF = new SubjectAccessPoint ( );
            anAPF.setItemNumber (amicusNumber);
            ((SubjectAccessPoint) anAPF).setUserViewString (userViewString);
            anAPF.setFunctionCode (functionCode);
            anAPF.markNew ( );
            anAPF.setHeadingNumber (descriptor.getKey ( ).getHeadingNumber ( ));
            persistByStatus (anAPF, session);
          } catch (HibernateException e) {
            logger.error (MessageCatalogStorage._00010_DATA_ACCESS_FAILURE, e);
          }
        }
      });
      transaction.commit ( );

    } catch (Exception e) {
      cleanUp (transaction);
      logger.error (MessageCatalogStorage._00010_DATA_ACCESS_FAILURE, e);
      throw new HibernateException (e);
    }
  }

  /* (non-Javadoc)
   * @see CatalogDAO#getCatalogItemByKey(java.lang.Object[])
   */
  @Deprecated
  public CatalogItem getCatalogItemByKey(Object[] key)
    throws DataAccessException {

	    /*int id = ((Integer) key[0]).intValue();
		int cataloguingView = ((Integer) key[1]).intValue();
		return getBibliographicItemByAmicusNumber(id, cataloguingView);*/
    return null;
  }

  @Deprecated
  public BibliographicItem getBibliographicItemWithoutRelationships(final int id,
                                                                    int userView)
    throws DataAccessException {
        /*try {
            BibliographicItem item = getBibliographicItem(id, userView);
            item.addAllTags(
                    (Tag[]) getHeaderFields(item, userView).toArray(new Tag[0]));

            String lingua = item.getItemEntity().getLanguageOfCataloguing();
            try {
                item.addAllTags((Tag[]) getBibliographicNotes(id, userView,
                        lingua).toArray(new Tag[0]));
            } catch (Exception e) {}

            try {
                item.addAllTags((Tag[]) getAccessPointTags(
                        ClassificationAccessPoint.class, id, userView).toArray(
                        new Tag[0]));
            } catch (Exception e) {}

            try{
                item.addAllTags(
                        (Tag[]) getAccessPointTags(
                                ControlNumberAccessPoint.class,
                                id,
                                userView).toArray(
                                new Tag[0]));
            }catch (Exception e) {}
            try{
                item.addAllTags(
                        (Tag[]) getNonNameTitleAccessPointTags(
                                NameAccessPoint.class,
                                id,
                                userView).toArray(
                                new Tag[0]));
            }catch (Exception e) {}
            try{
                item.addAllTags(
                        (Tag[]) getNonNameTitleAccessPointTags(
                                TitleAccessPoint.class,
                                id,
                                userView).toArray(
                                new Tag[0]));
            }catch (Exception e) {}
            try{
                item.addAllTags(
                        (Tag[]) getNameTitleAccessPointTags(id, userView).toArray(
                                new Tag[0]));
            }catch (Exception e) {}
            try{
                item.addAllTags(
                        (Tag[]) getPublisherTags(id, userView).toArray(new Tag[0]));
            }catch (Exception e) {}
            try{
                item.addAllTags(
                        (Tag[]) getAccessPointTags(
                                SubjectAccessPoint.class,
                                id,
                                userView).toArray(
                                new Tag[0]));
            }catch (Exception e) {}
            //TODO change the load method with that of the session
            //item.setModelItem(new DAOBibliographicModelItem().load(id));
            Iterator iter = item.getTags().iterator();
            while (iter.hasNext()) {
                ((Tag) iter.next()).setTagImpl(new BibliographicTagImpl());
            }


            return item;
        } catch (HibernateException he) {
            logAndWrap(he);
            return null;
        }*/
    return null;
  }

  @Deprecated
  public void updateCacheTable(
    final int bibItemNumber,
    final int cataloguingView)
    throws DataAccessException {
        /*new TransactionalHibernateOperation() {
            public void doInHibernateTransaction(Session s) throws SQLException, HibernateException, CacheUpdateException
            {
                int result;
                CallableStatement proc = null;
                try {
                    Connection connection = s.connection();
                    proc =
                            connection.prepareCall(
                                    "{call AMICUS.CFN_PR_CACHE_UPDATE(?, ?, ?, ?) }");
                    proc.setInt(1, bibItemNumber);
                    proc.setInt(2, cataloguingView);
                    proc.setInt(3, -1); // this parameter no longer used
                    proc.registerOutParameter(4, Types.INTEGER);
                    proc.execute();
                    result = proc.getInt(4);
                    if (result != 0) {
                        logger.error("Errore nella proc AMICUS.CFN_PR_CACHE_UPDATE, code : " + result);
                        throw new CacheUpdateException();
                    }
                } finally {
                    try {
                        if(proc!=null) proc.close();
                    } catch (SQLException ex) {
                        // TODO _MIKE Auto-generated catch block
                        ex.printStackTrace();
                    }
                }
            }
        }
                .execute();*/
  }


}
