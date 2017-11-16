package librisuite.bean.searching;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import librisuite.business.authorisation.AuthorisationException;
import librisuite.business.common.DataAccessException;
import net.sf.hibernate.HibernateException;

import org.apache.struts.action.ActionForm;

public interface ICodeTableManager {

	/**
	 * Preserve CardForm data before exiting cataloguing page
	 */
	public abstract void onBeforeLeavingCard(HttpServletRequest request) throws CodeTableException;

	/**
	 * Preserve Copy data before exiting Copy page
	 */
	public abstract void onBeforeLeavingCopy(HttpServletRequest request) throws DataAccessException, CodeTableException;

	public abstract void initTable(HttpServletRequest request, CodeTableEditBean bean, ActionForm form, Locale currentLocale) throws CodeTableException, HibernateException, DataAccessException, ClassNotFoundException,
			AuthorisationException;

	public abstract void initEquivalentTable(HttpServletRequest request, CodeTableGridBean bean, ActionForm form, Locale currentLocale) throws CodeTableException, HibernateException, DataAccessException, ClassNotFoundException,
	AuthorisationException;

}