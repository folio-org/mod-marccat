package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.bibliographic.*;
import org.folio.cataloging.business.cataloguing.common.AccessPoint;
import org.folio.cataloging.business.cataloguing.common.CatalogItem;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.CacheUpdateException;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.util.XmlUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

import static org.folio.cataloging.F.deepCopy;

public class BibliographicCatalogDAO extends CatalogDAO
{
	private static final Log logger = LogFactory.getLog(BibliographicCatalogDAO.class);

	public BibliographicCatalogDAO() {
		super();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.CatalogDAO#getBibliographicItemByAmicusNumber(int)
	 */
	public BibliographicItem getBibliographicItemByAmicusNumber(
		int id,
		int userView)
		throws DataAccessException {
		try {
			BibliographicItem item =
				getBibliographicItemWithoutRelationships(id, userView);
			item.addAllTags(
				(Tag[]) getBibliographicRelationships(id, userView).toArray(
					new Tag[0]));

			Iterator iter = item.getTags().iterator();
			while (iter.hasNext()) {
				((Tag) iter.next()).setTagImpl(new BibliographicTagImpl());
			}

			if (logger.isDebugEnabled())
				logger.debug(
					"Bibliographic item: "
						+ Integer.toString(id)
						+ " successfully loaded from DB");
			return item;
		} catch (HibernateException he) {
			logAndWrap(he);
			return null;
		}
	}

	// 2018 Paul Search Engine Java
	@Override
	public void updateFullRecordCacheTable(CatalogItem item)
			throws DataAccessException {
		updateFullRecordCacheTable(item, true);
	}

	// 2018 Paul Search Engine Java
	private void updateFullRecordCacheTable(CatalogItem item,
											boolean updateRelatedRecs) throws DataAccessException {
		FULL_CACHE cache;
		DAOFullCache dao = new DAOFullCache();
		try {
			cache = dao.load(item.getAmicusNumber(), item.getUserView());
		} catch (RecordNotFoundException e) {
			cache = new FULL_CACHE();
			cache.setItemNumber(item.getAmicusNumber());
			cache.setUserView(item.getUserView());
		}
		org.w3c.dom.Document d = item.toExternalMarcSlim();
		cache.setRecordData(XmlUtils.documentToString(d));
		cache.markChanged();
		logger.debug(cache);
		logger.debug("Slim record: " + cache.getRecordData());
		persistByStatus(cache);
		cache.evict();
		if (updateRelatedRecs) {
			for (Object o : item.getTags()) {
				if (o instanceof BibliographicRelationshipTag) {
					BibliographicRelationshipTag t = (BibliographicRelationshipTag) o;
					CatalogItem relItem = getBibliographicItemByAmicusNumber(t
							.getTargetBibItemNumber(), item.getUserView());
					updateFullRecordCacheTable(relItem, false);
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.CatalogDAO#getBibliographicItemByAmicusNumber(int)
	 */
	public BibliographicItem getBibliographicItemWithoutRelationships(
		int id,
		int userView)
		throws DataAccessException {
		try {
			BibliographicItem item = getBibliographicItem(id, userView);
			item.addAllTags(
				(Tag[]) getHeaderFields(item, userView).toArray(new Tag[0]));
			/*recupero la lingua dal tag 040 per la gestione delle note standard*/
			
//			CataloguingSourceTag t040 =
//				(CataloguingSourceTag) item.findFirstTagByNumber("040");
//			String lingua = t040.getStringText().getSubfieldsWithCodes("b").getDisplayText();
			
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

			if (logger.isDebugEnabled())
				logger.debug(
					"Bibliographic item: "
						+ Integer.toString(id)
						+ " successfully loaded from DB");
			return item;
		} catch (HibernateException he) {
			logAndWrap(he);
			return null;
		}
	}

	public List getHeaderFields(BibliographicItem item, int userView)
		throws HibernateException, DataAccessException {

		List result = new ArrayList();

		if (logger.isDebugEnabled()) {
			logger.debug(
				"loading all Fixed Fields for bibliographic item: "
					+ item.getAmicusNumber());
		}

		result.add(new BibliographicLeader());
		result.add(new BibliographicControlNumberTag());
		result.add(new BibliographicDateOfLastTransactionTag());
		result.add(new BibliographicCataloguingSourceTag());
//		if (item.getBibItmData().getAuthenticationCenterStringText() != null) {
		if (item.getBibItmData().getAuthenticationCenterStringText() != null && item.getBibItmData().getAuthenticationCenterStringText().trim().length()>0 ) {
			result.add(new BibliographicAuthenticationCodeTag());
		}
//		if (item.getBibItmData().getCountryStringText() != null) {
		if (item.getBibItmData().getCountryStringText() != null && item.getBibItmData().getCountryStringText().trim().length()>0) {
			result.add(new CountryOfPublicationTag());
		}
//		if (item.getBibItmData().getFormOfMusicStringText() != null) {
		if (item.getBibItmData().getFormOfMusicStringText() != null && item.getBibItmData().getFormOfMusicStringText().trim().length()>0) {
			result.add(new FormOfMusicalCompositionTag());
		}
//		if (item.getBibItmData().getGeographicAreaStringText() != null) {
		if (item.getBibItmData().getGeographicAreaStringText() != null && item.getBibItmData().getGeographicAreaStringText().trim().length()>0) {
			result.add(new BibliographicGeographicAreaTag());
		}
//		if (item.getBibItmData().getLanguageStringText() != null) {
		if (item.getBibItmData().getLanguageStringText() != null && item.getBibItmData().getLanguageStringText().trim().length()>0) {
			result.add(new LanguageCodeTag());
		}
//		if (item.getBibItmData().getProjectedPublicationDateCode() != null) {
		if (item.getBibItmData().getProjectedPublicationDateCode() != null && item.getBibItmData().getProjectedPublicationDateCode().trim().length()>0) {
			result.add(new ProjectedPublicationDateTag());
		}
//		if (item.getBibItmData().getSpecialCodedDatesStringText() != null) {
		if (item.getBibItmData().getSpecialCodedDatesStringText() != null && item.getBibItmData().getSpecialCodedDatesStringText().trim().length()>0) {
			result.add(new SpecialCodedDatesTag());
		}
//		if (item.getBibItmData().getTimePeriodStringText() != null) {
		if (item.getBibItmData().getTimePeriodStringText() != null && item.getBibItmData().getTimePeriodStringText().trim().length()>0) {
			result.add(new TimePeriodOfContentTag());
		}
		result.addAll(getMaterialDescriptions(item, userView));
		result.addAll(getPhysicalDescriptions(item, userView));
		result.addAll(getMusicalInstruments(item, userView));

		Iterator iter = result.iterator();
		Tag ff;
		while (iter.hasNext()) {
			ff = (Tag) iter.next();
			if (ff instanceof PersistsViaItem) {
				// set the bib_itm data portion of the fixed field
				 ((PersistsViaItem) ff).setItemEntity(item.getBibItmData());
			}
		}

		return result;
	}

	private List getMaterialDescriptions(BibliographicItem item, int userView)
		throws HibernateException, DataAccessException {
		List multiView;
		multiView =
			currentSession().find(
				"from MaterialDescription t "
					+ "where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1' ",
				new Object[] { item.getAmicusNumber(), new Integer(userView)},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });

		List singleView = isolateViewForList(multiView, userView);
		return singleView;
	}

	private List getPhysicalDescriptions(BibliographicItem item, int userView)
		throws HibernateException, DataAccessException {
		List multiView;
		multiView =
			currentSession().find(
				"from librisuite.business.cataloguing.bibliographic.PhysicalDescription t "
					+ "where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1' ",
				new Object[] { item.getAmicusNumber(), new Integer(userView)},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });

		List singleView = isolateViewForList(multiView, userView);
		return singleView;
	}

	private List getMusicalInstruments(BibliographicItem item, int userView)
		throws HibernateException, DataAccessException {
		List multiView;
		multiView =
			currentSession().find(
				"from librisuite.business.cataloguing.bibliographic.NumberOfMusicalInstrumentsTag t "
					+ "where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1' ",
				new Object[] { item.getAmicusNumber(), new Integer(userView)},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });

		List singleView = isolateViewForList(multiView, userView);
		return singleView;
	}

	private List getPublisherTag(int id, int userView) throws HibernateException, DataAccessException 
	{
		List result = new ArrayList();
		List l = getAccessPointTags(PublisherAccessPoint.class, id, userView);
		if (l.size() > 0) {
			PublisherTag pu = new PublisherTag(l);
			//Ho solo la data nel tag 260, in modifica tag deve rimuovere DescriptorDefaults
			pu.removeDescriptorDefaults();
			result.add(pu);
		}
		return result;
	}
	
	/**
	 * @param id
	 * @return
	 */
	private List getBibliographicNotes(int id, int userView, String lingua)
		throws HibernateException, DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug(
				"loading all Notes for bibliographic item: "
					+ Integer.toString(id));

		List noteTags = new ArrayList();
		List multiView =
			currentSession().find(
				"from BibliographicNote t "
					+ "where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1'",
				new Object[] { new Integer(id), new Integer(userView)},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });

		List singleView = isolateViewForList(multiView, userView);

		for (int i = 0; i < singleView.size(); i++) {
			noteTags.add(i,	new BibliographicNoteTag((BibliographicNote) singleView.get(i)));
			BibliographicNoteTag currentNote = ((BibliographicNoteTag) noteTags.get(i));
			currentNote.markUnchanged();
			currentNote.setOverflowList(currentNote.getOverflowList(userView));
			if(!lingua.equals("")){
			currentNote.setNoteStandard(currentNote.loadNoteStandard(userView,lingua));
			
			/*Standard Note 03/04/2009*/
			if(currentNote.isStandardNoteType()&& currentNote.getNote().getContent()!=null/*.indexOf("\u001f")==-1*/){
				if(currentNote.getNote().getContent().indexOf("\u001f")==-1){
                  currentNote.getNote().setContent("\u001f"+"a"+currentNote.getNote().getContent());
				  currentNote.getNote().markUnchanged();
			      currentNote.markUnchanged();
				}
			 }
			else if(currentNote.isStandardNoteType()&& currentNote.getNote().getContent()==null/*.indexOf("\u001f")==-1*/){
                currentNote.getNote().setContent("\u001f"+"a"+"");
                currentNote.getNote().markUnchanged();
			    currentNote.markUnchanged();
			}
		  }
		}
		
		//return singleView;
		return noteTags;
	}

	private List getBibliographicRelationships(int id, int userView)
		throws HibernateException, DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug(
				"loading all Relationships for bibliographic item: "
					+ Integer.toString(id));

		List relationships = new ArrayList();
		List multiView =
			currentSession().find(
				"from BibliographicRelationship t "
					+ "where t.bibItemNumber = ? and substr(t.userViewString,?,1) = '1'",
				new Object[] { id, userView },
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });

		List singleView = isolateViewForList(multiView, userView);

		for (int i = 0; i < singleView.size(); i++) {
			relationships.add(
				i,
				new BibliographicRelationshipTag(
					(BibliographicRelationship) singleView.get(i),
					userView));
			((BibliographicRelationshipTag) relationships.get(i))
				.markUnchanged();
			((BibliographicRelationship) singleView.get(i)).markUnchanged();
			/*if(((BibliographicRelationship) singleView.get(i)).getStringTextString().equals(""))  {					
				((BibliographicRelationship) singleView.get(i)).setStringText(((BibliographicRelationship) singleView.get(i)).BuildStringText(userView));
				((BibliographicRelationship) singleView.get(i)).setStringText(((BibliographicRelationship) singleView.get(i)).getRelationshipStringText());					
			}*/
		}

		return relationships;
	}

	private List getNameTitleAccessPointTags(int id, int userView)
		throws HibernateException, DataAccessException {
		List result = getAccessPointTags(NameTitleAccessPoint.class, id, userView);
		Iterator iter = result.iterator();
		NameTitleAccessPoint tag;
		while (iter.hasNext()) {
			tag = (NameTitleAccessPoint) iter.next();
			NME_TTL_HDG hdg = (NME_TTL_HDG) tag.getDescriptor();
			hdg.setNameHeading(
				(NME_HDG) new DAONameDescriptor().load(
					hdg.getNameHeadingNumber(),
					userView));
			hdg.setTitleHeading(
				(TTL_HDG) new DAOTitleDescriptor().load(
					hdg.getTitleHeadingNumber(),
					userView));
		}
		return result;
	}

	private List getNonNameTitleAccessPointTags(
		Class apfClass,
		int id,
		int userView)
		throws HibernateException, DataAccessException {
		List result = getAccessPointTags(apfClass, id, userView);
		Iterator iter = result.iterator();
		NameTitleComponent tag;
		while (iter.hasNext()) {
			tag = (NameTitleComponent) iter.next();
			if (tag.isPartOfNameTitle()) {
				iter.remove();
			}
		}
		return result;
	}

	/**
	 * @param id
	 * @return
	 */
	private List getAccessPointTags(final Class apfClass, final int id, final int userView) throws HibernateException, DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug(
				"loading all "
					+ apfClass
					+ " for bibliographic item: "
					+ Integer.toString(id));
		List multiView =
			currentSession().find(
				"from "
					+ apfClass.getName()
					+ " as t "
					+ " where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1'",
				new Object[] { new Integer(id), new Integer(userView)},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });

		List singleView = isolateViewForList(multiView, userView);

		loadHeadings(singleView, userView);

		return singleView;
	}

	public List loadAccessPointTags(Class apfClass, int id, int userView)
		throws DataAccessException {
		try {
			return getAccessPointTags(apfClass, id, userView);
		} catch (HibernateException e) {
			logAndWrap(e);
		}

		return null;
	}

	/**
	 * loads the BIB_ITM data into a new BibliographicItem.  DO NOT USE to
	 * load a BibliographicItem.
	 * @see BibliographicCatalogDAO#getBibliographicItemByAmicusNumber(int, int)
	 */
	private BibliographicItem getBibliographicItem(int id, int userView)
		throws HibernateException, DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("loading bibliographic item: " + Integer.toString(id));

		BibliographicItem item = new BibliographicItem();

		BIB_ITM bibItm = new DAOBibItem().load(id, userView);
		item.setBibItmData(bibItm);
		item.setUserView(userView);

		return item;
	}

	protected void updateCacheTable(final CatalogItem item)
		throws DataAccessException {
		try{
			updateCacheTable(item.getAmicusNumber().intValue(), item.getUserView());
		}catch(Exception e){}
	}
	
	protected void insertDeleteTable(final CatalogItem item, final UserProfile user )
	throws DataAccessException {
		insertDeleteTable(item.getAmicusNumber().intValue(), item.getUserView(),user);
}

	public void updateCacheTable(
		final int bibItemNumber,
		final int cataloguingView)
		throws DataAccessException {
		new TransactionalHibernateOperation() {
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
		.execute();
	}

	public void insertDeleteTable(
			final int bibItemNumber,
			final int cataloguingView,
			final UserProfile user)
			throws DataAccessException {
			new TransactionalHibernateOperation() {
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
							// TODO _MIKE Auto-generated catch block
							ex.printStackTrace();
						}
					}
				}
			}
			.execute();
		}

	/* (non-Javadoc)
	 * @see CatalogDAO#getCatalogItemByKey(java.lang.Object[])
	 */
	public CatalogItem getCatalogItemByKey(Object[] key)
		throws DataAccessException {
		int id = ((Integer) key[0]).intValue();
		int cataloguingView = ((Integer) key[1]).intValue();
		return getBibliographicItemByAmicusNumber(id, cataloguingView);
	}

	/**
	 * Transfers the bibliographic items from one heading to another
	 */
	public void transferItems(final Descriptor source, final Descriptor target)	throws DataAccessException 
	{
		if (target == null) {
			throw new IllegalArgumentException("target descriptor is null");
		}
		
		if (source.getClass() != target.getClass()) {
			throw new IllegalArgumentException("target and source from different classes");
		}
		
		if(source instanceof PUBL_HDG){
    	   transferPublisherItems(source, target);
        } else {
        	new TransactionalHibernateOperation() {
        		public void doInHibernateTransaction(Session s) throws HibernateException, DataAccessException 
        		{
					/*TODO currently Hibernate doesn't support update(string) so we are
					 * selecting the set and then looping.  Since we are modifying the key
					 * of the object, we also need to delete the old and save the new rather 
					 * than updating.  If this becomes a performance issue, a JDBC call 
					 * with an UPDATE statement could be used.
					 */
					short cataloguingView =  View.toIntView(source.getUserViewString());
					List raw = s.find("from " + source.getAccessPointClass().getName() + " as apf "
								      + " where apf.headingNumber = ? "
								      + " and substr(apf.userViewString, ?, 1) = '1'",
						new Object[] {new Integer(source.getKey().getHeadingNumber()), new Short(cataloguingView)}, new Type[] { Hibernate.INTEGER, Hibernate.SHORT });
					List l = isolateViewForList(raw, cataloguingView);
					Iterator iter = l.iterator();
					while (iter.hasNext()) {
						AccessPoint anApf = (AccessPoint) iter.next();
						anApf.markDeleted();
						persistByStatus(anApf);
						anApf = (AccessPoint) anApf.clone();
						anApf.markNew();
						anApf.setHeadingNumber(new Integer(target.getKey().getHeadingNumber()));
						persistByStatus(anApf);
					}
        		}
        	}
        	.execute();

		//TODO only need to update the original source list but must be done after the transfer is committed
			if (target.changeAffectsCacheTable()) {
				((DAODescriptor) target.getDAO()).updateCacheTable(target);
			}
       }
	}

	public void transferPublisherItems(final Descriptor source, final Descriptor target) throws DataAccessException 
	{
		if (target == null) {
			throw new IllegalArgumentException("target descriptor is null");
		}
		if (source.getClass() != target.getClass()) {
			throw new IllegalArgumentException("target and source from different classes");
		}

		new TransactionalHibernateOperation() 
		{
			public void doInHibernateTransaction(Session s) throws HibernateException, DataAccessException 
			{
				/*TODO currently Hibernate doesn't support update(string) so we are
				 * selecting the set and then looping.  Since we are modifying the key
				 * of the object, we also need to delete the old and save the new rather 
				 * than updating.  If this becomes a performance issue, a JDBC call 
				 * with an UPDATE statement could be used.
				 */
				short cataloguingView =	View.toIntView(source.getUserViewString());
				List raw =
					s.find(
						"from "
							+ "  PUBL_TAG as tag "
							+ " where  "
							+ " tag.publisherHeadingNumber=?"
							+ " and substr(tag.userViewString, ?, 1) = '1'",
						new Object[] {
							new Integer(source.getKey().getHeadingNumber()),
							new Short(cataloguingView)},
						new Type[] { Hibernate.INTEGER, Hibernate.SHORT });
				List l = isolateViewForList(raw, cataloguingView);
				Iterator iter = l.iterator();
				while (iter.hasNext()) {
					PUBL_TAG anApf = (PUBL_TAG) iter.next();
					anApf.markChanged();
					anApf.setPublisherHeadingNumber(new Integer(target.getKey().getHeadingNumber()));
					persistByStatus(anApf);
				}
			}
		}
		.execute();

		//TODO only need to update the original source list but must be done after the transfer is committed
		if (target.changeAffectsCacheTable()) {
			((DAODescriptor) target.getDAO()).updateCacheTable(target);
		}
	}

	/**
	 * Finds the items subjects that have equivalent subject headings (other languages)
	 * and adds these equivalent headings to the item
	 */
	public Collection<SubjectAccessPoint> getEquivalentSubjects(final CatalogItem item) throws DataAccessException {
		final DAODescriptor dao = new DAOSubjectDescriptor();
		final Collection<SubjectAccessPoint> newTags = new HashSet();
		final Iterator iter = item.getTags().iterator();
		while (iter.hasNext()) {
			final Tag aTag = (Tag) iter.next();
			if (aTag instanceof SubjectAccessPoint) {
				List refs =
					dao.getCrossReferences(
						((SubjectAccessPoint) aTag).getDescriptor(),
						item.getUserView());
				Iterator iter2 = refs.iterator();
				while (iter2.hasNext()) {
					final REF aRef = (REF) iter2.next();
					if (ReferenceType.isEquivalence(aRef.getType())) {
						/* 
						 * Since the new SubjectAccessPoints will be treated in a HashSet
						 * we need to be sure that their equals "key" values are established
						 * Note that a deepCopy takes the same itemNumber, view and
						 * function code as the original heading (duplicates AMICUS behaviour)
						 */
						final SubjectAccessPoint aSubj = (SubjectAccessPoint) deepCopy(aTag);
						aSubj.markNew();
						aSubj.setDescriptor(dao.load(aRef.getTarget(), item.getUserView()));
						aSubj.setHeadingNumber(
								aSubj
									.getDescriptor()
									.getKey()
									.getHeadingNumber());
						if (!item.getTags().contains(aSubj)) {
							newTags.add(aSubj);
						}
					}
				}
			}
		}
		return newTags;
	}
	private List getPublisherTags(int id, int userView) throws HibernateException, DataAccessException {
		List/*<PublisherManager>*/ result = new ArrayList/*<PublisherManager>*/();
		List/*<PublisherAccessPoint>*/ apfs = getAccessPointTags(PublisherAccessPoint.class, id, userView);
		Iterator ite =apfs.iterator();
		while(ite.hasNext()){
			PublisherAccessPoint apf =(PublisherAccessPoint)ite.next();
			PublisherManager pu= new PublisherManager(apf);
			//pu.setDates(apf.getDate());
			result.add(pu);
			
		}
		return result;
	}
	
	public void transferItemsForSuggest(final Descriptor source, final Descriptor target, final short functionCode ) throws DataAccessException {
		if (target == null) {
			throw new IllegalArgumentException("target descriptor is null");
		}
		/*if (source.getClass() != target.getClass()) {
			throw new IllegalArgumentException("target and source from different classes");
		}*/

		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s) throws HibernateException, DataAccessException {
				/*
				 * TODO currently Hibernate doesn't support update(string) so we
				 * are selecting the set and then looping. Since we are
				 * modifying the key of the object, we also need to delete the
				 * old and save the new rather than updating. If this becomes a
				 * performance issue, a JDBC call with an UPDATE statement could
				 * be used.
				 */
				short cataloguingView = View.toIntView(source.getUserViewString());
				List raw = s.find("from " + " ClassificationAccessPoint " + " as apf " + " where apf.headingNumber = ?", new Object[] {
						new Integer(source.getKey().getHeadingNumber()) }, new Type[] { Hibernate.INTEGER });
				//List l = isolateViewForList(raw, cataloguingView);
				Iterator iter = raw.iterator();
				while (iter.hasNext()) {
					AccessPoint anApf = (AccessPoint) iter.next();
					int bibItem = anApf.getItemNumber();
					String userViewString = ((ClassificationAccessPoint)anApf).getUserViewString();
					anApf.markDeleted();
					persistByStatus(anApf);
					anApf = new SubjectAccessPoint();
					anApf.setItemNumber(bibItem);
					//TODO 2Â° correlation del tag
					((SubjectAccessPoint)anApf).setUserViewString(userViewString);
					((SubjectAccessPoint)anApf).setFunctionCode(functionCode);
					anApf.markNew();
					anApf.setHeadingNumber(new Integer(target.getKey().getHeadingNumber()));
					/*
					 * make sure the mad isn't already attached to the target
					 * before adding
					 */
					persistByStatus(anApf);
					
				}
			}
		}.execute();

		// TODO only need to update the original source list but must be done
		// after
		// the transfer is committed
		if (target.changeAffectsCacheTable()) {
			((DAODescriptor) target.getDAO()).updateCacheTable(target);
		}
	}
	
	/*
	 * updates the third correlation only for subjects(Function code) 
	 */
	public void updateSubjectFunctionCode(final Descriptor descriptor) throws DataAccessException 
	{
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s) throws HibernateException, DataAccessException 
			{
				/*TODO currently Hibernate doesn't support update(string) so we are
				 * selecting the set and then looping.  Since we are modifying the key
				 * of the object, we also need to delete the old and save the new rather 
				 * than updating.  If this becomes a performance issue, a JDBC call 
				 * with an UPDATE statement could be used.
				 */
				DAOBibliographicCorrelation daoBiblioCorr = new DAOBibliographicCorrelation();
				SBJCT_HDG sbj = (SBJCT_HDG)descriptor;
				int functionCode =daoBiblioCorr.getFirstAllowedValue2((short)4,(short)sbj.getTypeCode(), (short)sbj.getSourceCode());
				List<BibliographicCorrelation> list2 = daoBiblioCorr.getFirstAllowedValue2List((short)4,(short)sbj.getTypeCode(), (short)sbj.getSourceCode());
				short cataloguingView = View.toIntView(descriptor.getUserViewString());
				
				List raw = s.find("from "
							+ descriptor.getAccessPointClass().getName()
							+ " as apf "
							+ " where apf.headingNumber = ? "
							+ " and substr(apf.userViewString, ?, 1) = '1'",
						new Object[] {
							descriptor.getKey().getHeadingNumber(),
							cataloguingView},
						new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
				
				List l = isolateViewForList(raw, cataloguingView);
				Iterator iter = l.iterator();
				while (iter.hasNext()) {
					AccessPoint anApf = (AccessPoint) iter.next();
					boolean isPresentSecondCorr=daoBiblioCorr.isPresentSecondCorrelation(list2,(short)anApf.getFunctionCode());
					if(!isPresentSecondCorr){
					    int bibItem = anApf.getItemNumber();
						String userViewString = ((SubjectAccessPoint)anApf).getUserViewString();
						anApf.markDeleted();
						persistByStatus(anApf);
						anApf = new SubjectAccessPoint();
						anApf.setItemNumber(bibItem);
						((SubjectAccessPoint)anApf).setUserViewString(userViewString);
						anApf.setFunctionCode(functionCode);
						anApf.markNew();
						anApf.setHeadingNumber(new Integer(descriptor.getKey().getHeadingNumber()));
						persistByStatus(anApf);
					}
				}
			}
		}
		.execute();
	}	
}