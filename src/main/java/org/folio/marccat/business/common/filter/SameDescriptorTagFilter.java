package org.folio.marccat.business.common.filter;

import org.folio.marccat.business.cataloguing.common.Browsable;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.exception.DataAccessException;

public class SameDescriptorTagFilter implements TagFilter {

  public boolean accept(Tag tag, Object optionalCondition) throws DataAccessException {
    if (!tag.isBrowsable()) {
      return false;
    }
    // ok, the tag is browsable
    if ( !(optionalCondition instanceof Descriptor)) {
      return false;
    }
    // ok, the optionalCondition is presents and it is a correct type
    Descriptor tagDescriptor = ((Browsable) tag).getDescriptor();
    if (tagDescriptor == null) return false;
    // ok, tag has a decriptor associated
    Descriptor toFind = (Descriptor) optionalCondition;

    // check if the descriptors are the same...
    return tagDescriptor.equals(toFind);
  }
}
