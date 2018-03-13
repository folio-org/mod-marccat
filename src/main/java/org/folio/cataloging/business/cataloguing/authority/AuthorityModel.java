package org.folio.cataloging.business.cataloguing.authority;

import org.folio.cataloging.business.cataloguing.common.Model;
import org.folio.cataloging.dao.AuthorityModelDAO;
import org.folio.cataloging.dao.common.HibernateUtil;


/**
 * Represents a Model/Template for initiating new Authority Items
 *
 * @author paulm
 * @since 1.0
 */
public class AuthorityModel extends Model {

	private String wemiSecondGroup;
	private String wemiThirdGroup;

	public AuthorityModel() {
		super();
	}

	public AuthorityModelDAO getAuthorityModelDAO() {
		return new AuthorityModelDAO();
	}

	public String getWemiSecondGroup() {
		return wemiSecondGroup;
	}

	public void setWemiSecondGroup(String wemiSecondGroup) {
		this.wemiSecondGroup = wemiSecondGroup;
	}

	public String getWemiThirdGroup() {
		return wemiThirdGroup;
	}

	public void setWemiThirdGroup(String wemiThirdGroup) {
		this.wemiThirdGroup = wemiThirdGroup;
	}

	@Override
	public HibernateUtil getDAO() {
		return null;
	}
}
