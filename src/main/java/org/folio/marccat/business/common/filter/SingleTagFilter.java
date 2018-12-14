package org.folio.marccat.business.common.filter;

import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.exception.DataAccessException;

public class SingleTagFilter implements TagFilter {
  private Tag sourceTag;

  public SingleTagFilter(Tag sourceTag) {
    super();
    this.sourceTag = sourceTag;
  }

  public boolean accept(Tag tag, Object optionalCondition) throws DataAccessException {
    return
      sourceTag.getMarcEncoding().getMarcTag()
        .equals(tag.getMarcEncoding().getMarcTag());
  }
}
