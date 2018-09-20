package org.folio.cataloging.business.descriptor;

import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.PublisherTagDescriptorDAO;
import org.folio.cataloging.dao.persistence.Descriptor;
import org.folio.cataloging.dao.persistence.PUBL_TAG;
import org.folio.cataloging.dao.persistence.PublisherAccessPoint;
import org.folio.cataloging.shared.CorrelationValues;

import java.util.ArrayList;
import java.util.List;

/**
 * An adaptor to implement Descriptor semantics for the set of PUBL_TAG objects representing this Publisher APF.
 *
 * @author paul
 * @author natasciab
 *
 */
public class PublisherTagDescriptor extends Descriptor
{
	private final PublisherTagDescriptorDAO theDAO = new PublisherTagDescriptorDAO();

	private static final long serialVersionUID = 1L;
	private List<PUBL_TAG> publisherTagUnits = new ArrayList<>();

	@Override
	public Class getAccessPointClass() {
		return PublisherAccessPoint.class;
	}

	@Override
	public int getCategory() {
		return 7;
	}

	@Override
	public CorrelationValues getCorrelationValues() {
		return new CorrelationValues();
	}

	@Override
	public String getDefaultBrowseKey() {
		return null;
	}

	@Override
	public String getHeadingNumberSearchIndexKey() {
		return null;
	}

	@Override
	public String getLockingEntityType() {
		return null;
	}

	@Override
	public String getNextNumberKeyFieldCode() {
		return null;
	}

	@Override
	public Class getReferenceClass(Class targetClazz) {
		return null;
	}

	@Override
	public SortFormParameters getSortFormParameters() {
		return null;
	}

	@Override
	public void setCorrelationValues(CorrelationValues v) {}

	public AbstractDAO getDAO() {
		return theDAO;
	}

	public List<PUBL_TAG> getPublisherTagUnits() {
		return publisherTagUnits;
	}

	public void setPublisherTagUnits(final List<PUBL_TAG> publisherTags) {
		publisherTagUnits = publisherTags;
	}
}
