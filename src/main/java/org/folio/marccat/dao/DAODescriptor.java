package org.folio.marccat.dao;

import static org.folio.marccat.util.F.deepCopy;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.SortFormException;
import org.folio.marccat.business.common.View;
import org.folio.marccat.business.descriptor.SortFormParameters;
import org.folio.marccat.config.log.Global;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.DescriptorKey;
import org.folio.marccat.dao.persistence.NME_HDG;
import org.folio.marccat.dao.persistence.NME_TTL_HDG;
import org.folio.marccat.dao.persistence.REF;
import org.folio.marccat.dao.persistence.SBJCT_HDG;
import org.folio.marccat.dao.persistence.TTL_HDG;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ReferentialIntegrityException;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;


/**
 * An abstract class representing separately indexed tables that access
 * bibliographic items. (e.g. Name, Title, Subject, ...)
 * Descriptor objects contain members to identify unique keys for "headings" and
 * also contain a reference to the associated persistence class.
 *
 * @author paulm
 * @author natasciab
 * @author carment
 */
public abstract class DAODescriptor extends AbstractDAO {
	private String queryAccessPoint = " AND hdg.accessPointLanguage=? ";
	private String queryTarget = " AND ref.key.target=hdg.key.keyNumber ";
	private String queryType = " AND ref.key.type=5 ";
	private String queryAndHdg = " and hdg.key.userViewString = '";
	private String queryAndRef = " and ref.key.userViewString = '";
	private String queryAsApf = " as apf ";
	private String queryAsC = " as c ";
	private String queryAsHdg = " as hdg ";
	private String queryAsRef = " as ref, ";
	private String queryWhereRefSource = " where ref.key.source = ? ";
	private String querySelectCount = "select count(*) from ";
	private String querySelectRefFrom = "select ref from ";
	

  /**
   * The blank sortform.
   */
  private static final String BLANK_SORTFORM = Global.EMPTY_VALUE;

  /**
   * The sort sortform length.
   */
  private static final int SORTFORM_LENGTH = 1080;

  /**
   * The max sort sortform length.
   */
  private static final int MAX_SORTFORM_LENGTH = 250;

  /**
   * Gets the name of the associated Persistent class.
   *
   * @return the name
   */
  public abstract Class getPersistentClass();

  /**
   * default implemenation indicating whether the Descriptor class may have
   * cross-references (default true).
   *
   * @return true, if successful
   */
  public boolean supportsCrossReferences() {
    return true;
  }

  /**
   * default implemenation indicating whether the Descriptor class may have
   * authorities (default false).
   *
   * @return true, if successful
   */
  public boolean supportsAuthorities() {
    return true;
  }


  /**
   * Calculates the sortform of a descriptor. The method is overloaded. The
   * (String) version can be used to normalize a string when no Descriptor is
   * available.
   *
   * @param descriptor the descriptor
   * @param session    the session
   * @return the string
   * @throws HibernateException the hibernate exception
   * @throws SQLException       the SQL exception
   * @since 1.0
   */
  public String calculateSortForm(final Descriptor descriptor, final Session session) throws HibernateException, SQLException {

    if (Global.EMPTY_STRING.equals(descriptor.getDisplayValue())) {
      return BLANK_SORTFORM;
    }
    descriptor.calculateAndSetSortForm();
    return descriptor.getSortForm();
  }

  /**
   * Calculates the sortform of a String with known search index. The method
   * is overloaded. (String) and (Descriptor) versions exist.
   *
   * @param s              the s
   * @param browseIndexKey the browse index key
   * @param session        the session
   * @return the string
   * @throws HibernateException the hibernate exception
   * @throws SQLException       the SQL exception
   * @since 1.0
   */
  private String calculateSortForm(final String s, final String browseIndexKey, final Session session)
    throws HibernateException, SQLException {
    if ("".equals(s)) {
      return " ";
    }
    final SortFormParameters parms = new DAOIndexList()
      .getSortFormParametersByKey(browseIndexKey, session);
    return calculateSortForm(s, parms, session);
  }

  /**
   * Calculate sort form of a String.
   *
   * @param text    the text
   * @param parms   the parms
   * @param session the session
   * @return the string
   * @throws HibernateException the hibernate exception
   * @throws SortFormException  the sort form exception
   * @throws SQLException       the SQL exception
   */
  public String calculateSortForm(final String text, final SortFormParameters parms, final Session session)
    throws HibernateException, SQLException {
    String result;
    CallableStatement proc = null;
    Connection connection;
    final int bufSize = 300;
    int rc;
    connection = session.connection();
    try {
      proc = connection.prepareCall("{ call PACK_SORTFORM.SF_PREPROCESS(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
      proc.setString(1, text);
      proc.setInt(2, bufSize);
      proc.setInt(3, parms.getSortFormMainType());
      proc.setInt(4, parms.getSortFormSubType());
      proc.setInt(5, parms.getNameTitleOrSubjectType());
      proc.setInt(6, parms.getNameSubtype());
      proc.setInt(7, parms.getSkipInFiling());
      proc.registerOutParameter(8, Types.INTEGER);
      proc.registerOutParameter(9, Types.VARCHAR);
      proc.execute();
      rc = proc.getInt(8);
      if (rc != 0) {
        throw new SortFormException(String.valueOf(rc));
      }
      result = proc.getString(9);
      proc.close();
      proc = connection.prepareCall("{ call PACK_SORTFORM.SF_BUILDSRTFRM(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
      proc.setString(1, result);
      proc.setInt(2, bufSize);
      proc.setInt(3, parms.getSortFormMainType());
      proc.setInt(4, parms.getSortFormSubType());
      proc.setInt(5, parms.getNameTitleOrSubjectType());
      proc.setInt(6, parms.getNameSubtype());
      proc.setInt(7, parms.getSkipInFiling());
      proc.registerOutParameter(8, Types.INTEGER);
      proc.registerOutParameter(9, Types.VARCHAR);
      proc.execute();

      rc = proc.getInt(8);

      if (rc != 0) {
        throw new SortFormException(String.valueOf(rc));
      }
      result = proc.getString(9);
    } catch (SQLException e) {
      throw new SQLException(e);
    } finally {
      if (proc != null) {
        proc.close();
      }

    }

    return result;
  }

  /**
   * Load a heading by heading number and cataloguing view.
   *
   * @deprecated replaced by {@link #load(int, int, Session)}
   */
  @Deprecated
  public Descriptor load(final int keyNumber, final int cataloguingView) {
    return null;
  }

  /**
   * loads the heading member from the database based on the settings of the
   * key values.
   *
   * @param keyNumber   the heading number
   * @param cataloguingView the cataloguing view
   * @param session         the session
   * @return the descriptor
   * @throws HibernateException the hibernate exception
   */
  public Descriptor load(final int keyNumber, final int cataloguingView, final Session session)
    throws HibernateException {
    return load(keyNumber, cataloguingView, getPersistentClass(), session);
  }


  /**
   * Load a heading by heading number and cataloguing view.
   *
   * @param keyNumber   the heading number
   * @param cataloguingView the cataloguing view
   * @param persistentClass the persistent class
   * @param session         the session
   * @return the descriptor
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  public Descriptor load(final int keyNumber, final int cataloguingView,
                         final Class persistentClass, final Session session) throws HibernateException {

    final List<Descriptor> descriptorList = session.find("from " + persistentClass.getName()
        + " as hdg where hdg.key.keyNumber = ? "
        + queryAndHdg + View.makeSingleViewString(cataloguingView) + "'",
      new Object[]{
        keyNumber},
      new Type[]{
        Hibernate.INTEGER});

    Descriptor descriptor = null;
    if (!descriptorList.isEmpty()) {
      descriptor = descriptorList.get(0);
      descriptor = (Descriptor) isolateView(descriptor, cataloguingView, session);
    }
    return descriptor;
  }

  /**
   * Load a heading by heading number.
   *
   * @param keyNumber   the heading number
   * @param persistentClass the persistent class
   * @param session         the session
   * @return the descriptor
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  public Descriptor load(final int keyNumber, final Class persistentClass, final Session session)
    throws HibernateException {
    List<Descriptor> l = session.find("from " + persistentClass.getName()
        + " as hdg where hdg.key.keyNumber = ? ",
      new Object[]{
        keyNumber},
      new Type[]{
        Hibernate.INTEGER});
    Descriptor result = null;
    if (!l.isEmpty()) {
      result = l.get(0);
    }
    return result;
  }

  /**
   * Returns the first n rows having sortform > term.
   *
   * @param operator      the operator
   * @param direction     the direction
   * @param term          the term
   * @param filter        the filter
   * @param searchingView the searching view
   * @param count         the count
   * @param session       the session
   * @return the headings by sortform
   * @throws HibernateException the hibernate exception
   */
  public List<Descriptor> getHeadingsBySortform(final String operator, final String direction,
                                                final String term, final String filter, int searchingView, final int count, final Session session)
    throws HibernateException {
    List<Descriptor> descriptorList;
    String viewClause = "";
    if (searchingView == View.AUTHORITY) {
      searchingView = 1;
    }
    if (searchingView != View.ANY) {
      viewClause = queryAndHdg + View.makeSingleViewString(searchingView) + "' ";
    }
    final Query q = session.createQuery(" select hdg from " + getPersistentClass().getName()
      + " as hdg where hdg.sortForm " + operator + " :term "
      + viewClause + filter + " order by hdg.sortForm "
      + direction);
    q.setString("term", term);
    q.setMaxResults(count);
    descriptorList = q.list();
    return (List<Descriptor>) isolateViewForList(descriptorList, searchingView, session);

  }

  /**
   * Gets the document count.
   *
   * @param d             the d
   * @param searchingView the searching view
   * @return the doc count
   * @throws DataAccessException the data access exception
   */
  @SuppressWarnings("unchecked")
  public int getDocCount(final Descriptor d, final int searchingView, final Session session)
    throws HibernateException {
    final List<Integer> counList;
    int result = 0;
    if (searchingView == View.ANY) {
      counList = session.find(" select count(distinct apf.bibItemNumber) from "
          + d.getAccessPointClass().getName() + queryAsApf
          + " where apf.keyNumber = ? ",
        new Object[]{
          d.getKey().getKeyNumber()},
        new Type[]{
          Hibernate.INTEGER});

      if (!counList.isEmpty()) result = counList.get(0);
      return result;
    } else {
      counList = session.find(" select count(distinct apf.bibItemNumber) from "
          + d.getAccessPointClass().getName() + queryAsApf
          + " where apf.keyNumber = ? and "
          + " apf.userViewString = '" + View.makeSingleViewString(searchingView) + "'",
        new Object[]{
          d.getKey().getKeyNumber()},
        new Type[]{
          Hibernate.INTEGER});
      if (!counList.isEmpty()) result = counList.get(0);
      return result;
    }
  }


  /**
   * Gets the document list.
   *
   * @param descriptor    the descriptor
   * @param searchingView the searching view
   * @param session       the session
   * @return the doc list
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  public List<Integer> getDocList(final Descriptor descriptor, final int searchingView, final Session session) throws HibernateException {
    final List<Integer> documentList;
    if (searchingView == View.ANY) {
      documentList = session.find(
        " select apf.bibItemNumber from "
          + descriptor.getAccessPointClass().getName() + queryAsApf
          + " where apf.keyNumber = ?",
        new Object[]{
          descriptor.getKey().getKeyNumber()},
        new Type[]{Hibernate.INTEGER});
    } else {
      documentList = session.find(" select apf.bibItemNumber from "
          + descriptor.getAccessPointClass().getName() + queryAsApf
          + " where apf.keyNumber = ? and "
          + " apf.userViewString = '" + View.makeSingleViewString(searchingView) + "'",
        new Object[]{
          descriptor.getKey().getKeyNumber()},
        new Type[]{
          Hibernate.INTEGER});
    }
    return documentList;
  }


  /**
   * Persist by status(new, update...) a descriptor.
   *
   * @param descriptor the descriptor
   * @param session    the session
   * @throws HibernateException the hibernate exception
   */
  public void persist(final Descriptor descriptor, final Session session) throws HibernateException {
    if (descriptor.isNew()) {
      final int keyNumber = (new SystemNextNumberDAO())
        .getNextNumber(descriptor.getNextNumberKeyFieldCode(), session);
      descriptor.setKey(new DescriptorKey(keyNumber, descriptor
        .getKey().getUserViewString()));
      descriptor.setKeyNumber(keyNumber);
    }
    if (descriptor.isChanged() && descriptor.changeAffectsCacheTable()) {
      persistByStatus(descriptor, session);
      updateCacheTable(descriptor, session);
    } else {
      persistByStatus(descriptor, session);
    }
  }

  /**
   * Updates the cache table for each of the documents attached to the
   * descriptor.
   *
   * @param descriptor the descriptor
   * @since 1.0
   * @deprecated
   */
  @Deprecated
  public void updateCacheTable(Descriptor descriptor) {
  }

  /**
   * Updates the cache table for each of the documents attached to the descriptor.
   *
   * @param descriptor -- the descriptor related to heading/tag.
   * @param session    -- the hibernate current session.
   * @throws HibernateException in case of hibernate exception.
   */
  public void updateCacheTable(final Descriptor descriptor, final Session session) throws HibernateException {
    final BibliographicCatalogDAO dao = new BibliographicCatalogDAO();
    int cataloguingView = View.toIntView(descriptor.getUserViewString());
    List<Integer> ids = getDocList(descriptor, cataloguingView, session);
    ids.stream().forEach(amicusNumber -> {
      dao.updateCacheTable(amicusNumber, cataloguingView);
    });
  }

  /**
   * Allows individual dao's (especially publisher) to override the descriptor
   * handling of sortform.
   *
   * @param descriptor the descriptor
   * @return the browsing sort form
   */
  public String getBrowsingSortForm(Descriptor descriptor) {
    return descriptor.getSortForm();
  }

  /**
   * Finds any existing Descriptor with the same key as the one
   * selected but with the user view isolated to the given cataloguing view.
   * If no such object exists, a new one is created by copying the original
   * Descriptor object.
   *
   * @param keyNumber    the heading number
   * @param recordViewString the record view string
   * @param cataloguingView  the cataloguing view
   * @param session          the session
   * @return the descriptor
   * @throws HibernateException the hibernate exception
   */
  public Descriptor findOrCreateMyView(final int keyNumber,
                                       final String recordViewString, int cataloguingView,
                                       final Session session)
    throws HibernateException {
    final short onFileView = View.toIntView(recordViewString);
    final Descriptor existing = load(keyNumber, cataloguingView, session);
    if (existing != null) {
      return existing;
    } else {
      final Descriptor descriptor = load(keyNumber, onFileView, session);
      final Descriptor newDescriptor = (Descriptor) deepCopy(descriptor);
      newDescriptor.setUserViewString(View.makeSingleViewString(cataloguingView));
      save(newDescriptor);
      return newDescriptor;
    }
  }

  /**
   * In the past some sortforms included "extra" information to help
   * in identifying uniqueness (some headings added a code for "language of
   * access point" so that otherwise identical headings could be
   * differentiated if they had different languages). Now, the only example I
   * can think of is the Dewey Decimal classification where the sortform
   * includes the Dewey Edition Number so that identical numbers from
   * different editions will have different sortforms.
   *
   * @param descriptor the descriptor
   * @param session    the session
   * @return true, if is matching another heading
   * @throws HibernateException the hibernate exception
   * @throws SQLException       the SQL exception
   */
  @SuppressWarnings("unchecked")
  public boolean isMatchingAnotherHeading(final Descriptor descriptor, final Session session) throws HibernateException, SQLException {
    final String sortForm = calculateSortForm(descriptor, session);
    final List<Integer> countList = session.find(
    		querySelectCount + getPersistentClass().getName()
        + queryAsC
        + " where c.sortForm = ? and c.stringText = ? "
        + " and c.key.userViewString = ?"
        + " and c.key.keyNumber <> ?",
      new Object[]{sortForm, descriptor.getDisplayValue(),
        descriptor.getUserViewString(),
        descriptor.getKey().getKeyNumber()},
      new Type[]{
        Hibernate.STRING, Hibernate.STRING,
        Hibernate.STRING, Hibernate.INTEGER});
    return countList.get(0) > 0;

  }

  /**
   * Gets the matching heading.
   *
   * @param descriptor the descriptor
   * @param session    the session
   * @return the matching heading
   * @throws HibernateException the hibernate exception
   * @throws SQLException       the SQL exception
   */
  @SuppressWarnings("unchecked")
  public Descriptor getMatchingHeading(final Descriptor descriptor, final Session session)
    throws HibernateException, SQLException {
    descriptor.setSortForm(calculateSortForm(descriptor, session));
    final List<Descriptor> descriptorList = session.find(
      "from " + getPersistentClass().getName() + queryAsC
        + " where c.sortForm = ? and c.stringText = ? "
        + " and c.key.userViewString = ? ",
      new Object[]{
        descriptor.getSortForm(),
        descriptor.getDisplayValue(),
        descriptor.getUserViewString()},
      new Type[]{
        Hibernate.STRING,
        Hibernate.STRING,
        Hibernate.STRING});

    final Optional<Descriptor> firstElement = descriptorList.stream()
      .filter(Objects::nonNull).findFirst();
    return firstElement.isPresent() ? firstElement.get() : null;

  }


  /**
   * Delete a persistente object(Descriptor).
   *
   * @param p       the p
   * @param session the session
   * @throws HibernateException the hibernate exception
   */
  @Override
  public void delete(final Persistence p, final Session session) throws
    HibernateException {
    if (!(p instanceof Descriptor)) {
      throw new IllegalArgumentException("I can only delete Descriptor objects");
    }
    final Descriptor descriptor = ((Descriptor) p);
    if (getDocCount(descriptor, View.toIntView(descriptor.getUserViewString()), session) > 0) {
      throw new ReferentialIntegrityException(descriptor.getAccessPointClass()
        .getName(), descriptor.getClass().getName());
    }
      if (supportsCrossReferences() && getXrefCount(descriptor, View.toIntView(descriptor.getUserViewString()), session) > 0) {
        throw new ReferentialIntegrityException(descriptor.getReferenceClass(
          descriptor.getClass()).getName(), descriptor.getClass().getName());
      }
      if (supportsAuthorities() && descriptor.getAuthorityCount() > 0) {
        throw new ReferentialIntegrityException(descriptor.getReferenceClass(
          descriptor.getClass()).getName(), descriptor.getClass().getName());
      }

    if (getAuthorityApfReferenceCount(descriptor, session) > 0) {
      throw new ReferentialIntegrityException("AUT_X_ACS_PNT", descriptor
        .getClass().getName());
    }

    super.delete(p, session);
  }

  /**
   * Counts the number of references to the given descriptor in the corresponding
   * Authority Access Point table.
   *
   * @param descriptor the descriptor
   * @param session    the session
   * @return the authority apf reference count
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  private int getAuthorityApfReferenceCount(final Descriptor descriptor, final Session session) throws HibernateException {
    if (View.toIntView(descriptor.getUserViewString()) != 1 || descriptor.getAuthorityAccessPointClass() == null)
      return 0;
    List<Integer> countList = session.find("from " + descriptor.getAuthorityAccessPointClass().getName() + " as apf where apf.keyNumber = ?",
      new Object[]{
        descriptor.getHeadingNumber()},
      new Type[]{
        Hibernate.INTEGER});
    return countList.size();
  }

  /**
   * Returns the number of cross references for the given descriptor in the
   * given view.
   *
   * @param source          the descriptor whose xref count is to be calculated
   * @param cataloguingView the view to use for counting
   * @param session         the session
   * @return the count of cross references
   * @throws HibernateException the hibernate exception
   */

  @SuppressWarnings("unchecked")
  public int getXrefCount(final Descriptor source, final int cataloguingView, final Session session)
    throws HibernateException {
    if (source.getReferenceClass(source.getClass()) == null)
      return 0;
    final List<Integer> countList = session.find(querySelectCount
        + source.getReferenceClass(source.getClass()).getName()
        + " as ref where ref.key.source = ? and "
        + " ref.key.userViewString = '" + View.makeSingleViewString(cataloguingView) + "'",
      new Object[]{
        source.getKey().getKeyNumber()},
      new Type[]{
        Hibernate.INTEGER});

    final Optional<Integer> firstElement = countList.stream().filter(Objects::nonNull).findFirst();
    return firstElement.isPresent() ? firstElement.get() : 0;

  }

  /**
   * Gets the cross references for the given source and view.
   *
   * @param source          the descriptor whose references are to be retrieved.
   * @param cataloguingView the view to be used.
   * @param session         the session
   * @return a list of cross references.
   * @throws HibernateException in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  public List<REF> getCrossReferences(final Descriptor source, final int cataloguingView, final Session session)
    throws HibernateException {
    return session.find("from "
        + source.getReferenceClass(source.getClass()).getName()
        + " as ref " + queryWhereRefSource
        + queryAndRef + View.makeSingleViewString(cataloguingView) + "' "
        + " order by ref.key.target, ref.key.type",
      new Object[]{
        source.getKey().getKeyNumber()},
      new Type[]{
        Hibernate.INTEGER});
  }

  /**
   * Gets the cross references for the given source and view.
   *
   * @param source          the source
   * @param cataloguingView the cataloguing view
   * @return the cross references
   * @throws DataAccessException the data access exception
   * @deprecated
   */
  @Deprecated
  public List getCrossReferences(final Descriptor source, final int cataloguingView) {
    return Collections.emptyList();
  }


  /**
   * Load reference by source and target.
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
  public REF loadReference(final Descriptor source, final Descriptor target,
                           final short referenceType, final int cataloguingView, final Session session)
    throws HibernateException {

    REF result = null;
    if (source.getClass() == target.getClass()) {
      final List<REF> refList = session.find("from "
          + source.getReferenceClass(target.getClass()).getName()
          + " as ref " + " where ref.key.source = ? AND "
          + " ref.key.target = ? AND "
          + " ref.key.userViewString = '" + View.makeSingleViewString(cataloguingView) + "' AND "
          + " ref.key.type = ?", new Object[]{
          source.getKey().getKeyNumber(),
          target.getKey().getKeyNumber(),
          referenceType},
        new Type[]{
          Hibernate.INTEGER,
          Hibernate.INTEGER,
          Hibernate.SHORT});
      if (refList.size() == 1) {
        result = refList.get(0);
        result = (REF) isolateView(result, cataloguingView, session);
      }
    }

    return result;
  }

  /**
   * Calculate search term.
   *
   * @param term        the term
   * @param browseIndex the browse index
   * @param session     the session
   * @return the string
   * @throws HibernateException the hibernate exception
   * @throws SQLException       the SQL exception
   */
  public String calculateSearchTerm(final String term, final String browseIndex, final Session session)
    throws HibernateException, SQLException {
    String searchTerm;
    try {
      searchTerm = calculateSortForm(term, browseIndex, session);
    } catch (SortFormException e) {
      int lterm = term.getBytes().length;
      String newTerm = lterm > SORTFORM_LENGTH ? term.substring(0, MAX_SORTFORM_LENGTH)
        : term;
      try {
        searchTerm = calculateSortForm(newTerm, browseIndex, session);
      } catch (SortFormException e1) {
        searchTerm = " ";
      }
    }
    return searchTerm;
  }

  /**
   * Gets the cross references with language.
   *
   * @param source           the source
   * @param cataloguingView  the cataloguing view
   * @param indexingLanguage the indexing language
   * @param session          the session
   * @return the cross references with language
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  public REF getCrossReferencesWithLanguage(final Descriptor source, final int cataloguingView, final short indexingLanguage, final Session session)
    throws HibernateException {
    REF result;
    final List<REF> refList;
    if (source instanceof SBJCT_HDG) {
      refList = session.find(querySelectRefFrom
          + source.getReferenceClass(source.getClass()).getName()
          + queryAsRef + source.getClass().getName() + queryAsHdg
          + queryWhereRefSource
          + queryAndRef + View.makeSingleViewString(cataloguingView) + "' "
          + queryType
          + queryTarget
          + queryAccessPoint,
        new Object[]{
          source.getKey().getKeyNumber(),
          indexingLanguage}, new Type[]{
          Hibernate.INTEGER, Hibernate.INTEGER,
          Hibernate.SHORT});

    } else if (source instanceof NME_TTL_HDG) {
      refList = session.find(querySelectRefFrom
          + source.getReferenceClass(source.getClass()).getName()
          + queryAsRef + "" + source.getClass().getName()
          + " as hdg, " + " NME_HDG as nme, " + " TTL_HDG as ttl "
          + " where hdg.nameHeadingNumber = nme.key.keyNumber "
          + " and hdg.titleHeadingNumber = ttl.key.keyNumber "
          + " and ref.key.source = ? "
          + queryAndRef + View.makeSingleViewString(cataloguingView) + "' "
          + queryType
          + queryTarget
          + " AND nme.indexingLanguage=? "
          + " AND ttl.indexingLanguage=? ", new Object[]{
          source.getKey().getKeyNumber(),
          indexingLanguage,
          indexingLanguage},
        new Type[]{
          Hibernate.INTEGER, Hibernate.SHORT,
          Hibernate.SHORT});
    } else {
      refList = session.find(querySelectRefFrom
          + source.getReferenceClass(source.getClass()).getName()
          + queryAsRef + source.getClass().getName() + queryAsHdg
          + queryWhereRefSource
          + queryAndRef + View.makeSingleViewString(cataloguingView) + "' "
          + queryType
          + queryTarget
          + queryAccessPoint,
        new Object[]{
          source.getKey().getKeyNumber(),
          indexingLanguage}, new Type[]{
          Hibernate.INTEGER,
          Hibernate.SHORT});

    }
    if (refList.size() == 1) {
      result = refList.get(0);
      result = (REF) isolateView(result, cataloguingView, session);
    } else {
      result = getSourceHeadingNumberByTarget(source, cataloguingView,
        indexingLanguage, session);
    }
    return result;

  }

  /**
   * Gets the source heading number by target.
   *
   * @param source           the source
   * @param cataloguingView  the cataloguing view
   * @param indexingLanguage the indexing language
   * @param session          the session
   * @return the source heading number by target
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  private REF getSourceHeadingNumberByTarget(final Descriptor source, final int cataloguingView, final short indexingLanguage, final Session session)
    throws HibernateException {
    final List<REF> firstList;
    final List<REF> secondList;
    REF result = null;
    int targetHeadingNumber = 0;

    secondList = session.find(querySelectRefFrom
        + source.getReferenceClass(source.getClass()).getName()
        + queryAsRef + source.getClass().getName() + queryAsHdg
        + queryWhereRefSource
        + queryAndRef + View.makeSingleViewString(cataloguingView) + "' "
        + queryTarget
        + queryType, new Object[]{
        source.getKey().getKeyNumber()},
      new Type[]{
        Hibernate.INTEGER});

    if (secondList.size() == 1) {
      targetHeadingNumber = secondList.get(0).getTarget();
      firstList = session.find(querySelectRefFrom
          + source.getReferenceClass(source.getClass()).getName()
          + queryAsRef + source.getClass().getName() + queryAsHdg
          + queryWhereRefSource
          + queryAndRef + View.makeSingleViewString(cataloguingView) + "' "
          + queryType
          + queryTarget
          + queryAccessPoint,

        new Object[]{
          targetHeadingNumber,
          indexingLanguage},
        new Type[]{
          Hibernate.INTEGER,
          Hibernate.SHORT});
      if (firstList.size() == 1) {
        result = firstList.get(0);
        result = (REF) isolateView(result, cataloguingView, session);
      }
    }
    return result;

  }

  /**
   * Gets the document count for the name/title descriptor.
   *
   * @param descriptor    the descriptor
   * @param searchingView the searching view
   * @param session       the session
   * @return the doc count NT
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  public int getDocCountNT(final Descriptor descriptor, final int searchingView, final Session session)
    throws HibernateException {

    int result = 0;
    List<Integer> countList = null;
    String viewClause = "";
    if (searchingView != View.ANY) {
      viewClause = queryAndHdg + View.makeSingleViewString(searchingView) + "' ";
    }
    if (descriptor instanceof NME_HDG) {
      final Query q = session.createQuery(" select count(*) from  NME_TTL_HDG as hdg"
        + " where hdg.nameHeadingNumber = " + descriptor.getKey().getKeyNumber()
        + viewClause);
      countList = q.list();

    } else if (descriptor instanceof TTL_HDG) {
      final Query q = session.createQuery(" select count(*) from  NME_TTL_HDG as hdg"
        + " where hdg.titleHeadingNumber = " + descriptor.getKey().getKeyNumber()
        + viewClause);
      countList = q.list();
    }
    if (countList != null && !countList.isEmpty())
      result = countList.get(0);
    return result;
  }

  /**
   * Return true if the given descriptor duplicates the sortform of another
   * descriptor in a different view.
   *
   * @param descriptor the descriptor
   * @param session    the session
   * @return true, if successful
   * @throws HibernateException the hibernate exception
   * @throws SQLException       the SQL exception
   */
  @SuppressWarnings("unchecked")
  public boolean hasMatchingSortformInAnotherView(final Descriptor descriptor, final Session session) throws HibernateException, SQLException {
    final String sortForm = calculateSortForm(descriptor, session);
    final List<Integer> countList = session.find(
    		querySelectCount + getPersistentClass().getName()
        + queryAsC + " where c.sortForm = ? "
        + " and c.key.userViewString <> ?",
      new Object[]{
        sortForm,
        descriptor.getUserViewString()},
      new Type[]{
        Hibernate.STRING,
        Hibernate.STRING});
    return countList.get(0) > 0;
  }

  /**
   * Load cross reference through a specific sql query.
   *
   * @param source          the source
   * @param target          the target
   * @param referenceType   the reference type
   * @param cataloguingView the cataloguing view
   * @param query           the query
   * @param session         the session
   * @return the ref
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  protected REF loadReferenceByQuery(final Descriptor source, final Descriptor target, final short referenceType, final int cataloguingView, final String query, final Session session) throws HibernateException {
    REF ref = null;
    final List<REF> refList =
      session.find(
        query,
        new Object[]{
          source.getKey().getKeyNumber(),
          target.getKey().getKeyNumber(),
          cataloguingView,
          referenceType},
        new Type[]{
          Hibernate.INTEGER,
          Hibernate.INTEGER,
          Hibernate.INTEGER,
          Hibernate.SHORT});
    if (refList.size() == 1) {
      ref = refList.get(0);
      ref = (REF) isolateView(ref, cataloguingView, session);
    }
    return ref;
  }
}
