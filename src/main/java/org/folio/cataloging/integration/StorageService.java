package org.folio.cataloging.integration;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.codetable.ValueLabelElement;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.dao.DAOIndexList;
import org.folio.cataloging.dao.DAOSearchIndex;
import org.folio.cataloging.dao.common.HibernateSessionProvider;
import org.folio.cataloging.dao.persistence.BibliographicNoteType;
import org.folio.cataloging.dao.persistence.DB_LIST;
import org.folio.cataloging.dao.persistence.T_VRFTN_LVL;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.folio.cataloging.F.locale;

/**
 * Storage layer service.
 * This is the interface towards our storage service. Any R/W access to the persistence layer needs to pass through
 * this interface.
 *
 * @author agazzarini
 * @author carment
 * @since 1.0
 */
public class StorageService implements Closeable {

    private final Session session;

    /**
     * Builds a new {@link StorageService} with the given session.
     *
     * @param session the Hibernate session, which will be used for gathering a connection to the RDBMS.
     */
    public StorageService(final Session session) {
        this.session = session;
    }

    /**
     * Returns the logical views associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the logical view associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<ValueLabelElement<String>> getLogicalViews(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, DB_LIST.class, locale(lang));
    }

    /**
     * Returns the note types associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the note type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<ValueLabelElement<String>> getNoteTypes(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, BibliographicNoteType.class, locale(lang));
    }

    /**
     * Returns the verification levels associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the verification level associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<ValueLabelElement<String>> getVerificationLevels(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_VRFTN_LVL.class, locale(lang));
    }

    /**
     * Returns a list of all categories belonging to the requested type.
     *
     * @param type the index type, used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @return a list of categories by index type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<ValueLabelElement<String>> getIndexCategories(final String type, final String lang) throws DataAccessException {
        final DAOSearchIndex searchIndexDao = new DAOSearchIndex();
        return searchIndexDao.getIndexCategories(session, type, locale(lang));
    }

    /**
     * Returns the categories belonging to the requested type.
     *
     * @param type the index type, used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @return a list of categories by index type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<ValueLabelElement<String>> getIndexes(final String type, final int categoryCode, final String lang) throws DataAccessException {
        DAOSearchIndex searchIndexDao = new DAOSearchIndex();
        return searchIndexDao.getIndexes(session, type, categoryCode, locale(lang));
    }

    /**
     * Returns the constraints (optional) to the requested index.
     *
     * @param code the index code, used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @return a list of all constraints (optional) to the requested index.
     * @throws DataAccessException in case of data access failure.
     * @throws HibernateException
     */
    public List<ValueLabelElement<String>> getIndexesByCode(final String code, final String lang) throws DataAccessException, HibernateException {
        final DAOIndexList daoIndex = new DAOIndexList();
        final String tableName = daoIndex.getCodeTableName(session, code, locale(lang));

        final DAOCodeTable dao = new DAOCodeTable();
        final Optional<Class> className  = ofNullable(HibernateSessionProvider.getHibernateClassName(tableName));
        return className.isPresent()
                ? dao.getList(session, className.get(), locale(lang))
                : Collections.emptyList();
    }

    /**
     * Returns the description for index code.
     *
     * @param code the index code, used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @return the description for index code associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public String getIndexDescription(final String code, final String lang) throws DataAccessException {
        DAOSearchIndex searchIndexDao = new DAOSearchIndex();
        return searchIndexDao.getIndexDescription(session, code, locale(lang));
    }

    @Override
    public void close() throws IOException {
        try {
            session.close();
        } catch (final HibernateException exception) {
            throw new IOException(exception);
        }
    }


}