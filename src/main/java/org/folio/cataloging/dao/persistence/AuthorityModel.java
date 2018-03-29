package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.dao.AuthorityModelDAO;
import org.folio.cataloging.dao.common.HibernateUtil;


/**
 * Represents a Model/Template for of authority type
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class AuthorityModel extends Model {

	/** The frbr second group. This group is related to the FRBR entity: Persons, Families and Corporate Bodies. */
	private String frbrSecondGroup;

	/** The frbr third group. This group is related to the FRBR entity: Concepts, Objcts, Places, Events. */
	private String frbrThirdGroup;

	/**
	 * Instantiates a new authority model.
	 */
	public AuthorityModel() {
		super();
	}

	/**
	 * Gets the authority model DAO.
	 *
	 * @return the authority model DAO
	 */
	public AuthorityModelDAO getAuthorityModelDAO() {
		return new AuthorityModelDAO();
	}

	/**
	 * Gets the frbr second group.
	 *
	 * @return the frbr second group
	 */
	public String getFrbrSecondGroup() {
		return frbrSecondGroup;
	}

	/**
	 * Sets the frbr second group.
	 *
	 * @param frbrSecondGroup the new frbr second group
	 */
	public void setFrbrSecondGroup(String frbrSecondGroup) {
		this.frbrSecondGroup = frbrSecondGroup;
	}

	/**
	 * Gets the frbr third group.
	 *
	 * @return the frbr third group
	 */
	public String getFrbrThirdGroup() {
		return frbrThirdGroup;
	}

	/**
	 * Sets the frbr third group.
	 *
	 * @param frbrThirdGroup the new frbr third group
	 */
	public void setFrbrThirdGroup(String frbrThirdGroup) {
		this.frbrThirdGroup = frbrThirdGroup;
	}

	/**
	 * Gets the dao.
	 *
	 * @return the dao
	 */
	@Deprecated
	public HibernateUtil getDAO() {
		return null;
	}
}
