/*
 * (c) LibriCore
 * 
 * Created on Aug 16, 2004
 * 
 * DeleteTagCommand.java
 */
package librisuite.business.cataloguing.bibliographic;

import librisuite.bean.cataloguing.common.EditBean;
import librisuite.business.cataloguing.common.Tag;

import com.libricore.librisuite.common.Command;

/**
 * @author paulm
 * @version $Revision: 1.10 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class DeleteTagCommand implements Command {
	private EditBean bean;
	private Tag tag;
	private int tagIndex;
	private int updateStatus;

	public DeleteTagCommand(EditBean bean){
		setBean(bean);
		setTag(bean.getCurrentTag());
		setTagIndex(bean.getTagIndex());
		setUpdateStatus(getTag().getUpdateStatus());	
	}
	
	/* (non-Javadoc)
	 * @see com.Command#execute()
	 */
	public void execute() {
		bean.getCatalogItem().removeTag(getTag());
		if (!getTag().isNew()){
			getTag().markDeleted();
			bean.getCatalogItem().addDeletedTag(getTag());
		}
		if (getTagIndex() > 0) {
			bean.setTagIndex(getTagIndex() - 1);
		}
	}

	/* (non-Javadoc)
	 * @see com.Command#reExecute()
	 */
	public void reExecute() {
		execute();
	}

	/* (non-Javadoc)
	 * @see com.Command#unExecute()
	 */
	public void unExecute() {
		if (getTag().isDeleted()) {
			bean.getCatalogItem().removeDeletedTag(getTag());
		} 
		else if (getTag().isRemoved()) {
			getTag().markNew();
		}
		else {
			getTag().setUpdateStatus(getUpdateStatus());
			/*
			 * If the originally deleted tag was "Unchanged" on the database, we need
			 * to change this to "Changed" so that the row will be re-added if required
			 */
			getTag().markChanged();
		}
		bean.getCatalogItem().addTag(getTagIndex() - 1, getTag());
		bean.setTagIndex(getTagIndex());
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
	public Tag getTag() {
		return tag;
	}

	/**
	 * 
	 */
	public int getTagIndex() {
		return tagIndex;
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
	public void setTag(Tag tag) {
		this.tag = tag;
	}

	/**
	 * 
	 */
	public void setTagIndex(int i) {
		tagIndex = i;
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

}
