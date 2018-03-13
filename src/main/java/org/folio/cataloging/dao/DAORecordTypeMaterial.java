
package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.Global;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.RecordTypeMaterial;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author paulm
 * @since 1.0
 */
public class DAORecordTypeMaterial extends HibernateUtil {

	/**
	 * The method gets the form of item for 006/008 tags field using leader values.
	 *
	 * @param session the valid hibernate session.
	 * @param recordType the record type value used here as filter criterion.
	 * @param bibliographicLevel the bibliographic level value used here as filter criterion.
	 * @return RecordTypeMaterial to represent form of item
	 * @throws HibernateException in case of hibernate exception.
	 */
	public RecordTypeMaterial getMaterialHeaderCode(final Session session, final char recordType, final char bibliographicLevel) throws HibernateException {

		List<RecordTypeMaterial> recordTypeMaterials = session.find(
						"from RecordTypeMaterial as t "
							+ " where t.recordTypeCode = ? and "
							+ "       (t.bibliographicLevel = ? "
							+ "        OR t.bibliographicLevel is NULL)",
						new Object[] {
							new Character(recordType),
							new Character(bibliographicLevel)},
						new Type[] { Hibernate.CHARACTER, Hibernate.CHARACTER });

		Optional<RecordTypeMaterial> rtm = recordTypeMaterials.stream().filter(Objects::nonNull).findFirst();
		if (rtm.isPresent()) {
			return rtm.get();
		}else
			return null;

	}

	//TODO : dont'call me! Use getMaterialHeaderCode()
	@Deprecated
	public RecordTypeMaterial get008HeaderCode(char recordType, char bibliographicLevel){
		RecordTypeMaterial result = null;
		/*try {
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
		}*/
		return result;
	}
	//TODO : dont'call me! Use getMaterialHeaderCode()
	@Deprecated
	public RecordTypeMaterial get006HeaderCode(Character materialType) {
		RecordTypeMaterial result = null;
		/*try {
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
		}*/
		return result;
	}

	public RecordTypeMaterial getDefaultTypeByHeaderCode(final Session session, final short headerCode, final String code) throws HibernateException {

		List<RecordTypeMaterial> recordTypeMaterials = session.find(
					"from RecordTypeMaterial as t "
						+ (code.equals(Global.OTHER_MATERIAL_TAG_CODE) ? " where t.bibHeader006 = ?" : " where t.bibHeader008 = ?"),
					new Object[] { new Short(headerCode) },
					new Type[] { Hibernate.SHORT });

		Optional<RecordTypeMaterial> rtm = recordTypeMaterials.stream().filter(Objects::nonNull).findFirst();
		if (rtm.isPresent()) {
			return rtm.get();
		}else
			return null;
	}
}
