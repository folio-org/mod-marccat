package org.folio.marccat.business.cataloguing.authority;

import java.io.Serializable;
import java.util.Iterator;

import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.cataloguing.common.TagImpl;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.ItemEntity;

import net.sf.hibernate.Session;

/**
 * @author elena
 *
 */
public class AuthorityItem extends CatalogItem implements Serializable {
	private static final long serialVersionUID = 8676099561229020012L;
	private AUT autItmData;

	public AuthorityItem() {
		super();
	}

	public AuthorityItem(Integer id) {
		super();
		this.autItmData.setAmicusNumber(id);
	}

	public AUT getAutItmData() {
		return autItmData;
	}

	public void setAutItmData(AUT autItm) {
		autItmData = autItm;
		/*
		 * Set all PersistsViaBibItem tags
		 */
		Iterator<?> iter = getTags().iterator();
		while (iter.hasNext()) {
			Tag aTag = (Tag) iter.next();
			if (aTag instanceof PersistsViaItem) {
				((PersistsViaItem) aTag).setItemEntity(autItm);
			}
		}
	}

	@Override
	public TagImpl getTagImpl() {
		return new AuthorityTagImpl();
	}

	public String getAuthorityType() {
		return getAutItmData().getHeadingType();
	}

	public ItemEntity getItemEntity() {
		return getAutItmData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see CatalogItem#setItemEntity(ItemEntity)
	 */
	public void setItemEntity(ItemEntity item) {
		setAutItmData((AUT) item);
	}

	@Override
	public int getUserView() {
		return View.AUTHORITY;
	}

	@Override
	public void checkForMandatoryTags(Session session) {
		// TODO Auto-generated method stub
		
	}

}
