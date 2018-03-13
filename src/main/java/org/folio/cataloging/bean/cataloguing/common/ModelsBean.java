/*
 * (c) LibriCore
 * 
 * Created on Aug 2, 2004
 * 
 * $Author: Paulm $
 * $Date: 2006/01/13 16:23:36 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.2 $
 * $Source: /source/LibriSuite/src/librisuite/bean/cataloguing/common/ModelsBean.java,v $
 * $State: Exp $
 */
package org.folio.cataloging.bean.cataloguing.common;

import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicCatalog;
import org.folio.cataloging.business.cataloguing.common.Catalog;
import org.folio.cataloging.business.cataloguing.common.Model;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.controller.SessionUtils;
import org.folio.cataloging.business.controller.UserProfile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Wim Crols
 */
public class ModelsBean extends LibrisuiteBean {

	private Catalog catalog = new BibliographicCatalog(); // default setting

	private String model = null;
	
	private UserProfile userProfile;

	// FIXME SESSION + USER PROFILE!
	public static ModelsBean getInstance(HttpServletRequest request) {
		ModelsBean bean = (ModelsBean) ModelsBean
				.getSessionAttribute(request, ModelsBean.class);
		if (bean == null) {
			bean = new ModelsBean();
			bean.setSessionAttribute(request, bean.getClass());
			
		}
		bean.setUserProfile(SessionUtils.getUserProfile(
				request.getSession(false)));
		return bean;
	}

	/**
	 * Getter for models
	 */
	public List getAvailableModels() throws DataAccessException {
		throw new IllegalArgumentException("Don't call me!");
//		return getCatalog().getModelDAO().getModelList();
	}

	public Model load(int modelId) throws DataAccessException {
		return getCatalog().getModelDAO().load(modelId);
	}

	public void delete(Model model) throws DataAccessException {
		//getCatalog().getModelDAO().delete(model);
	}

	/**
	 * @since 1.0
	 */
	public String getModel() {
		return model;
	}


	/**
	 * Retrieves a boolean representing whether a model in the list of all
	 * models is currently in use by a bib item.
	 *
	 * @return a boolean representing whether a model in the list of all models is currently in use by a bib item.
	 * @throws DataAccessException in case of data access failure.
	 */
	public List getLinkedToItems() throws DataAccessException {
		//return getCatalog().getModelDAO().getModelUsageList();
		return null;
	}

	/**
	 * @since 1.0
	 */
	public void setModel(String string) {
		model = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Catalog getCatalog() {
		return catalog;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public Integer getDefaultAuthorityModel() {
		return userProfile.getDefaultAuthorityModel();
	}

	public Integer getDefaultBibliographicModel() {
		return userProfile.getDefaultBibliographicModel();
	}

    public void updateDefaultBibModel(Integer defaultBibModel) throws DataAccessException{
    	userProfile.setDefaultBibliographicModel(defaultBibModel);
    	userProfile.persistAccountSettings();
    }	
    
    public void updateDefaultAutModel(Integer defaultAutModel) throws DataAccessException{
    	userProfile.setDefaultAuthorityModel(defaultAutModel);
    	userProfile.persistAccountSettings();
    }

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}	

}