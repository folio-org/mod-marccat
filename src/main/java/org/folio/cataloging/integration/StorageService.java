package org.folio.cataloging.integration;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.codetable.ValueLabelElement;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.dao.DAOIndexList;
import org.folio.cataloging.dao.DAOSearchIndex;
import org.folio.cataloging.dao.common.HibernateSessionProvider;
import org.folio.cataloging.dao.persistence.DB_LIST;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.folio.cataloging.bean.cataloguing.bibliographic.codelist.CodeListsBean.getDatabaseViewList;

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
    public List<ValueLabelElement> getLogicalViews(final String lang) throws DataAccessException {
        return getDatabaseViewList().getDAO().getList(session, DB_LIST.class, Locale.forLanguageTag(lang));
    }

    /**
     * Returns a list of all categories belonging to the requested type.
     *
     * @param type the index type, used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @return a list of categories by index type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<ValueLabelElement> getIndexCategories(final String type, final String lang) throws DataAccessException {
        DAOSearchIndex searchIndexDao = new DAOSearchIndex();
        return searchIndexDao.getIndexCategories(session, type, Locale.forLanguageTag(lang));
    }

    /**
     * Returns the categories belonging to the requested type.
     *
     * @param type the index type, used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @return a list of categories by index type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<ValueLabelElement> getIndexes(final String type, final String categoryCode, final String lang) throws DataAccessException {
        DAOSearchIndex searchIndexDao = new DAOSearchIndex();
        return searchIndexDao.getIndexes(session, type, categoryCode, Locale.forLanguageTag(lang));
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
    public List<ValueLabelElement> getIndexesByCode(final String code, final String lang) throws DataAccessException, HibernateException {
        DAOIndexList daoIndex = new DAOIndexList();
        String tableName = daoIndex.getCodeTableName(session,code,Locale.forLanguageTag(lang));
        DAOCodeTable dao = new DAOCodeTable();
        Class className  = HibernateSessionProvider.getHibernateClassName(tableName.toUpperCase());
        return className!=null ? dao.getList(session,className, Locale.forLanguageTag(lang)) : new ArrayList<ValueLabelElement>();
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
        return searchIndexDao.getIndexDescription(session, code, Locale.forLanguageTag(lang));
    }

    @Override
    public void close() throws IOException {
        try {
            session.close();
        } catch (HibernateException exception) {
            throw new IOException(exception);
        }
    }
}