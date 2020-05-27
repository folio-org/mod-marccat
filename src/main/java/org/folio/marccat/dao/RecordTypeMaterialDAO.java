package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.dao.persistence.RecordTypeMaterial;
import java.util.List;
import java.util.Objects;


public class RecordTypeMaterialDAO extends AbstractDAO {

  private static final String FROM_RECORD_TYPE_MATERIAL_AS_T = "from RecordTypeMaterial as t ";

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
    final List<RecordTypeMaterial> recordTypeMaterials = session.find(
      FROM_RECORD_TYPE_MATERIAL_AS_T
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
    final List<RecordTypeMaterial> recordTypeMaterials = session.find(
      FROM_RECORD_TYPE_MATERIAL_AS_T
        + (code.equals(Global.OTHER_MATERIAL_TAG_CODE) ? " where t.bibHeader006 = ?" : " where t.bibHeader008 = ?"),
      new Object[]{headerCode},
      new Type[]{Hibernate.INTEGER});

    return recordTypeMaterials.stream().filter(Objects::nonNull).findFirst().orElse(null);
  }

  /**
   * The method gets the material type for 006 tag field using material type.
   *
   * @param session            the valid hibernate session.
   * @param materialType       the material type
   * @return RecordTypeMaterial to represent form of item
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  public RecordTypeMaterial get006HeaderCode(final Session session, final char materialType) throws HibernateException {
    final List <RecordTypeMaterial> recordTypeMaterials =
      session.find(
        FROM_RECORD_TYPE_MATERIAL_AS_T
          + " where t.recordTypeCode = ?",
        new Object[]{materialType},
        new Type[]{Hibernate.CHARACTER});
    return recordTypeMaterials.stream().filter(Objects::nonNull).findFirst().orElse(null);
  }



}
