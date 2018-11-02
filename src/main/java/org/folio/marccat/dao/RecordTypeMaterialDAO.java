package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.Global;
import org.folio.marccat.dao.persistence.RecordTypeMaterial;

import java.util.List;
import java.util.Objects;

/**
 * @author paulm
 * @since 1.0
 */
public class RecordTypeMaterialDAO extends AbstractDAO {

  /**
   * The method gets the material type for 006/008 tags field using leader values.
   *
   * @param session            the valid hibernate session.
   * @param recordType         the record type value used here as filter criterion.
   * @param bibliographicLevel the bibliographic level value used here as filter criterion.
   * @return RecordTypeMaterial to represent form of item
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  public RecordTypeMaterial getMaterialHeaderCode(final Session session, final char recordType, final char bibliographicLevel) throws HibernateException {
    final List <RecordTypeMaterial> recordTypeMaterials = session.find(
      "from RecordTypeMaterial as t "
        + " where t.recordTypeCode = ? and "
        + "       (t.bibliographicLevel = ? "
        + "        OR t.bibliographicLevel is NULL)",
      new Object[]{
        recordType,
        bibliographicLevel},
      new Type[]{Hibernate.CHARACTER, Hibernate.CHARACTER});

    return recordTypeMaterials.stream().filter(Objects::nonNull).findFirst().orElse(null);
  }

  @SuppressWarnings("unchecked")
  public RecordTypeMaterial getDefaultTypeByHeaderCode(final Session session, final int headerCode, final String code) throws HibernateException {
    final List <RecordTypeMaterial> recordTypeMaterials = session.find(
      "from RecordTypeMaterial as t "
        + (code.equals(Global.OTHER_MATERIAL_TAG_CODE) ? " where t.bibHeader006 = ?" : " where t.bibHeader008 = ?"),
      new Object[]{headerCode},
      new Type[]{Hibernate.INTEGER});

    return recordTypeMaterials.stream().filter(Objects::nonNull).findFirst().orElse(null);
  }
}
