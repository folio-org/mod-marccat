package org.folio.marccat.business.cataloguing.authority;

import java.io.Serializable;
import java.util.Iterator;

import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.cataloguing.common.TagImpl;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.NameDescriptorDAO;
import org.folio.marccat.dao.SubjectDescriptorDAO;
import org.folio.marccat.dao.TitleDescriptorDAO;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.AuthorityHeadingTag;
import org.folio.marccat.dao.persistence.AuthorityNameHeadingTag;
import org.folio.marccat.dao.persistence.AuthoritySubjectHeadingTag;
import org.folio.marccat.dao.persistence.AuthorityTitleHeadingTag;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.dao.persistence.ModelItem;
import org.folio.marccat.exception.MandatoryTagException;

import net.sf.hibernate.HibernateException;
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

	public void setModelItem(ModelItem modelItem) {
		this.modelItem = modelItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see CatalogItem#checkForMandatoryTags()
	 */
	/*
	 * Note that only editable mandatory tags are included -- generated tags like
	 * 000, 001, 005 cannot be added by the user and will be generated if not
	 * present
	 */
	@Override
	public void checkForMandatoryTags(Session session) {
		final String[] tags;
		tags = new String[] { "000", "008", "040", "1" };
		for (int i = 0; i < tags.length; i++) {
			Tag tag = findFirstTagByNumber(tags[i], session);
			if (tag == null) {
				if ("1".equals(tags[i])) {
					throw new MandatoryTagException("1XX");
				} else {
					throw new MandatoryTagException(tags[i]);
				}
			} else {
				if ((tag instanceof AuthorityNameHeadingTag) || (tag instanceof AuthoritySubjectHeadingTag)
						|| (tag instanceof AuthorityTitleHeadingTag)) {
					AuthorityHeadingTag aht = (AuthorityHeadingTag) tag;
					if (aht.getDescriptor().getKey().getHeadingNumber() == -1) {
						throw new MandatoryTagException("1XX");
					}
				}
			}
		}
	}

	public String getAuthorityType() {
		return getAutItmData().getHeadingType();
	}

	public String getAuthorityText(Session session) throws HibernateException {
		Descriptor heading = null;
		if (getAuthorityType().equals("NH"))
			heading = new NameDescriptorDAO().load(getAutItmData().getHeadingNumber(), getUserView(), session);
		else if (getAuthorityType().equals("SH"))
			heading = new SubjectDescriptorDAO().load(getAutItmData().getHeadingNumber(), getUserView(), session);
		else if (getAuthorityType().equals("TH"))
			heading = new TitleDescriptorDAO().load(getAutItmData().getHeadingNumber(), getUserView(), session);
		if (heading != null) {
			return heading.getDisplayText();
		} else
			return "";
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

}
