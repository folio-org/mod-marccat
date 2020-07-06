package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ReferentialIntegrityException;
import java.util.List;

/**
 * Manages headings in the NME_HDG table.
 *
 * @author paulm
 * @author carment
 */
public class NameDescriptorDAO extends DescriptorDAO {

  private static final String WHERE_REF_NAME_HEADING_NUMBER = " where ref.nameHeadingNumber = ? ";
  private static final String AND_REF_SOURCE_HEADING_TYPE_NH = " and ref.sourceHeadingType = 'NH' ";
  private static final String AND_REF_USER_VIEW_STRING = " and ref.userViewString = '";


  /**
   * Gets the persistent class.
   *
   * @return the persistent class
   */

  public Class getPersistentClass() {
    return NME_HDG.class;
  }

  /**
   * Supports authorities.
   *
   * @return true, if successful
   */
  @Override
  public boolean supportsAuthorities() {
    return true;
  }

  /**
   * Gets the cross reference count.
   *
   * @param source          the source
   * @param cataloguingView the cataloguing view
   * @return the xref count
   * @throws DataAccessException the data access exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public int getXrefCount(final Descriptor source, final int cataloguingView, final Session session)
    throws HibernateException {

    int count = super.getXrefCount(source, cataloguingView, session);
    List<Integer> countList = session.find(
      "select count(*) from NME_NME_TTL_REF as ref "
        + WHERE_REF_NAME_HEADING_NUMBER
        + AND_REF_SOURCE_HEADING_TYPE_NH
        + AND_REF_USER_VIEW_STRING + View.makeSingleViewString(cataloguingView) + "'",
      new Object[]{
        source.getKey().getHeadingNumber()},
      new Type[]{
        Hibernate.INTEGER});
    count = count + countList.get(0);
    countList =
      session.find(
        "select count(*) from NME_TO_TTL_REF as ref "
          + WHERE_REF_NAME_HEADING_NUMBER
          + AND_REF_SOURCE_HEADING_TYPE_NH
          + AND_REF_USER_VIEW_STRING + View.makeSingleViewString(cataloguingView) + "'",
        new Object[]{
          source.getKey().getHeadingNumber()},
        new Type[]{
          Hibernate.INTEGER});
    count = count + countList.get(0);
    return count;
  }

  /**
   * Gets the cross references.
   *
   * @param source          the source
   * @param cataloguingView the cataloguing view
   * @param session         the session
   * @return the cross references
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<REF> getCrossReferences(final Descriptor source, final int cataloguingView, final Session session)
    throws HibernateException {

    List<REF> refList = super.getCrossReferences(source, cataloguingView, session);

    refList.addAll(
      session.find(
        "from NME_NME_TTL_REF as ref "
          + WHERE_REF_NAME_HEADING_NUMBER
          + AND_REF_SOURCE_HEADING_TYPE_NH
          + AND_REF_USER_VIEW_STRING + View.makeSingleViewString(cataloguingView) + "'",
        new Object[]{
          source.getKey().getHeadingNumber()},
        new Type[]{
          Hibernate.INTEGER}));
    refList.addAll(
      session.find(
        "from NME_TO_TTL_REF as ref "
          + WHERE_REF_NAME_HEADING_NUMBER
          + AND_REF_SOURCE_HEADING_TYPE_NH
          + AND_REF_USER_VIEW_STRING + View.makeSingleViewString(cataloguingView) + "'",
        new Object[]{
          source.getKey().getHeadingNumber()},
        new Type[]{
          Hibernate.INTEGER}));
    return refList;
  }

  /**
   * Load cross reference.
   *
   * @param source          the source
   * @param target          the target
   * @param referenceType   the reference type
   * @param cataloguingView the cataloguing view
   * @return the ref
   * @throws DataAccessException the data access exception
   */
  @Override
  public REF loadReference(
    final Descriptor source,
    final Descriptor target,
    final short referenceType,
    final int cataloguingView,
    final Session session)
    throws HibernateException {

    final String nameToTitle = new StringBuilder()
      .append("from NME_TO_TTL_REF as ref ")
      .append(" where ref.nameHeadingNumber = ? AND ")
      .append(" ref.titleHeadingNumber = ? AND ")
      .append(" ref.sourceHeadingType = 'NH' AND ")
      .append(" ref.userViewString = '" + View.makeSingleViewString(cataloguingView) + "' AND ")
      .append(" ref.type = ?").toString();

    final String nameNameTitle = new StringBuilder()
      .append("from NME_NME_TTL_REF as ref ")
      .append(" where ref.nameHeadingNumber = ? AND ")
      .append(" ref.nameTitleHeadingNumber = ? AND ")
      .append(" ref.sourceHeadingType = 'NH' AND ")
      .append(" ref.userViewString = '" + View.makeSingleViewString(cataloguingView) + "' AND ")
      .append(" ref.type = ").toString();

    if (source.getClass() == target.getClass()) {
      return super.loadReference(source, target, referenceType, cataloguingView, session);
    } else if (target.getClass() == TTL_HDG.class) {
      return loadReferenceByQuery(source, target, referenceType, cataloguingView, nameToTitle, session);
    } else {
      return loadReferenceByQuery(source, target, referenceType, cataloguingView, nameNameTitle, session);
    }
  }


  /**
   * Delete NME_HDG.
   *
   * @param p       the p
   * @param session the session
   * @throws ReferentialIntegrityException the referential integrity exception
   * @throws HibernateException            the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public void delete(final Persistence p, final Session session)
    throws HibernateException {

    final NME_HDG nameHeading = (NME_HDG) p;
    final List<Integer> countList =
      session.find(
        "select count(*) from NME_TTL_HDG as t where "
          + " t.nameHeadingNumber = ? and "
          + " t.key.userViewString = '" + View.makeSingleViewString(View.toIntView(nameHeading.getUserViewString())) + "'",
        new Object[]{
          nameHeading.getKey().getHeadingNumber()},
        new Type[]{
          Hibernate.INTEGER});
    if (countList.get(0) > 0) {
      throw new ReferentialIntegrityException("NME_TTL_HDG", "NME_HDG");
    }
    p.markDeleted();
    persistByStatus(p, session);
  }




}
