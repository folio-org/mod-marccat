/*
 * (c) LibriCore
 * 
 * Created on Jul 5, 2004
 * 
 * LibrisuiteBean.java
 */
package librisuite.bean;

import javax.servlet.http.HttpServletRequest;

import librisuite.bean.cataloguing.authority.AuthorityEditBean;
import librisuite.bean.cataloguing.bibliographic.BibliographicEditBean;
import librisuite.bean.cataloguing.common.EditBean;
import librisuite.bean.searching.SearchTypeBean;
import librisuite.business.authorisation.AuthorisationAgent;
import librisuite.business.authorisation.AuthorisationException;
import librisuite.business.authorisation.Permission;
import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.common.DataAccessException;
import librisuite.business.exception.DuplicateTagException;
import librisuite.business.exception.LibrisuiteException;
import librisuite.business.exception.RecordInUseException;
import librisuite.business.exception.ValidationException;

/**
 * This class can be used as super class for all beans used in LibriSuite. All
 * method which are common or related to these beans can be in this class
 * definition.
 * 
 * @author Wim Crols
 * @version $Revision: 1.8 $, $Date: 2006/12/14 10:31:06 $
 * @since 1.0
 */
public abstract class LibrisuiteBean {
	private AuthorisationAgent authorisationAgent;
	private boolean displayRecord;

	
	public void setSessionAttribute(
		HttpServletRequest httpServletRequest,
		Class aClass) {
		httpServletRequest.getSession(false).setAttribute(
			aClass.getName(),
			this);
	}

	public static LibrisuiteBean getSessionAttribute(
		HttpServletRequest httpServletRequest,
		Class aClass) {
		return (LibrisuiteBean) httpServletRequest.getSession(
			false).getAttribute(
			aClass.getName());
	}

	public static void removeSessionAttribute(
		HttpServletRequest httpServletRequest,
		Class aClass) {
		httpServletRequest.getSession(false).removeAttribute(aClass.getName());
	}

	public void setRequestAttribute(
		HttpServletRequest httpServletRequest,
		Class aClass)
		throws LibrisuiteException {
		httpServletRequest.setAttribute(aClass.getName(), this);
	}

	public static LibrisuiteBean getRequestAttribute(
		HttpServletRequest httpServletRequest,
		Class aClass)
		throws LibrisuiteException {
		return (LibrisuiteBean) httpServletRequest.getAttribute(
			aClass.getName());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public AuthorisationAgent getAuthorisationAgent() {
		return authorisationAgent;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAuthorisationAgent(AuthorisationAgent agent) {
		authorisationAgent = agent;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void checkPermission(Permission aPermission)
		throws AuthorisationException {
		authorisationAgent.checkPermission(aPermission);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void checkPermission(Permission[] somePermissions)
		throws AuthorisationException {
		authorisationAgent.checkPermission(somePermissions);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void checkPermission(String permissionName)
		throws AuthorisationException {
		authorisationAgent.checkPermission(permissionName);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void checkPermission(String[] someNames)
		throws AuthorisationException {
		authorisationAgent.checkPermission(someNames);
	}

//	public EditBean prepareItemForEditing(Object[] key, HttpServletRequest request)	throws DataAccessException, MarcCorrelationException, RecordInUseException
	public EditBean prepareItemForEditingTransfert(int recordView, Object[] key, HttpServletRequest request)	throws DataAccessException, MarcCorrelationException, RecordInUseException
	{
		EditBean editBean;
//		if (SearchTypeBean.getInstance(request).getSearchingView() > 0) {
		if (recordView > 0) {
			editBean = BibliographicEditBean.getInstance(request);
		} else {
			editBean = AuthorityEditBean.getInstance(request);
		}
		//editBean.loadItem(key);
		editBean.loadItemWithoutLock(key);

		return editBean;
	}
	
	public EditBean prepareItemForEditingDuplicate(
			Object[] key,
			HttpServletRequest request)
			throws DataAccessException, MarcCorrelationException, RecordInUseException {

			EditBean editBean;
			if (SearchTypeBean.getInstance(request).getSearchingView() > 0) {
				editBean = BibliographicEditBean.getInstance(request);
			} else {
				editBean = AuthorityEditBean.getInstance(request);
			}
			editBean.loadItemDuplicate(key);

			return editBean;
		}
	public boolean isDisplayRecord() {
		return displayRecord;
	}

	public void setDisplayRecord(boolean displayRecord) {
		this.displayRecord = displayRecord;
	}
	
	public EditBean prepareItemForEditing(int recordView, Object[] key,	HttpServletRequest request) throws DataAccessException, MarcCorrelationException, RecordInUseException, ValidationException, AuthorisationException, DuplicateTagException 
	{
		EditBean editBean;
		
		if (recordView > 0) {
			editBean = BibliographicEditBean.getInstance(request);
		} else {
			editBean = AuthorityEditBean.getInstance(request);
		}
		editBean.prepareItemForEditing(recordView, key);

		return editBean;
	}
	
	public EditBean prepareItemForVisualizeCodes(int recordView, Object[] key,	HttpServletRequest request) throws DataAccessException, MarcCorrelationException, RecordInUseException, ValidationException, AuthorisationException, DuplicateTagException 
	{
		EditBean editBean;
		
		if (recordView > 0) {
			editBean = BibliographicEditBean.getInstance(request);
		} else {
			editBean = AuthorityEditBean.getInstance(request);
		}
		editBean.prepareItemForVisualizeCodes(recordView, key);

		return editBean;
	}
	

	
}