/*
 * (c) LibriCore
 * 
 * Created on Aug 16, 2004
 * 
 * NewTagCommand.java
 */
package librisuite.business.cataloguing.bibliographic;

import librisuite.bean.cataloguing.common.EditBean;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.UpdateStatus;

import com.libricore.librisuite.common.Command;

/**
 * @author paulm
 * @version $Revision: 1.9 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class NewTagCommand implements Command {	
	private EditBean bean;
	private int tagIndex;
	private Tag tag;

	public NewTagCommand(EditBean bean, int tagIndex, Tag t) {
		setBean(bean);
		setTagIndex(tagIndex);
		setTag(t);
	}
	
	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.Command#execute()
	 */
	public void execute() {
		bean.getCatalogItem().addTag(getTagIndex(), getTag());
		//int i = bean.getCatalogItem().getTags().indexOf(getTag());
		//bean.setTagIndex(i);
		bean.setTagIndex(getTagIndex()+1);
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.Command#reExecute()
	 */
	public void reExecute() {		
		bean.getCatalogItem().addTag(getTagIndex(), getTag());		
		int i = bean.getCatalogItem().getTags().indexOf(getTag());
		bean.setTagIndex(i);
		
		if (getTag().isRemoved()) {
			getTag().markNew();
		}
		else {			
			getTag().setUpdateStatus(UpdateStatus.CHANGED); // in case there have been saves and undo's
		}	
		bean.getCatalogItem().removeDeletedTag(getTag()); // in case the undo was saved
		bean.setNavigation(false);
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.Command#unExecute()
	 */
	public void unExecute() {		
		bean.getCatalogItem().removeTag(getTag());		
		bean.setTagIndex(getTagIndex());
		// if the new tag has already been saved we need to delete it		
		if (!getTag().isNew()){			
			bean.getCatalogItem().addDeletedTag(getTag());
			getTag().markDeleted();
		}
		bean.setNavigation(true);
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

}
