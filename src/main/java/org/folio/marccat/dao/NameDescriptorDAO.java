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
public class NameDescriptorDAO extends DAODescriptor {

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
        + " where ref.nameHeadingNumber = ? "
        + " and ref.sourceHeadingType = 'NH' "
        + " and ref.userViewString = '"+ View.makeSingleViewString(cataloguingView) +"'",
      new Object[]{
        source.getKey().getHeadingNumber()},
      new Type[]{
        Hibernate.INTEGER});
    count = count + countList.get(0);
    countList =
      session.find(
        "select count(*) from NME_TO_TTL_REF as ref "
          + " where ref.nameHeadingNumber = ? "
          + " and ref.sourceHeadingType = 'NH' "
          + " and ref.userViewString = '"+ View.makeSingleViewString(cataloguingView) +"'",
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
          + " where ref.nameHeadingNumber = ? "
          + " and ref.sourceHeadingType = 'NH' "
          + " and ref.userViewString = '"+ View.makeSingleViewString(cataloguingView) +"'",
        new Object[]{
          source.getKey().getHeadingNumber()},
        new Type[]{
          Hibernate.INTEGER}));
    refList.addAll(
      session.find(
        "from NME_TO_TTL_REF as ref "
          + " where ref.nameHeadingNumber = ? "
          + " and ref.sourceHeadingType = 'NH' "
          + " and ref.userViewString = '"+ View.makeSingleViewString(cataloguingView) +"'",
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
      .append(" ref.userViewString = '"+ View.makeSingleViewString(cataloguingView) +"' AND ")
      .append(" ref.type = ?").toString();

    final String nameNameTitle = new StringBuilder()
      .append("from NME_NME_TTL_REF as ref ")
      .append(" where ref.nameHeadingNumber = ? AND ")
      .append(" ref.nameTitleHeadingNumber = ? AND ")
      .append(" ref.sourceHeadingType = 'NH' AND ")
      .append(" ref.userViewString = '"+ View.makeSingleViewString(cataloguingView) +"' AND ")
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
          + " t.key.userViewString = '"+ View.makeSingleViewString(View.toIntView(nameHeading.getUserViewString())) +"'",
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

  /**
   * Checks if is matching another heading(NME_HDG).
   *
   * @param desc    the desc
   * @param session the session
   * @return true, if is matching another heading
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean isMatchingAnotherHeading(final Descriptor desc, final Session session)
    throws HibernateException {
    final NME_HDG nameHeading = (NME_HDG) desc;
    final List<NME_HDG> nameHeadingList = session.find(" from "
        + getPersistentClass().getName()
        + " as c "
        + " where c.stringText= ? "
        + " and c.indexingLanguage = ? "
        + " and c.accessPointLanguage = ?"
        + " and c.typeCode =? "
        + " and c.subTypeCode =? "
        + " and c.key.userViewString = ?"
        + " and c.key.headingNumber <> ?",
      new Object[]{
        nameHeading.getStringText(),
        nameHeading.getIndexingLanguage(),
        nameHeading.getAccessPointLanguage(),
        nameHeading.getTypeCode(),
        nameHeading.getSubTypeCode(),
        nameHeading.getUserViewString(),
        nameHeading.getKey().getHeadingNumber()},
      new Type[]{Hibernate.STRING,
        Hibernate.INTEGER,
        Hibernate.INTEGER,
        Hibernate.INTEGER,
        Hibernate.INTEGER,
        Hibernate.STRING,
        Hibernate.INTEGER});
    nameHeadingList.stream().forEach((NME_HDG descriptor) ->
      compareHeading(nameHeading, descriptor));
    return false;
  }

  /**
   * Compare the headings by authority source.
   *
   * @param descriptorFrom the heading to insert
   * @param descriptorTo   descriptor already present
   * @return true, if successful
   */
   private boolean compareHeading(Descriptor descriptorFrom, Descriptor descriptorTo) {
    if (descriptorFrom.getAuthoritySourceCode() == descriptorTo.getAuthoritySourceCode()) {
      if (descriptorFrom.getAuthoritySourceCode() == T_AUT_HDG_SRC.SOURCE_IN_SUBFIELD_2) {
        return descriptorFrom.getAuthoritySourceText().equals(descriptorTo.getAuthoritySourceText());
      } else {
        return true;
      }
    }
    return false;
  }

}
