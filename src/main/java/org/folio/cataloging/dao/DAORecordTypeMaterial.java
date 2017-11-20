/*
 * (c) LibriCore
 * 
 * Created on Oct 8, 2004
 * 
 * DAORecordTypeMaterial.java
 */
package org.folio.cataloging.dao;

import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.RecordTypeMaterial;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2004/11/15 15:50:26 $
 * @since 1.0
 */
public class DAORecordTypeMaterial extends HibernateUtil {

	public RecordTypeMaterial get008HeaderCode(char recordType, char bibliographicLevel){
		RecordTypeMaterial result = null;
		try {
			Session s = currentSession();
				List l =
					s.find(
						"from RecordTypeMaterial as t "
							+ " where t.recordTypeCode = ? and "
							+ "       (t.bibliographicLevel = ? "
							+ "        OR t.bibliographicLevel is NULL)",
						new Object[] {
							new Character(recordType),
							new Character(bibliographicLevel)},
						new Type[] { Hibernate.CHARACTER, Hibernate.CHARACTER });
				if (l.size() > 0) {
					result = (RecordTypeMaterial) l.get(0);
				}
		} catch (DataAccessException e) {
			return result;
		} catch (HibernateException e) {
			return result;
		}
		return result;
	}

	public RecordTypeMaterial get006HeaderCode(Character materialType) {
		RecordTypeMaterial result = null;
		try {
			Session s = currentSession();
				List l =
					s.find(
						"from RecordTypeMaterial as t "
							+ " where t.recordTypeCode = ?",
						new Object[] { materialType },
						new Type[] { Hibernate.CHARACTER });
				if (l.size() > 0) {
					result = (RecordTypeMaterial) l.get(0);
				}
		} catch (DataAccessException e) {
			return result;
		} catch (HibernateException e) {
			return result;
		}
		return result;
	}

	public RecordTypeMaterial getDefaultTypeBy006Header(short bibHeader) {
		RecordTypeMaterial result = null;
		try {
			Session s = currentSession();
				List l =
					s.find(
						"from RecordTypeMaterial as t "
							+ " where t.bibHeader006 = ?",
						new Object[] { new Short(bibHeader) },
						new Type[] { Hibernate.SHORT });
				if (l.size() > 0) {
					result = (RecordTypeMaterial) l.get(0);
				}
		} catch (DataAccessException e) {
			return result;
		} catch (HibernateException e) {
			return result;
		}
		return result;
	}
}
