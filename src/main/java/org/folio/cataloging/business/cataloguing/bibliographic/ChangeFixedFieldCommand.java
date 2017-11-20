/*
 * (c) LibriCore
 * 
 * Created on Oct 4, 2004
 * 
 * ChangeFixedFieldCommand.java
 */
package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.bean.cataloguing.common.EditBean;
import org.folio.cataloging.bean.cataloguing.common.ModelBean;
import org.folio.cataloging.business.common.DataAccessException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.folio.cataloging.business.Command;

/**
 * @author paulm
 * @version $Revision: 1.7 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class ChangeFixedFieldCommand implements Command {
	private static Log logger = LogFactory
			.getLog(ChangeFixedFieldCommand.class);
	private EditBean bean;
	private ModelBean modelBean;
	private FixedFieldUsingItemEntity oldTag;
	private FixedFieldUsingItemEntity newTag;

	public ChangeFixedFieldCommand(EditBean bean, FixedFieldUsingItemEntity ff) {
		setBean(bean);
		setOldTag((FixedFieldUsingItemEntity) bean.getCurrentTag());
		setNewTag(ff);
	}

	public ChangeFixedFieldCommand(ModelBean bean, FixedFieldUsingItemEntity ff) {
		setBean(bean);
		setOldTag((FixedFieldUsingItemEntity) bean.getCurrentField());
		setNewTag(ff);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.folio.cataloging.business.Command#execute()
	 */
	public void execute() throws DataAccessException {
		logger.debug("execute:");
		logger.debug("  old tag is: " + getOldTag());
		logger.debug("  new tag is: " + getNewTag());
		getBean().getCatalogItem().setTag(getOldTag(), getNewTag());
		getBean().getCatalogItem().setItemEntity(getNewTag().getItemEntity());
		getNewTag().markChanged();
		getNewTag().getItemEntity().markChanged();
		getOldTag().evict();
		if (getOldTag().getItemEntity() != getNewTag().getItemEntity()) {
			getOldTag().getItemEntity().evict();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.folio.cataloging.business.Command#reExecute()
	 */
	public void reExecute() throws DataAccessException {
		execute();
		int i = getBean().getCatalogItem().getTags().indexOf(getNewTag());
		getBean().setTagIndex(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.folio.cataloging.business.Command#unExecute()
	 */
	public void unExecute() throws DataAccessException {
		getBean().getCatalogItem().setTag(getNewTag(), getOldTag());
		getBean().getCatalogItem().setItemEntity(getOldTag().getItemEntity());
		getOldTag().markChanged();
		getOldTag().getItemEntity().markChanged();
		// in case of change, save, undo, save
		int i = getBean().getCatalogItem().getTags().indexOf(getOldTag());
		getBean().setTagIndex(i);
		getNewTag().evict();
		getNewTag().getItemEntity().evict();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public EditBean getBean() {
		return bean;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public FixedFieldUsingItemEntity getNewTag() {
		return newTag;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public FixedFieldUsingItemEntity getOldTag() {
		return oldTag;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setBean(EditBean bean) {
		this.bean = bean;
	}

	public void setBean(ModelBean modelBean) {
		this.modelBean = modelBean;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setNewTag(FixedFieldUsingItemEntity tag) {
		newTag = tag;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setOldTag(FixedFieldUsingItemEntity tag) {
		oldTag = tag;
	}

}
