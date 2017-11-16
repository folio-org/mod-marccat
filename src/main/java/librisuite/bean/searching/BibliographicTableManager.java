package librisuite.bean.searching;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import librisuite.business.authorisation.AuthorisationException;
import librisuite.business.common.DataAccessException;
import librisuite.form.searching.CodeTablesForm;
import net.sf.hibernate.HibernateException;

import org.apache.struts.action.ActionForm;

import com.libricore.librisuite.common.HibernateSessionProvider;

public class BibliographicTableManager implements ICodeTableManager {

	/* (non-Javadoc)
	 * @see librisuite.bean.searching.ITagManager#onBeforeLeavingCard(javax.servlet.http.HttpServletRequest)
	 */
	public void onBeforeLeavingCard(HttpServletRequest request) throws CodeTableException {
		// TODO: to implement this bibliographic method
	}

	/* (non-Javadoc)
	 * @see librisuite.bean.searching.ITagManager#onBeforeLeavingCopy(javax.servlet.http.HttpServletRequest)
	 */
	public void onBeforeLeavingCopy(HttpServletRequest request) throws DataAccessException, CodeTableException {
		// TODO: to implement this bibliographic method
	}

	/* (non-Javadoc)
	 * @see librisuite.bean.searching.ITagManager#initTable(librisuite.bean.searching.CodeTableEditBean, librisuite.form.searching.CodeTablesForm, java.util.Locale)
	 */
	public void initTable(HttpServletRequest request, CodeTableEditBean bean, ActionForm form, Locale currentLocale) throws CodeTableException, HibernateException, DataAccessException, ClassNotFoundException, AuthorisationException {
		String fieldName = ((CodeTablesForm) form).getCurrentFieldName();
		String tableName = bean.getCodeTableName(fieldName);
		Class clazz = HibernateSessionProvider.getInstance().getHibernateClassName(tableName);

		if (clazz != null) {
			bean.init(clazz, tableName, currentLocale);
		} else {
			throw new ClassNotFoundException(tableName);
		}
	}
	

	public void initEquivalentTable(HttpServletRequest request, CodeTableGridBean bean, ActionForm form, Locale currentLocale) throws CodeTableException, HibernateException, DataAccessException,
			ClassNotFoundException, AuthorisationException {
		String fieldName = ((CodeTablesForm) form).getCurrentFieldName();
		String tableName = bean.getCodeTableName(fieldName);
		Class clazz = HibernateSessionProvider.getInstance().getHibernateClassName(tableName);

		if (clazz != null) {
			bean.init(clazz, tableName, currentLocale);
		} else {
			throw new ClassNotFoundException(tableName);
		}
		
	}
}
