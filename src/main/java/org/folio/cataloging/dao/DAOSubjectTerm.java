package org.folio.cataloging.dao;

import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.SubjectTerm;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.folio.cataloging.dao.common.HibernateUtil;


public class DAOSubjectTerm extends HibernateUtil {
	private static Log logger = LogFactory.getLog(DAOSubjectTerm.class);
	
	public DAOSubjectTerm() {
		super();
	}
	
	public void persist(int headingNumber,SubjectTerm subjectTerm)
			throws DataAccessException {
		SubjectTerm subjectTerm2;
		if (loadSubjectTerm(headingNumber).size() == 0) {
			subjectTerm.setCodeTerm(subjectTerm.getCodeTerm());
			subjectTerm.setHeadingNumber(headingNumber);
			persistByStatus(subjectTerm);

		} else {
			subjectTerm2 = (SubjectTerm) loadSubjectTerm(headingNumber).get(0);
			subjectTerm2.setCodeTerm(subjectTerm.getCodeTerm());
			subjectTerm2.markChanged();
			persistByStatus(subjectTerm2);
		}
		
	}

	public List loadSubjectTerm(int headingNumber) throws DataAccessException {
		List result = null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct"
					+ " from SubjectTerm as ct " + " where ct.headingNumber ="
					+ headingNumber);
			q.setMaxResults(1);
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	
}