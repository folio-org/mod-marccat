package org.folio.marccat.business.common.group;

import org.apache.regexp.RE;
import org.folio.marccat.business.cataloguing.common.Tag;



public class RegExpGroup extends MarcTagGroup {

  private RE regexp;

  public RegExpGroup(boolean canSort, boolean singleSort, String pattern) {
    super(canSort, singleSort);
    regexp = new RE(pattern);
  }


  public boolean contains(Tag t) {
    return regexp.match(t.getMarcEncoding().getMarcTag());
  }

}
