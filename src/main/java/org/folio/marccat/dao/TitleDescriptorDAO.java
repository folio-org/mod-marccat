package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.REF;
import org.folio.marccat.dao.persistence.TTL_HDG;
import org.folio.marccat.dao.persistence.T_AUT_HDG_SRC;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ReferentialIntegrityException;

import java.util.List;

/**
 * Manages headings in the TTL_HDG table.
 *
 * @author paulm
 * @author carment
 */
public class TitleDescriptorDAO extends DAODescriptor {

  public static final String TH = "'TH'";

  /**
   * Gets the persistent class.
   *
   * @return the persistent class
   */
  public Class getPersistentClass() {
    return TTL_HDG.class;
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
    List<Integer> countList =
      session.find(
        "select count(*) from TTL_NME_TTL_REF as ref "
          + " where ref.titleHeadingNumber = ? "
          + " and ref.sourceHeadingType = " + TH + " "
          + " and ref.userViewString = '" + View.makeSingleViewString(cataloguingView) + "'",
        new Object[]{
          source.getKey().getHeadingNumber()},
        new Type[]{
          Hibernate.INTEGER});
    count = count + countList.get(0);
    countList =
      session.find(
        "select count(*) from NME_TO_TTL_REF as ref "
          + " where ref.titleHeadingNumber = ? "
          + " and ref.sourceHeadingType = " + TH + " "
          + " and ref.userViewString = '" + View.makeSingleViewString(cataloguingView) + "'",
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
   * @throws DataAccessException the data access exception
   * @throws HibernateException  the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<REF> getCrossReferences(final Descriptor source, final int cataloguingView, final Session session)
    throws HibernateException {

    List<REF> refList = super.getCrossReferences(source, cataloguingView, session);

    refList.addAll(
      session.find(
        "from TTL_NME_TTL_REF as ref "
          + " where ref.titleHeadingNumber = ? "
          + " and ref.sourceHeadingType = " + TH + " "
          + " and ref.userViewString = '" + View.makeSingleViewString(cataloguingView) + "'",
        new Object[]{
          source.getKey().getHeadingNumber()},
        new Type[]{
          Hibernate.INTEGER}));
    refList.addAll(
      session.find(
        "from NME_TO_TTL_REF as ref "
          + " where ref.titleHeadingNumber = ? "
          + " and ref.sourceHeadingType = " + TH + " "
          + " and ref.userViewString = '" + View.makeSingleViewString(cataloguingView) + "'",
        new Object[]{
          source.getKey().getHeadingNumber()},
        new Type[]{
          Hibernate.INTEGER}));
    return refList;
  }

  /**
   * Load reference.
   *
   * @param source          the source
   * @param target          the target
   * @param referenceType   the reference type
   * @param cataloguingView the cataloguing view
   * @param session         the session
   * @return the ref
   * @throws HibernateException the hibernate exception
   */

  @SuppressWarnings("unchecked")
  @Override
  public REF loadReference(final Descriptor source, final Descriptor target, final short referenceType, final int cataloguingView, final Session session) throws HibernateException {

    if (source.getClass() == target.getClass()) {
      return super.loadReference(source, target, referenceType, cataloguingView, session);
    } else {
      final String query = "from TTL_NME_TTL_REF as ref "
        + " where ref.titleHeadingNumber = ? AND "
        + " ref.nameTitleHeadingNumber = ? AND "
        + " ref.sourceHeadingType = " + TH + " AND "
        + " ref.key.userViewString = '" + View.makeSingleViewString(cataloguingView) + "' AND "
        + " ref.key.type = ?";
      return loadReferenceByQuery(source, target, referenceType, cataloguingView, query, session);
    }
  }

  /**
   * Delete the TTL_HDG.
   *
   * @param p       the p
   * @param session the session
   * @throws ReferentialIntegrityException the referential integrity exception
   * @throws HibernateException            the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public void delete(final Persistence p, final Session session)
    throws ReferentialIntegrityException, HibernateException {

    final TTL_HDG title = (TTL_HDG) p;
    final List<Integer> countList =
      session.find(
        "select count(*) from NME_TTL_HDG as d where "
          + " d.nameHeadingNumber = ? and "
          + " d.key.userViewString = '" + View.makeSingleViewString(View.toIntView(title.getUserViewString())) + "'",
        new Object[]{
          title.getKey().getHeadingNumber()},
        new Type[]{Hibernate.INTEGER});
    if (countList.get(0) > 0) {
      throw new ReferentialIntegrityException("NME_TTL_HDG", "TTL_HDG");
    }
    super.delete(p, session);
  }

  /**
   * Checks if is matching another heading(TTL_HDG).
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

    final TTL_HDG titleHeading = (TTL_HDG) desc;
    final List<TTL_HDG> titleHeadingList = session.find(" from "
        + getPersistentClass().getName()
        + " as c "
        + " where c.stringText= ? "
        + " and c.indexingLanguage = ? "
        + " and c.accessPointLanguage = ?"
        + " and c.key.userViewString = ?"
        + " and c.key.headingNumber <> ?",
      new Object[]{
        titleHeading.getStringText(),
        titleHeading.getIndexingLanguage(),
        titleHeading.getAccessPointLanguage(),
        titleHeading.getUserViewString(),
        titleHeading.getKey().getHeadingNumber()},
      new Type[]{Hibernate.STRING,
        Hibernate.INTEGER,
        Hibernate.STRING,
        Hibernate.INTEGER});
    titleHeadingList.forEach((TTL_HDG descriptor) ->
      compareHeading(titleHeading, descriptor));
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
