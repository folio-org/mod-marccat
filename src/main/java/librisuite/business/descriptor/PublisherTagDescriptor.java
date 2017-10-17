package librisuite.business.descriptor;

import java.util.ArrayList;
import java.util.List;

import librisuite.business.cataloguing.bibliographic.PublisherAccessPoint;
import librisuite.business.common.CorrelationValues;

import com.libricore.librisuite.common.HibernateUtil;

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
	public short getCategory() {
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