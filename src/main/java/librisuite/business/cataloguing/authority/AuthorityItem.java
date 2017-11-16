/*
 * (c) LibriCore
 * 
 * Created on Oct 27, 2005
 * 
 * AuthorityItem.java
 */
package librisuite.business.cataloguing.authority;

import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.common.CatalogItem;
import librisuite.business.cataloguing.common.ItemEntity;
import librisuite.business.cataloguing.common.Model;
import librisuite.business.cataloguing.common.ModelItem;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.cataloguing.common.TagImpl;
import librisuite.business.common.DataAccessException;
import librisuite.business.exception.MandatoryTagException;
import librisuite.hibernate.AUT;

/**
 * @author paulm
 * @version $Revision: 1.5 $, $Date: 2006/05/11 10:51:29 $
 * @since 1.0
 */
public class AuthorityItem extends CatalogItem {

	private static final Log logger = LogFactory.getLog(AuthorityItem.class);
	private AUT autItmData;
	
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AuthorityItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @since 1.0
	 */
	public AUT getAutItmData() {
		return autItmData;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.CatalogItem#getItemEntity()
	 */
	public ItemEntity getItemEntity() {
		return getAutItmData();
	}

	/**
	 * @since 1.0
	 */
	public ModelItem getModelItem() {
		return modelItem;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.CatalogItem#getTagImpl()
	 */
	public TagImpl getTagImpl() {
		return new AuthorityTagImpl();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAutItmData(AUT aut) {
		autItmData = aut;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.CatalogItem#setItemEntity(librisuite.business.cataloguing.common.ItemEntity)
	 */
	public void setItemEntity(ItemEntity item) {
		setAutItmData((AUT)item);
	}

	/**
	 * @since 1.0
	 */
	public void setModelItem(Model model) {
		this.modelItem = new AuthorityModelItem();
		this.modelItem.markNew();
		this.modelItem.setItem(this.getAmicusNumber().longValue());
		this.modelItem.setModel(model);
		this.modelItem.setXmlFields(
			model.getXmlFields());
	}

	/**
	 * @since 1.0
	 */
	public void setModelItem(ModelItem modelItem) {
		this.modelItem = modelItem;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.CatalogItem#getUserView()
	 */
	public int getUserView() {
		return AuthorityCatalog.CATALOGUING_VIEW;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.CatalogItem#checkForMandatoryTags()
	 */
	public void checkForMandatoryTags() throws MandatoryTagException {
		final String[] tags = new String[] {"000","008", "040", "1"};
		for (int i=0; i<tags.length; i++) {
			if (findFirstTagByNumber(tags[i]) == null) {
				if ("1".equals(tags[i])) {
					throw new MandatoryTagException("1XX");
				}
				else {
					throw new MandatoryTagException(tags[i]);
				}
			}
		}
	}
	@Override
	public void sortTags() {
		Collections.sort(getTags(), new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				Tag t1 = (Tag)o1;
				Tag t2 = (Tag)o2;
				try {
					return t1.getMarcEncoding().getMarcTag().
						compareTo(t2.getMarcEncoding().getMarcTag());
				} catch (Exception e) {
					logger.warn(e);
					return 0;
				}
			}
		});
	}
}
