package org.folio.marccat.dao;


import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.SortFormException;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.DataAccessException;

import java.util.List;


/**
 * Manages headings in the NME_TTL_HDG table.
 *
 * @author paulm
 * @author carment
 */
public class NameTitleDescriptorDAO extends DescriptorDAO {

  /**
   * Gets the persistent class.
   *
   * @return the persistent class
   */
  public Class getPersistentClass() {
    return NME_TTL_HDG.class;
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
   * Load headings.
   *
   * @param nameTitleHeadingList the name title heading list
   * @param cataloguingView      the cataloguing view
   * @param session              the session
   * @throws HibernateException the hibernate exception
   */
  protected void loadHeadings(final List<NME_TTL_HDG> nameTitleHeadingList, final int cataloguingView, final Session session)
    throws HibernateException {
    for (NME_TTL_HDG aHdg : nameTitleHeadingList) {
      loadHeadings(aHdg, cataloguingView, session);
    }
  }

  /**
   * Load headings.
   *
   * @param d               the d
   * @param cataloguingView the cataloguing view
   * @param session         the session
   * @throws HibernateException the hibernate exception
   */
  protected void loadHeadings(final NME_TTL_HDG d, final int cataloguingView, final Session session)
    throws HibernateException {
    final NameDescriptorDAO daoName = new NameDescriptorDAO();
    final TitleDescriptorDAO daoTitle = new TitleDescriptorDAO();
    d.setNameHeading((NME_HDG) daoName.load(d.getNameHeadingNumber(), cataloguingView, session));
    d.setTitleHeading((TTL_HDG) daoTitle.load(d.getTitleHeadingNumber(), cataloguingView, session));
  }

  /**
   * Load.
   *
   * @param headingNumber   the heading number
   * @param cataloguingView the cataloguing view
   * @param session         the session
   * @return the descriptor
   * @throws HibernateException the hibernate exception
   */
  @Override
  public Descriptor load(final int headingNumber, final int cataloguingView, final Session session)
    throws HibernateException {
    NME_TTL_HDG nameTitleHeading = (NME_TTL_HDG) super.load(headingNumber, cataloguingView, session);
    loadHeadings(nameTitleHeading, cataloguingView, session);
    return nameTitleHeading;
  }

  /**
   * Gets the cross reference count.
   *
   * @param source          the source
   * @param cataloguingView the cataloguing view
   * @param session         the session
   * @return the xref count
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public int getXrefCount(final Descriptor source, final int cataloguingView, final Session session)
    throws HibernateException {

    int count = super.getXrefCount(source, cataloguingView, session);
    List<Integer> countList = session.find("select count(*) from NME_NME_TTL_REF as ref "
        + " where ref.nameTitleHeadingNumber = ? "
        + " and ref.sourceHeadingType = 'MH' "
        + " and ref.userViewString = '" + View.makeSingleViewString(cataloguingView) + "'",
      new Object[]{
        source.getKey().getHeadingNumber()},
      new Type[]{
        Hibernate.INTEGER});
    count += countList.get(0);

    countList = session.find("select count(*) from TTL_NME_TTL_REF as ref "
        + " where ref.nameTitleHeadingNumber = ? "
        + " and ref.sourceHeadingType = 'MH' "
        + " and ref.userViewString = '" + View.makeSingleViewString(cataloguingView) + "'",
      new Object[]{
        source.getKey().getHeadingNumber()},
      new Type[]{
        Hibernate.INTEGER});

    count += countList.get(0);
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
    refList.addAll(session.find("from NME_NME_TTL_REF as ref "
        + " where ref.nameTitleHeadingNumber = ? "
        + " and ref.sourceHeadingType = 'MH' "
        + " and ref.userViewString = '" + View.makeSingleViewString(cataloguingView) + "'",
      new Object[]{
        source.getKey().getHeadingNumber()},
      new Type[]{
        Hibernate.INTEGER}));

    refList.addAll(session.find("from TTL_NME_TTL_REF as ref "
        + " where ref.nameTitleHeadingNumber = ? "
        + " and ref.sourceHeadingType = 'MH' "
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
  @Override
  public REF loadReference(final Descriptor source, final Descriptor target,
                           final short referenceType, final int cataloguingView, final Session session)
    throws HibernateException {

    REF ref = null;
    if (source.getClass() == target.getClass()) {
      return super.loadReference(source, target, referenceType,
        cataloguingView, session);
    } else if (target.getClass() == NME_HDG.class) {
      final String query = "from NME_NME_TTL_REF as ref "
        + " where ref.nameTitleHeadingNumber = ? AND "
        + " ref.nameHeadingNumber = ? AND "
        + " ref.sourceHeadingType = 'MH' AND "
        + " ref.userViewString = '" + View.makeSingleViewString(cataloguingView) + "' AND "
        + " ref.type = ?";
      return loadReferenceByQuery(source, target, referenceType, cataloguingView, query, session);

    } else if (target.getClass() == TTL_HDG.class) {
      final String query = "from TTL_NME_TTL_REF as ref "
        + " where ref.nameTitleHeadingNumber = ? AND "
        + " ref.titleHeadingNumber = ? AND "
        + " ref.sourceHeadingType = 'MH' AND "
        + " ref.userViewString = '" + View.makeSingleViewString(cataloguingView) + "' AND "
        + " ref.type = ?";
      return loadReferenceByQuery(source, target, referenceType, cataloguingView, query, session);

    }
    return ref;
  }

  /**
   * Gets the matching heading.
   *
   * @param d       the d
   * @param session the session
   * @return the matching heading
   * @throws HibernateException the hibernate exception
   * @throws SortFormException  the sort form exception
   */
  @Override
  public Descriptor getMatchingHeading(final Descriptor d, final Session session)
    throws HibernateException {
    NME_TTL_HDG nameTitleHeading = (NME_TTL_HDG) d;
    List<NME_TTL_HDG> nameTitleHeadingList = loadHeadings(nameTitleHeading.getNameHeading(), nameTitleHeading.getTitleHeading(), nameTitleHeading
      .getKey().getUserViewString(), session);
    if (nameTitleHeadingList != null && !nameTitleHeadingList.isEmpty()) {
      NME_TTL_HDG hdg = nameTitleHeadingList.get(0);
      hdg.setNameHeading(nameTitleHeading.getNameHeading());
      hdg.setTitleHeading(nameTitleHeading.getTitleHeading());
      return hdg;
    }
    return null;
  }

  /**
   * Checks if is matching another heading.
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

    final NME_TTL_HDG nameTitleHeading = (NME_TTL_HDG) desc;
    final int view = View.toIntView(nameTitleHeading.getUserViewString());
    final Query q = session.createQuery("select distinct hdg from "
      + "NME_TTL_HDG as hdg, "
      + " where hdg.nameHeadingNumber = :nameKey "
      + " and hdg.titleHeadingNumber = :titleKey "
      + " and c.key.headingNumber <> :currHdgNbr "
      + " and hdg.key.userViewString = '" + View.makeSingleViewString(view) + "'");
    q.setInteger("nameKey", nameTitleHeading.getNameHeadingNumber());
    q.setInteger("titleKey", nameTitleHeading.getTitleHeadingNumber());
    q.setInteger("currHdgNbr", nameTitleHeading.getHeadingNumber());
    List<NME_TTL_HDG> nameTitleHeadingList = (List<NME_TTL_HDG>) q.list();
    nameTitleHeadingList = (List<NME_TTL_HDG>) isolateViewForList(nameTitleHeadingList, view, session);
    nameTitleHeadingList.forEach((NME_TTL_HDG descriptor) -> compareHeading(nameTitleHeading, descriptor));
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

  /**
   * Calculate search term.
   *
   * @param term        the term
   * @param browseIndex the browse index
   * @return the string
   * @throws DataAccessException the data access exception
   */
/*  @Override
  public String calculateSearchTerm(final String term, final String browseIndex, final Session session)
    throws HibernateException, SQLException {

    String searchTerm = super.calculateSearchTerm(term, browseIndex, session);
    if (term.indexOf(':') > -1) {
      String[] parsedTerm = term.split(":");
      String name = parsedTerm[0].trim();
      String title = parsedTerm[1].trim();
      searchTerm = calculateSearchTerm(name, "2P0", session) + " : "
        + calculateSearchTerm(title, "7P0", session);
    }
    return searchTerm;
  }*/

  /**
   * Load headings.
   *
   * @param nameHdg               the name hdg
   * @param titleHdg              the title hdg
   * @param cataloguingViewString the cataloguing view string
   * @param session               the session
   * @return the list
   * @throws HibernateException the hibernate exception
   */
  private List<NME_TTL_HDG> loadHeadings(final NME_HDG nameHdg, final TTL_HDG titleHdg,
                                         final String cataloguingViewString, final Session session) throws HibernateException {

    final int view = View.toIntView(cataloguingViewString);
    final Query q = session.createQuery("select distinct hdg from "
      + "NME_TTL_HDG as hdg, "
      + " where hdg.nameHeadingNumber = :nameKey "
      + " and hdg.titleHeadingNumber = :titleKey " + "  and "
      + " hdg.key.userViewString = '" + View.makeSingleViewString(view) + "'");
    q.setInteger("nameKey", nameHdg.getKey().getHeadingNumber());
    q.setInteger("titleKey", titleHdg.getKey().getHeadingNumber());
    List<NME_TTL_HDG> nameTitleHeadingList = q.list();
    nameTitleHeadingList = (List<NME_TTL_HDG>) isolateViewForList(nameTitleHeadingList, view, session);
    return nameTitleHeadingList;
  }


}
