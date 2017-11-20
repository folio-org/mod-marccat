/*
 * (c) LibriCore
 * 
 * Created on Aug 16, 2004
 * 
 * ReplaceDescriptorCommand.java
 */
package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.bean.cataloguing.common.EditBean;
import org.folio.cataloging.business.cataloguing.common.Browsable;
import org.folio.cataloging.business.cataloguing.common.CatalogItem;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.descriptor.Descriptor;

import org.folio.cataloging.business.Command;

/**
 * @author paulm
 * @version $Revision: 1.10 $, $Date: 2007/02/13 09:17:59 $
 * @since 1.0
 */
public class ReplaceDescriptorCommand implements Command {

	private EditBean bean;
	private Tag tag;
	private Tag newTag;
	private Descriptor oldDescriptor;
	private Descriptor newDescriptor;
	private int updateStatus;
	
	public ReplaceDescriptorCommand(EditBean bean, Tag tag, Descriptor d) {

		setBean(bean);
		setTag(tag);
		setOldDescriptor(((Browsable) tag).getDescriptor());
		setNewDescriptor(d);
		setUpdateStatus(tag.getUpdateStatus());
	}

	/* (non-Javadoc)
	 * @see org.folio.cataloging.business.Command#execute()
	 */
	public void execute() throws DataAccessException {
		CatalogItem catalogItem = bean.getCatalogItem();
		Tag srcTag = getTag();
		Descriptor replacingDescriptor = getNewDescriptor();
		
		Tag newTag = MarcCommandLibrary.replaceDescriptor(catalogItem, srcTag, replacingDescriptor);

		setNewTag(newTag);
	}

	/* (non-Javadoc)
	 * @see org.folio.cataloging.business.Command#reExecute()
	 */
	public void reExecute() throws DataAccessException{
		execute();
		int i = bean.getCatalogItem().getTags().indexOf(getNewTag());
		bean.setTagIndex(i);
	}

	/* (non-Javadoc)
	 * @see org.folio.cataloging.business.Command#unExecute()
	 */
	public void unExecute() {
		bean.getCatalogItem().setTag(getNewTag(), getTag());
		if (getTag().isDeleted()) {
			bean.getCatalogItem().removeDeletedTag(getTag());
		}
		getTag().setUpdateStatus(getUpdateStatus());
		int i = bean.getCatalogItem().getTags().indexOf(getTag());
		bean.setTagIndex(i);
	}

	/**
	 * 
	 */
	public Descriptor getNewDescriptor() {
		return newDescriptor;
	}

	/**
	 * 
	 */
	public Descriptor getOldDescriptor() {
		return oldDescriptor;
	}

	/**
	 * 
	 */
	public Tag getTag() {
		return tag;
	}

	/**
	 * 
	 */
	public void setNewDescriptor(Descriptor descriptor) {
		newDescriptor = descriptor;
	}

	/**
	 * 
	 */
	public void setOldDescriptor(Descriptor descriptor) {
		oldDescriptor = descriptor;
	}

	/**
	 * 
	 */
	public void setTag(Tag point) {
		tag = point;
	}

	/**
	 * 
	 */
	public EditBean getBean() {
		return bean;
	}

	/**
	 * 
	 */
	public void setBean(EditBean bean) {
		this.bean = bean;
	}

	/**
	 * 
	 */
	public int getUpdateStatus() {
		return updateStatus;
	}

	/**
	 * 
	 */
	public void setUpdateStatus(int i) {
		updateStatus = i;
	}

	/**
	 * 
	 */
	public Tag getNewTag() {
		return newTag;
	}

	/**
	 * 
	 */
	public void setNewTag(Tag point) {
		newTag = point;
	}

}
