package org.folio.marccat.business.descriptor;

import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.PublisherTagDescriptorDAO;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.PUBL_TAG;
import org.folio.marccat.dao.persistence.PublisherAccessPoint;
import org.folio.marccat.shared.CorrelationValues;

import java.util.ArrayList;
import java.util.List;

/**
 * An adaptor to implement Descriptor semantics for the set of PUBL_TAG objects representing this Publisher APF.
 */
public class PublisherTagDescriptor extends Descriptor {
  private static final long serialVersionUID = 1L;
  private final PublisherTagDescriptorDAO theDAO = new PublisherTagDescriptorDAO();
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
  public void setCorrelationValues(CorrelationValues v) {
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
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

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
