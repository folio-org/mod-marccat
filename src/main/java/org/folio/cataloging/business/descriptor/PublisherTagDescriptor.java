package org.folio.cataloging.business.descriptor;

import org.folio.cataloging.business.cataloguing.bibliographic.PublisherAccessPoint;
import org.folio.cataloging.dao.DAOPublisherTagDescriptor;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.shared.CorrelationValues;

import java.util.ArrayList;
import java.util.List;

/**
 * An adaptor to implement Descriptor semantics for the set
 * of PUBL_TAG objects representing this Publisher APF
 * 
 * @author paultest
 *
 */
public class PublisherTagDescriptor extends Descriptor 
{
	private static DAOPublisherTagDescriptor theDAO = new DAOPublisherTagDescriptor();

	private static final long serialVersionUID = 1L;
	private List/*<PUBL_TAG>*/ publisherTagUnits = new ArrayList/*<PUBL_TAG>*/();

	//@Override
	public Class getAccessPointClass() {
		return PublisherAccessPoint.class;
	}

	//@Override
	public int getCategory() {
		return 7;
	}

	//@Override
	public CorrelationValues getCorrelationValues() {
		return new CorrelationValues();
	}

	//@Override
	public String getDefaultBrowseKey() {
		return null;
	}

	//@Override
	public String getHeadingNumberSearchIndexKey() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public String getLockingEntityType() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public String getNextNumberKeyFieldCode() {
		return null;
	}

	//@Override
	public Class getReferenceClass(Class targetClazz) {
		return null;
	}

	//@Override
	public SortFormParameters getSortFormParameters() {
		return null;
	}

	//@Override
	public void setCorrelationValues(CorrelationValues v) {
	}

	public HibernateUtil  getDAO() {
		return theDAO;
	}

	public List/*<PUBL_TAG>*/ getPublisherTagUnits() {
		return publisherTagUnits;
	}

	public void setPublisherTagUnits(List/*<PUBL_TAG>*/ publisherTagUnits) {
		publisherTagUnits = publisherTagUnits;
	}
}