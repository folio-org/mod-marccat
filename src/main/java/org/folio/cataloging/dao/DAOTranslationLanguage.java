/*
 * (c) LibriCore
 * 
 * Created on Jun 24, 2004
 * 
 * DAOTranslationLanguage.java
 */
package org.folio.cataloging.dao;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.T_TRLTN_LANG_CDE;

import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class DAOTranslationLanguage extends HibernateUtil {

	public T_TRLTN_LANG_CDE load(int i) throws DataAccessException {
		return (T_TRLTN_LANG_CDE) get(T_TRLTN_LANG_CDE.class, new Integer(i));
	}

}
