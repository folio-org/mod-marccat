/*
 * (c) LibriCore
 * 
 * Created on Aug 16, 2004
 * 
 * ChangeValuesCommand.java
 */
package librisuite.business.cataloguing.bibliographic;

import librisuite.bean.cataloguing.common.EditBean;
import librisuite.business.cataloguing.common.Browsable;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.libricore.librisuite.common.Command;

/**
 * @author paulm
 * @version $Revision: 1.15 $, $Date: 2006/01/11 13:36:23 $
 * @since 1.0
 */
public class ChangeValuesIndicatorCommand implements Command {
	private static Log logger = LogFactory.getLog(ChangeValuesCommand.class);
	private CorrelationValues oldValues;
	private CorrelationValues newValues;
	private EditBean bean;
	private Tag currentTag;
	private Tag newTag;
	private int updateStatus;

	/*
	 * Need to treat as a delete of old tag and new of the appropriate type
	 */
	public ChangeValuesIndicatorCommand(EditBean bean, CorrelationValues v) {
		setBean(bean);
		oldValues = bean.getCurrentTag().getCorrelationValues();
		newValues = v;
		setCurrentTag(bean.getCurrentTag());
		setNewTag(null);
		setUpdateStatus(getCurrentTag().getUpdateStatus());
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.Command#execute()
	 */
	public void execute() throws DataAccessException {
		/*
		 * Changes in correlation values may result in changes to keys.  If this is
		 * the case then a new tag needs to be created (with the new key value) and the
		 * old tag needs to be deleted.  However, if no change in key is required, then 
		 * the existing tag can (must) be updated.
		 * 
		 * The strategy here is to first copy the tag and carry out the changes on the
		 * copy.  Then test the resulting changes with the original tag to see if the
		 * key would be changed.  Then take the appropriate actions.
		 */
		Tag tag = (Tag) getCurrentTag().clone();

		/*
		 * When the user changes the first correlation value, the lists for 2nd 
		 * and 3rd values need to be reset to appropriate values for value1, 
		 * then the tags settings for 2 and 3 need to be adjusted to the default 
		 * for these new reset values.
		 * 
		 * Similarly when value2 is changed, list 3 needs adjusting and then 
		 * the tag setting for value3
		 */

		if (newValues.isValueDefined(1) && getOldValue1() != getNewValue1()) {
			/*
			 * The user has changed the value1 correlation
			 */
//			logger.debug(
//				"changing value 1 from "
//					+ getOldValue1()
//					+ " to "
//					+ getNewValue1());
			try {
				tag.updateFirstCorrelation(getNewValue1());
			} catch (DataAccessException e) {
				setNewValue2((short) - 1);
				setNewValue3((short) - 1);
			}
		} if (
			newValues.isValueDefined(2) /*&& getOldValue2() != getNewValue2()*/) {
			/*
			 * The user has changed value2
			 */
//			logger.debug(
//				"changing value 2 from "
//					+ getOldValue2()
//					+ " to "
//					+ getNewValue2());
			try {
				tag.updateSecondCorrelation(getNewValue2());
			} catch (DataAccessException e) {
				setNewValue3((short) - 1);
			}
		} if (
			newValues.isValueDefined(3) /*&& getOldValue3() != getNewValue3()*/) {
			/*
			 * The user has changed value3
			 */
//			logger.debug(
//				"changing value 3 from "
//					+ getOldValue3()
//					+ " to "
//					+ getNewValue3());
			tag.setCorrelation(3, getNewValue3());
		}
		newValues = tag.getCorrelationValues();
//		logger.info("new values are " + newValues);

		/* now we have made the required changes to the duplicate
		 * so, see if we can just do an update or if we need to do
		 * a delete and an insert
		 */

		//TODO need to worry about the case where the user makes a change resulting in a new
		//     key and then makes a subsequent change back to the original key (rare but possible)

		logger.debug("done the correlation changes to tag");
		if (getCurrentTag().correlationChangeAffectsKey(newValues)) {
			// we need to delete and insert
//			logger.debug("new tag required");
			try {
				tag =
					getBean().getCatalog().getNewTag(
						getBean().getCatalogItem(),
						tag.getCategory(),
						newValues);
//				logger.debug("new tag is " + tag);
			} catch (NewTagException e) {
				throw new RuntimeException("error creating new tag object");
			}
			if (!getCurrentTag().isNew()) {
				getBean().getCatalogItem().addDeletedTag(getCurrentTag());
				getCurrentTag().markDeleted();
				getCurrentTag().evict();
			}
			// keep the old descriptor in the new tag
			if (tag instanceof Browsable
				&& getCurrentTag() instanceof Browsable) {
				((Browsable) tag).setDescriptor(
					((Browsable) getCurrentTag()).getDescriptor());
			}
			setNewTag(tag);
			bean.getCatalogItem().setTag(getCurrentTag(), getNewTag());
		} else {
//			logger.debug("no new tag required, doing an update");
			// we can do an update		
			getCurrentTag().setCorrelationValues(newValues);
			getCurrentTag().markChanged();
		}
		logger.debug("returning from execute");
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.Command#reExecute()
	 */
	public void reExecute() throws DataAccessException {
		/*
		 * To redo we either re-delete the old tag or
		 * re-establish the new correlation values
		 */

		int i = bean.getCatalogItem().getTags().indexOf(getCurrentTag());
		bean.setTagIndex(i);

		if (getNewTag() != null) {
			getBean().getCatalogItem().setTag(getCurrentTag(), getNewTag());
			getBean().getCatalogItem().addDeletedTag(getCurrentTag());
			getCurrentTag().markDeleted();
			getCurrentTag().evict();
			getNewTag().setCorrelationValues(newValues);
		} else {
			getCurrentTag().setCorrelationValues(newValues);
			getCurrentTag().markChanged();
		}
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.Command#unExecute()
	 */
	public void unExecute() throws DataAccessException {
		/*
		 * To undo we either need to bring back the deleted tag or not
		 * and then re-establish the original correlation values
		 * 
		 */

		if (getNewTag() != null) {
			bean.getCatalogItem().setTag(getNewTag(), getCurrentTag());
			bean.getCatalogItem().removeDeletedTag(getCurrentTag());
			getNewTag().evict();
		}
		/**
		 * In both cases we need to reestablish the old correlation values since
		 * the newTag still could have had the same descriptor as the old tag
		 */
		getCurrentTag().setCorrelationValues(oldValues);
		getCurrentTag().setUpdateStatus(getUpdateStatus());
		int i = bean.getCatalogItem().getTags().indexOf(getCurrentTag());
		bean.setTagIndex(i);
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
	public short getNewValue1() {
		return newValues.getValue(1);
	}

	/**
	 * 
	 */
	public short getNewValue2() {
		return newValues.getValue(2);
	}

	/**
	 * 
	 */
	public short getNewValue3() {
		return newValues.getValue(3);
	}

	/**
	 * 
	 */
	public short getOldValue1() {
		return oldValues.getValue(1);
	}

	/**
	 * 
	 */
	public short getOldValue2() {
		return oldValues.getValue(2);
	}

	/**
	 * 
	 */
	public short getOldValue3() {
		return oldValues.getValue(3);
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
	public void setNewValue1(short s) {
		newValues = newValues.change(1, s);
	}

	/**
	 * 
	 */
	public void setNewValue2(short s) {
		newValues = newValues.change(2, s);
	}

	/**
	 * 
	 */
	public void setNewValue3(short s) {
		newValues = newValues.change(3, s);
	}

	/**
	 * 
	 */
	public void setOldValue1(short s) {
		oldValues = oldValues.change(1, s);
	}

	/**
	 * 
	 */
	public void setOldValue2(short s) {
		oldValues = oldValues.change(2, s);
	}

	/**
	 * 
	 */
	public void setOldValue3(short s) {
		oldValues = oldValues.change(3, s);
	}

	/**
	 * 
	 */
	public Tag getCurrentTag() {
		return currentTag;
	}

	/**
	 * 
	 */
	public void setCurrentTag(Tag tag) {
		currentTag = tag;
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
	public void setNewTag(Tag tag) {
		newTag = tag;
	}

}
