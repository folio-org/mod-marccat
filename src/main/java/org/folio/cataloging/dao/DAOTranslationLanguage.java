/*
 * (c) LibriCore
 * 
 * Created on Jun 24, 2004
 * 
 * DAOTranslationLanguage.java
 */
package org.folio.cataloging.dao;

import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.T_TRLTN_LANG_CDE;

/**
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class DAOTranslationLanguage extends HibernateUtil {

	public T_TRLTN_LANG_CDE load(final int i) throws DataAccessException {

		//TODO fix session!
		final Session session = currentSession();

		return (T_TRLTN_LANG_CDE) get(session, T_TRLTN_LANG_CDE.class, new Integer(i));
	}

}
