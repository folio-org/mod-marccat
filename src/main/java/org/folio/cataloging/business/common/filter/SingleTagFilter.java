package org.folio.cataloging.business.common.filter;

import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;

public class SingleTagFilter implements TagFilter {
  private Tag sourceTag;

  public SingleTagFilter(Tag sourceTag) {
    super ( );
    this.sourceTag = sourceTag;
  }

  public boolean accept(Tag tag, Object optionalCondition) throws MarcCorrelationException, DataAccessException {
    return
      sourceTag.getMarcEncoding ( ).getMarcTag ( )
        .equals (tag.getMarcEncoding ( ).getMarcTag ( ));
  }
}
