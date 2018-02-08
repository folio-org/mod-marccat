package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.cataloguing.common.Model;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.log.Log;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Abstract class for common implementations of DAOModels (Bib and Auth).
 *
 * @author agazzarini
 * @author paulm
 * @since 1.0
 */
public abstract class ModelDAO extends HibernateUtil {

	private static final Log logger = new Log(ModelDAO.class);

	/**
	 * Retrieves a list of booleans representing whether a model in the list of all
	 * models is currently in use by a bib item.  Used in Models page to prompt for
	 * cascading delete, if required.
	 *
	 * @return a list of booleans representing whether a model in the list of all models is currently in use by a bib item.
	 * @throws DataAccessException in case of data access failure.
	 */
	public List<Boolean> getModelUsageList() throws DataAccessException {
		return getModelList().stream()
				.map(avp -> getModelItemDAO().getModelUsage(avp.getValue()))
				.collect(toList());
	}

	/**
	 * Retrieves a boolean representing whether a model in the list of all
	 * models is currently in use by a bib item.
	 *
	 * @return a boolean representing whether a model in the list of all models is currently in use by a bib item.
	 * @throws DataAccessException in case of data access failure.
	 */
	public boolean getModelUsage(final int modelId) throws DataAccessException {
		return getModelItemDAO().getModelUsage(modelId);
	}

	protected abstract DAOModelItem getModelItemDAO();

	public Model load(int id) throws DataAccessException {

		logger.debug("model number: " + id);
		Model result = (Model) load(getPersistentClass(), new Integer(id));
		return result;
	}

	abstract protected Class getPersistentClass();

	public void delete(final Model model)
		throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session session)
				throws HibernateException {
				session.delete(
					"from "
						+ getModelItemDAO().getPersistentClass().getName()
						+ " as item"
						+ " where item.model.id = ? ",
					new Integer(model.getId()),
					Hibernate.INTEGER);
				session.delete(model);
			}
		}
		.execute();
	}

	/**
	 * Retrieves the list of all available models.
	 *
	 * @return the list of all available models.
	 * @throws DataAccessException in case of data access failure
	 */
	public List<Avp<Integer>> getModelList() throws DataAccessException {
		return find("select new Avp(m.id, m.label) from " + getPersistentClass().getName() + " as m order by m.label");
	}

	/**
	 * Retrieves the list of all available bibliographic models.
	 *
     * @param session the hibernate session
	 * @return the list of all available bibliographic models.
	 * @throws HibernateException in case of data access failure
	 */
	public List<Avp<Integer>> getBibliographicModelList(final Session session) throws HibernateException {
		return session.find(
				" select new org.folio.cataloging.business.codetable.Avp(m.id, m.label) from "
				+ " org.folio.cataloging.business.cataloguing.bibliographic.BibliographicModel "
				+ " as m order by m.label");
	}

	/**
	 * Retrieves the list of all available authority models.
	 *
     * @param session the hibernate session
	 * @return the list of all available authority models.
	 * @throws HibernateException in case of data access failure
	 */
	public List<Avp<Integer>> getAuthorityModelList(final Session session) throws HibernateException {
		return session.find(
				" select new org.folio.cataloging.business.codetable.Avp(m.id, m.label) from "
				+ " org.folio.cataloging.business.cataloguing.authority.AuthorityModel "
				+ " as m order by m.label");
	}
}