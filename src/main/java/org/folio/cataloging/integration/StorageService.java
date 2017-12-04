package org.folio.cataloging.integration;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.codetable.ValueLabelElement;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.DAOSearchIndex;
import org.folio.cataloging.dao.persistence.DB_LIST;
import org.folio.rest.jaxrs.resource.IndexCategoriesResource;

import java.io.Closeable;
import java.io.IOException;
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

    @Override
    public void close() throws IOException {
        try {
            session.close();
        } catch (HibernateException exception) {
            throw new IOException(exception);
        }
    }
}