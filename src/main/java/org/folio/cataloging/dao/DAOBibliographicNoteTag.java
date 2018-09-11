/*
 * (c) LibriCore
 * 
 * Created on Dec 22, 2004
 * 
 * DAOPublisherTag.java
 */
package org.folio.cataloging.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.UpdateStatus;
import org.folio.cataloging.dao.persistence.BibliographicNote;
import org.folio.cataloging.dao.persistence.BibliographicNoteOverflow;
import org.folio.cataloging.dao.persistence.BibliographicNoteTag;
import org.folio.cataloging.dao.persistence.StandardNoteAccessPoint;

import java.util.Iterator;
/**
  * @author hansv
 * @version $Revision: 1.4 $ $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */
public class DAOBibliographicNoteTag extends AbstractDAO {
	private static final Log logger = LogFactory.getLog(BibliographicNoteTag.class);
	/* (non-Javadoc)
	 * @see HibernateUtil#delete(librisuite.business.common.Persistence)
	 */
	public void delete(Persistence po) throws DataAccessException 
	{
			if (!(po instanceof BibliographicNoteTag)) {
				throw new IllegalArgumentException("I can only persist BibliographicNoteTag objects");
			}
			BibliographicNoteTag aNote = (BibliographicNoteTag) po;
			aNote.getNote().markDeleted();
			persistByStatus(aNote.getNote());
			if(aNote.getNoteStandard()!=null){
			  aNote.getNoteStandard().markDeleted();
			  persistByStatus(aNote.getNoteStandard());
			}
			Iterator iter = aNote.getOverflowList().iterator();
			BibliographicNoteOverflow overflow;
			while (iter.hasNext()) {
				overflow = (BibliographicNoteOverflow) iter.next();
				overflow.markDeleted();
				persistByStatus(overflow);			
				super.delete(overflow);
			}
			aNote.setUpdateStatus(UpdateStatus.REMOVED);
	}

	/* (non-Javadoc)
	 * @see HibernateUtil#save(librisuite.business.common.Persistence)
	 */
	public void save(Persistence po) throws DataAccessException {
		if (!(po instanceof BibliographicNoteTag)) {
			throw new IllegalArgumentException(
					"I can only persist BibliographicNoteTag objects");
		}
		BibliographicNoteTag aNote = (BibliographicNoteTag) po;

		logger.debug("save bibliographicNoteTag");

		Iterator iter = aNote.getOverflowList().iterator();

		logger.debug("overflow list size " + aNote.getOverflowList().size());
		BibliographicNote note = aNote.getNote();
		while (iter.hasNext()) {
			BibliographicNoteOverflow noteOverflow = (BibliographicNoteOverflow) iter
					.next();
			noteOverflow.setBibItemNumber(note.getItemNumber());
			noteOverflow.setUserViewString(note.getUserViewString());
			noteOverflow.setNoteNbr(note.getNoteNbr());
			if (noteOverflow.isNew()) {
				noteOverflow.generateNewKey();
			}
			persistByStatus(noteOverflow);
		}

		iter = aNote.getDeletedOverflowList().iterator();
		while (iter.hasNext()) {
			BibliographicNoteOverflow noteOverflow = (BibliographicNoteOverflow) iter
					.next();
			// MIKE: process only the overlow marked as deleted
			if (noteOverflow.isDeleted()) {
				persistByStatus(noteOverflow);
			}
		}

		aNote.getDeletedOverflowList().clear();

		// StandardNotes
		StandardNoteAccessPoint noteStandard = aNote.getNoteStandard();
		if (aNote.isStandardNoteType()) {
			noteStandard.setBibItemNumber(note.getItemNumber());
			noteStandard.setUserViewString(note.getUserViewString());
			noteStandard.setNoteNbr(note.getNoteNbr());
			/* Testo variabile RIMOZIONE CODICE SOTTOCAMPO 03/04/2009*/
			persistByStatus(noteStandard);
			/*if (aNote.isStandardNoteType()){
				if(note.getContent().substring(2).trim().length()>0)
				note.setContent(note.getContent().substring(2));
				else
				  note.setContent(" "); */
			//}
			
		}

		if (aNote.isNew()) {
			aNote.getNote().markNew();
			if (aNote.isStandardNoteType())
				aNote.getNoteStandard().markNew();

		}
	
		persistByStatus(note);
		
		if(aNote.isStandardNoteType())
		  persistByStatus(aNote.getNoteStandard());
		aNote.markUnchanged();
	}

	/* (non-Javadoc)
	 * @see HibernateUtil#update(librisuite.business.common.Persistence)
	 */
	public void update(Persistence p)
		throws DataAccessException {
		/*
		 * Since we are deleting and re-adding, save and update are the same
		 */
		 logger.debug("update bibliographicNoteTag");
		 
		save(p);
	}

}
