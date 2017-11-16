/*
 * (c) LibriCore
 * 
 * Created on Aug 14, 2004
 * 
 * ReplaceTagCommand.java
 */
package librisuite.business.cataloguing.bibliographic;

import librisuite.bean.cataloguing.common.EditBean;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.DataAccessException;

import com.libricore.librisuite.common.Command;

/**
 * @author paulm
 * @version $Revision: 1.8 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class ReplaceTagCommand implements Command {

	private Tag oldTag;
	private Tag newTag;
	private EditBean bean;
	private int oldUpdateStatus;
	
	public ReplaceTagCommand(EditBean bean, Tag oldTag, Tag newTag){
		setBean(bean);
		setOldTag(oldTag);
		setNewTag(newTag);
		setOldUpdateStatus(getOldTag().getUpdateStatus());
	}
	
	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.Command#execute()
	 */
	public void execute() throws DataAccessException {
		if (!oldTag.isNew()) {
			oldTag.markDeleted();
			oldTag.evict();
			bean.getCatalogItem().addDeletedTag(oldTag);
		}
		bean.getCatalogItem().setTag(oldTag, newTag);
		getNewTag().markChanged();
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.Command#reExecute()
	 */
	public void reExecute() throws DataAccessException {
		execute();
		int i = bean.getCatalogItem().getTags().indexOf(newTag);
		bean.setTagIndex(i);
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.Command#unExecute()
	 */
	public void unExecute() throws DataAccessException {
		bean.getCatalogItem().setTag(newTag, oldTag);
		int i = bean.getCatalogItem().getTags().indexOf(oldTag);
		bean.setTagIndex(i);
		if (oldTag.isDeleted()){
			bean.getCatalogItem().removeDeletedTag(oldTag);
		}
		getNewTag().evict();
		oldTag.setUpdateStatus(getOldUpdateStatus());
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
	public Tag getOldTag() {
		return oldTag;
	}

	/**
	 * 
	 */
	public void setNewTag(Tag tag) {
		newTag = tag;
	}

	/**
	 * 
	 */
	public void setOldTag(Tag tag) {
		oldTag = tag;
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
	public int getOldUpdateStatus() {
		return oldUpdateStatus;
	}

	/**
	 * 
	 */
	public void setOldUpdateStatus(int i) {
		oldUpdateStatus = i;
	}

}
