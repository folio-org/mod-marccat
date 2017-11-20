package org.folio.cataloging.dao;

import java.util.ArrayList;
import java.util.List;

import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicNoteOverflow;
import org.folio.cataloging.business.common.DataAccessException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class DAOBibliographicNotesOverflow extends HibernateUtil {

	private static final Log logger =
		LogFactory.getLog(DAOBibliographicNotesOverflow.class);

	public List getBibNotesOverflowList(
		int bibItemNumber,
		int userView,
		int noteNumber)
		throws DataAccessException {
		Session s = currentSession();
		List notesOverflow = new ArrayList();

		try {
			notesOverflow =
				s.find(
					"from BibliographicNoteOverflow t "
						+ " where t.bibItemNumber = ? and "
						+ " substr(t.userViewString, ?, 1) = '1' and "
						+ " t.noteNbr = ?"
						+ " order by t.noteOverflowNumber",
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

		return notesOverflow;

	}

	public String getBibNotesOverflow(List list) throws DataAccessException {
		Session s = currentSession();
		String notesOverflowString = new String("");

		for (int i = 0; i < list.size(); i++) {
			//notesOverflowString = notesOverflowString + ((BibliographicNoteOverflow) list.get(i)).getStringText().getMarcDisplayString("").toString();
			notesOverflowString =
				notesOverflowString
					+ ((BibliographicNoteOverflow) list.get(i))
						.getStringText()
						.toString();
		}
		return notesOverflowString;
	}

}