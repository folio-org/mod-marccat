/*
 * (c) LibriCore
 * 
 * Created on Dec 22, 2004
 * 
 * DAOPublisherTag.java
 */
package org.folio.cataloging.dao;

import java.util.List;

import org.folio.cataloging.business.codetable.ValueLabelElement;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.StandardNoteAccessPoint;
import org.folio.cataloging.dao.persistence.T_STD_NTE_TYP;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.folio.cataloging.dao.common.HibernateUtil;


public class DAOBibliographicStandardNote extends HibernateUtil {
	private static final Log logger = LogFactory.getLog(DAOBibliographicStandardNote.class);

	//public getBibNoteStardard(note.getBibItemNumber(), userView, note.getNoteNbr())
	public StandardNoteAccessPoint getBibNoteStardard(
			int bibItemNumber,
			int userView,
			int noteNumber)
			throws DataAccessException {
			Session s = currentSession();
			List notesStandard = null;
			StandardNoteAccessPoint std = null;
		    try {
				notesStandard =
					s.find(
						"from StandardNoteAccessPoint t "
							+ " where t.bibItemNumber = ? and "
							+ " substr(t.userViewString, ?, 1) = '1' and "
							+ " t.noteNbr = ?",
						new Object[] {
							new Integer(bibItemNumber),
							new Integer(userView),
							new Integer(noteNumber)},
						new Type[] {
							Hibernate.INTEGER,
							Hibernate.INTEGER,
							Hibernate.INTEGER });

			} catch (HibernateException e) {
				logAndWrap(e);
			}
            if(notesStandard.size()>0)
            	std = (StandardNoteAccessPoint)notesStandard.get(0);
			return std;

		}
	
	
	
	public ValueLabelElement getSTDDisplayString(short code, String language) throws DataAccessException{
	ValueLabelElement val = new ValueLabelElement();
		List l = null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct"
					 +" from T_STD_NTE_TYP  as ct " + " where ct.code ="+code+" and ct.language ='"+language+"'");
			q.setMaxResults(1);
			l = q.list();
			if (l.size() > 0) {
				T_STD_NTE_TYP STDType= (T_STD_NTE_TYP) l.get(0);
				val.setValue(new Short(STDType.getCode()).toString());
				val.setLabel(STDType.getLongText());
				return val;
			} else {
				return null;
			}

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return val;
	}

}
