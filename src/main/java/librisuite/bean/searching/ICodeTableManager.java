package librisuite.bean.searching;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import librisuite.business.authorisation.AuthorisationException;
import librisuite.business.common.DataAccessException;
import librisuite.form.LibrisuiteForm;
import net.sf.hibernate.HibernateException;


public interface ICodeTableManager {

	/**
	 * Preserve CardForm data before exiting cataloguing page
	 */
	public abstract void onBeforeLeavingCard(HttpServletRequest request) throws CodeTableException;

	/**
	 * Preserve Copy data before exiting Copy page
	 */
	public abstract void onBeforeLeavingCopy(HttpServletRequest request) throws DataAccessException, CodeTableException;

	public abstract void initTable(HttpServletRequest request, CodeTableEditBean bean, LibrisuiteForm form, Locale currentLocale) throws CodeTableException, HibernateException, DataAccessException, ClassNotFoundException,
			AuthorisationException;

	public abstract void initEquivalentTable(HttpServletRequest request, CodeTableGridBean bean, LibrisuiteForm form, Locale currentLocale) throws CodeTableException, HibernateException, DataAccessException, ClassNotFoundException,
	AuthorisationException;

}