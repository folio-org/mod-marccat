/*
 * (c) LibriCore
 * 
 * Created on Dec 6, 2004
 * 
 * S_CACHE_BIB_ITM_DSPLY.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class Cache implements Persistence {
private int bibItemNumber;
private short cataloguingView;
private char formOfItemCode;
private String languageOfCataloguingCode;
private char itemRecordTypeCode;
private boolean relationshipCode;
private boolean locationCode;
private String titleVolumeNumberDescription;
private String Date1;
private String Date2;
private String titleHeadingMainSortForm;
private String mainEntrySortForm;
private String codeTableAbreviatedName;
private String nameMainEntryStringText;
private String titleHeadingUniformStringText;
private String titleHeadingMainStringText;
private String editionStrngTxt;
private String publisherStrngTxt;
private String collationStrngTxt;
private String titleHeadingSeriesStringText;
private PersistenceState persistenceState = new PersistenceState();
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public Cache() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Cache(int amicusNumber, short cataloguingView) {
		this();
		this.bibItemNumber = amicusNumber;
		this.cataloguingView = cataloguingView;
	}
/**
 * 
 * @since 1.0
 */
public int getBibItemNumber() {
	return bibItemNumber;
}

/**
 * 
 * @since 1.0
 */
public short getCataloguingView() {
	return cataloguingView;
}

/**
 * 
 * @since 1.0
 */
public PersistenceState getPersistenceState() {
	return persistenceState;
}

/**
 * 
 * @since 1.0
 */
public void setBibItemNumber(int i) {
	bibItemNumber = i;
}

/**
 * 
 * @since 1.0
 */
public void setCataloguingView(short s) {
	cataloguingView = s;
}

/**
 * 
 * @since 1.0
 */
public void setPersistenceState(PersistenceState state) {
	persistenceState = state;
}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 instanceof Cache) {
			Cache s = (Cache) arg0;
			return getBibItemNumber() == s.getBibItemNumber() &&
			getCataloguingView() == s.getCataloguingView();
		}
		return false;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void evict(Object obj) throws DataAccessException {
		persistenceState.evict(obj);
	}

	public void evict() throws DataAccessException {
		evict((Object)this);
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public HibernateUtil getDAO() {
		return persistenceState.getDAO();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getBibItemNumber() + getCataloguingView();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isChanged() {
		return persistenceState.isChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isDeleted() {
		return persistenceState.isDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isNew() {
		return persistenceState.isNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isRemoved() {
		return persistenceState.isRemoved();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markChanged() {
		persistenceState.markChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markDeleted() {
		persistenceState.markDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markNew() {
		persistenceState.markNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markUnchanged() {
		persistenceState.markUnchanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onDelete(Session arg0) throws CallbackException {
		return persistenceState.onDelete(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void onLoad(Session arg0, Serializable arg1) {
		persistenceState.onLoad(arg0, arg1);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onSave(Session arg0) throws CallbackException {
		return persistenceState.onSave(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onUpdate(Session arg0) throws CallbackException {
		return persistenceState.onUpdate(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		// not applicable for this class

	}

/**
 * 
 * @since 1.0
 */
public String getCodeTableAbreviatedName() {
	return codeTableAbreviatedName;
}

/**
 * 
 * @since 1.0
 */
public String getCollationStrngTxt() {
	return collationStrngTxt;
}

/**
 * 
 * @since 1.0
 */
public String getDate1() {
	return Date1;
}

/**
 * 
 * @since 1.0
 */
public String getDate2() {
	return Date2;
}

/**
 * 
 * @since 1.0
 */
public String getEditionStrngTxt() {
	return editionStrngTxt;
}

/**
 * 
 * @since 1.0
 */
public char getFormOfItemCode() {
	return formOfItemCode;
}

/**
 * 
 * @since 1.0
 */
public char getItemRecordTypeCode() {
	return itemRecordTypeCode;
}

/**
 * 
 * @since 1.0
 */
public String getLanguageOfCataloguingCode() {
	return languageOfCataloguingCode;
}

/**
 * 
 * @since 1.0
 */
public boolean isLocationCode() {
	return locationCode;
}

/**
 * 
 * @since 1.0
 */
public String getMainEntrySortForm() {
	return mainEntrySortForm;
}

/**
 * 
 * @since 1.0
 */
public String getNameMainEntryStringText() {
	return nameMainEntryStringText;
}

/**
 * 
 * @since 1.0
 */
public String getPublisherStrngTxt() {
	return publisherStrngTxt;
}

/**
 * 
 * @since 1.0
 */
public boolean isRelationshipCode() {
	return relationshipCode;
}

/**
 * 
 * @since 1.0
 */
public String getTitleHeadingMainSortForm() {
	return titleHeadingMainSortForm;
}

/**
 * 
 * @since 1.0
 */
public String getTitleHeadingMainStringText() {
	return titleHeadingMainStringText;
}

/**
 * 
 * @since 1.0
 */
public String getTitleHeadingSeriesStringText() {
	return titleHeadingSeriesStringText;
}

/**
 * 
 * @since 1.0
 */
public String getTitleHeadingUniformStringText() {
	return titleHeadingUniformStringText;
}

/**
 * 
 * @since 1.0
 */
public String getTitleVolumeNumberDescription() {
	return titleVolumeNumberDescription;
}

/**
 * 
 * @since 1.0
 */
public void setCodeTableAbreviatedName(String string) {
	codeTableAbreviatedName = string;
}

/**
 * 
 * @since 1.0
 */
public void setCollationStrngTxt(String string) {
	collationStrngTxt = string;
}

/**
 * 
 * @since 1.0
 */
public void setDate1(String string) {
	Date1 = string;
}

/**
 * 
 * @since 1.0
 */
public void setDate2(String string) {
	Date2 = string;
}

/**
 * 
 * @since 1.0
 */
public void setEditionStrngTxt(String string) {
	editionStrngTxt = string;
}

/**
 * 
 * @since 1.0
 */
public void setFormOfItemCode(char c) {
	formOfItemCode = c;
}

/**
 * 
 * @since 1.0
 */
public void setItemRecordTypeCode(char c) {
	itemRecordTypeCode = c;
}

/**
 * 
 * @since 1.0
 */
public void setLanguageOfCataloguingCode(String string) {
	languageOfCataloguingCode = string;
}

/**
 * 
 * @since 1.0
 */
public void setLocationCode(boolean b) {
	locationCode = b;
}

/**
 * 
 * @since 1.0
 */
public void setMainEntrySortForm(String string) {
	mainEntrySortForm = string;
}

/**
 * 
 * @since 1.0
 */
public void setNameMainEntryStringText(String string) {
	nameMainEntryStringText = string;
}

/**
 * 
 * @since 1.0
 */
public void setPublisherStrngTxt(String string) {
	publisherStrngTxt = string;
}

/**
 * 
 * @since 1.0
 */
public void setRelationshipCode(boolean b) {
	relationshipCode = b;
}

/**
 * 
 * @since 1.0
 */
public void setTitleHeadingMainSortForm(String string) {
	titleHeadingMainSortForm = string;
}

/**
 * 
 * @since 1.0
 */
public void setTitleHeadingMainStringText(String string) {
	titleHeadingMainStringText = string;
}

/**
 * 
 * @since 1.0
 */
public void setTitleHeadingSeriesStringText(String string) {
	titleHeadingSeriesStringText = string;
}

/**
 * 
 * @since 1.0
 */
public void setTitleHeadingUniformStringText(String string) {
	titleHeadingUniformStringText = string;
}

/**
 * 
 * @since 1.0
 */
public void setTitleVolumeNumberDescription(String string) {
	titleVolumeNumberDescription = string;
}

}
