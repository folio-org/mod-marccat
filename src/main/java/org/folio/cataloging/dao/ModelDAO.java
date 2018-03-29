package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.dao.persistence.Model;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.ModelItem;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Abstract class for common implementations of ModelsDAO (Bib and Auth).
 *
 * @author agazzarini
 * @author carment
 * @author paulm
 * @since 1.0
 */
public abstract class ModelDAO{

	/**
	 * Retrieves a list of booleans representing whether a model in the list of all
	 * models is currently in use by a bib item.  Used in Models page to prompt for
	 * cascading delete, if required.
	 *
	 * @param session the session
	 * @return a list of booleans representing whether a model in the list of all models is currently in use by a bib item.
	 * @throws HibernateException in case of data access failure.
	 */
	public List<Boolean> getModelUsageList(final Session session) throws HibernateException {
		return getModelList(session).stream()
				.map(avp -> {
					try {
						return getModelItemDAO().getModelUsage(avp.getValue(), session);
					} catch (HibernateException e) {
						return null;
					}
				})
				.collect(toList());
	}

	/**
	 * Retrieves a boolean representing whether a model in the list of all
	 * models is currently in use by a bib item.
	 *
	 * @param modelId the model id
	 * @param session the session
	 * @return a boolean representing whether a model in the list of all models is currently in use by a bib item.
	 * @throws HibernateException in case of data access failure.
	 */
	public boolean getModelUsage(final int modelId, final Session session) throws HibernateException {
		return getModelItemDAO().getModelUsage(modelId, session);
	}

	/**
	 * Gets the model item DAO.
	 *
	 * @return the model item DAO
	 */
	protected abstract ModelItemDAO getModelItemDAO();

	/**
	 * Load the Model
	 *
	 * @param id the id
	 * @param session the id
	 * @return the model
	 * @throws HibernateException the data access exception
	 */
	 public Model load(final int id, final Session session) throws HibernateException {
		 List<Model> list = session.find(
					"from "
							+ getPersistentClass().getName()
							+ " as itm where itm.item = ? ",
					new Object[] { new Long(id)},
					new Type[] { Hibernate.LONG });
		 final Optional<Model> firstElement = list.stream().filter(Objects::nonNull).findFirst();
		 return firstElement.isPresent() ? firstElement.get() : null;
	 }


	/**
	 * Gets the persistent class.
	 *
	 * @return the persistent class
	 */
	abstract protected Class getPersistentClass();

	/**
	 * Delete a model and a model item.
	 *
	 * @param model the model
	 * @param session the hibernate session
	 * @throws HibernateException in case of data access failure
	 */
	public void delete(final Model model, final Session session)
		throws HibernateException {
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
	 * Save a model.
	 *
	 * @param model the model
	 * @param session the hibernate session
	 * @throws HibernateException in case of data access failure
	 */
	public void save(final Model model, final Session session)
			throws HibernateException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session session)
					throws HibernateException {
				session.save(model);
			}
		 }
		.execute();
	}

	/**
	 * Retrieves the list of all available models.
	 *
	 * @param session the session
	 * @return the list of all available models.
	 * @throws HibernateException in case of data access failure
	 */
	public List<Avp<Integer>> getModelList(final Session session) throws HibernateException {
		return session.find("select new Avp(m.id, m.label) from " + getPersistentClass().getName() + " as m order by m.label");
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
				+ " org.folio.cataloging.dao.persistence.BibliographicModel "
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
				+ " org.folio.cataloging.dao.persistence.AuthorityModel "
				+ " as m order by m.label");
	}

}