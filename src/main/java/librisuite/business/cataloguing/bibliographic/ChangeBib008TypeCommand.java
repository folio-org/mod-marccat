/*
 * (c) LibriCore
 * 
 * Created on Oct 15, 2004
 * 
 * Change008TypeCommand.java
 */
package librisuite.business.cataloguing.bibliographic;

import librisuite.bean.cataloguing.bibliographic.BibliographicEditBean;
import librisuite.bean.cataloguing.common.ModelBean;
import librisuite.business.common.LibrisuiteUtils;

import com.libricore.librisuite.common.Command;

/**
 * A command to change a leader where the resulting change also affects the 
 * form of material described in the 008 tag
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class ChangeBib008TypeCommand implements Command {
	private BibliographicLeader oldLeader;
	private BibliographicLeader newLeader;
	private MaterialDescription old008;
	private MaterialDescription new008;
	private BibliographicEditBean bean;
	private ModelBean modelBean;

	public ChangeBib008TypeCommand(BibliographicEditBean bean, BibliographicLeader ldr) {
		setBean(bean);
		setOldLeader((BibliographicLeader) bean.getCurrentTag());
		setNewLeader(ldr);
		setOld008(
			(MaterialDescription) bean
				.getBibliographicItem()
				.findFirstTagByNumber(
				"008"));
	}
	
	public ChangeBib008TypeCommand(librisuite.bean.cataloguing.common.ModelBean bean, BibliographicLeader ldr) {
		setBean(bean);
		setOldLeader((BibliographicLeader) bean.getCurrentField());
		setNewLeader(ldr);
		/*setOld008(
			(MaterialDescription) bean
				.getBibliographicItem()
				.findFirstTagByNumber(
				"008"));*/
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.Command#execute()
	 */
	public void execute() {
		getBean().getBibliographicItem().setTag(getOldLeader(), getNewLeader());
		setNew008((MaterialDescription)LibrisuiteUtils.deepCopy(getOld008()));
		getNew008().setItemEntity(getNewLeader().getItemEntity());
		getNew008().setToDefault();
		getBean().getBibliographicItem().setTag(getOld008(), getNew008());
		getBean().getBibliographicItem().setBibItmData((BIB_ITM)getNewLeader().getItemEntity());
		getNewLeader().markChanged();
		getNewLeader().getItemEntity().markChanged();
		getNew008().markChanged();
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.Command#reExecute()
	 */
	public void reExecute() {
		getBean().getBibliographicItem().setTag(getOldLeader(), getNewLeader());
		getBean().getBibliographicItem().setTag(getOld008(), getNew008());
		getBean().getBibliographicItem().setBibItmData((BIB_ITM)getNewLeader().getItemEntity());
		getNewLeader().markChanged();
		getNewLeader().getItemEntity().markChanged();
		getNew008().markChanged();
		int i = getBean().getBibliographicItem().getTags().indexOf(getNewLeader());
		getBean().setTagIndex(i);
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.Command#unExecute()
	 */
	public void unExecute() {
		getBean().getBibliographicItem().setTag(getNewLeader(), getOldLeader());
		getBean().getBibliographicItem().setTag(getNew008(), getOld008());
		getBean().getBibliographicItem().setBibItmData((BIB_ITM)getOldLeader().getItemEntity());
		getOldLeader().markChanged();
		getOld008().markChanged();
		getOldLeader().getItemEntity().markChanged();  // in case of change, save, undo, save
		int i = getBean().getBibliographicItem().getTags().indexOf(getOldLeader());
		getBean().setTagIndex(i);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public MaterialDescription getOld008() {
		return old008;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setOld008(MaterialDescription description) {
		old008 = description;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public BibliographicEditBean getBean() {
		return bean;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public MaterialDescription getNew008() {
		return new008;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public BibliographicLeader getNewLeader() {
		return newLeader;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public BibliographicLeader getOldLeader() {
		return oldLeader;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setBean(BibliographicEditBean bean) {
		this.bean = bean;
	}

	public void setBean(ModelBean modelBean) {
		this.modelBean = modelBean;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setNew008(MaterialDescription description) {
		new008 = description;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setNewLeader(BibliographicLeader leader) {
		newLeader = leader;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setOldLeader(BibliographicLeader leader) {
		oldLeader = leader;
	}

}
