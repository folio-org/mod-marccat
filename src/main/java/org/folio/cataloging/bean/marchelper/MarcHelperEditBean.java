package org.folio.cataloging.bean.marchelper;

import java.util.HashMap;
import java.util.Map;

import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.business.marchelper.MarcHelperManager;
import org.folio.cataloging.business.marchelper.TagModelItem;

import org.folio.cataloging.util.StringText;

public class MarcHelperEditBean extends LibrisuiteBean {

	private TagModelItem item = null;
	
	private MarcHelperManager manager = null;
	private Map tempValues = new HashMap();

	public MarcHelperEditBean(MarcHelperManager manager) {
		super();
		this.manager = manager;
	}

	public void refreshItem(Map newValues){
		item.refresh(newValues);
	}

	public MarcHelperManager getManager() {
		return manager;
	}

	public TagModelItem getItem() {
		return item;
	}

	public void setItem(TagModelItem item) {
		this.item = item;
	}
	
	public boolean isItemAvailable(){
		return item!=null;
	}

	/**
	 * @return
	 */
	public Map getTempValues() {
		return tempValues;
	}

	public void setTempValues(Map tempValues) {
		this.tempValues = tempValues;
		if(item!=null) {
			refreshItem(tempValues);
		}
	}

	public void reset() {
		item = null;
		tempValues = new HashMap();
	}
	
	public StringText getStringText(){
		return item.getStringText();
	}
}
